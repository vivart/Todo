package com.example.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class PrefsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)
    }
}