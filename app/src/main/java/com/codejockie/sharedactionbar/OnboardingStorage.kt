package com.codejockie.sharedactionbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class OnboardingState {
    NotOnboarded,
    Onboarded
}

enum class OnboardingResult {
    Completed,
    Cancelled
}

@Singleton
class OnboardingStorage @Inject constructor() {
    private val _onboardingState = MutableStateFlow(OnboardingState.NotOnboarded)
    val onboardingState = _onboardingState.asStateFlow()

    fun onOnboarded() {
        _onboardingState.value = OnboardingState.Onboarded
    }
}