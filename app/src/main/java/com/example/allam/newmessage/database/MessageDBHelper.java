package com.example.allam.newmessage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.allam.newmessage.database.MessageDBSchema.DATABASE_NAME;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.BODY;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.DATE;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.USER;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.NAME;



public class MessageDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private String query = "create table " + NAME + "( " +
            "_id integer primary key autoincrement, " +
            USER + ", " +
            BODY + ", " +
            DATE + ")";

    public MessageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + NAME + "if exists");
        db.execSQL(query);
    }
}
