package ch.trq.countdown

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Setup(private val context: Context) {
    private val root: File?
    private val soundPackRoot: File

    init {
        root = context.applicationContext.getExternalFilesDir(null)
        soundPackRoot = File(root, SOUNDPACK_DIR)
    }

    fun verify() {
        if (true) {
            Log.i(LOG_TAG, "Creating root directory" + root.toString())
            firstTimeSetup()
        } else {
            Log.d(LOG_TAG, "Root directory already exists")
        }
    }

    fun firstTimeSetup() {
        Log.i(LOG_TAG, "running first time setup")
        if (!root!!.exists()) {
            root.mkdirs()
        }
        val defaultSoundpack = File(soundPackRoot, SOUNDPACK_DEFAULT_DIR)
        if (!defaultSoundpack.exists()) {
            defaultSoundpack.mkdirs()
        }
        writeDefaultSoundpack(defaultSoundpack)
    }

    private fun writeDefaultSoundpack(dir: File) {
        writeSoundToDir(R.raw.min1, File(dir, "min1.wav"))
        writeSoundToDir(R.raw.min2, File(dir, "min2.wav"))
        writeSoundToDir(R.raw.min3, File(dir, "min3.wav"))
        writeSoundToDir(R.raw.min5, File(dir, "min5.wav"))
        writeSoundToDir(R.raw.sec30, File(dir, "sec30.wav"))
        writeSoundToDir(R.raw.sec20, File(dir, "sec20.wav"))
        writeSoundToDir(R.raw.sec10, File(dir, "sec10.wav"))
        writeSoundToDir(R.raw.sec9, File(dir, "sec9.wav"))
        writeSoundToDir(R.raw.sec8, File(dir, "sec8.wav"))
        writeSoundToDir(R.raw.sec7, File(dir, "sec7.wav"))
        writeSoundToDir(R.raw.sec6, File(dir, "sec6.wav"))
        writeSoundToDir(R.raw.sec5, File(dir, "sec5.wav"))
        writeSoundToDir(R.raw.sec4, File(dir, "sec4.wav"))
        writeSoundToDir(R.raw.sec3, File(dir, "sec3.wav"))
        writeSoundToDir(R.raw.sec2, File(dir, "sec2.wav"))
        writeSoundToDir(R.raw.sec1, File(dir, "sec1.wav"))
    }

    private fun writeSoundToDir(soundFile: Int, file: File) {
        var buffer: ByteArray? = null
        val fIn = context.resources.openRawResource(soundFile)
        var size = 0
        try {
            size = fIn.available()
            buffer = ByteArray(size)
            fIn.read(buffer)
            fIn.close()
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException Buffer", e)
            return
        }
        val save: FileOutputStream
        try {
            save = FileOutputStream(file)
            save.write(buffer)
            save.flush()
            save.close()
        } catch (e: FileNotFoundException) {
            Log.d(LOG_TAG, "FileNotFoundException", e)
            return
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException FileOutputStream", e)
            return
        }
        return
    }

    @Throws(SoundpackNotPresentException::class)
    fun getSoundpackDir(soundPackName: String?): File {
        val out = File(soundPackRoot, soundPackName)
        return if (out.isDirectory) {
            out
        } else {
            throw SoundpackNotPresentException("Tried to retrieve invalid soundpack dir: $out")
        }
    }

    companion object {
        private const val SOUNDPACK_DIR = "soundpacks"
        private const val SOUNDPACK_DEFAULT_DIR = "default"
        private const val LOG_TAG = "Setup"
    }
}