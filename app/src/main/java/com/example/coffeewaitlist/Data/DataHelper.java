package com.example.coffeewaitlist.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.coffeewaitlist.Data.DataContract.WaitListEntry;
import com.example.coffeewaitlist.WaitList;

import static java.sql.Types.INTEGER;

public class DataHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "waitlist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + WaitListEntry.TABLE_NAME + " (" +
                WaitListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WaitListEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitListEntry.COLUMN_PARTY_SIZE + " TEXT NOT NULL, " +
                WaitListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + WaitListEntry.TABLE_NAME);
        onCreate(db);
    }
}
