package ai;

import java.util.ArrayList;

import util.ObjectiveType;

/**
 * Every Actor Implementation owns a ActorData Object where he stores his info
 * @author ozzi
 */
public class ActorData {
	
	private double x;
	private double y;
	private double x_end;
	private double y_end;
	
	private int speed;
	private int radius;
	
	private double direction;
	
	private ArrayList<Objective> objectiveList = new ArrayList<Objective>();
	
	
	public ActorData(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY (double y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return direction in polar
	 */
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getX_end() {
		return x_end;
	}

	public void setX_end(double x_end) {
		this.x_end = x_end;
	}

	public double getY_end() {
		return y_end;
	}

	public void setY_end(double y_end) {
		this.y_end = y_end;
	}


	public int getRadius() {
		return radius;
	}
	
	public int getDiameter() {
		return radius*2;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public ArrayList<Objective> getObjectiveList() {
		return objectiveList;
	}

	public void setObjectiveList(ArrayList<Objective> objectiveList) {
		this.objectiveList = objectiveList;
	}

	public void finishedCurrentObjective(){
		if(objectiveList.size()>0){
			objectiveList.remove(0);			
		}
	}
	
	public void insertObjective(Objective objective){
		objectiveList.add(0, objective);
	}
	
	public Objective getCurrentObjective(){
		if(objectiveList.size()>0){
			return objectiveList.get(0);			
		}else{
			return new Objective(ObjectiveType.IDLE, null);
		}
	}	
}
