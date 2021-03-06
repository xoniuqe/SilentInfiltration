package de.mih.core.engine.ability;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.lua.LuaScript;

public class Ability
{
	private int id = -1;
	private String scriptPath;
	private String iconPath;

	private transient LuaScript script;

	public int getId()
	{
		return id;
	}

	public void setScript(LuaScript script)
	{
		this.script = script;
	}

	public String getScriptPath()
	{
		return scriptPath;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void castNoTarget(int caster){
		script.run("onNoTarget",caster);
	}

	public void castOnPoint(int caster, Vector3 target){
		script.run("onPoint",caster,target);
	}

	public void castOnTarget(int caster, int targetId, Vector3 intersection){
		script.run("onTarget",caster,targetId,intersection);
	}

	public void castOnTarget(int caster, int targetId){
		script.run("onTarget",caster,targetId);
	}
	
	public LuaScript getScript()
	{
		return script;
	}
}
