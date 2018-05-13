package fr.doofapp.doof.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_ID = "id";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";
    public static final String USER_IS_CONECTED = "isconnected";
    public static final String USER_TOKEN = "token";

    public static final String USER_TABLE_CREATE = "CREATE TABLE " +
            USER_TABLE_NAME + " (" +
            USER_ID + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_TOKEN + " TEXT, " +
            USER_ROLE + " INTEGER, " +
            USER_IS_CONECTED + " INTEGER);";

    public static final String USER_TABLE_DROP = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";";

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(USER_TABLE_DROP);
        onCreate(db);
    }
}
