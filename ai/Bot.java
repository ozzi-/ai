package ai;

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

	public Bot(ActorData ad) {
		this.ad = ad;
		this.ad.setX_end(ad.getX());
		this.ad.setY_end(ad.getY());
		this.ad.setRadius(50);
		setClosestPointObjective();
	}

	private Point setClosestPointObjective() {
		Point closestPoint = getClosestPoint();
		Objective obj;
		if(closestPoint==null){
			obj = new Objective(ObjectiveType.IDLE,null);
		}else{			
			obj = new Objective(ObjectiveType.GOTO,getClosestPoint().getActorData());
		}
		this.ad.setObjective(obj);
		return closestPoint;
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
		switch(ad.getObjective().getObj()){
			case ObjectiveType.GOING_AROUND_OBSTACLE:
				callGoingAround();
				break;
			case ObjectiveType.GOTO:
				callGoto();
				break;
			case ObjectiveType.IDLE:
				break;
		}
		return ad;
	}

	private void callGoingAround() {
		ActorData obstacle = ad.getObjective().getTarget();
		ActorData obstaclePoint = new ActorData(obstacle.getX(), obstacle.getY());
		//goToPoint(new Point(obstaclePoint));
	}

	private void callGoto() {
		Point closestPoint = setClosestPointObjective();
		goToPoint(closestPoint);
		
	}

	private void goToPoint(Point closestPoint) {
		
		
		ad.setX(ad.getX_end());
		ad.setY(ad.getY_end());
		
		
		double direction = util.Calculation.getDirection(closestPoint.getActorData(), ad);
		ad.setDirection(direction);
		
		int speed = ad.getSpeed();
		speed = getMaxSpeed(closestPoint, speed);

		double delta_X = (speed * Math.cos(direction));
		double delta_Y = (speed * Math.sin(direction));
		
		double next_X = (ad.getX() + delta_X);
		double next_Y = (ad.getY() + delta_Y);

	 
		ActorData nextStep = new ActorData(next_X, next_Y); // TODO CLone
		nextStep.setRadius(ad.getRadius());
		
		ActorData wallCollide = checkForWalls(nextStep);
		if(wallCollide!=null){
			ad.setObjective(new Objective(ObjectiveType.GOING_AROUND_OBSTACLE, wallCollide));
			ActorData projectedCollisionPoint = Calculation.getClosestPointOnSegment(wallCollide.getX(), wallCollide.getY(), wallCollide.getX_end(), wallCollide.getY_end(), ad.getX(), ad.getY());
			double totalDist = Calculation.getDistance(projectedCollisionPoint, ad);
			System.out.println("total dist: "+totalDist);
			double newspeed = totalDist - ad.getRadius();
			delta_X = (newspeed * Math.cos(direction));
			delta_Y = (newspeed * Math.sin(direction));
			next_X = (ad.getX() + delta_X);
			next_Y = (ad.getY() + delta_Y);
			ad.setX(next_X);
			ad.setY(next_Y);
		}

	
		ad.setX_end(next_X);
		ad.setY_end(next_Y);


	}
	
	private ActorData wallCollisionPoint(ActorData collidingWall, ActorData nextStep){
		double x1 = collidingWall.getX();
		double y1 = collidingWall.getY();
		double x2 = collidingWall.getX_end();
		double y2 = collidingWall.getY_end();
		double x3 = nextStep.getX();
		double y3 = nextStep.getY();
		

		
		double x4 = nextStep.getX()+ ad.getSpeed() * Math.cos(nextStep.getDirection());
		double y4 = nextStep.getY()+ ad.getSpeed() * Math.sin(nextStep.getDirection());
		
		double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) return null;
		double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		
		return new ActorData(xi, yi);
	}

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
	


	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return "X";
	}

}
