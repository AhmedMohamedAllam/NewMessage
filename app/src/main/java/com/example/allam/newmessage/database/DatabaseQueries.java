package com.example.allam.newmessage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.allam.newmessage.Utiles.Utiles;
import com.example.allam.newmessage.model.Message;

import java.util.ArrayList;
import java.util.List;

import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.BODY;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.DATE;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.Cols.USER;
import static com.example.allam.newmessage.database.MessageDBSchema.MessagesTable.NAME;


public class DatabaseQueries {
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public DatabaseQueries(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MessageDBHelper(mContext).getWritableDatabase();
    }

    private ContentValues getContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(USER, message.getUser());
        values.put(BODY, message.getBody());
        values.put(DATE, message.getDate());
        return values;
    }

    public long addMessage(Message message) {
        ContentValues contentValues = getContentValues(message);
        long count = mDatabase.insert(NAME, null, contentValues);
        Log.i(Utiles.LOG_TAG, "inserted");
        return count;
    }

    public boolean deleteMessage(String id) {
        int deletedRows = mDatabase.delete(NAME, BODY + " = ? ", new String[]{id});
        return deletedRows > 0;
    }

    public List<Message> getAllMessages() {
        List<Message> messagesList = new ArrayList<>();
        Cursor cursor = mDatabase.query(NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String User = cursor.getString(cursor.getColumnIndex(USER));
            String Body = cursor.getString(cursor.getColumnIndex(BODY));
            String Date = cursor.getString(cursor.getColumnIndex(DATE));

            Message message = new Message(User, Body, Date);
            messagesList.add(message);
            cursor.moveToNext();
        }
        return messagesList;
    }

    public List<Message> getAllMessagesOfNumber(String number) {
        List<Message> messagesList = new ArrayList<>();
        Cursor cursor = mDatabase.query(NAME, null, USER + " = ? ", new String[]{number}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String User = cursor.getString(cursor.getColumnIndex(USER));
            String Body = cursor.getString(cursor.getColumnIndex(BODY));
            String Date = cursor.getString(cursor.getColumnIndex(DATE));
            Message message = new Message(User, Body, Date);
            messagesList.add(message);
            cursor.moveToNext();
        }
        return messagesList;
    }
}
