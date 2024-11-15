package com.makechi.pesa.tally;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SwitchPreferenceCompat biometricPref = findPreference("biometric_authentication");
        if (biometricPref != null) {
            biometricPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabled = (boolean) newValue;
                if (isEnabled) {
                    enableBiometrics();
                } else {
                    disableBiometrics();
                }
                return true;
            });
        }

        applyTheme(PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("pref_theme", "system"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (key.equals("pref_theme")) {
            String theme = sharedPreferences.getString(key, "system");
            applyTheme(theme);
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle(R.string.settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void enableBiometrics() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        assert sharedPreferences != null;
        sharedPreferences.edit().putBoolean("biometric_enabled", true).apply();
    }

    private void disableBiometrics() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        assert sharedPreferences != null;
        sharedPreferences.edit().putBoolean("biometric_enabled", false).apply();
    }

    private void applyTheme(String theme) {
        switch (theme) {
            case "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            case "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}