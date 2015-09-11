package ai;

import util.ActorList;
import util.ActorName;

/**
 * The bot class moves to the closest point
 * @author ozzi
 *
 */
public class Bot implements Actor{

	private ActorData ad;

	public Bot(ActorData ad) {
		this.ad = ad;
	}
	
	private Point getClosestPoint(){
		Point point = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(int i = 0; i< ActorList.get(ActorName.POINT).size();i++){
			int distanceToPoint = util.Calculation.getDistance(this.ad, ActorList.get(ActorName.POINT).get(i).getActorData());
			if (distanceToPoint < shortestDistance){
				point = (Point)ActorList.get(ActorName.POINT).get(i);
				shortestDistance = distanceToPoint;
			}
		}
		return point;
	}
	
	@Override
	public ActorData call() {
		Point closestPoint = getClosestPoint();
		if(closestPoint!=null){
			double direction = util.Calculation.getDirection(closestPoint.getActorData(), ad);
			int speed = ad.getSpeed();
			int distance = util.Calculation.getDistance(ad, closestPoint.getActorData());
			if(speed>distance){
				speed=distance;
			}
		    ad.setX((int)(ad.getX() + (speed * Math.cos(direction))));
		    ad.setY((int)(ad.getY() + (speed * Math.sin(direction))));
		}
		return ad;
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
