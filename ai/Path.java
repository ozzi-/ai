package ai;

import java.util.ArrayList;

public class Path {

	private ArrayList<Step> steps = new ArrayList<Step>();
	private int currentStep =0 ;

	
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
		return new Path(new ArrayList<Step>(steps));			
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}
	
	/**
	 * Returns null if the path contains no steps
	 * @return
	 */
	public Step getCurrentStep(){
		if(steps.size()>0){
			return steps.get(currentStep);			
		}
		return null;
	}
	
	public double getPathLength(){
		double totLength=0;
		for (Step step : steps) {
			totLength+=step.getLength();
		}
		return totLength;
	}
	
	public boolean advanceStep(){
		
		if(currentStep<steps.size()-1){
			currentStep++;
			return true;
		}
		return false;
	}

}
