package com.example.assign4_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.assign4_1.ui.theme.Assign4_1Theme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.format
import java.util.Locale
import kotlin.collections.get


data class LifeCycleEvent(
    val name: String,
    val timestamp: String
)

class MainActivity : ComponentActivity() {
    // Define a constant for our Logcat tag.
    // This helps in filtering messages specifically for this app.
    private val TAG = "ActivityStateTransition"
    private val viewModel: MyViewModel by viewModels()

    /**
     * `onCreate` is the very first method called when the activity is created.
     * This is where you set up the activity, including setting the Compose content.
     * State Transition: (Does not exist) -> Created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // `setContent` is the entry point for Jetpack Compose.
        // It defines the UI for this activity.
        setContent {
            // Apply the app's theme.
            Assign4_1Theme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LifecycleTracker(viewModel = viewModel)
                    Screen(viewModel = viewModel, modifier = Modifier)
//                }
            }
        }
    }
}

class MyViewModel: ViewModel() {
    var events by mutableStateOf(emptyList<LifeCycleEvent>())
    var showSnackbar by mutableStateOf(true)
    fun append(event: LifeCycleEvent) { events += event }
    fun toggle() { showSnackbar = !showSnackbar}
}

/**
 * A Composable function that displays instructions and observes its own lifecycle events.
 *
 * @param lifecycleOwner The LifecycleOwner (typically the Activity) whose lifecycle will be observed.
 */
@Composable
fun LifecycleTracker(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current, viewModel: MyViewModel = MyViewModel()) {
    // A constant for logging from within this Composable.
    val TAG = "ActivityStateTransition"

    // `DisposableEffect` is a side-effect Composable used for managing resources
    // that need to be cleaned up when the composable leaves the screen (is "disposed").
    // It's perfect for adding and removing observers.
    // The `key1 = lifecycleOwner` means this effect will re-run if the lifecycleOwner changes.
    DisposableEffect(lifecycleOwner) {
        // Create an observer that logs lifecycle events.
        val observer = LifecycleEventObserver { _, event ->
            // We can log the event that the Composable's observer receives.
            // This shows how a Composable can react to the Activity's state.
            Log.d(TAG, "[Composable] Observed Event: ${event.name}")

            // 1. Get current time in milliseconds
            val timestampMillis = System.currentTimeMillis()

            // 2. Format the timestamp into a readable string (e.g., "14:35:10.456")
            val simpleDateFormat = SimpleDateFormat(
                "HH:mm:ss.SSS",
                Locale.getDefault()
            )
            val formattedTimestamp = simpleDateFormat.format(Date(timestampMillis))

            viewModel.append(LifeCycleEvent(event.name, formattedTimestamp))
        }

        // Add the observer to the lifecycle of the owner (our Activity).
        lifecycleOwner.lifecycle.addObserver(observer)
        // The `onDispose` block is crucial. It's called when the Composable
        // is removed from the composition. We must clean up our observer here
        // to prevent memory leaks.
        onDispose {
            Log.d(TAG, "[Composable] Disposing Effect. Removing observer.")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

fun colorCode(status: String): Color {
    return when (status) {
        "ON_CREATE" -> Color.Blue
        "ON_START" -> Color.Green
        "ON_RESUME" -> Color.Cyan
        "ON_PAUSE" -> Color.Yellow
        "ON_STOP" -> Color.Magenta
        "ON_DESTROY" -> Color.Red
        else -> Color.Gray
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(viewModel: MyViewModel = MyViewModel(), modifier: Modifier){
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.events.size, viewModel.showSnackbar) {
        // Get the latest event, if the list is not empty.
        viewModel.events.lastOrNull()?.let { lastEvent ->
            if (viewModel.showSnackbar) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Lifecycle Event: ${lastEvent.name}"
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Column(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(modifier = modifier
                    .weight(8f)
                    .fillMaxSize()) {
                    items(viewModel.events.size) { index ->
                        Text(
                            text = "${viewModel.events[index].timestamp} - ${viewModel.events[index].name}",
                            textAlign = TextAlign.Center,
                            color = colorCode(viewModel.events[index].name)
                        )
                    }
                }
//                Row(modifier = modifier
//                    .weight(2f)
//                    .background(color=Color.Gray)
//                    .fillMaxSize()) {
//                    Text("Settings", textAlign = TextAlign.Left, fontSize = 24.sp, color = Color.White)
//                    Button(onClick = { viewModel.toggle() }, modifier = modifier.background(color=Color.Blue)) {
//                        Text(text = if (viewModel.showSnackbar) "Hide Snackbar" else "Show Snackbar", color = Color.White)
//                    }
//                }
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = modifier.fillMaxWidth()
            ){
                Text("Settings", textAlign = TextAlign.Left, fontSize = 24.sp)
                Button(onClick = { viewModel.toggle() }) {
                    Text(text = if (viewModel.showSnackbar) "Hide Snackbar" else "Show Snackbar", color = Color.White)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("LifeTracker – A Lifecycle-Aware Activity Logger", style = MaterialTheme.typography.titleLarge) },
            )
        }
    )

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Assign4_1Theme {
//        LifecycleScreen()
//    }
//}