package no.geodata.authenticatetomap

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val model: T) : UiState<T>()
    data class Error(
        val title: String,
        val text: String,
    ) : UiState<Nothing>()
}
