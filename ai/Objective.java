package ai;

import java.util.ArrayList;


public class Objective {
	private String objType;
	private ActorData target;
	private double origTargetX;
	private double origTargetY;
	private boolean targetHasMoved;
	private Path pathToTarget;
	
	private ArrayList<Objective> stepsToObjective = new ArrayList<Objective>();
	
	public Objective(String objType, ActorData target) {
		this.objType = objType;
		this.target= target;
		if(target!=null){
			this.origTargetX=target.getX();
			this.origTargetY=target.getY();			
		}
	}
	
	public void addStep(Objective step){
		stepsToObjective.add(step);
	}
	
	public void removeCurrentStep(){
		stepsToObjective.remove(0);
	}
	
	public Objective getCurrentStep(){
		return stepsToObjective.get(0);
	}
	
	public ArrayList<Objective> getStepsToTarget(){
		return stepsToObjective;
	}
	
	public ActorData getTarget() {
		return target;
	}
	public void setTarget(ActorData target) {
		this.target = target;
	}
	public String getObj() {
		return objType;
	}
	public void setObj(String obj) {
		this.objType = obj;
	}

	public Path getPathToTarget() {
		return pathToTarget;
	}

	public void setPathToTarget(Path pathToTarget) {
		this.pathToTarget = pathToTarget;
	}

	public void resetOriginalTargetPosition(){
		this.origTargetX=target.getX();
		this.origTargetY=target.getY();
	}
	
	public boolean hasTargetHasMoved() {
		if(target.getX()!=origTargetX || target.getY()!=origTargetY){
			targetHasMoved=true;
		}else{
			targetHasMoved=false;
		}
		return targetHasMoved;			
	}


	public double getOrigTargetY() {
		return origTargetY;
	}

	public double getOrigTargetX() {
		return origTargetX;
	}
	
}
