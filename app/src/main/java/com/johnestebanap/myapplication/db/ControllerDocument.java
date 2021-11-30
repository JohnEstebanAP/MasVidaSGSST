package com.johnestebanap.myapplication.db;

import static com.johnestebanap.myapplication.db.Config.SQL_READ_USERDOCUMENTS;
import static com.johnestebanap.myapplication.db.Config.UserDocuments.COLUMN_DESCRIPTION;
import static com.johnestebanap.myapplication.db.Config.UserDocuments.COLUMN_STATUS;
import static com.johnestebanap.myapplication.db.Config.UserDocuments.COLUMN_TYPE;
import static com.johnestebanap.myapplication.db.Config.UserDocuments.COLUMN_URL;
import static com.johnestebanap.myapplication.db.Config.UserDocuments.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class ControllerDocument extends DbHelper{

    Context context;

    public ControllerDocument(Context context) {
        super(context);
        this.context = context;
    }



    public long insertDocument(Document document){
        long result = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_TYPE, document.getTipoDoc());
            values.put(COLUMN_DESCRIPTION, document.getDescription());
            values.put(COLUMN_URL, document.getUrl());
            values.put(COLUMN_STATUS, document.getEstado());

            result = db.insert(TABLE_NAME, null, values);


        } catch (Exception ex){
                Toast.makeText(context, "Error: "+ex, Toast.LENGTH_LONG).show();
        }


        return result;
    }

    public ArrayList<Document> getListDocuments (){
        ArrayList<Document> listDocuments = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursorDocument = db.rawQuery(SQL_READ_USERDOCUMENTS, null);
        if (cursorDocument != null && cursorDocument.moveToFirst()){
            do {
                Document document = new Document();
                document.setTipoDoc(cursorDocument.getString(1));
                document.setUrl(cursorDocument.getString(2));
                document.setDescription(cursorDocument.getString(3));
                document.setEstado(cursorDocument.getString(4));

                listDocuments.add(document);
            }while (cursorDocument.moveToNext());
        }

        cursorDocument.close();
        return listDocuments;
    }


}
