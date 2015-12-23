package ai;

import java.util.ArrayList;

import config.Config;
import util.Calculation;

public class Pathfinding {
	
	static void findPathInternal(ActorData ad, ActorData next, ActorData target, Path currentPath, ArrayList<Path> possiblePathList) {	
		ArrayList<Step> currentSteps = currentPath.getSteps();
		for (Step currentStep : currentSteps) {
			if(next.equalsXY(currentStep.getEnd())){
				if(Config.debugOutput){
					System.out.println("10 - we already tried this step, not a viable path AD={"+ad.getX()+"-"+ad.getY()+"} NEXT={"+next.getX()+"-"+next.getY()+"}");
				}
				return;
			}
		}
		
		if(Config.debugOutput){
			System.out.println("1 - new step AD={"+ad.getX()+"-"+ad.getY()+"} NEXT={"+next.getX()+"-"+next.getY()+"}");
		}
		currentPath.addStep(new Step(ad,next));						
		
		ArrayList<WallCollision> collisionWallsActual = null;
		ActorData adActual = null;
		ArrayList<WallCollision> collisionWallsAN = Calculation.getCollisionPoints(ad, next);
		if(collisionWallsAN.size()>0){
			// something is blocking our next step!
			collisionWallsActual = collisionWallsAN;
			adActual=ad;
		}else{
			ArrayList<WallCollision> collisionWallsNT = Calculation.getCollisionPoints(next, target);
			if(collisionWallsNT.size()>0){
				// its not the last step yet.. advance brave bot!
				collisionWallsActual = collisionWallsNT;
				adActual=next;
			}else{
				// we found a direct way 
				if(Config.debugOutput){
					System.out.println("4 - direct way found NEXT={"+next.getX()+"-"+next.getY()+"}  TARGET={"+target.getX()+"-"+target.getY()+"}");
				}				
				currentPath.addStep(new Step(next,target));
				possiblePathList.add(currentPath);
				return;
			}
		}
		
		ActorData closestCollisionWall = Calculation.getClosestCollisionObject(ad, collisionWallsActual);
		ActorData leftAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, true);
		ActorData rightAvoid = Calculation.getWayAroundObstacle(closestCollisionWall, false);

		Path left = currentPath.getCopy();
		Path right = currentPath.getCopy();			
	
		
		if(Config.debugOutput){
			if(collisionWallsActual==collisionWallsAN){
				System.out.println("2 - something is blocking the next step LEFT={"+leftAvoid.getX()+"-"+leftAvoid.getY()+"}  RIGHT={"+rightAvoid.getX()+"-"+rightAvoid.getY()+"}");
			}else {
				System.out.println("3 - its not the last step LEFT={"+leftAvoid.getX()+"-"+leftAvoid.getY()+"}  RIGHT={"+rightAvoid.getX()+"-"+rightAvoid.getY()+"}");				
			}
		}
		findPathInternal(adActual, leftAvoid, target, left, possiblePathList);
		findPathInternal(adActual, rightAvoid, target, right, possiblePathList);
		
	}
}
