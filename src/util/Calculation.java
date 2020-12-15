package util;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import config.Config;
import ai.Actor;
import ai.ActorData;
import ai.Point;
import ai.WallCollision;

public class Calculation {

	private static int closest=0;
	private static int furthest=1;

	/**
	 * 
	 * @param line
	 * @param lengthToReduce
	 * @return line in an actordata element shortened by length at the end towards start
	 */
	public static ActorData reduceLenghtOfLine(ActorData line, double lengthToReduce){
		
		double dx = line.getX_end() - line.getX();
		double dy = line.getY_end() - line.getY();
		double length = Math.sqrt(dx * dx + dy * dy);
		if (length > 0)
		{
		    dx /= length;
		    dy /= length;
		}
		dx *= length - lengthToReduce;
		dy *= length - lengthToReduce;
		
		double x = line.getX() + dx;
		double y = line.getY() + dy;
		return new ActorData(x, y);
	}

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
	public static double getWallLength(ActorData wallAd){
		return Math.sqrt((wallAd.getX() - wallAd.getX_end())*(wallAd.getX() - wallAd.getX_end()) + (wallAd.getY() - wallAd.getY_end())*(wallAd.getY() - wallAd.getY_end()));
	}

	public static ActorData getMidPoint(ActorData line){
		ActorData ad = new ActorData(0,0);
		ad.setX((line.getX()+line.getX_end()) / 2);
		ad.setY((line.getY()+line.getY_end()) / 2);
		return ad;
	}
	
	public static ActorData getWayAroundObstacle(ActorData collisionWall,boolean left) {
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

	
	public static Point getClosestPointToAD(ActorData ad){
		Point point = null;
		double shortestDistance = Double.MAX_VALUE;
		for(int i = 0; i< ActorList.get(ActorName.POINT).size();i++){
			double distanceToPoint = util.Calculation.getDistance(ad, ActorList.get(ActorName.POINT).get(i).getActorData());
			if (distanceToPoint < shortestDistance){
				point = (Point)ActorList.get(ActorName.POINT).get(i);
				shortestDistance = distanceToPoint;
			}
		}
		return point;
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
//			if(!(ad.getX()==target.getX() && ad.getY()==target.getY())){
				if(Line2D.Double.linesIntersect(ad.getX(), ad.getY(), target.getX(), target.getY(),wallAd.getX(),wallAd.getY(),wallAd.getX_end(),wallAd.getY_end())){
					ActorData collisionPoint = wallCollisionPoint(wallActor.getActorData(), ad, target);
					collisionPoints.add(new WallCollision(wallAd,collisionPoint));
				}
//			}
		}
		return collisionPoints;
	}
	
	public static ActorData intersectionPoint(ActorData ad1, ActorData ad2) {
		int x1 = (int) ad1.getX(); int x2 = (int) ad1.getX_end();
		int y1 = (int) ad1.getY(); int y2 = (int) ad1.getY_end();
		
		int x3 = (int) ad2.getX(); int x4 = (int) ad2.getX_end();
		int y3 = (int) ad2.getY(); int y4 = (int) ad2.getY_end();
		
		int d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) return null;
		int xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		int yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		return new ActorData(xi,yi);
	}

	
	public static ActorData getClosestCollisionObject(ActorData ad, ArrayList<WallCollision> collisionPoints) {
		return getMinMaxDistCollisionObject(ad, collisionPoints, closest);
	}
	
	public static ActorData getFurthestCollisionObject(ActorData ad, ArrayList<WallCollision> collisionPoints) {
		return getMinMaxDistCollisionObject(ad, collisionPoints,furthest);
	}

	public static ActorData getCollisionObjectsSortedByDist(ActorData ad, ArrayList<WallCollision> collisionPoints){
		return ad;
		
	}
	

	 public static void sort(ArrayList<WallCollision> wc, ActorData ad) {
	    if (wc ==null || wc.size()<2){
	      return;
	    }
	    
	    quicksortAsc(wc, ad, 0,  wc.size()-1);
	  }

	
	 private static void quicksortAsc(ArrayList<WallCollision> wc, ActorData ad, int low, int high) {
		 	int i = low, j = high;
		    WallCollision pivotObj = wc.get(low + (high-low)/2);
		    double pivot = getDistance(pivotObj.getCollisionPoint(), ad);
		    while (i<=j) {
		      while (getDistance(wc.get(i).getCollisionPoint(),ad) < pivot) {
		        i++;
		      }

		      while (getDistance(wc.get(j).getCollisionPoint(),ad) > pivot) {
		        j--;
		      }

		      if (i <= j) {
		        exchange(wc,i, j);
		        i++;
		        j--;
		      }
		    }
		    
		    if (low < j){
		    	quicksortAsc(wc,ad,low, j);
		    }
		    if (i < high){
		    	quicksortAsc(wc,ad,i, high);
		    }
	 }
	 private static void exchange(ArrayList<WallCollision>wc, int i, int j) {
		    WallCollision temp = wc.get(i);
		    wc.set(i, wc.get(j));
		    wc.set(j, temp);
	 }
	
	private static ActorData getMinMaxDistCollisionObject(ActorData ad, ArrayList<WallCollision> collisionPoints,int mode) {
		double distance;
		if(mode==closest){
			distance = Double.MAX_VALUE;			
		}else{
			distance = Double.MIN_VALUE;						
		}
		ActorData firstCollisionObject = null;
		for (WallCollision collisionPoint : collisionPoints) {
			double pointDistance = getDistance(collisionPoint.getCollisionPoint(), ad);
			if(mode==closest && pointDistance<distance || mode==furthest && pointDistance > distance){
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
