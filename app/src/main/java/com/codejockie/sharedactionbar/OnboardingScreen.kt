package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

const val OnboardingKey = "onboarding"
@Composable
fun OnboardingRoute(
    onboardingStorage: OnboardingStorage,
    popBackStack: () -> Unit,
) {
    OnboardingScreen(
        onOnboarded = {
            onboardingStorage.onOnboarded()
            popBackStack()
        },
    )
}

@Composable
fun OnboardingScreen(
    onOnboarded: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onOnboarded,
        ) {
            Text(
                text = "Click here to complete onboarding"
            )
        }
    }
}