package com.example.asus.story;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "story";

    // Contacts table name
    private static final String TABLE_STORY = "stories";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_TRANSACTION = "transactions";

    // Contacts Table Columns names
    private static final String KEY_ID = "s_id";
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_DESC = "desc";
    private static final String KEY_PHOTO = "photo";

    private static final String CAT_ID = "c_id";
    private static final String CAT_CAPTION = "caption";

    private static final String TRA_ID = "t_id";
    private static final String TRA_CID = "c_id";
    private static final String TRA_SID = "s_id";

    // STORY TABLE
    String CREATE_TABLE_CONTACTS="CREATE TABLE " + TABLE_STORY + "("
            + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CAPTION +" TEXT,"
            + KEY_DESC +" TEXT,"
            + KEY_PHOTO  +" BLOB" + ")";

    // CATEGORY TABLE
    String CREATE_TABLE_CATEGORIES="CREATE TABLE " + TABLE_CATEGORY + "("
            + CAT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CAT_CAPTION +" TEXT" + ")";

    // CATEGORY TABLE
    String CREATE_TABLE_TRANSACTION="CREATE TABLE " + TABLE_TRANSACTION + "("
            + TRA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRA_SID +" INTEGER,"
            + TRA_CID +" INTEGER " + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public long addstory(Story story){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_CAPTION, story.getCaption());
        values.put(KEY_DESC, story.getDesc());
        values.put(KEY_PHOTO, story.getImage() );

        long sid = db.insert(TABLE_STORY, null, values);
        db.close();
        return sid;
    }
    // CREATE CATEGORY
    public long createCategory(Categories cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAT_CAPTION, cat.getCaption());

        // insert row
        long tag_id = db.insert(TABLE_CATEGORY, null, values);

        db.close();
        return tag_id;
    }
    /**Getting All Stories*/
    public List<Story> getAllStoryByID(int id) {
        List<Story> storyList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STORY + " WHERE " + KEY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setID(Integer.parseInt(cursor.getString(0)));
                story.setcaption(cursor.getString(1));
                story.setdesc(cursor.getString(2));
                story.setImage(cursor.getBlob(3));


                // Adding contact to list
                storyList.add(story);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return storyList;
    }
    /**Getting All Stories*/
    public List<Story> getAllStories() {
        List<Story> storyList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STORY + " ORDER BY " + KEY_ID + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setID(Integer.parseInt(cursor.getString(0)));
                story.setcaption(cursor.getString(1));
                story.setdesc(cursor.getString(2));
                story.setImage(cursor.getBlob(3));


                // Adding contact to list
                storyList.add(story);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return storyList;
    }

    /**Getting All Stories*/
    public List<Story> getAllStorieswithlimit(int limit) {
        List<Story> storyList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STORY + " ORDER BY " + KEY_ID + " DESC " + " LIMIT " + limit;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("LIMIT QUERY",selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                Story story = new Story();
                story.setID(Integer.parseInt(cursor.getString(0)));
                story.setcaption(cursor.getString(1));
                story.setdesc(cursor.getString(2));
                story.setImage(cursor.getBlob(3));

                // Adding contact to list
                storyList.add(story);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return storyList;
    }

    /* getting all todos under single tag */
    public List<Story> getAllStoryByCategory(String tag_name) {
        List<Story> storyList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_STORY + " td, "
                + TABLE_CATEGORY + " tg, " + TABLE_TRANSACTION + " tt WHERE tg."
                + CAT_CAPTION + " = '" + tag_name + "'" + " AND tg." + CAT_ID
                + " = " + "tt." + TRA_CID + " AND td." + KEY_ID + " = "
                + "tt." + TRA_SID + " ORDER BY " + KEY_ID + " DESC ";

        Log.e("", "QUERY" + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setID(Integer.parseInt(cursor.getString(0)));
                story.setcaption(cursor.getString(1));
                story.setdesc(cursor.getString(2));
                story.setImage(cursor.getBlob(3));

                // adding to
                storyList.add(story);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return storyList;
    }

    /*Get All Categories */
    /**
     * getting all tags
     * */
    public List<Categories> getAllCategory() {
        List<Categories> catlist = new ArrayList<Categories>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        Log.e(" ",selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Categories cat1 = new Categories();
                cat1.setID(Integer.parseInt(c.getString(0)));
                cat1.setcaption(c.getString(1));
                // adding to tags list
                catlist.add(cat1);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return catlist;
    }

    public int CountCategory()
    {
        int count=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_CATEGORY;
        Cursor mCount = db.rawQuery(selectQuery, null);
        if (mCount!=null) {
            mCount.moveToFirst();
            count = mCount.getInt(0);
            mCount.close();
        }
        db.close();
        return count;
    }

    // GET selected Category ID
    public long getCategoryId(String category) {
        String selectQuery = "SELECT "+ CAT_ID +" FROM " + TABLE_CATEGORY + " WHERE " + CAT_CAPTION  + " = '" + category + "';"  ;

        Log.e("QUERY GET CATEGORY",selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        long cid=0;
        // looping through all rows and adding to list
        if(c!=null)
        {
            c.moveToFirst();
            cid = c.getLong(0);
            c.close();
        }
        db.close();
        return cid;
    }

    /* CREATE TRANSACTION */
    public long createTransaction(long s_id, long c_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRA_SID, s_id);
        values.put(TRA_CID, c_id);

        long tid = db.insert(TABLE_TRANSACTION, null, values);
        db.close();
        return tid;
    }

    /* Updating single contact **/

    public int updatestory(Story story, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAPTION, story.getCaption());
        values.put(KEY_PHOTO, story.getImage());


        // updating row
        return db.update(TABLE_STORY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     *Deleting single contact
     **/
/*
    public void deleteContact(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(Id) });
        db.close();
    }*/

    //
}
