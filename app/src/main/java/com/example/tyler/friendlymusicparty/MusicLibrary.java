package com.example.tyler.friendlymusicparty;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zissou on 4/24/15.
 */
public class MusicLibrary implements Cursor {
    // SDCard Path
    final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().getPath());
    private ArrayList<HashMap<String, String>> songsList;


    public MusicLibrary() {
        songsList = new ArrayList<HashMap<String, String>>();
    }

    public ArrayList<HashMap<String, String>> getPlayList(Context c) {



    /*use content provider to get beginning of database query that queries for all audio by display name, path
    and mimtype which i dont use but got it incase you want to scan for mp3 files only you can compare with RFC  mimetype for mp3's
    */
        final Cursor mCursor = c.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.MIME_TYPE }, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        String songs_name = "";
        String mAudioPath = "";

    /* run through all the columns we got back and save the data we need into the arraylist for our listview*/
        if (mCursor.moveToFirst()) {
            do {

                String file_type = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));


                songs_name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                mAudioPath = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                HashMap<String, String> song = new HashMap<String, String>();

                song.put("songTitle", songs_name);
                song.put("songPath", mAudioPath);

                songsList.add(song);

            } while (mCursor.moveToNext());
        }

        mCursor.close(); //cursor has been consumed so close it
        return songsList;
    }


    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3")   || name.endsWith(".mp4") || name.endsWith(".MP4"));
        }
    }
    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public boolean move(int offset) {
        return false;
    }

    @Override
    public boolean moveToPosition(int position) {
        return false;
    }

    @Override
    public boolean moveToFirst() {
        return false;
    }

    @Override
    public boolean moveToLast() {
        return false;
    }

    @Override
    public boolean moveToNext() {
        return false;
    }

    @Override
    public boolean moveToPrevious() {
        return false;
    }

    @Override
    public boolean isFirst() {
        return false;
    }

    @Override
    public boolean isBeforeFirst() {
        return false;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return 0;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
        return null;
    }

    @Override
    public short getShort(int columnIndex) {
        return 0;
    }

    @Override
    public long getLong(int columnIndex) {
        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        return 0;
    }

    @Override
    public double getDouble(int columnIndex) {
        return 0;
    }

    @Override
    public int getType(int columnIndex) {
        return 0;
    }

    @Override
    public boolean isNull(int columnIndex) {
        return false;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isAfterLast() {
        return false;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public int getInt(int columnIndex) {
        return 0;
    }
}
