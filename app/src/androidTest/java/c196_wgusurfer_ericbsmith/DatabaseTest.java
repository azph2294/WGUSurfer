package c196_wgusurfer_ericbsmith;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dao.CoursesDao;
import dao.TermsDao;
import dataProvider.CourseDataProvider;
import dataProvider.TermDataProvider;
import database.SurferDatabase;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static final String TAG = "JUnit";
    private SurferDatabase sDb;
    private TermsDao sTermsDao;
    private CoursesDao sCoursesDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        sDb = Room.inMemoryDatabaseBuilder(context, SurferDatabase.class).build();

        sTermsDao = sDb.termsDao();
        sCoursesDao = sDb.coursesDao();

        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb() {
        sDb.close();

        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveTerms() {
        sTermsDao.insertAllTerms(TermDataProvider.getTerms());
        int count = sTermsDao.getCount();
        Log.i(TAG, "createAndRetrieveTerms: count = " + count);
        assertEquals(TermDataProvider.getTerms().size(), count);
    }

    @Test
    public void createAndRetrieveCourses() {
        sCoursesDao.insertAllCourses(CourseDataProvider.getCourses());
        int count = sCoursesDao.getCount();
        Log.i(TAG, "createAndRetrieveCourses: count = " + count);
        assertEquals(CourseDataProvider.getCourses().size(), count);

    }

}
