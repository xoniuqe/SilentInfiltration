package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.navigation.test.NavPoint;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class MoveToTile_Task extends LeafTask<Integer> {

	Vector3 movetarget = new Vector3();

	@Override
	public void run(Integer object) {

		EntityManager entityM = EntityManager.getInstance();

		VelocityC vel = entityM.getComponent(object, VelocityC.class);
		PositionC pos = entityM.getComponent(object, PositionC.class);
		MoveOrder order = (MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;

		
		if (order.target.dst2(pos.getPos()) < 0.1f) {
			System.out.println("succsess");
			success();
			vel.velocity.setZero();
			entityM.getComponent(object, OrderableC.class).currentorder = null;
			return;
		}
		
		if (movetarget != order.target) {
			movetarget.set(order.path[0].pos.x, 0, order.path[0].pos.y);
		}

		if (order.path[0].pos.dst2(pos.getX(), pos.getZ()) < 0.1f && !(movetarget == order.target)) {
			if (order.path[0] == order.path[1]) {
				System.out.println("movint to end");
				movetarget = order.target;
			} else {
				movetarget.set(order.path[0].pos.x, 0, order.path[0].pos.y);
			}
			order.path[0] = order.path[0].router.get(order.path[1]).nav;
		}

		vel.velocity.x = movetarget.x - pos.getX();
		vel.velocity.y = movetarget.y - pos.getY();
		vel.velocity.z = movetarget.z - pos.getZ();
		vel.velocity.setLength(vel.maxspeed);

		/*
		 * float distToGoal = pos.position.dst(order.target); float distToNode =
		 * pos.position.dst(order.currentGoal.getCenter()); switch(order.state)
		 * { case Moving: if(distToNode <= 1) { vel.velocity.setZero();
		 * vel.steering.setZero(); order.state =
		 * MoveOrder.MoveState.NodeReached; } else { Vector3 desiredVel = new
		 * Vector3(order.currentGoal.getCenter());
		 * desiredVel.sub(pos.position).nor().scl(vel.maxspeed);
		 * 
		 * vel.velocity.x = order.currentGoal.getCenter().x - pos.position.x;
		 * vel.velocity.y = order.currentGoal.getCenter().y - pos.position.y;
		 * vel.velocity.z = order.currentGoal.getCenter().z - pos.position.z;
		 * //vel.velocity.clamp(-vel.maxspeed, vel.maxspeed);
		 * vel.velocity.setLength(vel.maxspeed);
		 * 
		 * vel.steering = desiredVel.sub(vel.velocity); } break; case
		 * NodeReached: if(order.currentGoal != order.end) { order.currentGoal =
		 * order.path.get(order.currentGoal); order.state =
		 * MoveOrder.MoveState.Moving; } else { order.currentGoal = order.end;
		 * order.state = MoveOrder.MoveState.MoveToGoal; } break; case
		 * MoveToGoal: if(distToGoal <= 1) { Vector3 desiredVel = new
		 * Vector3(order.target);
		 * desiredVel.sub(pos.position).nor().scl(vel.maxspeed);
		 * 
		 * vel.velocity.x = order.target.x - pos.position.x; vel.velocity.y =
		 * order.target.y - pos.position.y; vel.velocity.z = order.target.z -
		 * pos.position.z; //vel.velocity.clamp(-vel.maxspeed, vel.maxspeed);
		 * vel.velocity.setLength(vel.maxspeed); } else {
		 * vel.velocity.setZero(); order.state =
		 * MoveOrder.MoveState.GoalReached; } break; case GoalReached:
		 * order.state = MoveOrder.MoveState.Finished; success(); break;
		 * default: break; } // Tile endPos =
		 * order.tilemap.getTileAt((int)order.target.x, (int)order.target.z); //
		 * Tile currentGoal = order.currentGoal; // Tile currentTile =
		 * order.tilemap.getTileAt(order.tilemap.coordToIndex_x((int)pos.
		 * position.x), order.tilemap.coordToIndex_z((int)pos.position.z)); //
		 * // Tile tmp = currentTile; // float dist =
		 * pos.position.dst(endPos.getCenter()); // // Vector3 pos2 =
		 * order.target; // // if(order.path.containsKey(currentTile)) // { //
		 * tmp = order.path.get(currentTile); // pos2 = tmp.getCenter(); // } //
		 * while(order.path.containsKey(currentTile) && dist > 0.001 &&
		 * order.path.get(tmp) != currentTile){ // tmp = order.path.get(tmp); //
		 * dist = tmp.getCenter().dst(pos.position); // } // desired_velocity =
		 * normalize(target - position) * max_velocity // steering =
		 * desired_velocity - velocity // Vector3 desiredVel = new
		 * Vector3(pos2); //
		 * desiredVel.sub(pos.position).nor().scl(vel.maxspeed); // //
		 * vel.velocity.x = pos2.x - pos.position.x; // vel.velocity.y = pos2.y
		 * - pos.position.y; // vel.velocity.z = pos2.z - pos.position.z; //
		 * //vel.velocity.clamp(-vel.maxspeed, vel.maxspeed); //
		 * vel.velocity.setLength(vel.maxspeed); // // vel.steering =
		 * desiredVel.sub(vel.velocity); // // //
		 * //vel.velocity.setLength(vel.maxspeed); // // if (dist <= 0.1 ||
		 * vel.velocity.len() <= 1)//
		 * order.tilemap.getTileAt((int)pos.position.x, (int)pos.position.z) ==
		 * order.end) // { // order = null; // vel.velocity.setZero(); //
		 * success(); // }
		 */
	}

	@Override
	protected Task<Integer> copyTo(Task<Integer> task) {
		task = this;
		return task;
	}

}
