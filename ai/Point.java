package ai;

import config.Config;

public class Point implements Actor{
	
	private ActorData ad;
	private String representation;
	private boolean selected = false;
	
	public Point(ActorData ad) {
		this.ad = ad;
		this.setRepresentation("0");
		this.ad.setRadius(Config.actorRadius);
	}
	
	@Override
	public ActorData call() {
		return ad;
	}

	@Override
	public ActorData getActorData() {
		return ad;
	}

	public String getRepresentation() {
		return representation;
	}


	public void setRepresentation(String representation) {
		this.representation = representation;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void toggleSelected() {
		setSelected(!selected);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if(selected){
			representation="<o>";
		}else{
			representation="0";
		}
	}

}
