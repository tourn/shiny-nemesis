package ch.trq.countdown.announcer

import android.os.Vibrator
import android.util.Log
import ch.trq.countdown.announcer.Announcement

class VibrateAnnouncement(vibrator: Vibrator, secondsToEnd: Int, pattern: LongArray) :
    Announcement() {
    private val pattern: LongArray
    private val vibrator: Vibrator

    /**
     * @param vibrator the android system vibrator
     * @param secondsToEnd @see [Announcement]
     * @param pattern a pattern to be passed to @link [Vibrator]
     */
    init {
        super.secondsToEnd = secondsToEnd
        this.pattern = pattern
        this.vibrator = vibrator
    }

    override fun play() {
        vibrator.vibrate(pattern, -1)
        Log.d(
            "VibrateAnnouncement",
            Integer.valueOf(secondsToEnd).toString() + " Vibrating " + pattern.toString()
        )
    }
}