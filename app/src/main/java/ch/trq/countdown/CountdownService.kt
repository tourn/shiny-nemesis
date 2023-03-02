package ch.trq.countdown

import android.app.*
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import ch.trq.countdown.DialogActivity
import ch.trq.countdown.announcer.Announcer
import ch.trq.countdown.announcer.SoundAnnouncer
import ch.trq.countdown.announcer.VibrateAnnouncer
import java.sql.Date
import java.util.*

class CountdownService : Service() {
    private var timer: Timer? = null
    private var wakeLock: WakeLock? = null
    private var notificationManger: NotificationManager? = null
    private var countdownTimer: CountDownTimer? = null
    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    /**
     * Schedules sounds of the default voiceset
     *
     * @param endTime
     * @param sound
     * @param vibrate
     */
    private fun scheduleSounds(
        endTime: Long, vibrate: Boolean,
        sound: Boolean
    ) {
        timer = Timer()
        notificationManger = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val announcers: MutableList<Announcer> = ArrayList()
        if (vibrate) {
            announcers.add(VibrateAnnouncer(this))
        }
        if (sound) {
            try {
                announcers.add(SoundAnnouncer(this))
            } catch (e: SoundpackNotPresentException) {
                Toast.makeText(this, "Default soundpack wasn't found", Toast.LENGTH_SHORT).show()
            }
        }
        for (announcer in announcers) {
            for (announcement in announcer.generateAnnouncements()) {
                val schedule = endTime - 1000 * announcement.secondsToEnd - 1000
                if (schedule >= Calendar.getInstance().timeInMillis) {
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {
                            announcement.play()
                        }
                    }, Date(schedule))
                }
            }
        }
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                // wakeLock.release();
                try {
                    // give the last sound some time to finish
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                stopSelf()
            }
        }, Date(endTime))
        countdownTimer = object : CountDownTimer(
            endTime
                    - Calendar.getInstance().timeInMillis + 1000, 1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = Calendar.getInstance()
                remainingTime.timeInMillis = millisUntilFinished - 1000
                notificationManger!!.notify(1, buildNotification(endTime))
            }

            override fun onFinish() {}
        }.start()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //TODO prevent multiple execution
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "ch.trq.countdown:countdownlock"
        )
        wakeLock?.acquire(10*60*1000L /*10 minutes*/)
        val endTime = intent.getLongExtra(EXTRA_END_TIME, -1)
        val vibrate = intent.getBooleanExtra(EXTRA_VIBRATE, false)
        val sound = intent.getBooleanExtra(EXTRA_SOUND, false)
        scheduleSounds(endTime, vibrate, sound)
        Log.d(LOG_TAG, "Starting Countdown Service in Foreground")
        startForeground(1, buildNotification(endTime))
        Toast.makeText(
            this, getFormattedTimeRemaining(endTime) + " remaining",
            Toast.LENGTH_SHORT
        ).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {}
    private fun buildNotification(endTime: Long): Notification {
        if(notificationManger == null){
            throw Exception("BLEH")
        }
        val channelId = "timer"
        notificationManger
            ?.createNotificationChannel(NotificationChannel(channelId, "Timer Service", NotificationManager.IMPORTANCE_NONE))

        val remainingString = getFormattedTimeRemaining(endTime)
        val notificationIntent = Intent(this, DialogActivity::class.java)
        notificationIntent.putExtra(DialogActivity.INTENT_EXTRA_KILL, true)
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = Notification.Builder(
            this, channelId
        ).setContentTitle(getString(R.string.app_name))
            .setContentText("$remainingString left. Tap to cancel.")
            .setSmallIcon(R.drawable.ic_stat_general).setOngoing(true)
            .setWhen(endTime).setContentIntent(contentIntent)
        return builder.build()
    }

    private fun getFormattedTimeRemaining(endTime: Long): String {
        val millisUntilFinished = (endTime
                - Calendar.getInstance().timeInMillis)
        val remainingTime = Calendar.getInstance()
        remainingTime.timeInMillis = millisUntilFinished - 1000
        return (remainingTime[Calendar.MINUTE].toString() + ":"
                + secondsAsString(remainingTime[Calendar.SECOND]))
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "Service destroyed")
        timer!!.cancel()
        countdownTimer!!.cancel()
        if (wakeLock!!.isHeld) wakeLock!!.release()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_END_TIME = "endtime"
        const val EXTRA_VIBRATE = "vibrate"
        const val EXTRA_SOUND = "sound"
        private const val LOG_TAG = "CountdownService"

        /**
         * Returns the given second integer as a zero-padded string
         *
         * @param seconds
         * @return
         */
        fun secondsAsString(seconds: Int): String {
            val out = Integer.valueOf(seconds).toString()
            return if (out.length == 1) "0$out" else out
        }
    }
}