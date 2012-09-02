package ch.bulletproof.countdown;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Setup {
	public static final String DEFAULT_VOICESET_DIR = "default";
	public static final String BASE_VOICESET_DIR = Environment.getExternalStorageDirectory().getPath() + "/ShinyNemesis/VoiceSets";
	
	public static void init(Context context){
		File defaultDir = new File(BASE_VOICESET_DIR + "/" + DEFAULT_VOICESET_DIR);
		
		defaultDir.mkdirs();
		
		String[] content = defaultDir.list();
		if(content.length > 0)
			return;

		writeSoundToDir(R.raw.min1, "min1.wav", context);
		writeSoundToDir(R.raw.min2, "min2.wav", context);
		writeSoundToDir(R.raw.min3, "min3.wav", context);
		writeSoundToDir(R.raw.min5, "min5.wav", context);
		writeSoundToDir(R.raw.sec30, "sec30.wav", context);
		writeSoundToDir(R.raw.sec20, "sec20.wav", context);
		writeSoundToDir(R.raw.sec10, "sec10.wav", context);
		writeSoundToDir(R.raw.sec9, "sec9.wav", context);
		writeSoundToDir(R.raw.sec8, "sec8.wav", context);
		writeSoundToDir(R.raw.sec7, "sec7.wav", context);
		writeSoundToDir(R.raw.sec6, "sec6.wav", context);
		writeSoundToDir(R.raw.sec5, "sec5.wav", context);
		writeSoundToDir(R.raw.sec4, "sec4.wav", context);
		writeSoundToDir(R.raw.sec3, "sec3.wav", context);
		writeSoundToDir(R.raw.sec2, "sec2.wav", context);
		writeSoundToDir(R.raw.sec1, "sec1.wav", context);
		
		content = defaultDir.list();
		Log.d(SelectActivity.class.toString(), "Files in Dir:");
		for (String file : content) {
			Log.d(SelectActivity.class.toString(), file);
		}
		
		Log.d(SelectActivity.class.toString(), "finished Setup");
	}
	
	private static void writeSoundToDir(int soundFile, String filename, Context context) {
		byte[] buffer=null;
		InputStream fIn = context.getResources().openRawResource(soundFile);
		int size=0;
		
		try {
			 size = fIn.available();
			 buffer = new byte[size];
			 fIn.read(buffer);
			 fIn.close();
		} catch (IOException e) {
			 Log.d("Setup", "IOException Buffer", e);
			 return;
		}
		
		String path=BASE_VOICESET_DIR + "/" + DEFAULT_VOICESET_DIR;
		
		boolean exists = (new File(path)).exists();
		if (!exists){new File(path).mkdirs();}
		
		FileOutputStream save;
		try {
			save = new FileOutputStream(path + "/" + filename);
			save.write(buffer);
			save.flush();
			save.close();
		} catch (FileNotFoundException e) {
			 Log.d("Setup", "FileNotFoundException", e);
			 return;
		} catch (IOException e) {
			 Log.d("Setup", "IOException FileOutputStream", e);
			return;
		}    
		return;
	}
}
