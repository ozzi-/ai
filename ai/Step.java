package ai;

import util.Calculation;

public class Step {
	private ActorData start;
	private ActorData end;
	private double length=Double.MIN_VALUE;
	
	public Step(ActorData start, ActorData end) {
		this.setStart(start);
		this.setEnd(end);
	}
	
	public ActorData getStart() {
		return start;
	}

	public void setStart(ActorData start) {
		this.start = start;
	}

	public ActorData getEnd() {
		return end;
	}

	public void setEnd(ActorData end) {
		this.end = end;
	}

	public double getLength() {
		if(length==Double.MIN_VALUE){
			length = Calculation.getDistance(start, end);
		}
		return length;
	}
	
}
