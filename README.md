# TrackExpenses

### Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Example run](#example-run)

#### General Info
TrackExpenses is an Android application which allows the user to add, edit, or delete any of the expenses that are displayed using a RecyclerView (expenses are saved in the internal storage of the device as a .txt file).<br>
The project contains a main activity which serves as a fragment container, and two fragments - one for displaying the expenses and another for adding or editing expenses.<br>
In addition, the project is built using MVVM architecture for separating the view and the logic (the adapter acts as the Model, the fragment for displaying the recyclerview is the View, and the View-Model holds the live data).<br>

The menu contains the following functionalities:
* Add a new expense
* Sum of current displayed expenses button
* Filter expenses by date. The user has the ability to choose the year, year-month, or leave both fields empty to display all expenses ever added
* Sort expenses by name, date, or amount. The chosen sorting method is saved using SharedPreferences to allow the expenses to be displayed by that method upon the next launch of the app

<br>A broadcast receiver is used when launching the app, which broadcasts the current month and year, thus, displaying the relevant expenses. Lastly, a foreground service is used as well, which uses another broadcast receiver that sends a daily notifiction to the user at 8 PM to enter their daily expenses.

#### Technologies
* JAVA SE Platform 15 (JDK 15.0.1)
* Android Studio IDE

#### Setup
APK file is included.

#### Example run
https://user-images.githubusercontent.com/80578540/221003704-efaaf9e5-b826-42e0-8b41-11799ecc9d31.mp4
