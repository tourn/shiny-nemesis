package ch.trq.countdown.announcer

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import ch.trq.countdown.Setup
import java.io.File
import java.util.regex.Pattern

class SoundAnnouncer @JvmOverloads constructor(
    private val ctx: Context,
    soundPackName: String? = "default"
) : Announcer {
    private val soundPool: SoundPool
    private val announcements: MutableList<SoundAnnouncement> = ArrayList()

    init {

        // verify soundpack directory
        val soundPack = Setup(ctx).getSoundpackDir(soundPackName)

        // XXX might make the stream configurable
        soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
        for (sound in soundPack.listFiles()) {
            add(sound)
        }
    }

    private fun add(sound: File) {
        val secondsToEnd = parseSecondsToEnd(sound.name)
        if (secondsToEnd == -1) {
            return
        }
        val soundId = soundPool.load(sound.path, 1)
        announcements.add(SoundAnnouncement(soundPool, soundId, secondsToEnd))
    }

    /**
     * Parses the filename, decoding how many seconds before the end of the
     * countdown it is meant to be played
     *
     * @param filename
     * @return the seconds or -1 on an error
     */
    private fun parseSecondsToEnd(filename: String): Int {
        try {
            val minute = Pattern.compile("^min([0-9]+)\\.")
            var matcher = minute.matcher(filename)
            if (matcher.find()) {
                return matcher.group(1).toInt() * 60
            }
            val second = Pattern.compile("^sec([0-9]+)\\.")
            matcher = second.matcher(filename)
            if (matcher.find()) {
                return matcher.group(1).toInt()
            }
        } catch (e: NumberFormatException) {
            // this actually shouldn't happen since the regex only matches
            // numbers
            Log.e(LOG_TAG, "error parsing seconds to end", e)
        }
        return -1
    }

    override fun generateAnnouncements(): Array<Announcement> {
        return announcements.toTypedArray()
    }

    companion object {
        private const val LOG_TAG = "SoundAnnouncer"
    }
}