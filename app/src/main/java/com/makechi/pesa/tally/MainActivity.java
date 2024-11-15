package com.makechi.pesa.tally;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makechi.pesa.tally.util.BetterActivityResult;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private final BetterActivityResult<Intent, ActivityResult> launcher = BetterActivityResult.registerForActivityResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BiometricManager biometricManager = BiometricManager.from(this);
//        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
//            case BiometricManager.BIOMETRIC_SUCCESS -> authenticateUser();
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
//                 BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
//                 BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
//                 BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
//                 BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
//                 BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> authenticateWithFallback();
//        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            bottomNavigationView.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.ic_home_outline);
            bottomNavigationView.getMenu().findItem(R.id.nav_transactions).setIcon(R.drawable.ic_receipt_long_outline);
            bottomNavigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.ic_settings_outline);

            if (menuItem.getItemId() == R.id.nav_home) {
                loadFragment(HomeFragment.newInstance());
                menuItem.setIcon(R.drawable.ic_home);
                return true;
            }

            if (menuItem.getItemId() == R.id.nav_goals) {
                loadFragment(GoalsFragment.newInstance());
                return true;
            }

            if (menuItem.getItemId() == R.id.nav_transactions) {
                loadFragment(TransactionsFragment.newInstance());
                menuItem.setIcon(R.drawable.ic_receipt_long);
                return true;
            }

            if (menuItem.getItemId() == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                menuItem.setIcon(R.drawable.ic_settings);
                return true;
            }

            return false;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void authenticateUser() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = getBiometricPrompt(executor);
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometrics_authentication))
                .setSubtitle(getString(R.string.biometrics_sub_title))
                .setNegativeButtonText(getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private BiometricPrompt getBiometricPrompt(Executor executor) {
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showErrorToast("Error: " + errString);
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                showToastMessage("Authentication success!");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToastMessage("Authentication failed");
            }
        };

        return new BiometricPrompt(this, executor, callback);
    }

    private void authenticateWithFallback() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (keyguardManager.isDeviceSecure()) {
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(
                    "Authentication required",
                    "Please authenticate to proceed"
            );

            if (intent != null) {
                launcher.launch(intent, result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        showToastMessage("Fallback Authentication succeeded!");
                    } else {
                        showToastMessage("Fallback Authentication failed");
                    }
                });
            }
        } else {
            showToastMessage("Device security is not enabled");
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
