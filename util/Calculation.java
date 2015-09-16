package util;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import ai.Actor;
import ai.ActorData;
import ai.WallCollision;

public class Calculation {

	public static double getDirection (ActorData target, ActorData source){
		return Math.atan2((target.getY()- source.getY()),(target.getX()- source.getX()));
	}
	
	public static double getDistance(ActorData a, ActorData b){
		double xdiff = Math.abs(a.getX() - b.getX());
		double ydiff = Math.abs(a.getY() - b.getY());
		return Math.sqrt(xdiff*xdiff+ydiff*ydiff);
	}
	
	public static double getDistanceSecondEnd(ActorData a, ActorData b){
		return getDistance(a, new ActorData(b.getX_end(), b.getY_end()));
	}
	
	public static boolean equals(double a, double b, double eps) {
		if (a==b) return true;
		return Math.abs(a - b) < eps;
	}
	
	public static ActorData wallCollisionPoint(ActorData collidingWall, ActorData ad, ActorData target){
		double x1 = collidingWall.getX();
		double y1 = collidingWall.getY();
		double x2 = collidingWall.getX_end();
		double y2 = collidingWall.getY_end();
		
		double x3 = ad.getX();
		double y3 = ad.getY();
		
		double x4 = target.getX();
		double y4 = target.getY();
		
		double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) return null;
		double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		
		return new ActorData(xi, yi);
	}

	
	public static double angleBetween2Lines(ActorData line1, ActorData line2){
		double angle1 = Math.atan2(line1.getY() - line1.getY_end(),
	                               line1.getX() - line1.getX_end());
		double angle2 = Math.atan2(line2.getY() - line2.getY_end(),
	                               line2.getX() - line2.getX_end());
		return angle1-angle2;
	}
	
	public static boolean xyIsInRadiusOfActor(double x, double y, ActorData actor){
		ActorData xy = new ActorData(x, y);
		return (getDistance(xy, actor)<= actor.getRadius());
	}
	
	public static double getAngleOfLine(ActorData line){
		double xDiff = line.getX() - line.getX_end();
		double yDiff = line.getY() - line.getY_end();
		return Math.toDegrees(Math.atan2(yDiff, xDiff));
	}
	  

	public static ArrayList<WallCollision> getCollisionPoints(ActorData ad, ActorData target) {
		ArrayList<WallCollision> collisionPoints = new ArrayList<WallCollision>();
		for (Actor wallActor : ActorList.get(ActorName.WALL)) {
			ActorData wallAd = wallActor.getActorData();
			if(Line2D.Double.linesIntersect(ad.getX(), ad.getY(), target.getX(), target.getY(),wallAd.getX(),wallAd.getY(),wallAd.getX_end(),wallAd.getY_end())){
				ActorData collisionPoint = wallCollisionPoint(wallActor.getActorData(), ad, target);
				collisionPoints.add(new WallCollision(wallAd,collisionPoint));
			}
		}
		return collisionPoints;
	}

	public static ActorData getClosestCollisionObject(ActorData ad, ArrayList<WallCollision> collisionPoints) {
		double distance = Double.MAX_VALUE;
		ActorData firstCollisionObject = null;
		for (WallCollision collisionPoint : collisionPoints) {
			double pointDistance = getDistance(collisionPoint.getCollisionPoint(), ad);
			if(pointDistance<distance){
				distance=pointDistance;
				firstCollisionObject=collisionPoint.getWall();
			}
		}
		return firstCollisionObject;
	}

	
	public static ActorData getClosestPointOnSegment(double sx1, double sy1, double sx2, double sy2, double px, double py) {
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

	    final ActorData closestPoint;
	    if (u < 0) {
	    	closestPoint = new ActorData(sx1, sy1);
	    }else if (u > 1) {
	    	closestPoint = new ActorData(sx2, sy2); 
    	}else {
    		closestPoint = new ActorData( Math.round(sx1 + u * xDelta), Math.round(sy1 + u * yDelta));
    	}
	    return closestPoint;
	}
}
