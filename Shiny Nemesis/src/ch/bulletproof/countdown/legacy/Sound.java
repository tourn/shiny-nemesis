package ch.bulletproof.countdown.legacy;

public class Sound {
	private int secToEnd;
	private String fileName;
	
	public Sound(int secToEnd, String fileName){
		this.secToEnd = secToEnd;
		this.fileName = fileName;
	}
	
	public int getSecToEnd(){
		return secToEnd;
	}
	
	public String getFileName(){
		return fileName;
	}
}
