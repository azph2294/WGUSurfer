package entity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import converters.DateConverter;

import static Utilities.Constants.N_COURSENOTE;
import static Utilities.Constants.N_CREATEDATE;
import static Utilities.Constants.N_ID;
import static Utilities.Constants.N_LASTUPDATE;
import static Utilities.Constants.N_NOTE_ID;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int n_id;

    @ForeignKey(entity = Courses.class, parentColumns = "id", childColumns = "note_Id")
    private int note_Id;

    private String courseNote;

    @TypeConverters(DateConverter.class)
    private String createDate;

    @TypeConverters(DateConverter.class)
    private String lastUpdate;

    public Notes(int n_id, int note_Id, String courseNote, String createDate, String lastUpdate) {
        this.n_id = n_id;
        this.note_Id = note_Id;
        this.courseNote = courseNote;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Ignore
    public Notes(int n_id, int note_Id, String courseNote) {
        this.n_id = n_id;
        this.note_Id = note_Id;
        this.courseNote = courseNote;
    }

    public int getN_id() {
        return n_id;
    }

    public void setN_id(int n_id) {
        this.n_id = n_id;
    }

    public int getNote_Id() {
        return note_Id;
    }

    public void setNote_Id(int note_Id) {
        this.note_Id = note_Id;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notes{" +
                "id=" + n_id +
                ", note_Id=" + note_Id +
                ", courseNote='" + courseNote + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public Notes(Bundle n) {
        if (n != null) {
            this.n_id = n.getInt(N_ID);
            this.note_Id = n.getInt(N_NOTE_ID);
            this.courseNote = n.getString(N_COURSENOTE);
            this.createDate = n.getString(N_CREATEDATE);
            this.lastUpdate = n.getString(N_LASTUPDATE);
        }
    }

    public Bundle notesToBundle() {
        Bundle n = new Bundle();
        n.putInt(N_ID, this.n_id);
        n.putInt(N_NOTE_ID, this.note_Id);
        n.putString(N_COURSENOTE, this.courseNote);
        n.putString(N_CREATEDATE, this.createDate);
        n.putString(N_LASTUPDATE, this.lastUpdate);

        return n;
    }

}