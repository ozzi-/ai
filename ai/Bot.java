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
	private boolean foundPath = false;
	
	public Bot(ActorData ad) {
		this.ad = ad;
		this.ad.setX_end(ad.getX());
		this.ad.setY_end(ad.getY());
		this.ad.setRadius(50);
		this.ad.getObjectiveList().add(new Objective(ObjectiveType.FOLLOW,getClosestPoint().getActorData()));
		//this.ad.getObjectiveList().add(new Objective(ObjectiveType.GOTO, new ActorData(500, 600)));
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
			case ObjectiveType.GOTO:
			case ObjectiveType.FOLLOW:
				callGoto();
				break;
			case ObjectiveType.IDLE:
				break;
		}
		return ad;
	}



	private void callGoto() {
	
		// TODO check if target has moved in the meantime
		Objective currentObjective = ad.getCurrentObjective();
		
		if(!foundPath){			
			Path path = findPath(ad,currentObjective.getTarget());	
		}
		//TODO  SET IDLE IF NOTHING TO DO
	}







	private void goToPoint(Point target) {
		ad.setX(ad.getX_end());
		ad.setY(ad.getY_end());
		
		
		double direction = util.Calculation.getDirection(target.getActorData(), ad);
		ad.setDirection(direction);
		
		int speed = ad.getSpeed();
		speed = getMaxSpeed(target, speed);

		double delta_X = (speed * Math.cos(direction));
		double delta_Y = (speed * Math.sin(direction));
		double next_X = (ad.getX() + delta_X);
		double next_Y = (ad.getY() + delta_Y);

		ad.setX_end(next_X);
		ad.setY_end(next_Y);
	}


	private boolean checkIfObjectiveReached(ActorData target) {
		//if(!ad.getCurrentObjective().getObj().equals(ObjectiveType.FOLLOW)){
			if(Calculation.equals(ad.getX(), target.getX(), Config.epsilon) && Calculation.equals(ad.getY(), target.getY(), Config.epsilon)){
				return true;
			}			
		//}
		return false;
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
	
	
	public Path findPath(ActorData ad,ActorData target){
		foundPath=true;
		ArrayList<Path> pathsList = new ArrayList<Path>();
		Path currentPath = new Path();
		findPathInternal(ad, target, target, currentPath, pathsList);
		// GET BEST PATH
		
		int check= Integer.MAX_VALUE;
		Path bestPath=null;
		for (Path path : pathsList) {
			if(path.getSteps().size()<check){
				bestPath=path;
				check=path.getSteps().size();
			}
		}
		System.out.println("BEST PATH:"+bestPath.getSteps().size());
		for (Step step : bestPath.getSteps()) {
			System.out.println(step.getStart().getX()+"-"+step.getStart().getY()+" TO "+step.getEnd().getX()+"-"+step.getEnd().getY());
		}
		return null;
	}
	
	private void findPathInternal(ActorData ad, ActorData next, ActorData target, Path currentPath, ArrayList<Path> possiblePathList) {
	
		
		
		ArrayList<Step> currentSteps = currentPath.getSteps();
		for (Step currentStep : currentSteps) {
			if(ad.getX()==currentStep.getStart().getX() && next.getX()==currentStep.getEnd().getX()){
				// we already tried this!
				return;
			}
		}
	
		currentPath.addStep(new Step(ad,next));

		
		ArrayList<WallCollision> collisionWallsAN = Calculation.getCollisionPoints(ad, next);
		if(collisionWallsAN.size()>0){
			// something is blocking our next step!
			ActorData closestCollisionWall = Calculation.getClosestCollisionObject(ad, collisionWallsAN);
			ActorData leftAvoid = getWayAroundObstacle(closestCollisionWall, true);
			ActorData rightAvoid = getWayAroundObstacle(closestCollisionWall, false);
			Path left = currentPath.getCopy();
			Path right = currentPath.getCopy();
		
			findPathInternal(ad, leftAvoid, target, left, possiblePathList);
			findPathInternal(ad, rightAvoid, target, right, possiblePathList);
		}else{
			ArrayList<WallCollision> collisionWallsNT = Calculation.getCollisionPoints(next, target);
			if(collisionWallsNT.size()>0){
				// its not the last step yet.. advance brave bot!
				ActorData closestCollisionWall = Calculation.getClosestCollisionObject(ad, collisionWallsNT);
				ActorData leftAvoid = getWayAroundObstacle(closestCollisionWall, true);
				ActorData rightAvoid = getWayAroundObstacle(closestCollisionWall, false);

				Path left = currentPath.getCopy();
				Path right = currentPath.getCopy();			
			

				findPathInternal(next, leftAvoid, target, left, possiblePathList);
				findPathInternal(next, rightAvoid, target, right, possiblePathList);
			}else{
				// we found a direct way 
				System.out.println("FOUND A WAY!");
				//currentPath.addStep(new Step(target.getX(),target.getY()));
				currentPath.addStep(new Step(next,target));
				possiblePathList.add(currentPath);
				return;
			}
			
		}
	}

	
	


	private ActorData getWayAroundObstacle(ActorData collisionWall,boolean left) {
		double wallLength = Calculation.getWallLength(collisionWall);
		double x; double y;
		if(left){
			x = collisionWall.getX()+(collisionWall.getX()-collisionWall.getX_end()) / wallLength * Config.avoidDistance;
			y = collisionWall.getY()+(collisionWall.getY()-collisionWall.getY_end()) / wallLength * Config.avoidDistance;	
		}else{			
			x = collisionWall.getX_end()+(collisionWall.getX_end()-collisionWall.getX()) / wallLength * Config.avoidDistance;
			y = collisionWall.getY_end()+(collisionWall.getY_end()-collisionWall.getY()) / wallLength * Config.avoidDistance;
		}
		return new ActorData(x,y);
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
