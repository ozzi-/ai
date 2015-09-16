package ai;

import java.util.ArrayList;

import util.ActorList;
import util.ActorName;
import util.Calculation;
import util.ObjectiveType;
import config.Config;

/**
 * The bot class moves to the closest point
 * @author ozzi
 *
 */
public class Bot implements Actor{

	private ActorData ad;
	
	public Bot(ActorData ad) {
		this.ad = ad;
		this.ad.setX_end(ad.getX());
		this.ad.setY_end(ad.getY());
		this.ad.setRadius(50);
		this.ad.getObjectiveList().add(new Objective(ObjectiveType.GOTO, new ActorData(100, 100)));
		this.ad.getObjectiveList().add(new Objective(ObjectiveType.FOLLOW,getClosestPoint().getActorData()));
	}


	private Point getClosestPoint(){
		Point point = null;
		double shortestDistance = Double.MAX_VALUE;
		for(int i = 0; i< ActorList.get(ActorName.POINT).size();i++){
			double distanceToPoint = util.Calculation.getDistance(this.ad, ActorList.get(ActorName.POINT).get(i).getActorData());
			if (distanceToPoint < shortestDistance){
				point = (Point)ActorList.get(ActorName.POINT).get(i);
				shortestDistance = distanceToPoint;
			}
		}
		return point;
	}
	
	@Override
	public ActorData call() {
		switch(ad.getCurrentObjective().getObj()){
			case ObjectiveType.GOING_AROUND_OBSTACLE:
				callGoingAround();
				break;
			case ObjectiveType.GOTO:
			case ObjectiveType.FOLLOW:
				callGoto();
				break;
			case ObjectiveType.IDLE:
				break;
		}
		return ad;
	}

	private void callGoingAround() {
		ActorData obstacleEnd = ad.getCurrentObjective().getTarget();
		goToPoint(new Point(obstacleEnd));
	}

	private void callGoto() {
		Point target = new Point(ad.getCurrentObjective().getTarget());
		findPath(ad, target.getActorData());
		goToPoint(target);		
	}


	private void goToPoint(Point target) {
		ad.setX(ad.getX_end());
		ad.setY(ad.getY_end());
		
		checkIfObjectiveReached(target);
		
		double direction = util.Calculation.getDirection(target.getActorData(), ad);
		ad.setDirection(direction);
		
		int speed = ad.getSpeed();
		speed = getMaxSpeed(target, speed);

		double delta_X = (speed * Math.cos(direction));
		double delta_Y = (speed * Math.sin(direction));
		double next_X = (ad.getX() + delta_X);
		double next_Y = (ad.getY() + delta_Y);

//		ActorData nextStep = new ActorData(next_X, next_Y); // TODO CLone
//		nextStep.setRadius(ad.getRadius());
//		
//		ActorData wallCollide = checkForWalls(nextStep);
//		if(wallCollide!=null && ad.getCurrentObjective().getObj().equals(ObjectiveType.GOTO)){
//			double distanceStart = Calculation.getDistance(ad,wallCollide);
//			double distanceEnd = Calculation.getDistanceSecondEnd(ad,wallCollide);
//			double target_X;
//			double target_Y;
//			if(distanceStart>=distanceEnd){
//				target_X = ad.getX()-(distanceEnd * Math.cos(wallCollide.getDirection()));
//				target_Y = ad.getY()-(distanceEnd * Math.sin(wallCollide.getDirection()));				
//			}else{
//				target_X = ad.getX()+(distanceStart * Math.cos(wallCollide.getDirection()));
//				target_Y = ad.getY()+(distanceStart * Math.sin(wallCollide.getDirection()));
//			}
//			
//			ActorData obstaclePoint = new ActorData(target_X,target_Y);
//			//ad.setObjective(new Objective(ObjectiveType.GOING_AROUND_OBSTACLE, obstaclePoint)); TODO
//			ActorData projectedCollisionPoint = Calculation.getClosestPointOnSegment(wallCollide.getX(), wallCollide.getY(), wallCollide.getX_end(), wallCollide.getY_end(), ad.getX(), ad.getY());
//			double totalDist = Calculation.getDistance(projectedCollisionPoint, ad);
//			double newspeed = totalDist - ad.getRadius();	
//			next_X = (ad.getX() + (newspeed * Math.cos(direction)));
//			next_Y = (ad.getY() + (newspeed * Math.sin(direction)));
//		}

		ad.setX_end(next_X);
		ad.setY_end(next_Y);
	}


	private void checkIfObjectiveReached(Point target) {
		if(!ad.getCurrentObjective().getObj().equals(ObjectiveType.FOLLOW)){
			if(Calculation.equals(ad.getX(), target.getActorData().getX(), Config.epsilon) && Calculation.equals(ad.getY(), target.getActorData().getY(), Config.epsilon)){
				ad.finishedCurrentObjective();
			}			
		}
	}
	

	@SuppressWarnings("unused")
	private ActorData checkForWalls(ActorData actor) {
		for (Actor wallActor : ActorList.get(ActorName.WALL)) {
			ActorData wallAd = wallActor.getActorData();
			ActorData closestPointOnWall = Calculation.getClosestPointOnSegment(wallAd.getX(), wallAd.getY(), wallAd.getX_end(), wallAd.getY_end(), actor.getX(), actor.getY());
			if(Calculation.xyIsInRadiusOfActor(closestPointOnWall.getX(), closestPointOnWall.getY(), actor)){
				return wallAd;
			}
		}
		return null;
	}

	private int getMaxSpeed(Point closestPoint, int speed) {
		double distance = util.Calculation.getDistance(ad, closestPoint.getActorData());
		if(speed>distance){
			speed=(int)distance;
		}
		return speed;
	}
	
	public void findPath(ActorData ad, ActorData target){
		ArrayList<WallCollision> collisionWalls = Calculation.getCollisionPoints(ad, target);
		ActorData closestCollisionPoint = Calculation.getClosestCollisionObject(ad, collisionWalls);
		if(ad.getCurrentObjective().getTarget().equals(closestCollisionPoint)){
			return;
		} // TODO special case
		if(closestCollisionPoint!=null){
			double distance1 = Calculation.getDistance(ad, closestCollisionPoint);
			double distance2 = Calculation.getDistanceSecondEnd(ad, closestCollisionPoint);
			ActorData avoidPoint ;
			double delta_X = (10 * Math.cos(closestCollisionPoint.getDirection()));
			double delta_Y = (10 * Math.sin(closestCollisionPoint.getDirection()));
			if(distance1>=distance2){
				avoidPoint = new ActorData(closestCollisionPoint.getX_end()+delta_X, closestCollisionPoint.getY_end()+delta_Y);
			}else{
				avoidPoint = new ActorData(closestCollisionPoint.getX()+delta_X, closestCollisionPoint.getY()+delta_Y);
			}
			ad.insertObjective(new Objective(ObjectiveType.GOING_AROUND_OBSTACLE, avoidPoint));
		}
	}



	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return "X";
	}

}
