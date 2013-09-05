package ch.bulletproof.countdown;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class Setup {
	private Context context;
	private static final String SOUNDPACK_DIR = "soundpacks";
	private static final String SOUNDPACK_DEFAULT_DIR = "default";
	private static final String LOG_TAG = "Setup";
	
	private final File root;

	public Setup(Context context) {
		this.context = context;
		root = context.getApplicationContext().getExternalFilesDir(null);
	}
	
	public void verify(){
		if (true) {
			Log.i(LOG_TAG, "Creating root directory" + root.toString());
			firstTimeSetup();
		} else {
			Log.d(LOG_TAG, "Root directory already exists");
		}
	}

	void firstTimeSetup() {
		Log.i(LOG_TAG, "running first time setup");
		if (!root.exists()) {
			root.mkdirs();
		}
		File defaultSoundpack = new File(root, SOUNDPACK_DIR + "/"
				+ SOUNDPACK_DEFAULT_DIR);
		if (!defaultSoundpack.exists()) {
			defaultSoundpack.mkdirs();
		}
		writeDefaultSoundpack(defaultSoundpack);
	}

	private void writeDefaultSoundpack(File dir) {
		writeSoundToDir(R.raw.min1, new File(dir, "min1.wav"));
		writeSoundToDir(R.raw.min2, new File(dir, "min2.wav"));
		writeSoundToDir(R.raw.min3, new File(dir, "min3.wav"));
		writeSoundToDir(R.raw.min5, new File(dir, "min5.wav"));
		writeSoundToDir(R.raw.sec30, new File(dir, "sec30.wav"));
		writeSoundToDir(R.raw.sec20, new File(dir, "sec20.wav"));
		writeSoundToDir(R.raw.sec10, new File(dir, "sec10.wav"));
		writeSoundToDir(R.raw.sec9, new File(dir, "sec9.wav"));
		writeSoundToDir(R.raw.sec8, new File(dir, "sec8.wav"));
		writeSoundToDir(R.raw.sec7, new File(dir, "sec7.wav"));
		writeSoundToDir(R.raw.sec6, new File(dir, "sec6.wav"));
		writeSoundToDir(R.raw.sec5, new File(dir, "sec5.wav"));
		writeSoundToDir(R.raw.sec4, new File(dir, "sec4.wav"));
		writeSoundToDir(R.raw.sec3, new File(dir, "sec3.wav"));
		writeSoundToDir(R.raw.sec2, new File(dir, "sec2.wav"));
		writeSoundToDir(R.raw.sec1, new File(dir, "sec1.wav"));
	}

	private void writeSoundToDir(int soundFile, File file) {
		byte[] buffer = null;
		InputStream fIn = context.getResources().openRawResource(soundFile);
		int size = 0;

		try {
			size = fIn.available();
			buffer = new byte[size];
			fIn.read(buffer);
			fIn.close();
		} catch (IOException e) {
			Log.d(LOG_TAG, "IOException Buffer", e);
			return;
		}

		FileOutputStream save;
		try {
			save = new FileOutputStream(file);
			save.write(buffer);
			save.flush();
			save.close();
		} catch (FileNotFoundException e) {
			Log.d(LOG_TAG, "FileNotFoundException", e);
			return;
		} catch (IOException e) {
			Log.d(LOG_TAG, "IOException FileOutputStream", e);
			return;
		}
		return;
	}

}
