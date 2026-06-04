package com.andresual.assesment_mandiri.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

abstract class BaseViewModel<T> : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    protected fun safeLaunch(
        showLoading: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            onError?.invoke(throwable)
                ?: run { _uiState.value = UiState.Error(throwable.message ?: "An unknown error occurred") }
        }

        viewModelScope.launch(handler) {
            if (showLoading) _uiState.value = UiState.Loading
            block()
        }
    }

    protected fun setSuccess(data: T) {
        _uiState.value = UiState.Success(data)
    }

    protected fun setError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    protected fun setLoading() {
        _uiState.value = UiState.Loading
    }

    protected suspend fun <R> handleResult(
        result: Result<R>,
        onSuccess: suspend (R) -> Unit,
        onError: (suspend (Throwable) -> Unit)? = null
    ) {
        if (result.isSuccess) {
            onSuccess(result.getOrThrow())
        } else {
            val exception = result.exceptionOrNull()
            onError?.invoke(exception ?: Exception("Unknown error"))
                ?: setError(exception?.message ?: "An unknown error occurred")
        }
    }
}
