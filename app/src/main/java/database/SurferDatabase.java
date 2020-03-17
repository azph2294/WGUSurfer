package database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import converters.DateConverter;
import dao.CoursesDao;
import dao.NotesDao;
import dao.ObjAssessmentDao;
import dao.PerfAssessmentDao;
import dao.TermsDao;
import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;
import entity.Courses;
import entity.Notes;
import entity.Terms;

@Database(entities = {Terms.class, Courses.class, Notes.class, PerformanceAssessments.class,
        ObjectiveAssessments.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class SurferDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "my-surfer-database.db";
    private static volatile SurferDatabase instance;
    private static final Object LOCK = new Object();
    public abstract CoursesDao coursesDao();
    public abstract TermsDao termsDao();
    public abstract PerfAssessmentDao perfAssessmentDao();
    public abstract ObjAssessmentDao objAssessmentDao();
    public abstract NotesDao notesDao();

    static SurferDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            SurferDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
