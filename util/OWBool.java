package util;


public class OWBool {
	private boolean bool = false;
	public void set(boolean value){
		if(value){
			bool=true;			
		}
	}
	public void reset(){
		bool=false;
	}
	public boolean getState(){
		return bool;
	}
}
