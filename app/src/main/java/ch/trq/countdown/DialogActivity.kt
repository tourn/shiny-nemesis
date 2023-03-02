package ch.trq.countdown

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class DialogActivity : AppCompatActivity() {
    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { grants: Map<String, Boolean> ->
            if (grants.values.all { it }) {
                Log.i("me", "GOTTEM")
            } else {
                Log.i("me", "BOO")
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    fun getNextInstantTime(): LocalDateTime? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val today = LocalDate.now()
        val instantTimes = prefs.getString("instant_times","")
            ?.split("\n")
            ?.filter {it != ""}
            ?.map {
                val (hour, minute) = it.split(":")
                val datetime = today.atTime(hour.toInt(), minute.toInt())
                if(datetime.isAfter(LocalDateTime.now())) datetime else datetime.plusDays(1)
            }
            ?.sorted()
            ?: listOf()

        Log.i("ME", "IT" + instantTimes.joinToString(","))
        return instantTimes.firstOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Setup(this).verify()
        if (intent.getBooleanExtra(INTENT_EXTRA_KILL, false)) {
            stopCountdown()
            finish()
        }
        val instantTime = getNextInstantTime()

        setContentView(R.layout.activity_dialog)
        val hour = findViewById<View>(R.id.hour) as TextView
        val numberPicker = findViewById<View>(R.id.minute) as NumberPicker
        numberPicker.maxValue = 59
        numberPicker.minValue = 0
        val minute = instantTime?.minute ?: minute
        numberPicker.value = minute
        hour.text = getMatchingHour(minute).toString() + ":"
        val vibrate = findViewById<View>(R.id.cbVibrate) as CheckBox
        vibrate.isChecked = getCheckboxStatus(vibrate)
        val sound = findViewById<View>(R.id.cbSound) as CheckBox
        sound.isChecked = getCheckboxStatus(sound)
        val goButton = findViewById<View>(R.id.buttonGo) as Button
        goButton.setOnClickListener {
            numberPicker.clearFocus()
            storeCheckboxStatus(vibrate)
            storeCheckboxStatus(sound)
            storeMinute(numberPicker.value)
            if(startCountdown(numberPicker.value, vibrate.isChecked, sound.isChecked)){
                finish()
            }
        }
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            hour.text = getMatchingHour(newVal).toString() + ":"
        }
        findViewById<View>(R.id.button_settings).setOnClickListener {
            val intent = Intent(this@DialogActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dialog, menu)
        return true
    }

    /**
     * Starts countdown up to the given minute
     * @param minute
     */
    private fun startCountdown(minute: Int, vibrate: Boolean, sound: Boolean): Boolean {
        Log.i("me", "START COUNTDOWN")
        val permissions = arrayOf("android.permission.WAKE_LOCK", "android.permission.FOREGROUND_SERVICE", "android.permission.VIBRATE")


        val granted = permissions.map {p -> ContextCompat.checkSelfPermission(this.applicationContext, p)}.all {it == PackageManager.PERMISSION_GRANTED}
        if (!granted) {
            Log.i("me", "REQUEST PERMISSION")
            requestPermissionLauncher.launch(permissions)
            return false;
        }
        Log.i("me", "WHEE")
        if (minute >= 0 && minute <= 59) {
            val endTime = getNextMinuteOccurrence(minute).timeInMillis
            val serviceIntent = Intent(this, CountdownService::class.java)
            serviceIntent.putExtra(CountdownService.EXTRA_END_TIME, endTime)
            serviceIntent.putExtra(CountdownService.EXTRA_VIBRATE, vibrate)
            serviceIntent.putExtra(CountdownService.EXTRA_SOUND, sound)
            startService(serviceIntent)
            return true;
        } else {
            Toast.makeText(applicationContext, "Invalid minute", Toast.LENGTH_SHORT).show()
            return false;
        }
    }

    /**
     * Returns a calendar pointing to the closest time having the given minute as minute
     * Example: it's 14:22, input is 7
     * The output calendar is set to 15:07 today
     * @param minute
     * @return
     */
    private fun getNextMinuteOccurrence(minute: Int): Calendar {
        val out = Calendar.getInstance()
        if (out[Calendar.MINUTE] >= minute) out.add(Calendar.HOUR, 1)
        out[Calendar.SECOND] = 0
        out[Calendar.MINUTE] = minute
        return out
    }

    private fun getMatchingHour(minute: Int): Int {
        return getNextMinuteOccurrence(minute)[Calendar.HOUR_OF_DAY]
    }

    private fun stopCountdown() {
        val intent = Intent(this, CountdownService::class.java)
        stopService(intent)
    }

    private fun storeCheckboxStatus(box: CheckBox) {
        val prefs = getPreferences(MODE_PRIVATE)
        prefs.edit().putBoolean(getStorageKey(box), box.isChecked).apply()
    }

    private fun getCheckboxStatus(box: CheckBox): Boolean {
        val prefs = getPreferences(MODE_PRIVATE)
        return prefs.getBoolean(getStorageKey(box), false)
    }

    private fun getStorageKey(view: View): String {
        return "ch.bulletproof.view." + view.id
    }

    private val isRememberMinute: Boolean
        private get() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            return !prefs.getBoolean("use_offset", true)
        }

    private fun storeMinute(minute: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putString("minute", Integer.valueOf(minute).toString()).apply()
    }

    private val minute: Int
        private get() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            return if (isRememberMinute) {
                Integer.valueOf(prefs.getString("minute", "0"))
            } else {
                val offset = Integer.valueOf(prefs.getString("minute_offset", "0"))
                val minute = Calendar.getInstance()[Calendar.MINUTE]
                (offset + minute) % 60
            }
        }

    companion object {
        const val INTENT_EXTRA_KILL = "ch.bulletproof.countdown.DialogActivity.kill"
    }
}