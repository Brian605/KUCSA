package com.return0.Kucsa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String _ID="_id";
    static final String REGNO="regno";
    static final String USER="user",PHONE="phone";
    static final String STUDYEAR="studyear",REGDATE="regdate";
    static final String COURSE="course";
    private static final String DB_NAME="Kucsa.DB";
    static final String TB_NAME="KUCSA_TABLE";
    private static final int VERSION=1;
     static String MSG_TABLE="Message_Table";
     static String MSG="message",STATUS="status",CATEGORY="category";

    private static final String SQL="CREATE TABLE IF NOT EXISTS "+TB_NAME +
            "("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+PHONE+" TEXT NOT NULL,"+
            REGNO+" TEXT NOT NULL,"+USER+" TEXT NOT NULL,"+
            COURSE+" TEXT NOT NULL,"+STUDYEAR+" TEXT NOT NULL,"+REGDATE+" TEXT NOT NULL)";

    private static final String query="CREATE TABLE IF NOT EXISTS "+MSG_TABLE+
            "("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+MSG+" TEXT NOT NULL,"+STATUS+" TEXT NOT NULL,"+CATEGORY+" TEXT NOT NULL)";

    DatabaseHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MSG_TABLE);
onCreate(sqLiteDatabase);

    }
}
