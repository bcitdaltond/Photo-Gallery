package bcitdaltond.application.myDatabase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by runej on 2017-09-19.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper db = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MPDB.db";
    public static final String DATABASE_ID = "id";

    //Driver Table and Columns
    //table name
    public static final String IMAGE_TABLE_NAME = "image";
    public static final String[] IMAGE_COLUMN_STRING = {"id", "uri", "caption", "date", "location"};

    //table name
    public static final String USER_TABLE_NAME = "user";
    //columns
    public static final String USER_COLUMN_USERNAME= "username";
    public static final String USER_COLUMN_PASSWORD = "password";
    public static final String USER_COLUMN_LNAME = "lastname";
    public static final String USER_COLUMN_FNAME = "firstname";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        //Uncomment this to update the database to change column layout
        this.onUpgrade(this.getWritableDatabase(), 1, 2);
    }

    public static DBHelper getInstance(Context context) {
        if (db == null) {
            db = new DBHelper(context.getApplicationContext());
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Creating Database Tables");
        String CREATE_TABLE;

        CREATE_TABLE = "CREATE TABLE " + IMAGE_TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uri TEXT," +
                "caption TEXT," +
                "date TEXT," +
                "location TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + USER_TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT," +
                "firstname TEXT," +
                "lastname TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS image");
        onCreate(db);
    }
    //
//
    //ADDING
    public void addImage(Image i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = i.getContent();
        db.insert(IMAGE_TABLE_NAME, null, values);
        db.close();
    }

    public void addUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_USERNAME, u.getUsername());
        values.put(USER_COLUMN_PASSWORD, u.getPassword());
        values.put(USER_COLUMN_FNAME, u.getFirstname());
        values.put(USER_COLUMN_LNAME, u.getLastname());

        db.insert(USER_TABLE_NAME, null, values);
        db.close();
    }
    //
//    //REMOVE
    public void removeImage(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IMAGE_TABLE_NAME, DATABASE_ID + " = ?",
                new String[] {id});
        db.close();
    }

    public void removeUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE_NAME, DATABASE_ID + " = ?",
                new String[] {id});
        db.close();
    }
    //
//    //GET

    public Image getImage(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(IMAGE_TABLE_NAME,
                IMAGE_COLUMN_STRING,
                DATABASE_ID + "=?",
                new String[] {id},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();//db.close();
        Image i = new Image(cursor);
        //cursor.close();
        db.close();
        return i;
    }

    public User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME,
                new String[] {DATABASE_ID, USER_COLUMN_USERNAME, USER_COLUMN_PASSWORD, USER_COLUMN_FNAME, USER_COLUMN_LNAME},
                DATABASE_ID + "=?",
                new String[] {id},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();//db.close();
        User u = new User(
                cursor.getInt(0), //id
                cursor.getString(1), //username
                cursor.getString(2), //password
                cursor.getString(3), //first name
                cursor.getString(4) //last name
        );
        cursor.close();
        db.close();
        return u;
    }
//
    //GET ALL
    public ArrayList<Image> getAllImages() {
        ArrayList<Image> images = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + IMAGE_TABLE_NAME + " ORDER BY ID ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Image temp = new Image(cursor);
                images.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }

    public ArrayList<Image> getDateImages(String date) {
        ArrayList<Image> images = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + IMAGE_TABLE_NAME + " ORDER BY ID ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Image temp = new Image(cursor);
                if (temp.getDate().equals(date)) {
                    images.add(temp);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }


//    public ArrayList<User> getAllUsers() {
//        ArrayList<User> users = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + USER_TABLE_NAME + " ORDER BY ID ASC";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                User temp = new User(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getString(5),
//                        cursor.getString(6),
//                        cursor.getInt(7)
//                );
//                users.add(temp);
//            } while (cursor.moveToNext());
//        }
//        //db.close();
//        cursor.close();
//        return users;
//    }
//
//    public void updateUser(String id, User newUser) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(USER_COLUMN_USERNAME, newUser.getUsername());
//        values.put(USER_COLUMN_PASSWORD, newUser.getPassword());
//        values.put(USER_COLUMN_FNAME, newUser.getFirstname());
//        values.put(USER_COLUMN_LNAME, newUser.getLastname());
//        values.put(USER_COLUMN_USERID, newUser.getUserid());
//        values.put(USER_COLUMN_TRUCKID, newUser.getTruckid());
//        values.put(USER_COLUMN_COMPANY, newUser.getCompany());
//
//        this.getWritableDatabase().update(
//                USER_TABLE_NAME,
//                values,
//                USER_COLUMN_ID + " = ?",
//                new String[]{id}
//        );
//    }

//    public void updateUser(String id, User newUser) {
//
//        String sql = "UPDATE user SET username='" + newUser.getUsername() + "' password='" + newUser.getPassword() + "' WHERE id='" + id + "'";
//
//    }


    public void dropAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS image");
        db.close();
    }

}
