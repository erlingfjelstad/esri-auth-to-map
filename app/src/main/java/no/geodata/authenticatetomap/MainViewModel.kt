package no.geodata.authenticatetomap

import android.util.Log
import androidx.lifecycle.ViewModel
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.httpcore.authentication.TokenCredential
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.PortalItem
import com.arcgismaps.portal.Portal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _mapFlow = MutableStateFlow<UiState<ArcGISMap>>(UiState.Loading)
    val mapFlow = _mapFlow.asStateFlow()

    suspend fun authenticate() {

        _mapFlow.value = UiState.Loading

        TokenCredential.create(
            url = BuildConfig.PORTAL_URL,
            username = BuildConfig.USERNAME,
            password = BuildConfig.PASSWORD
        ).onSuccess { tokenCredential ->
            loadPortal(tokenCredential)
        }.onFailure { throwable ->
            Log.e(TAG, "authenticate failed", throwable)
            throwable.printStackTrace()
            _mapFlow.value = UiState.Error(
                title = "Error while constructing TokenCredential.create()",
                text = throwable.message ?: "Throwable doesn't have any message",
            )
        }
    }

    private suspend fun loadPortal(tokenCredential: TokenCredential) {
        ArcGISEnvironment.authenticationManager.arcGISCredentialStore.add(tokenCredential)

        val portal =
            Portal(url = BuildConfig.PORTAL_URL, connection = Portal.Connection.Authenticated)
        portal.load()
            .onSuccess {
                loadMap(PortalItem(portal = portal, itemId = BuildConfig.PORTAL_ITEM_ID))
            }
            .onFailure { throwable ->
                Log.e(TAG, "loadPortal failed", throwable)
                throwable.printStackTrace()
                _mapFlow.value = UiState.Error(
                    title = "Error while Portal.load()",
                    text = throwable.message ?: "Throwable doesn't have any message",
                )
            }
    }

    private suspend fun loadMap(portalItem: PortalItem) {
        val map = ArcGISMap(item = portalItem)
        map.load()
            .onSuccess {
                _mapFlow.value = UiState.Success(map)
            }
            .onFailure { throwable ->
                Log.e(TAG, "loadMap failed", throwable)
                throwable.printStackTrace()
                _mapFlow.value = UiState.Error(
                    title = "Error while Map.load()",
                    text = throwable.message ?: "Throwable doesn't have any message",
                )
            }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}