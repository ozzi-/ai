package ai;

public class WallCollision {
	private ActorData wall;
	private ActorData collisionPoint;
	
	public WallCollision(ActorData wall, ActorData collisionPoint) {
		this.wall=wall;
		this.collisionPoint=collisionPoint;
	}
	
	public ActorData getCollisionPoint() {
		return collisionPoint;
	}
	public void setCollisionPoint(ActorData collisionPoint) {
		this.collisionPoint = collisionPoint;
	}
	public ActorData getWall() {
		return wall;
	}
	public void setWall(ActorData wall) {
		this.wall = wall;
	}
	
}
