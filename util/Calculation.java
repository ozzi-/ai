package util;

import ai.ActorData;

public class Calculation {

	
	public static double getDirection (ActorData target, ActorData source){
		return Math.atan2((target.getY()- source.getY()),(target.getX()- source.getX()) );
	}
	
	
	public static int getDistance(ActorData a, ActorData b){
		int xdiff = Math.abs(a.getX() - b.getX());
		int ydiff = Math.abs(a.getY() - b.getY());
		return (int) Math.sqrt(xdiff*xdiff+ydiff*ydiff);
	}
}
