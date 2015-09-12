package de.mih.core.engine.ecs;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.RenderSystem;

public class RenderManager {
	PerspectiveCamera camera;
	RenderSystem renderSystem;
	EntityManager entityManager;
	TilemapRenderer tilemapRenderer;
	public SpriteBatch spriteBatch;
	private ModelBatch modelBatch;
	private ModelBuilder modelBuilder;
	private ObjLoader modelLoader;
	private Environment environment;

	ArrayList<BaseRenderer> registertMBRenderer = new ArrayList<BaseRenderer>();
	ArrayList<BaseRenderer> registertSBRenderer = new ArrayList<BaseRenderer>();

	static RenderManager renderManager;

	public static RenderManager getInstance() {
		if (renderManager == null) {
			renderManager = new RenderManager();
		}
		return renderManager;
	}

	public RenderManager() {
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		modelLoader = new ObjLoader();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
	}

	public void register(BaseRenderer renderer) {
		if (renderer.usemodebatch) {
			if (!registertMBRenderer.contains(renderer))
				registertMBRenderer.add(renderer);
		} else {
			if (!registertSBRenderer.contains(renderer))
				registertSBRenderer.add(renderer);
		}
	}

	public void unregister(BaseRenderer renderer) {
		if (renderer.usemodebatch) {
			if (registertMBRenderer.contains(renderer))
				registertMBRenderer.remove(renderer);
		} else {
			if (registertSBRenderer.contains(renderer))
				registertSBRenderer.remove(renderer);
		}
	}

	public void setCamera(PerspectiveCamera camera) {
		this.camera = camera;
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		modelBatch.begin(camera);
		for (BaseRenderer renderer : registertMBRenderer) {
			renderer.render();
		}
		modelBatch.end();

		spriteBatch.begin();
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for (BaseRenderer renderer : registertSBRenderer) {
			renderer.render();
		}
		spriteBatch.end();

	}

	public PerspectiveCamera getCamera() {
		return camera;
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public void setModelBatch(ModelBatch modelBatch) {
		this.modelBatch = modelBatch;
	}

	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public void setModelBuilder(ModelBuilder modelBuilder) {
		this.modelBuilder = modelBuilder;
	}

	public ObjLoader getModelLoader() {
		return modelLoader;
	}

	public void setModelLoader(ObjLoader modelLoader) {
		this.modelLoader = modelLoader;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Vector3 getCameraTarget(float height) {
		return camera.position.cpy()
				.add(camera.direction.cpy().scl((height - camera.position.y) / (camera.direction.y)));
	}

	Vector3 temp_pos = new Vector3();
	Vector3 min_pos = new Vector3();
	int min_entity;

	@SuppressWarnings("unchecked")
	public int getSelectedEntityByFilter(int mouseX, int mouseY, Class<? extends Component>... classes) {
		EntityManager entityM = EntityManager.getInstance();
		Ray ray = camera.getPickRay(mouseX, mouseY);
		min_entity = -1;
		for (int i = 0; i < entityM.entityCount; i++) {
			if (!entityM.hasComponent(i, VisualC.class) || !entityM.hasComponent(i, PositionC.class)){
				continue;
			}
			
			boolean hasclass = true;
			for (Class<? extends Component> c : classes ){
				if (!entityM.hasComponent(i, c)){
					hasclass = false;
				}
			}
			if (!hasclass) continue;
			
			VisualC vis = entityM.getComponent(i, VisualC.class);
			PositionC pos = entityM.getComponent(i, PositionC.class);

			float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

			temp_pos = pos.position.cpy();
			temp_pos.add(vis.visual.pos);
			temp_pos.y += vis.visual.bounds.getHeight() / 2f;

			if (Intersector.intersectRaySphere(ray, temp_pos, radius, null)) {
				if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos)) {
					min_entity = i;
					min_pos = pos.position;
				}
			}

		}
		return min_entity;
	}

	Ray m_target = new Ray();

	public Vector3 getMouseTarget(float height, Input input) {
		m_target = camera.getPickRay(input.getX(), input.getY()).cpy();
		return m_target.origin.add(m_target.direction.scl((height - m_target.origin.y) / (m_target.direction.y)));
	}

	Vector3 pos = new Vector3();

	public boolean isVisible(Visual v) {
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return camera.frustum.sphereInFrustum(pos, v.radius);
	}

}
