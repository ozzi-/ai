package ai;

import java.util.ArrayList;

import util.ActorList;
import util.ActorName;
import util.Calculation;
import util.ObjectiveType;

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
		this.ad.getObjectiveList().add(new Objective(ObjectiveType.GOTO,getClosestPoint().getActorData()));
		//this.ad.getObjectiveList().add(new Objective(ObjectiveType.GOTO, new ActorData(500, 600)));
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
		
		Path path;
		if(!foundPath){			
			 path = findPath(ad,currentObjective.getTarget());
			currentObjective.setPathToTarget(path);
			foundPath=true;
		}else{
			path = currentObjective.getPathToTarget();
		}
		// TODO optimize path

		Step currentStep = path.getCurrentStep();
		goToPoint(currentStep.getEnd());
		
		if(ad.equalsXYEpsilon(currentStep.getEnd())){
			boolean advanced = path.advanceStep();
			if(!advanced){
				ad.finishedCurrentObjective();
			}
		}
		
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
	

	private void goToPoint(ActorData target) {
		ad.setX(ad.getX_end());
		ad.setY(ad.getY_end());
		
		
		double direction = util.Calculation.getDirection(target, ad);
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

	private int getMaxSpeed(ActorData closestPoint, int speed) {
		double distance = util.Calculation.getDistance(ad, closestPoint);
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
		// TODO GET BEST PATH
		int check= Integer.MAX_VALUE;
		Path bestPath=null;
		for (Path path : pathsList) {
			if(path.getSteps().size()<check){
				bestPath=path;
				check=path.getSteps().size();
			}
		}
		if(bestPath.getSteps().size()>1){
			// if we haven't found a direct path, remove the first step, as that would be a driect start->target step
			bestPath.getSteps().remove(0);
		}
		return bestPath;
	}
	
	private void findPathInternal(ActorData ad, ActorData next, ActorData target, Path currentPath, ArrayList<Path> possiblePathList) {
	
		ArrayList<Step> currentSteps = currentPath.getSteps();
		for (Step currentStep : currentSteps) {
			//if(ad.getX()==currentStep.getStart().getX() && next.getX()==currentStep.getEnd().getX() ){
			if(ad.equalsXY(currentStep.getStart()) && next.equalsXY(currentStep.getEnd())){
				// we already tried this step, not a viable path!
				return;
			}
		}
		
		currentPath.addStep(new Step(ad,next));						
		
		ArrayList<WallCollision> collisionWallsAN = Calculation.getCollisionPoints(ad, next);
		if(collisionWallsAN.size()>0){
			// something is blocking our next step!
			ActorData closestCollisionWall = Calculation.getClosestCollisionObject(ad, collisionWallsAN);
			ActorData leftAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, true);
			ActorData rightAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, false);
			Path left = currentPath.getCopy();
			Path right = currentPath.getCopy();
		
			findPathInternal(ad, leftAvoid, target, left, possiblePathList);
			findPathInternal(ad, rightAvoid, target, right, possiblePathList);
		}else{
			ArrayList<WallCollision> collisionWallsNT = Calculation.getCollisionPoints(next, target);
			if(collisionWallsNT.size()>0){
				// its not the last step yet.. advance brave bot!
				ActorData closestCollisionWall = Calculation.getClosestCollisionObject(ad, collisionWallsNT);
				ActorData leftAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, true);
				ActorData rightAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, false);

				Path left = currentPath.getCopy();
				Path right = currentPath.getCopy();			
			

				findPathInternal(next, leftAvoid, target, left, possiblePathList);
				findPathInternal(next, rightAvoid, target, right, possiblePathList);
			}else{
				// we found a direct way 
				currentPath.addStep(new Step(next,target));
				possiblePathList.add(currentPath);
				return;
			}
			
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
