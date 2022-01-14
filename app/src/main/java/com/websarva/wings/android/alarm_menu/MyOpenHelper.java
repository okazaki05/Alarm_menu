package com.websarva.wings.android.alarm_menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "alarm_list";
    private static final String TABLE_NAME = "t_alarm";
    private static final String _ID = "_id";
    public static final String COLUMN_NO = "a_no";
    public static final String COLUMN_TIME= "a_time";
    public static final String COLUMN_SNOOZE = "a_snooze";
    public static final String COLUMN_NAME = "a_name";
    public static final String COLUMN_VOICE = "a_voice";



    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER primary key autoincrement," +
                    COLUMN_NO + " TEXT, " +
                    COLUMN_TIME + " STRING not null," +
                    COLUMN_SNOOZE + " STRING," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_VOICE + " TEXT not null)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
