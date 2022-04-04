package com.example.lab3_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqlOpenHelper extends SQLiteOpenHelper {


    public SqlOpenHelper(@Nullable Context context) {
        super(context, "guest.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery =
                "CREATE TABLE GUEST_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "MESSAGE TEXT, " +
                        "SEND_AT TEXT)";

        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean add(GuestModel guest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("MESSAGE", guest.getMessage());
        cv.put("SEND_AT", guest.getSEND_AT());
        return db.insert("GUEST_TABLE", null, cv) >= 0;
    }

    public List<GuestModel> fetch() {
        List<GuestModel> guests = new ArrayList<>();

        String queryString = "SELECT * FROM GUEST_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            cursor.moveToFirst();
            do {
                guests.add(new GuestModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
            db.close();
            return guests;
        }
    }
}
