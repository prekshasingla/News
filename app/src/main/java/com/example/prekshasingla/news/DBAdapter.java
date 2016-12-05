package com.example.prekshasingla.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by prekshasingla on 05/12/16.
 */
public class DBAdapter  {
    private static DatabaseHelper DBHelper;
    static SQLiteDatabase db;


    public DBAdapter(Context context)
    {
        DBHelper = new DatabaseHelper(context);
    }


    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public long insertSource(String sourceid, String source,String category, String logo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SOURCEID, sourceid);
        contentValues.put(DatabaseHelper.SOURCE, source);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        contentValues.put(DatabaseHelper.LOGO, logo);
        long id = db.insert(DatabaseHelper.SOURCE_TABLE, null, contentValues);
        return id;
    }

    public void close() {
        DBHelper.close();
    }

    public void deleteSource(String item) {
        String qry="delete from "+DatabaseHelper.SOURCE_TABLE+" where "+DatabaseHelper
                .SOURCE+" = '"+item+"';";
        db.execSQL(qry);
    }



    static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "NewsDB";

        private static final String SOURCE_TABLE = "presettable";

        private static final String SOURCEID="sourceid";
        private static final String SOURCE="source";
        private static final String CATEGORY = "category";
        private static final String LOGO = "logo";


        private static final String TAG = "DBAdapter";

        private static final int DATABASE_VERSION = 1;



        private static final String SOURCE_CREATE = "create table if not exists "
                + SOURCE_TABLE + "( "+ SOURCEID + " VARCHAR(25) primary key, " + SOURCE + " VARCHAR(25), " + CATEGORY + " VARCHAR(25), " + LOGO + " VARCHAR(25) , " +" );";


        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SOURCE_CREATE);
             }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS" + SOURCE_TABLE);
            onCreate(db);
        }
    }
}

