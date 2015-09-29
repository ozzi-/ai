package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import util.ActorList;
import util.ActorName;
import ai.Actor;
import ai.Point;

public class MouseMotionHandler extends MouseMotionAdapter {
	private ArrayList<Actor> pointList;
	public MouseMotionHandler() {
		pointList = ActorList.get(ActorName.POINT);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		for (Actor actor : pointList) {
			Point point = (Point) actor;
			if(point.isSelected()){
				point.getActorData().setX_end(e.getX());
				point.getActorData().setY_end(e.getY());
			}
		}
		super.mouseMoved(e);
	}
}
