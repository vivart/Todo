package com.example.todo.prefs

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.todo.R

class PrefsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)
    }
}