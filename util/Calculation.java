package util;

import ai.ActorData;

public class Calculation {

	public static double getDirection (ActorData target, ActorData source){
		return Math.atan2((target.getY()- source.getY()),(target.getX()- source.getX()));
	}
	
	public static double getDistance(ActorData a, ActorData b){
		double xdiff = Math.abs(a.getX() - b.getX());
		double ydiff = Math.abs(a.getY() - b.getY());
		return Math.sqrt(xdiff*xdiff+ydiff*ydiff);
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
    		closestPoint = new ActorData((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
    	}
	    return closestPoint;
	}
}
