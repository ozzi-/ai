package spawn;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import util.ActorList;
import util.ActorName;
import ai.Actor;
import ai.ActorData;
import ai.Bot;
import ai.Point;
import config.Config;

public class Spawn {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		ArrayList<Future<ActorData>> actorThreadList = new ArrayList<Future<ActorData>>();

		gui.JFRAME ex = new gui.JFRAME();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ex.setVisible(true);
			}
		});
		
		try {
			ActorList.add(ActorName.BOT);
			ActorList.add(ActorName.POINT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Actor> pointList = ActorList.get(ActorName.POINT);
		ArrayList<Actor> botList = ActorList.get(ActorName.BOT);
		
		for (int i = 0; i < Config.botCount; i++) {
			int x = ThreadLocalRandom.current().nextInt(0,Config.windowX);
			int y = ThreadLocalRandom.current().nextInt(0,Config.windowY);
			Bot actorbot = new Bot(new ActorData(x,y,5));
			actorThreadList.add(threadPool.submit(actorbot));
			botList.add(actorbot);
		}
		
	
		
		Point point = new Point(new ActorData(Config.windowX/2, Config.windowY/2,5));
		pointList.add(point);
	
		Point pointTwo = new Point(new ActorData(Config.windowX/3, Config.windowY/2,5));
		pointList.add(pointTwo);

		
		while (true) {
			long preActorTime = System.currentTimeMillis();
			actorThreadList.clear();
			for (int i = 0; i < Config.botCount; i++) {

				Actor actor = botList.get(i);
				actorThreadList.add(threadPool.submit(actor));
			}
			for (int i = 0; i < actorThreadList.size(); i++) {
				actorThreadList.get(i).get();
			}
			long afterActorTime = System.currentTimeMillis();
			long executionActorTime=afterActorTime-preActorTime;
			long sleepTime = Config.simTime-executionActorTime;
			Thread.sleep(sleepTime);
			ex.getSurface().repaint();

	
		}

	}
}
