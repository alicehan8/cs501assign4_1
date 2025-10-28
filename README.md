## Assignment 4.1: LifeTracker – A Lifecycle-Aware Activity Logger
This app tracks all of the Android lifecycle events as they occur. 
In a LazyColumn, a continuous log of color-coded lifecycle state transitions is displayed alongside their timestamps. 
This log is stored using a ViewModel in order to allow for survival across configuration changes and recomposition, as we can see below, and each of the lifecycle events is captured with the LifecycleObserver. 
Additionally, there is a snackbar that appears upon new events, which can also be hidden with a button in the settings section of the page. 

**Running the app:** Clone this repo in Android Studio and run an emulator

### Below are screenshots of the app:
<img width="217" height="477" alt="Screenshot 2025-10-28 at 11 52 33 AM" src="https://github.com/user-attachments/assets/9bddc345-86f8-4b12-af77-d11e4c0c99dc" />

<img width="457" height="207" alt="Screenshot 2025-10-28 at 11 52 21 AM" src="https://github.com/user-attachments/assets/f3c86046-23c5-42bd-b7c1-c71bddab9a64" />
