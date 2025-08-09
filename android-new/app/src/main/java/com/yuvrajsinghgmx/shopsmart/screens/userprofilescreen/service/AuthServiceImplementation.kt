package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthService {

    override fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthState> = callbackFlow { // Add activity
        trySend(AuthState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // Called when verification is complete automatically (e.g., on some devices)
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            trySend(AuthState.AuthSuccess)
                        } else {
                            val errorMessage = task.exception?.message ?: "Auto-verification failed."
                            trySend(AuthState.Error(errorMessage))
                        }
                    }
            }

            // Called when there's an error
            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthState.Error(e.message ?: "An unknown error occurred."))
            }

            // Called when the OTP is sent to the user's phone
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                trySend(AuthState.CodeSent(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose { /* Cleanup */ }

    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthState> = callbackFlow {
        trySend(AuthState.Loading)

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthState.AuthSuccess)
                } else {
                    val errorMessage = task.exception?.message ?: "Invalid OTP or verification failed."
                    trySend(AuthState.Error(errorMessage))
                }
                channel.close() // Close the flow after completion
            }

        awaitClose { /* Cleanup */ }
    }

    override fun getLoggedInUser(): String? {
        return auth.currentUser?.phoneNumber
    }
}