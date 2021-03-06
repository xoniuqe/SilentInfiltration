package de.mih.core.game.ai.guard;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.StateMachineComponent.State;
import de.mih.core.game.events.order.OrderToPointEvent;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class Patrol extends State
{

	GameLogic game;
	int currentWaypoint = -1;
	int currentIndex    = 0;
	BaseOrder     currentOrder;
	List<Integer> waypoints;

	public Patrol(StateMachineComponent stateMachine, GameLogic game)
	{
		super(stateMachine);
		this.game = game;
	}

	@Override
	public void onEnter()
	{
		if (currentWaypoint == -1)
		{
			currentWaypoint = this.waypoints.get(0);
		}
		OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID, OrderableC.class);
		if (currentOrder != null)
			order.addOrder(currentOrder);
	}

	@Override
	public void onLeave()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setWaypoints(List<Integer> waypoints)
	{
		this.waypoints = waypoints;
	}
	
	@Override
	public void update()
	{
		//System.out.println("PATROL: " + this.stateMachine.entityID);
		if (currentOrder == null)
		{
			order();
		}
		if (currentOrder.isFinished())
		{
			currentIndex++;
			if (currentIndex >= this.waypoints.size())
			{
				currentIndex = 0;
			}
			//System.out.println("INCREMENT");
			currentWaypoint = this.waypoints.get(currentIndex);
			order();
		}
		else
		{
			OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID, OrderableC.class);
			order.removeOrder(currentOrder);
		}
	}
	
	public void order()
	{
		EntityManager entityM   = game.getEntityManager();
		Vector3     targetpos = entityM.getComponent(currentWaypoint, PositionC.class).getPos();

//		EntityManager entityM = game.getEntityManager();
//		PositionC actorpos = entityM.getComponent(stateMachine.entityID, PositionC.class);
//		PositionC targetpos = entityM.getComponent(currentWaypoint, PositionC.class);
//
//
//		Path path = game.getNavigationManager().getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());//findShortesPath(start, end);
//
//
//		OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID,OrderableC.class);
		Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(stateMachine.entityID, targetpos));
	}
}
