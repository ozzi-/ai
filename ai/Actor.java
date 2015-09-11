package ai;

import java.util.Random;
import java.util.concurrent.Callable;

public interface Actor extends Callable<ActorData>{
	Random rand = new Random(); 
	
	public ActorData call();
	public ActorData getActorData();
	public String getRepresentation();
}
