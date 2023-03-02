package ch.trq.countdown.announcer

import android.app.Service
import android.content.Context
import android.os.Vibrator

/**
 * @author tourn
 *
 * Uses vibration to announce the time running out
 */
class VibrateAnnouncer(private val ctx: Context) : Announcer {
    override fun generateAnnouncements(): Array<Announcement> {
        val vibrator =
            ctx.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        return arrayOf(
            VibrateAnnouncement(vibrator, 10 * 60, PATTERN_X),
            VibrateAnnouncement(vibrator, 5 * 60, PATTERN_V),
            VibrateAnnouncement(vibrator, 3 * 60, PATTERN_III),
            VibrateAnnouncement(vibrator, 2 * 60, PATTERN_II),
            VibrateAnnouncement(vibrator, 60, PATTERN_I),
            VibrateAnnouncement(vibrator, 30, PATTERN_V),
            VibrateAnnouncement(vibrator, 20, PATTERN_V),
            VibrateAnnouncement(vibrator, 15, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 14, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 13, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 12, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 11, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 10, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 9, PATTERN_1ps),
            VibrateAnnouncement(vibrator, 8, PATTERN_2ps),
            VibrateAnnouncement(vibrator, 7, PATTERN_2ps),
            VibrateAnnouncement(vibrator, 6, PATTERN_2ps),
            VibrateAnnouncement(vibrator, 5, PATTERN_2ps),
            VibrateAnnouncement(vibrator, 4, PATTERN_4ps),
            VibrateAnnouncement(vibrator, 3, PATTERN_4ps),
            VibrateAnnouncement(vibrator, 2, PATTERN_4ps),
            VibrateAnnouncement(vibrator, 1, PATTERN_4ps),
            VibrateAnnouncement(vibrator, 0, PATTERN_LONG)
        )
    }

    companion object {
        private const val I = 200
        private const val V = 500
        private const val X = 1000
        private const val BREAK = 200
        private val PATTERN_X = longArrayOf(0, X.toLong())
        private val PATTERN_V = longArrayOf(0, V.toLong())
        private val PATTERN_III =
            longArrayOf(0, I.toLong(), BREAK.toLong(), I.toLong(), BREAK.toLong(), I.toLong())
        private val PATTERN_II = longArrayOf(0, I.toLong(), BREAK.toLong(), I.toLong())
        private val PATTERN_I = longArrayOf(0, I.toLong())
        private val PATTERN_1ps = longArrayOf(0, 125)
        private val PATTERN_2ps = longArrayOf(0, 125, 375, 125)
        private val PATTERN_4ps = longArrayOf(0, 125, 125, 125, 125, 125, 125, 125, 125)
        private val PATTERN_LONG = longArrayOf(0, 2000)
    }
}