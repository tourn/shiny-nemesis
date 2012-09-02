package ch.bulletproof.countdown;

import java.io.IOException;
import java.util.HashSet;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class Player extends MediaPlayer{
	private String currentVoiceSetDir;
	private String baseVoiceSetDir;
	
	private HashSet<Sound> sounds = new HashSet<Sound>();
	
	public Player(String baseVoiceSetDir, String currentVoiceSetDir){
		this.baseVoiceSetDir = baseVoiceSetDir;
		this.currentVoiceSetDir = currentVoiceSetDir;

		sounds.add(new Sound(60*5, baseVoiceSetDir + "/" + currentVoiceSetDir + "/min5.wav"));
		sounds.add(new Sound(60*3, baseVoiceSetDir + "/" + currentVoiceSetDir + "/min3.wav"));
		sounds.add(new Sound(60*2, baseVoiceSetDir + "/" + currentVoiceSetDir + "/min2.wav"));
		sounds.add(new Sound(60*1, baseVoiceSetDir + "/" + currentVoiceSetDir + "/min1.wav"));
		sounds.add(new Sound(30, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec30.wav"));
		sounds.add(new Sound(20, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec20.wav"));
		sounds.add(new Sound(10, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec10.wav"));
		sounds.add(new Sound(9, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec9.wav"));
		sounds.add(new Sound(8, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec8.wav"));
		sounds.add(new Sound(7, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec7.wav"));
		sounds.add(new Sound(6, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec6.wav"));
		sounds.add(new Sound(5, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec5.wav"));
		sounds.add(new Sound(4, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec4.wav"));
		sounds.add(new Sound(3, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec3.wav"));
		sounds.add(new Sound(2, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec2.wav"));
		sounds.add(new Sound(1, baseVoiceSetDir + "/" + currentVoiceSetDir + "/sec1.wav"));
	}
	
	public HashSet<Sound> getSounds(){
		return sounds;
	}
	
	public boolean play(Sound sound){
		Log.d("Player", "Play Sound: "+sound.getFileName());
		reset();
		if(isPlaying())
			return false;
		
		try	{
			setDataSource(sound.getFileName());
			prepare();
			Log.d("Player", "Prepared");
		} catch (IOException e){
			//TODO Exception handling
			Log.d("Player", "IOException", e);
			return false;
		}
		
		start();
		Log.d("Player", "Started");
		
		return true;
	}

}
