package com.johnestebanap.myapplication.db;


import static com.johnestebanap.myapplication.db.Config.SQL_CREATE_USERDOCUMENTS;
import static com.johnestebanap.myapplication.db.Config.SQL_DELETE_USERDOCUMENTS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "phva.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Log.e("FFFFFFFFFF", "onCreate: "+SQL_CREATE_USERDOCUMENTS);
        db.execSQL(SQL_CREATE_USERDOCUMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL(SQL_DELETE_USERDOCUMENTS);
        onCreate(db);
    }

}
