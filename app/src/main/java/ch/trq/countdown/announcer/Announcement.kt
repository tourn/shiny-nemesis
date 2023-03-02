package ch.trq.countdown.announcer

/**
 * @author tourn
 * Represents a single announcement made by an announcer
 */
abstract class Announcement {
    /**
     * The amount of seconds this Announcement happens before the end of the countdown
     */
    var secondsToEnd = 0

    /**
     * Executes the Media action of this Announcement: Toasts, Vibration, Sounds...
     */
    abstract fun play()
}