package com.example.moneysaver.presentation.utils

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.presentation.MainActivity

private val biometricsIgnoredErrors = listOf(
    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_USER_CANCELED,
    BiometricPrompt.ERROR_NO_BIOMETRICS
)

fun showBiometricPrompt(onSucceeded: () -> Unit, activity: MainActivity) {
    // 2
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Login")
        .setSubtitle("Login with fingerprint")
        .setNegativeButtonText("Cancel")
        .build()

    // 3
    val biometricPrompt = BiometricPrompt(
        activity,
        object : BiometricPrompt.AuthenticationCallback() {
            // 4
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                if (errorCode !in biometricsIgnoredErrors) {
                    Toast.makeText(
                        MoneySaver.applicationContext(),
                        "Authentication Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            // 5
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                onSucceeded()
            }

            // 6
            override fun onAuthenticationFailed() {
                Toast.makeText(
                    MoneySaver.applicationContext(),
                    "Authentication Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
    // 7
    biometricPrompt.authenticate(promptInfo)
}