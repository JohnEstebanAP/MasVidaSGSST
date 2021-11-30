package com.johnestebanap.myapplication.db;

import android.provider.BaseColumns;

public final class Config {

    private Config(){}

    protected static final class UserDocuments implements BaseColumns {
        protected static final String TABLE_NAME = "tb_document";
        protected static final String COLUMN_TYPE = "type";
        protected static final String COLUMN_URL = "url";
        protected static final String COLUMN_DESCRIPTION = "description";
        protected static final String COLUMN_STATUS = "status";
        protected static final String COLUMN_DATE = "date";
    }

    protected static final String SQL_CREATE_USERDOCUMENTS =
            "CREATE TABLE " + UserDocuments.TABLE_NAME + " (" +
                    UserDocuments._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    UserDocuments.COLUMN_TYPE + " TEXT NOT NULL, " +
                    UserDocuments.COLUMN_URL + " TEXT NOT NULL, " +
                    UserDocuments.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    UserDocuments.COLUMN_STATUS + " TEXT NOT NULL, " +
                    UserDocuments.COLUMN_DATE + " DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime' ))) ";


    protected static final String SQL_DELETE_USERDOCUMENTS =
            "DROP TABLE IF EXISTS " + UserDocuments.TABLE_NAME;

    protected static final String SQL_READ_USERDOCUMENTS =
            "SELECT * FROM " + UserDocuments.TABLE_NAME;


}
