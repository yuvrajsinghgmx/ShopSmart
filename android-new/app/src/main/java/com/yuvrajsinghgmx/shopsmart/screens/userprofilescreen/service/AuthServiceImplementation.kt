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

    override fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flow<AuthState> = callbackFlow {
        trySend(AuthState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(AuthState.AuthSuccess)
                    } else {
                        trySend(AuthState.Error(task.exception?.message ?: "Auto-verification failed."))
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthState.Error(e.message ?: "An unknown error occurred."))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                trySend(AuthState.CodeSent(verificationId, token))
            }
        }

        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        // Add the token for resend requests
        if (resendingToken != null) {
            optionsBuilder.setForceResendingToken(resendingToken)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        awaitClose { /* Cleanup */ }
    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthState> = callbackFlow {
        trySend(AuthState.Loading)
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(AuthState.AuthSuccess)
            } else {
                trySend(AuthState.Error(task.exception?.message ?: "Invalid OTP."))
            }
            channel.close()
        }
        awaitClose { /* Cleanup */ }
    }

    override fun getLoggedInUser(): String? {
        return auth.currentUser?.phoneNumber
    }
}