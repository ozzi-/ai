package ai;

public class Point implements Actor{
	
	private ActorData ad;

	public Point(ActorData ad) {
		this.ad = ad;
	}
	
	@Override
	public ActorData call() {
		return ad;
	}

	@Override
	public ActorData getActorData() {
		return ad;
	}

	@Override
	public String getRepresentation() {
		return "O";
	}

}
