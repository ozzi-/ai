package spawn;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import util.ActorList;
import util.ActorName;
import util.ObjectiveType;
import ai.Actor;
import ai.ActorData;
import ai.Bot;
import ai.Objective;
import ai.Point;
import ai.Wall;
import config.Config;

public class Spawn {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		System.out.println("Starting with "+Config.threadsInPool+" threads in the pool");
		ExecutorService threadPool = Executors.newFixedThreadPool(Config.threadsInPool);
		
		ArrayList<Future<ActorData>> actorThreadList = new ArrayList<Future<ActorData>>();

		try {
			ActorList.add(ActorName.BOT);
			ActorList.add(ActorName.POINT);
			ActorList.add(ActorName.WALL);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		ArrayList<Actor> pointList = ActorList.get(ActorName.POINT);
		ArrayList<Actor> botList = ActorList.get(ActorName.BOT);
		ArrayList<Actor> wallList = ActorList.get(ActorName.WALL);

			
		createWallJail(wallList);
		//createRandomWalls(wallList);
			
		Point point = new Point(new ActorData(220,250));
		pointList.add(point);

		Point pointTwo = new Point(new ActorData(Config.windowX/5, Config.windowY/3));
		pointList.add(pointTwo);

		
		Point pointThree = new Point(new ActorData(299,285));
		pointList.add(pointThree);
		
		for (int i = 0; i < Config.botCount; i++) {
			Bot actorbot = new Bot(new ActorData(860,540));
			ActorData actorDataBot = actorbot.getActorData();
			//actorDataBot.getObjectiveList().add(new Objective(ObjectiveType.GOTO,point.getActorData())); 
			actorDataBot.getObjectiveList().add(new Objective(ObjectiveType.FOLLOW,point.getActorData()));
			//actorDataBot.getObjectiveList().add(new Objective(ObjectiveType.PATROL,new ActorData(100, 100), new ActorData(600,600)));
			actorDataBot.setSpeed(10);
			actorThreadList.add(threadPool.submit(actorbot));
			botList.add(actorbot);
		}
		
		
		gui.JFRAME ex = new gui.JFRAME();		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ex.setVisible(true);
			}
		});
	
		
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
			if(sleepTime<1){
				sleepTime=1;
			}
			Thread.sleep(sleepTime);
			ex.getSurface().repaint();
		}
	}

	private static void createRandomWalls(ArrayList<Actor> wallList) {
		ActorData adWall;
		Wall wall;
		adWall = new ActorData(200, 200);
		adWall.setX_end(500);
		adWall.setY_end(700);
		wall = new Wall(adWall);
		wall.getActorData().setName("W1");
		wallList.add(wall);

		adWall = new ActorData(410, 400);
		adWall.setX_end(670);
		adWall.setY_end(910);
		wall = new Wall(adWall);
		wall.getActorData().setName("W2");
		wallList.add(wall);
		
		adWall = new ActorData(200, 500);
		adWall.setX_end(800);
		adWall.setY_end(300);
		wall = new Wall(adWall);
		wall.getActorData().setName("W3");
		wallList.add(wall);


		adWall = new ActorData(220, 510);
		adWall.setX_end(780);
		adWall.setY_end(340);
		wall = new Wall(adWall);
		wall.getActorData().setName("W4");
		wallList.add(wall);
		
		adWall = new ActorData(240, 520);
		adWall.setX_end(790);
		adWall.setY_end(350);
		wall = new Wall(adWall);
		wall.getActorData().setName("W5");
		wallList.add(wall);
	}

	private static void createWallJail(ArrayList<Actor> wallList) {
		ActorData adWall = new ActorData(100, 100);
		adWall.setX_end(100);
		adWall.setY_end(300);
		Wall wall = new Wall(adWall);
		wall.getActorData().setName("W1");
		wallList.add(wall);
		
		adWall = new ActorData(100, 300);
		adWall.setX_end(300);
		adWall.setY_end(300);
		wall = new Wall(adWall);
		wall.getActorData().setName("W2");
		wallList.add(wall);

		adWall = new ActorData(100, 100);
		adWall.setX_end(300);
		adWall.setY_end(100);
		wall = new Wall(adWall);
		wall.getActorData().setName("W3");
		wallList.add(wall);
		
		adWall = new ActorData(300, 300);
		adWall.setX_end(300);
		adWall.setY_end(100);
		wall = new Wall(adWall);
		wall.getActorData().setName("W4");
		wallList.add(wall);
		

		adWall = new ActorData(200,300);
		adWall.setX_end(300);
		adWall.setY_end(200);
		wall = new Wall(adWall);
		wall.getActorData().setName("W5");
		wallList.add(wall);

		
		adWall = new ActorData(180,480);
		adWall.setX_end(480);
		adWall.setY_end(180);
		wall = new Wall(adWall);
		wall.getActorData().setName("W6");
		wallList.add(wall);
		
		adWall = new ActorData(200,500);
		adWall.setX_end(500);
		adWall.setY_end(200);
		wall = new Wall(adWall);
		wall.getActorData().setName("W6");
		wallList.add(wall);
	}
}
