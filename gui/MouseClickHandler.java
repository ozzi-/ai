package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import util.ActorList;
import util.ActorName;
import util.Calculation;
import ai.Actor;
import ai.Point;
import config.Config;

public class MouseClickHandler extends MouseAdapter {
	private ArrayList<Actor> pointList;
	public MouseClickHandler() {
		pointList = ActorList.get(ActorName.POINT);
	}
	public void mousePressed(MouseEvent e) {
		for (Actor actor : pointList) {
			Point point = (Point) actor;
			int mouse_x = e.getX(); int mouse_y = e.getY();
			if(Calculation.xyIsInRadiusOfActor(mouse_x,mouse_y, point.getActorData())){
				point.toggleSelected();
			}
		}
	}		
}