package de.mih.core.game.input.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.mih.core.engine.GameStates.GameStateManager;
import de.mih.core.engine.GameStates.PlayingGameState;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.input.ClickListener;
import de.mih.core.game.input.ui.UserInterface.Border;

public class Button {

	public Rectangle rect = new Rectangle();
	
	Border border;
	public Vector2 fixedoffset = new Vector2();
	public Vector2 ratiooffset = new Vector2();

	Texture texture;

	boolean visible = true;

	ArrayList<ClickListener> listener = new ArrayList<ClickListener>();

	public Button(Border b, float fix_x, float fix_y, float ratio_x, float ratio_y, Texture t) {
		this.border = b;
		fixedoffset.x = fix_x;
		fixedoffset.y = fix_y;
		ratiooffset.x = ratio_x;
		ratiooffset.y = ratio_y;
		rect.width = t.getWidth();
		rect.height = t.getHeight();
		this.texture = t;
		calculatePosition();
	}

	public void hide() {
		visible = false;
	}

	public void show() {
		visible = true;
	}

	public void addlistener(ClickListener l) {
		listener.add(l);
	}
	
	public void calculatePosition(){
		switch (border) {
		case BOTTOM_LEFT: {
			rect.x = (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
			rect.y = (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
			break;
		}
		case BOTTOM_RIGHT: {
			rect.x = Gdx.graphics.getWidth() - texture.getWidth()
					+ (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
			rect.y = (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
			break;
		}
		case TOP_LEFT: {
			rect.x = (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
			rect.y = Gdx.graphics.getHeight() - texture.getHeight()
					+ (Gdx.graphics.getHeight() -rect.height / 2f) * ratiooffset.y + fixedoffset.y;
			break;
		}
		case TOP_RIGHT: {
			rect.x = Gdx.graphics.getWidth() - texture.getWidth()
					+ (Gdx.graphics.getWidth() -rect.width / 2f) * ratiooffset.x + fixedoffset.x;
			rect.y = Gdx.graphics.getHeight() - texture.getHeight()
					+ (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
			break;
		}
		}
	}
}