package com.example.sqllitebookmanagement;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }

    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
    }
}
