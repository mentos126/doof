package fr.doofapp.doof.DataBase;

import android.content.ContentValues;
import android.content.Context;

import fr.doofapp.doof.ClassMetier.Profile;

public class ProfileDAO extends DAOBase {

    public static final String TABLE_NAME = "profile";
    public static final String PHOTO = "photo";
    public static final String NOTE_TOTAL = "note_total";
    public static final String NOTE_HOME = "note_home";
    public static final String NOTE_CLEANLESS = "note_cleanliness";
    public static final String NOTE_COOKED = "note_cooked²";
    public static final String FAMILY_NAME = "family_name";
    public static final String NAME = "name";
    public static final String BIRTH = "birth";
    public static final String AGE = "age";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            PHOTO + " TEXT, " +
            /*NOTE_TOTAL + " INTEGER, " +
            NOTE_HOME + " INTEGER," +
            NOTE_CLEANLESS + " INTEGER," +
            NOTE_COOKED + " INTEGER," +*/
            FAMILY_NAME + " TEXT," +
            NAME + " TEXT," +
            BIRTH + " TEXT," +
            AGE + " INTEGER );";


    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public ProfileDAO(Context pContext) {super(pContext);}

    public void addProfile(Profile p) {
        ContentValues value = new ContentValues();
        value.put(ProfileDAO.PHOTO, p.getPhoto());
        value.put(ProfileDAO.FAMILY_NAME, p.getFamilyName());
        value.put(ProfileDAO.NAME, p.getName());
        value.put(ProfileDAO.BIRTH, p.getBirth());
        value.put(ProfileDAO.AGE, p.getAge());
        mDb.insert(ProfileDAO.TABLE_NAME, null, value);
    }

    public void removeProfile() {
            mDb.delete(TABLE_NAME,  " 1 ", new String[] {});
    }

    /*public void updateUSer(User m) {
        ContentValues value = new ContentValues();
        value.put(ISCONECTED, m.getConnected());
        mDb.update(TABLE_NAME, value, EMAIL  + " = ?", new String[] {String.valueOf(m.getUserId())});
    }*/

    public Profile getProfile() {

        return null;
    }






}
