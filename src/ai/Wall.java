package ai;

import util.Calculation;

public class Wall implements Actor {
	
	private ActorData ad;
	
	public Wall(ActorData ad) {
		this.ad=ad;
		ad.setDirection(Math.toRadians(Calculation.getAngleOfLine(ad)));
	}

	@Override
	public ActorData call() {
		return null;
	}

	public double getLength(){
		return Math.sqrt((ad.getX() - ad.getX_end())*(ad.getX() - ad.getX_end()) + (ad.getY() - ad.getY_end())*(ad.getY() - ad.getY_end()));
	}
	
	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return null;
	}

	@Override
	public boolean isSelected() {
		return false;
	}


}
