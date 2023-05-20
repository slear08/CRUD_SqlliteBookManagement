package com.example.sqllitebookmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Book> books;
    private ArrayAdapter<Book> adapter;

    private EditText editTextTitle;
    private EditText editTextAuthor;
    private Button buttonAddBook;
    private ListView listViewBooks;

    private BookDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        buttonAddBook = findViewById(R.id.buttonAddBook);
        listViewBooks = findViewById(R.id.listViewBooks);

        // Create a new instance of BookDBHelper
        dbHelper = new BookDBHelper(this);

        // Load books from the database
        loadBooks();

        // Set the click listener for adding a book
        buttonAddBook.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String author = editTextAuthor.getText().toString();

            // Create a new book object
            Book book = new Book(-1, title, author);

            // Add the book to the list
            books.add(book);

            // Insert the book into the database
            dbHelper.addBook(book);

            // Update the list view
            adapter.notifyDataSetChanged();

            // Clear the input fields
            editTextTitle.setText("");
            editTextAuthor.setText("");
        });

        // Set the long click listener for deleting or updating a book
        listViewBooks.setOnItemLongClickListener((parent, view, position, id) -> {
            Book selectedBook = books.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Update Book");
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_book, null);
            builder.setView(dialogView);

            final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
            final EditText editTextAuthor = dialogView.findViewById(R.id.editTextAuthor);
            Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
            Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);

            editTextTitle.setText(selectedBook.getTitle());
            editTextAuthor.setText(selectedBook.getAuthor());

            final AlertDialog dialog = builder.create();

            buttonUpdate.setOnClickListener(v -> {
                // Get the updated book information
                String updatedTitle = editTextTitle.getText().toString();
                String updatedAuthor = editTextAuthor.getText().toString();

                // Update the selected book object
                selectedBook.setTitle(updatedTitle);
                selectedBook.setAuthor(updatedAuthor);

                // Update the book in the database
                dbHelper.updateBook(selectedBook);

                // Update the list view
                adapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            });

            buttonDelete.setOnClickListener(v -> {
                // Delete the selected book from the database
                dbHelper.deleteBook(selectedBook);

                // Remove the book from the list
                books.remove(selectedBook);

                // Update the list view
                adapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            });

            dialog.show();

            return true;
        });
    }

    // Load books from the database
    private void loadBooks() {
        books = dbHelper.getAllBooks();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, books);
        listViewBooks.setAdapter(adapter);
    }
}
