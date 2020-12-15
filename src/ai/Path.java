package ai;

import java.util.ArrayList;

import util.Calculation;

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
		Path newPath = new Path();
		for (Step step : steps) {
			newPath.addStep(step);			
		}
		return newPath;
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
	
	static Path getPathShortestLength(ArrayList<Path> pathsList) {
		double check= Double.MAX_VALUE;
		Path bestPath=null;
		for (Path path : pathsList) {
			if(path.getPathLength()<check){
				bestPath=path;
				check=path.getPathLength();
			}
		}
		return bestPath;
	}
	
	public int optimizeStepsInternal() {
		int optimizedSteps=0;
		ArrayList<Step> steps = this.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			for (int j = i+2; j < steps.size(); j++) {
				ArrayList<WallCollision> colPoints = Calculation.getCollisionPoints(steps.get(i).getStart(), steps.get(j).getStart());
				if(colPoints.size()==0){
					steps.get(i).setEnd(steps.get(j).getStart());
					steps.remove(j-1);
					optimizedSteps+=j-i-1;
				}
			}
		}
		return optimizedSteps;
	}
	
	@SuppressWarnings("unused")
	private static Path getPathLeastNodes(ArrayList<Path> pathsList) {
		int check= Integer.MAX_VALUE;
		Path bestPath=null;
		for (Path path : pathsList) {
			if(path.getSteps().size()<check){
				bestPath=path;
				check=path.getSteps().size();
			}
		}
		return bestPath;
	}
	

}
