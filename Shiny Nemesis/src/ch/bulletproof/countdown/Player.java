package ch.bulletproof.countdown;

import java.util.HashSet;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class Player {
	private AudioManager audioManager;
	
	private SoundPool soundPool;
	
	private boolean loaded = false;
	
	private HashSet<Sound> sounds = new HashSet<Sound>();
	
	public Player(Activity activity){
	    this.audioManager = (AudioManager) activity.getSystemService(Activity.AUDIO_SERVICE);
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

	    sounds.add(new Sound(soundPool.load(activity, R.raw.min5, 1), 5*60));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.min3, 1), 3*60));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.min2, 1), 2*60));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.min1, 1), 1*60));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec30, 1), 30));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec20, 1), 20));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec10, 1), 10));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec9, 1), 9));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec8, 1), 8));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec7, 1), 7));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec6, 1), 6));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec5, 1), 5));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec4, 1), 4));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec3, 1), 3));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec2, 1), 2));
	    sounds.add(new Sound(soundPool.load(activity, R.raw.sec1, 1), 1));
	}
	
	public HashSet<Sound> getSounds(){
		return sounds;
	}
	
	public boolean play(int soundID){
		if(soundID == 0)
			return false;
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		
		soundPool.play(soundID, volume, volume, 1, 0, 1f);
		
		return true;
	}

}
