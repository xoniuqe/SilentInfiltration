package com.MiH.game.components;

import java.util.ArrayList;
import java.util.List;

import com.MiH.engine.ecs.Component;

public class NodeC extends Component{
	
	public static final float TILE_SIZE = 0.5f;
	
	public boolean blocked;
	public List<Integer> neighbours = new ArrayList<Integer>();
	public TilemapC map;
}
