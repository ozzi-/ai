package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import util.ActorList;
import util.ActorName;
import ai.Actor;
import ai.ActorData;
import ai.Objective;
import config.Config;

public class Surface extends JComponent {

	private static final long serialVersionUID = 1L;
	private ArrayList<Actor> botList;
	private ArrayList<Actor> pointList;
	private ArrayList<Actor> wallList;

	public Surface() {
		botList = ActorList.get(ActorName.BOT);
		pointList = ActorList.get(ActorName.POINT);
		wallList = ActorList.get(ActorName.WALL);
	}

    public void paint(Graphics g) {
    	
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, Config.windowX,Config.windowY);        

		for(int i = 0; i < botList.size();i++){
			Actor botActor = botList.get(i);
			ActorData botAd = botActor.getActorData();
			g2d.drawString(botActor.getActorData().getObjectiveList().size()+"OBL - "+botActor.getActorData().getCurrentObjective().getObj(),(int)botAd.getX(), (int)botAd.getY());
			Ellipse2D.Double circle = new Ellipse2D.Double(botAd.getX()-botAd.getRadius(), botAd.getY()-botAd.getRadius(), botAd.getDiameter(),botAd.getDiameter());
			g2d.draw(circle);
			if(Config.showBotDirection){
				g2d.drawLine((int)botAd.getX(),(int) botAd.getY(), (int)botAd.getX_end(),(int)botAd.getY_end());				
			}
			Objective currentObjective = botActor.getActorData().getCurrentObjective();
		}
		
		g2d.drawString(".    <EE1>   1", 860, 540);
		g2d.drawString(".    <EE2>   2", 683, 936);
		g2d.drawString(".    <EE4>   3", 191, 518);
		g2d.drawString(".    <EE4>   4", 190, 400);
		
		
		
		for(int i = 0; i < pointList.size();i++){
			Actor pointActor = pointList.get(i);
			g2d.drawString(pointActor.getRepresentation(),(int)pointActor.getActorData().getX(), (int)pointActor.getActorData().getY());	
		}
		
		for(int i = 0; i < wallList.size();i++){
			Actor wallActor = wallList.get(i);
			ActorData wallAd = wallActor.getActorData();
			g2d.drawLine((int)wallAd.getX(),(int)wallAd.getY(), (int) wallAd.getX_end(),(int)wallAd.getY_end());	
			g2d.drawString(wallAd.getName(), (int)wallAd.getX()+40, (int)wallAd.getY());
		}
		
	}


}