package fr.doofapp.doof.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "doof.db";

    protected SQLiteDatabase mDb = null;
    protected DataBaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DataBaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
