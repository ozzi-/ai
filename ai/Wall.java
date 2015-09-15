package ai;

import util.Calculation;

public class Wall implements Actor {
	
	private ActorData ad;
	
	public Wall(ActorData ad) {
		this.ad=ad;
		ad.setDirection(Math.toRadians(Calculation.getAngleOfLine(ad)));
		System.out.println(ad.getDirection());
	}

	@Override
	public ActorData call() {
		return null;
	}

	
	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return null;
	}

}
