package ai;


public class Objective {
	private String objType;
	private ActorData target;
	
	public Objective(String objType, ActorData target) {
		this.objType = objType;
		this.target= target;
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
	
	
}
