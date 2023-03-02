package ch.trq.countdown.announcer

import android.media.SoundPool

class SoundAnnouncement(private val soundPool: SoundPool, private val id: Int, secondsToEnd: Int) :
    Announcement() {
    /**
     * Create a new soundannouncement
     * @param soundPool the soundpool containing this announcement
     * @param soundId the id of this sound in the soundpool
     * @param secondsToEnd the amount of seconds before the end of the countdown this sound should be played
     */
    init {
        this.secondsToEnd = secondsToEnd
    }

    override fun play() {
        val volume = 1f
        soundPool.play(id, volume, volume, 0, 0, 1f)
    }
}