**Android SQLite Notes App**

A simple and intuitive notes application built for Android, demonstrating fundamental CRUD (Create, Read, Update, Delete) operations using a local SQLite database. This app allows users to create, view, update, and delete notes directly on their device, providing a practical example of persistent data storage in Android.

**Features**

**Create Notes:** Easily add new notes with a title and content.
**View Notes:** Display all stored notes in a clear list.
**Update Notes:** Modify existing notes by their unique ID.
**Delete Notes:** Remove notes from the database using their ID.
**Local Data Storage:** All notes are stored securely on the device using SQLite, accessible offline.

Technologies Used
Language: Java
Platform: Android
Database: SQLite (built-in Android database)
IDE: Android Studio
-----

**Getting Started**

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

**Prerequisites**

**Android Studio:** Download and Install Android Studio
**Java Development Kit (JDK):** Android Studio typically bundles an appropriate JDK.
An Android device or emulator for running the app.

**Installation**

Clone the repository: 
git clone https://www.google.com/search?q=https://github.com/SentinelleTech/NoteNest.git

**Open in Android Studio:**

Open Android Studio.
Click File > Open...
Navigate to the cloned repository directory and select the project. Android Studio will then import and sync the project.

**Build and Run:**

Connect an Android device or start an Android emulator.
Click the Run button (green triangle) in Android Studio's toolbar.
Select your device/emulator to deploy the app.


**Project Structure (Key Files)**

app/src/main/java/com/yourpackage/sqlitenotesapp/MainActivity.java: The main activity handling UI interactions and invoking database operations.
app/src/main/java/com/yourpackage/sqlitenotesapp/DatabaseHelper.java: Extends SQLiteOpenHelper, responsible for creating and upgrading the database schema, and providing helper methods for CRUD operations.
app/src/main/res/layout/activity_main.xml: The XML layout file defining the user interface of the MainActivity.
app/src/main/res/values/strings.xml: Contains string resources, including the app name.
app/src/main/res/drawable/: Contains XML drawables for custom button and edit text styles.


**Database Schema**

The application uses a single table named notes to store note information.

Table Name: notes

Column Name	    Data Type (SQLite)	                    Description
id	            INTEGER PRIMARY KEY AUTOINCREMENT	      Unique identifier for each note
title	          TEXT NOT NULL	                          The title of the note
content	        TEXT	                                  The main body content of the note

SQL for creating the table (from DatabaseHelper.java):

CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT
);


**Contributing**

Contributions are welcome! If you have suggestions for improvements, find a bug, or want to add a new feature, please feel free to:

Fork the repository.
Create your feature branch (git checkout -b feature/AmazingFeature).
Commit your changes (git commit -m 'Add some AmazingFeature').
Push to the branch (git push origin feature/AmazingFeature).
Open a Pull Request.


**License**

This project is licensed under the MIT License.

**Contact**

John K. Macharia - sentinelletechno@gmail.com

Project Link: https://github.com/SentinelleTech/NoteNest/tree/master
