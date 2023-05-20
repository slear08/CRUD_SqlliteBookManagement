package com.example.sqllitebookmanagement;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BookDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "books";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";

    public BookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_AUTHOR + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                @SuppressLint("Range") Book book = new Book(id, title, author);
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return books;
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(book.getId())};
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(book.getId())};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
}

