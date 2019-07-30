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
           // new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private NotesDao notesDao;

        private PopulateDbAsyncTask(NoteDatabase database) {
            notesDao = database.notesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            notesDao.insert(new Note("Samsung Galaxy Note 10 release date, price, news and leaks", "3 minutes ago", "The Samsung Galaxy Note 10 needs to seriously impress consumers to hold folks' attention, and we're hearing plenty of Galaxy Note 10 leaks and rumors suggesting it might just pull that off.\n" +
                    "\n" +
                    "We've even seen the Galaxy Note 10 name on an image at Samsung's Korean campus, and heard Samsung CEO DJ Koh name-drop the soon-to-be-announced handset during a round-table interview.", 2));
            notesDao.insert(new Note("San Francisco Travel Plans", "20 hours ago", "San Francisco's top attractions include Fisherman's Wharf, Pier 39, Ghirardelli Square, Alcatraz, the Golden Gate Bridge, cable cars, Chinatown, Lombard Street and Golden Gate Park. You can see where they all are on the map of San Francisco attractions above."
            ,2));
            notesDao.insert(new Note("'Lover' New Album by Taylor Swift", "20 hours ago", "Taylor Swift has announced her seventh studio album: Lover. It comes out August 23 via Republic. Lover includes Taylor Swift's latest single “ME!,” as well as a new track titled “You Need to Calm Down.” The new album is Taylor's follow-up to 2017's reputation.",
                    1));

            return null;
        }
    }
}
