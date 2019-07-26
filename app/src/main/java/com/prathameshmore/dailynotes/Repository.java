package com.prathameshmore.dailynotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prathameshmore.dailynotes.models.Note;

import java.util.List;

public class Repository {

    private NotesDao notesDao;
    private LiveData<List<Note>> allToDos;

    public Repository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        notesDao = database.notesDao();
        allToDos =  notesDao.getAllNotes();
    }

    public void insert(Note note) {
        new InsertAsyncTask(notesDao).execute(note);
    }

    public void update(Note note) {
        new UpdateAsyncTask(notesDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteAsyncTask(notesDao).execute(note);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(notesDao).execute();
    }

    public LiveData<List<Note>> getAllToDos() {
        return allToDos;
    }

    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NotesDao notesDao;

        private InsertAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

        private NotesDao notesDao;

        private UpdateAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NotesDao notesDao;

        private DeleteAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private NotesDao notesDao;

        private DeleteAllAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            notesDao.deleteAllNotes();
            return null;
        }
    }
}
