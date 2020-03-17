package viewmodels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import database.SurferRepository;
import entity.Notes;

public class NotesViewModel extends AndroidViewModel {

    public LiveData<List<Notes>> mNotes;
    public MutableLiveData<Notes> noteLiveData = new MutableLiveData<>();
    private SurferRepository surferRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NotesViewModel(@NonNull Application application) {
        super(application);
        surferRepository = SurferRepository.getInstance(getApplication());
        mNotes = surferRepository.mNotes;
    }

    public void addNotesTestData() {
        surferRepository.addNotesTestData();
    }

    public void setNotes(int noteId) {
        executor.execute(() -> {
            Notes notes = surferRepository.getNotesById(noteId);
            noteLiveData.postValue(notes);
        });
    }

    public void deleteNote(Notes note) {
        surferRepository.deleteNote(note);
    }

    public void addNewNote(int id, int note_id, String n_note, String currentDateTime, String currentDateTime1) {
        Notes note;
        TextUtils.isEmpty(n_note.trim());
        note = new Notes(id, note_id, n_note, currentDateTime, currentDateTime1);
        surferRepository.insertNote(note);
    }

}
