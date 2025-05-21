package com.stech.notenest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etNoteTitle, etNoteContent, etNoteId;
    private Button btnAdd, btnView, btnUpdate, btnDelete;
    private ListView lvNotes;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize all UI views
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        etNoteId = findViewById(R.id.etNoteId);
        btnAdd = findViewById(R.id.buttonAdd);
        btnView = findViewById(R.id.buttonView);
        btnUpdate = findViewById(R.id.buttonUpdate);
        btnDelete = findViewById(R.id.buttonDelete);
        lvNotes = findViewById(R.id.lvNotes);

        //Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        //Obtain a writable database instance(This will call onCreate or onUpgrade if need be)
        database = dbHelper.getWritableDatabase();

        // Set up button click listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNotes();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });

        // Initially display notes when the app starts
        viewNotes();
    }

    /**
     * Adds a new note to the database.
     */
    private void addNote() {
        String title = etNoteTitle.getText().toString().trim();
        String content = etNoteContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        // ContentValues is used to store a set of values that a ContentResolver can process.
        // It maps column names to their values.
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);

        // Insert the new row, returning the primary key value of the new row.
        long newRowId = database.insert(DatabaseHelper.TABLE_NOTES, null, values);

        if (newRowId!= -1) {
            Toast.makeText(this, "Note added with ID: " + newRowId, Toast.LENGTH_SHORT).show();
            etNoteTitle.setText(""); // Clear title input
            etNoteContent.setText(""); // Clear content input
            viewNotes(); // Refresh the list of notes
        } else {
            Toast.makeText(this, "Error adding note", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retrieves and displays all notes from the database.
     */
    private void viewNotes() {
        List<String> notesList = new ArrayList<>();
        Cursor cursor = null; // Initialize cursor to null

        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String projection[] = {
                    DatabaseHelper.COLUMN_ID,
                    DatabaseHelper.COLUMN_TITLE,
                    DatabaseHelper.COLUMN_CONTENT
            };

            // How you want the results sorted in the resulting Cursor
            String sortOrder = DatabaseHelper.COLUMN_ID + " ASC";

            // Query the database
            cursor = database.query(
                    DatabaseHelper.TABLE_NOTES,   // The table to query
                    projection,                   // The columns to return
                    null,                         // The columns for the WHERE clause
                    null,                         // The values for the WHERE clause
                    null,                         // Don't group the rows
                    null,                         // Don't filter by row groups
                    sortOrder                     // The sort order
            );

            // Iterate through the cursor to extract data
            if (cursor!= null && cursor.moveToFirst()) {
                do {
                    // Get column indices
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                    int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
                    int contentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT);

                    // Check if column exists before trying to get its value
                    if (idIndex!= -1 && titleIndex!= -1 && contentIndex!= -1) {
                        long noteId = cursor.getLong(idIndex);
                        String noteTitle = cursor.getString(titleIndex);
                        String noteContent = cursor.getString(contentIndex);
                        notesList.add("ID: " + noteId + "\nTitle: " + noteTitle + "\nContent: " + noteContent);
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error viewing notes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor to release its resources
            if (cursor!= null) {
                cursor.close();
            }
        }

        // Populate the ListView with the notes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notesList);
        lvNotes.setAdapter(adapter);

        if (notesList.isEmpty()) {
            Toast.makeText(this, "No notes found.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates an existing note in the database based on its ID.
     */
    private void updateNote() {
        String idStr = etNoteId.getText().toString().trim();
        String title = etNoteTitle.getText().toString().trim();
        String content = etNoteContent.getText().toString().trim();

        if (idStr.isEmpty() || title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter ID, new title, and new content for update", Toast.LENGTH_SHORT).show();
            return;
        }

        long noteId = Long.parseLong(idStr);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);

        // Define 'where' part of query.
        String selection = DatabaseHelper.COLUMN_ID + " =?";
        // Specify arguments in placeholder order.
        String selectionArgs[] = { String.valueOf(noteId) };

        // Update the row(s)
        int count = database.update(
                DatabaseHelper.TABLE_NOTES,
                values,
                selection,
                selectionArgs);

        if (count > 0) {
            Toast.makeText(this, "Note(s) updated: " + count, Toast.LENGTH_SHORT).show();
            etNoteId.setText("");
            etNoteTitle.setText("");
            etNoteContent.setText("");
            viewNotes(); // Refresh the list
        } else {
            Toast.makeText(this, "No note found with ID: " + noteId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes a note from the database based on its ID.
     */
    private void deleteNote() {
        String idStr = etNoteId.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Please enter ID for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        long noteId = Long.parseLong(idStr);

        // Define 'where' part of query.
        String selection = DatabaseHelper.COLUMN_ID + " =?";
        // Specify arguments in placeholder order.
        String selectionArgs[] = { String.valueOf(noteId) };

        // Delete the row(s)
        int deletedRows = database.delete(
                DatabaseHelper.TABLE_NOTES,
                selection,
                selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(this, "Note(s) deleted: " + deletedRows, Toast.LENGTH_SHORT).show();
            etNoteId.setText("");
            viewNotes(); // Refresh the list
        } else {
            Toast.makeText(this, "No note found with ID: " + noteId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Close the database connection when the activity is destroyed
        if (database!= null && database.isOpen()) {
            database.close();
        }
        super.onDestroy();
    }
}