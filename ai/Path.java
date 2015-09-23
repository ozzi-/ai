package ai;

import java.util.ArrayList;

public class Path {

	private ArrayList<Step> steps = new ArrayList<Step>();
	
	public Path() {
	}
	
	public Path(ArrayList<Step> steps) {
		this.steps=steps;
	}

	public void addStep(Step step){
		steps.add(step);
	}
	
	public ArrayList<Step> getSteps() {
		return steps;
	}
	
	public Path getCopy(){
		System.out.println("COPY WITH "+steps.size());
		return new Path(new ArrayList<Step>(steps));			
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}


}
