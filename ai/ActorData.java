package ai;

/**
 * Every Actor Implementation owns a ActorData Object where he stores his info
 * @author ozzi
 *
 */
public class ActorData {
	
	private int x;
	private int y;
	private int speed;

	public ActorData(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.setSpeed(speed);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
