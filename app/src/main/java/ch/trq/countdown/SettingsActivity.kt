package ch.trq.countdown

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.*

class SettingsActivity : PreferenceActivity(), OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        PreferenceManager.setDefaultValues(
            this, R.xml.preferences,
            false
        )
        initSummary(preferenceScreen)
    }

    override fun onResume() {
        super.onResume()
        // Set up a listener whenever a key changes
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the listener whenever a key changes
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        key: String
    ) {
        updatePrefSummary(findPreference(key))
    }

    private fun initSummary(p: Preference) {
        if (p is PreferenceGroup) {
            val pGrp = p
            for (i in 0 until pGrp.preferenceCount) {
                initSummary(pGrp.getPreference(i))
            }
        } else {
            updatePrefSummary(p)
        }
    }

    private fun updatePrefSummary(p: Preference) {
        if (p is ListPreference) {
            p.setSummary(p.entry)
        }
        if (p is EditTextPreference) {
            if (p.getTitle().toString().contains("assword")) {
                p.setSummary("******")
            } else {
                p.setSummary(p.text)
            }
        }
    }
}