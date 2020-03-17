package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Notes;

@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotes(List<Notes> notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM notes WHERE note_Id = :id")
    Notes getNotesById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Notes note);

    @Delete(entity = Notes.class)
    void deleteNote(Notes note);

}
