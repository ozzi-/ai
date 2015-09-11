package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import util.ActorList;
import util.ActorName;
import ai.Actor;
import config.Config;

public class Surface extends JComponent {

	private static final long serialVersionUID = 1L;

	public Surface() {
	}

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, Config.windowX,Config.windowY);
		for(int i = 0; i<ActorList.get(ActorName.BOT).size();i++){
			Actor actor = ActorList.get(ActorName.BOT).get(i);
			g2d.drawString(actor.getRepresentation(),actor.getActorData().getX(), actor.getActorData().getY());	
		}
		for(int i = 0; i<ActorList.get(ActorName.POINT).size();i++){
			Actor actor = ActorList.get(ActorName.POINT).get(i);
			g2d.drawString(actor.getRepresentation(),actor.getActorData().getX(), actor.getActorData().getY());	
		}
	}


}