package ai;

import java.util.ArrayList;

import config.Config;
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
		this.ad.setRadius(10);
	}

	@Override
	public ActorData call() {
		switch(ad.getCurrentObjective().getObj()){
			case ObjectiveType.GOTO:
			case ObjectiveType.FOLLOW:
			case ObjectiveType.PATROL:
				callGoto();
				break;
			case ObjectiveType.THINKING:
			case ObjectiveType.IDLE:
				break;
		}
		return ad;
	}


	private synchronized void callGoto() {
		Objective currentObjective = ad.getCurrentObjective();
		Path path;
		
		if(!foundPath || currentObjective.hasTargetHasMoved()){
			ad.getObjectiveList().add(new Objective(ObjectiveType.THINKING, null));
			long startTime = System.currentTimeMillis();
			path = findPath(ad,currentObjective.getTarget());
			long endTime = System.currentTimeMillis();
			long executionTime = endTime-startTime;	
			if(Config.debugOutput){
				System.out.println("Found a path in "+executionTime+" ms with "+path.getSteps().size()+" steps");
				startTime = System.currentTimeMillis();
			}
			currentObjective.setPathToTarget(path);

			int op = optimizeSteps(path);
			
			if(Config.debugOutput){
				endTime = System.currentTimeMillis();
				executionTime = endTime-startTime;
				System.out.println("Optimization reduced steplist by "+op+" steps in "+executionTime+" ms");
			}
			
			currentObjective.resetOriginalTargetPosition();
			foundPath=true;
			ArrayList<Objective> a = ad.getObjectiveList();
			int i = -1 ;
			for (Objective objective : a) {
				objective.getObj().equals(ObjectiveType.THINKING);
				i++;
			}
			if(i!=-1){
				ad.getObjectiveList().remove(i);
			}
		}else{
			path = currentObjective.getPathToTarget();
		}
		if(path==null){
			ad.finishCurrentObjective();
		}
		doPath(path);
	}


	private void doPath(Path path) {
		String objectiveType = ad.getCurrentObjective().getObj();
		Step currentStep = path.getCurrentStep();
		goToPoint(currentStep.getEnd());
		if(ad.equalsXYEpsilon(currentStep.getEnd())){
			boolean advanced = path.advanceStep();
			if(!advanced){
				if(objectiveType.equals(ObjectiveType.PATROL)){
					ad.getCurrentObjective().swapTargets();
				}else if(objectiveType.equals(ObjectiveType.GOTO)){						
					ad.finishCurrentObjective();
				}
			}
		}	
	}

	private int optimizeSteps(Path path) {
		int totalOptimizedSteps=0; int currentlyOptimizedSteps=0;
		do{
			currentlyOptimizedSteps=path.optimizeStepsInternal();
			totalOptimizedSteps+=currentlyOptimizedSteps;
		}while(currentlyOptimizedSteps>0);
		return totalOptimizedSteps;
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
	
	
	/**
	 * 
	 * @param ad
	 * @param target
	 * @return returns null if no path exists
	 */
	public synchronized Path findPath(ActorData ad,ActorData target){
		foundPath=true;
		ArrayList<Path> pathsList = new ArrayList<Path>();
		Path currentPath = new Path(); 
		Pathfinding.findPathInternal(ad, target, target, currentPath, pathsList);
		//Path bestPath = getPathLeastNodes(pathsList);
		Path bestPath = Path.getPathShortestLength(pathsList);

		// there is no path, the target must be boxed in etc.
		if(bestPath==null){
			if(Config.debugOutput){
				System.out.println("Target unreachable!");
			}
			return new Path();
		}
		
		// if we haven't found a direct path, remove the first step, as that would be a direct start->target step
		if(bestPath.getSteps().size()>1){
			bestPath.getSteps().remove(0);
		}
		return bestPath;
	}


	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return "X";
	}

	@Override
	public boolean isSelected() {
		return false;
	}

}
