package ch.bulletproof.countdown;

public class Sound {
	private int id = 0;
	private int secToEnd;
	
	public Sound(int id, int secToEnd){
		this.id = id;
		this.secToEnd = secToEnd;
	}
	
	public int getID(){
		return id;
	}
	
	public int getSecToEnd(){
		return secToEnd;
	}
}
