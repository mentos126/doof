package fr.doofapp.doof.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import fr.doofapp.doof.ClassMetier.User;

public class UserDAO extends DAOBase{

    public static final String TABLE_NAME = "user";
    public static final String EMAIL = "id";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String ISCONECTED = "isconnected";
    public static final String TOKEN = "token";


    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            EMAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PASSWORD + " TEXT, " +
            TOKEN + " TEXT, " +
            ROLE + " INTEGER, " +
            ISCONECTED + " INTEGER);";


    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public UserDAO(Context pContext) {
        super(pContext);
    }

    public void addUser(User m) {
        ContentValues value = new ContentValues();
        value.put(UserDAO.EMAIL, m.getUserId());
        value.put(UserDAO.PASSWORD, m.getPassword());
        value.put(UserDAO.TOKEN, m.getToken());
        value.put(UserDAO.ROLE, m.getRole());
        value.put(UserDAO.ISCONECTED, m.getConnected());
        mDb.insert(UserDAO.TABLE_NAME, null, value);
    }

    public void removeUser(String email) {
        mDb.delete(TABLE_NAME, EMAIL + " = ?", new String[] {String.valueOf(email)});
    }

    public void updateUSer(User m) {
        ContentValues value = new ContentValues();
        value.put(ISCONECTED, m.getConnected());
        mDb.update(TABLE_NAME, value, EMAIL  + " = ?", new String[] {String.valueOf(m.getUserId())});
    }

    public User getUserConnected() {
        String email = "";
        String token = "";
        String password = "";
        int role = -1;
        int isConnected = 0;
        String sqlQuery = "select " + UserDAO.EMAIL + ", " +
                UserDAO.PASSWORD + ", " +
                UserDAO.TOKEN + ", " +
                UserDAO.ROLE + ", "  +
                UserDAO.ISCONECTED + " " +
                " from " + UserDAO.TABLE_NAME +
                " where " + UserDAO.ISCONECTED + " = ?; ";
        Cursor c = mDb.rawQuery(sqlQuery, new String[]{"1"});
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getInt(4) == 1){
                email = c.getString(0);
                password = c.getString(1);
                token = c.getString(2);
                role = c.getInt(3);
                isConnected = c.getInt(4);
            }
            break;
        }
        c.close();
        User u = new User(email, password, token, role, isConnected);
        return u;
    }



}
