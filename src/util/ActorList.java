package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.Actor;

public class ActorList {

	private static Map<String, ArrayList<Actor>> actorLists = new HashMap<String, ArrayList<Actor>>();

	public static ArrayList<Actor> add(String identifier) throws Exception{
		if(actorLists.containsKey(identifier)){
			throw new Exception("identifier exists already!");
		}
		ArrayList<Actor> actorList = new ArrayList<Actor>();
		actorLists.put(identifier,actorList);
		return actorList;
	}

	public static ArrayList<Actor> get(String identifier){
		return actorLists.get(identifier);
	}

}
