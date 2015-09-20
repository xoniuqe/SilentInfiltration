package de.mih.core.game.components;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.Component;

public class StatsC extends Component {

	public final static String name = "stats";

	public int alertlevel = 0;
	public float alertlevelmulti = 1f;

	public float speed = 2;

	public float sneakspeed = 1;
	public float walkspeed = 2;
	public float runspeed = 3;

	public int hits = 0;
	public int maxhits = 5;

	public boolean canunlockdoors = false;
	public boolean canlockdoors = false;
	public boolean cansmashdoors = false;
	public boolean canuseweapons = false;
	public boolean canko = false;
	public boolean candisguise = false;
	public boolean canhideincabinet = false;
	public boolean canclimbvent = false;
	public boolean canwalkinsecareas = false;

	@Override
	public void setField(String fieldName, String fieldValue) {

		Field field = null;

		if (fieldName.equals("values")) {
			StringTokenizer st = new StringTokenizer(fieldValue, "=\n ");
			while (st.hasMoreTokens()) {
				try {
					field = this.getClass().getField(st.nextToken());
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}

				try {
					if (field.getType().toString().equals("float")) {
						field.setFloat(this, Float.parseFloat(st.nextToken()));
					}
					if (field.getType().toString().equals("int")) {
						field.setInt(this, Integer.parseInt(st.nextToken()));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}

		if (fieldName.equals("booleans")) {
			StringTokenizer st = new StringTokenizer(fieldValue, "\n");
			while (st.hasMoreTokens()) {
				try {
					field = this.getClass().getField(st.nextToken());
					field.setBoolean(this, true);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public Component cpy() {
		StatsC tmp = new StatsC();
		Field[] fields = getClass().getFields();
		for (Field f : fields) {
			if (f.getName().equals("name"))
				continue;
			try {
				f.set(tmp, f.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
		}
		return tmp;
	}

}