package com.example.assign4_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.assign4_1.ui.theme.Assign4_1Theme
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.format
import java.util.Locale


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
                LifecycleScreen(viewModel = viewModel)
            }
        }
    }
}

class MyViewModel: ViewModel() {
    var events by mutableStateOf(emptyList<LifeCycleEvent>())
    fun append(event: LifeCycleEvent) { events += event }
}

/**
 * A Composable function that displays instructions and observes its own lifecycle events.
 *
 * @param lifecycleOwner The LifecycleOwner (typically the Activity) whose lifecycle will be observed.
 */
@Composable
fun LifecycleScreen(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current, viewModel: MyViewModel = MyViewModel()) {
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
            Log.d(TAG, viewModel.events.toString())
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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.events.size){ index ->
            Text(
                text = "${viewModel.events[index].timestamp} - ${viewModel.events[index].name}",
                textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assign4_1Theme {
        Greeting("Android")
    }
}