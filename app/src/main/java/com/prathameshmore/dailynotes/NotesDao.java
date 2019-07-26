package com.prathameshmore.dailynotes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.prathameshmore.dailynotes.models.Note;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
    @Query("DELETE FROM notes")
    void deleteAllNotes();
    @Query("SELECT * FROM notes ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}
