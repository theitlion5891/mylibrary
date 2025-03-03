package com.fantafeat.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fantafeat.db";
    public static final String IMAGE_TABLE_NAME = "all_images";
    public static final String id = "id";
    public static final String image_url = "image_url";
    public static final String downloaded_image = "downloaded_image";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +IMAGE_TABLE_NAME+
                        " ("+id+" integer primary key autoincrement, "+image_url+" text,"+downloaded_image+" blob)"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+IMAGE_TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllImages(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ IMAGE_TABLE_NAME);
        db.close();
    }

    public void insertContact (String url, byte[] downloadImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(image_url, url);
        contentValues.put(downloaded_image, downloadImage);
        db.insert(IMAGE_TABLE_NAME, null, contentValues);
        db.close();
    }

    public boolean isAvailableImage(String name) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query="SELECT * FROM "+IMAGE_TABLE_NAME+" WHERE "+image_url+" = '"+name+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount()>0){
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }else{
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
    }

    public byte[] getImageFile(String name) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query="SELECT * FROM "+IMAGE_TABLE_NAME+" WHERE "+image_url+" = '"+name+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount()>0){
            if (cursor.moveToFirst()){
                byte[] imageFile=cursor.getBlob(cursor.getColumnIndex(downloaded_image));
                cursor.close();
                sqLiteDatabase.close();
                return imageFile;
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return null;

    }

}
