package com.prathameshmore.dailynotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prathameshmore.dailynotes.models.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Note>> allToDos;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allToDos = repository.getAllToDos();
    }

     public void insert(Note note) {
        repository.insert(note);
     }

     public void update(Note note) {
        repository.update(note);
     }

     public void delete(Note note) {
        repository.delete(note);
     }

     public void deleteAll() {
        repository.deleteAll();
     }

    public LiveData<List<Note>> getAllNotes() {
        return allToDos;
    }
}
