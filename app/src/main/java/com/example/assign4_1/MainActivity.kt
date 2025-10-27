package com.example.assign4_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.assign4_1.ui.theme.Assign4_1Theme

class MainActivity : ComponentActivity() {
    // Define a constant for our Logcat tag.
    // This helps in filtering messages specifically for this app.
    private val TAG = "ActivityStateTransition"

    /**
     * `onCreate` is the very first method called when the activity is created.
     * This is where you set up the activity, including setting the Compose content.
     * State Transition: (Does not exist) -> Created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log that the Activity's onCreate method has been called.
        Log.d(TAG, "[Activity] ==> onCreate: The Activity is being created.")

        // `setContent` is the entry point for Jetpack Compose.
        // It defines the UI for this activity.
        setContent {
            // Apply the app's theme.
            Assign4_1Theme {
                // Create a Scaffold with a Surface.
                Greeting("Android")
            }
        }
    }

    /**
     * `onStart` is called when the activity is becoming visible to the user.
     * State Transition: Created -> Started
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "[Activity] ==> onStart: The Activity is becoming visible.")
    }

    /**
     * `onResume` is called when the activity will start interacting with the user.
     * The app is now in the foreground.
     * State Transition: Started -> Resumed (App is running and interactive)
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "[Activity] ==> onResume: The Activity is interactive.")
    }

    /**
     * `onPause` is called when the activity is going into the background.
     * This happens when the user navigates away (e.g., presses Home or Back).
     * State Transition: Resumed -> Paused
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "[Activity] ==> onPause: The Activity is going into the background.")
    }

    /**
     * `onStop` is called when the activity is no longer visible to the user.
     * State Transition: Paused -> Stopped
     */
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "[Activity] ==> onStop: The Activity is no longer visible.")
    }

    /**
     * `onDestroy` is the final call you receive before your activity is destroyed.
     * This happens when the user presses Back or the system needs to reclaim memory.
     * State Transition: Stopped -> Destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        // Note: super.onDestroy() is called last here to ensure our log is sent before destruction.
        Log.d(TAG, "[Activity] ==> onDestroy: The Activity is being destroyed.")
    }
}

class MyViewModel: ViewModel() {
    var name by rememberSaveable{ mutableStateOf({}) }
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