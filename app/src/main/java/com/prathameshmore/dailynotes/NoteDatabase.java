package com.prathameshmore.dailynotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prathameshmore.dailynotes.models.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    public abstract NotesDao notesDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class,"todo")
                    .fallbackToDestructiveMigration().addCallback(callback).build();
        }

        return instance;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private NotesDao notesDao;

        private PopulateDbAsyncTask(NoteDatabase database) {
            notesDao = database.notesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            notesDao.insert(new Note("Title", "3 minutes ago", "New added", 2));
            notesDao.insert(new Note("Sample text for the title of this news", "20 hours ago", "This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news."
            ,2));
            notesDao.insert(new Note("Sample text for the title of this news", "20 hours ago", "This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news."
                    ,2));
            notesDao.insert(new Note("Sample text for the title of this news", "20 hours ago", "This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news."
                    ,2));
            notesDao.insert(new Note("Sample text for the title of this news", "20 hours ago", "This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news."
                    ,2));
            notesDao.insert(new Note("Sample text for the title of this news", "20 hours ago", "This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news. This is a sample text for the content of this news that will be replaced with the original text for every news."
                    ,2));
            return null;
        }
    }
}
