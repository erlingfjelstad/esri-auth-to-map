package no.geodata.authenticatetomap

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel()
        setContent {
            val coroutineScope = rememberCoroutineScope()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    Log.d("ArcGIS-Main", "onCreate: $innerPadding")
                    coroutineScope.launch {
                        viewModel.authenticate()
                    }
                }) {
                    Text("Authenticate")
                }
                val uiState by viewModel.mapFlow.collectAsState()
                when (val state = uiState) {
                    is UiState.Error -> {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        Log.d("ArcGIS-Main", "onCreate: Error ${state.text}")
                        Log.d("ArcGIS-Main", "onCreate: Error ${state.title}")
                    }

                    UiState.Loading -> {
                        Log.d("ArcGIS-Main", "onCreate: LOADING")
                    }

                    is UiState.Success -> {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        Log.d("ArcGIS-Main", "onCreate: Success $uiState")
                    }
                }
            }
        }
    }
}