package ch.trq.countdown.announcer

interface Announcer {
    /**
     * @return an array containing all the announcements this Announcer provides
     */
    fun generateAnnouncements(): Array<Announcement>
}