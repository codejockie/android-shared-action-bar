package com.codejockie.sharedactionbar

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class HomeUiState(
    val isRefreshing: Boolean,
    val transactions: Unit?
)

@Immutable
sealed interface TransactionsUiState {
    data class Success(val transactions: Unit) : TransactionsUiState
    object Error : TransactionsUiState
    object Loading : TransactionsUiState
}

@HiltViewModel
class ReceivingViewModel @Inject constructor(
    repository: ReceivingRepository
) : ViewModel() {
    private val f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val dateTime = LocalDateTime.from(f.parse("2023-10-20T04:00:00Z"))

    private val response: Flow<Result<Unit>> =
        repository.getTransactions().asResult()

    private val isRefreshing = MutableStateFlow(true)

    val uiState: StateFlow<HomeUiState> = combine(
        response,
        isRefreshing
    ) { result, refreshing ->

        var transactions: Unit? = null
        val data: TransactionsUiState = when (result) {
            is Result.Success -> {
                isRefreshing.value = false
                transactions = result.data
                TransactionsUiState.Success(result.data)
            }
            is Result.Loading -> TransactionsUiState.Loading
            is Result.Error -> TransactionsUiState.Error
        }
        Log.i("RECEIVING_VIEW_MODEL", data.toString())
        HomeUiState(
            refreshing,
            transactions
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(
                isRefreshing = false,
                transactions = null
            )
        )
}