package database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dataProvider.AssessmentDataProvider;
import dataProvider.CourseDataProvider;
import dataProvider.NotesDataProvider;
import dataProvider.TermDataProvider;
import entity.Courses;
import entity.Notes;
import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;
import entity.Terms;

import static Utilities.Constants.ALLTERMS_TEST_DATA_ADDED;
import static Utilities.Constants.CURRTERM_TEST_DATA_ADDED;
import static Utilities.Constants.EDIT_TERM;
import static Utilities.Constants.NOTES_FRAG_TEST_DATA_ADDED;
import static Utilities.Constants.OBJ_FRAG_TEST_DATA_ADDED;
import static Utilities.Constants.PERF_FRAG_TEST_DATA_ADDED;

public class SurferRepository {

    private static SurferRepository ourInstance;

    public LiveData<List<Terms>> mTerms;
    public LiveData<List<Courses>> mCourses;
    public LiveData<List<Notes>> mNotes;
    public LiveData<List<PerformanceAssessments>> mPerfAssessments;
    public LiveData<List<ObjectiveAssessments>> mObjAssessments;

    private SurferDatabase surferDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static SurferRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SurferRepository(context);
        }
        return ourInstance;
    }

    private SurferRepository(Context context) {
        surferDatabase = SurferDatabase.getInstance(context);
        mTerms = getTerms();
        mCourses = getCourses();
        mPerfAssessments = getPerfAssessments();
        mObjAssessments = getObjAssessments();
        mNotes = getNotes();
    }

    /*--------------------------TEST DATA-----------------------------------*/

    public void addTermTestData() {
        executor.execute(() -> {
            surferDatabase.termsDao().insertAllTerms(TermDataProvider.getTerms());
        });
        Log.i(ALLTERMS_TEST_DATA_ADDED, "allTerms TestData added.");
    }

    public void addCoursesTestData() {
        executor.execute(() -> surferDatabase.coursesDao().insertAllCourses(CourseDataProvider.getCourses()));
        Log.d(CURRTERM_TEST_DATA_ADDED, "addCoursesTestData added.");
    }

    public void addPerfAssesTestData() {
        executor.execute(() -> {
            surferDatabase.perfAssessmentDao()
                    .insertAllPerfAssessments(AssessmentDataProvider.getPerfAssessments());
        });
        Log.d(PERF_FRAG_TEST_DATA_ADDED, "addPerfAssesTestData: ");
    }

    public void addObjAssesTestData() {
        executor.execute(() -> {
            surferDatabase.objAssessmentDao()
                    .insertAllObjAssessments(AssessmentDataProvider.getObjAssessments());
        });
        Log.d(OBJ_FRAG_TEST_DATA_ADDED, "addObjAssesTestData: ");
    }

    public void addNotesTestData() {
        executor.execute(() -> {
            surferDatabase.notesDao().insertAllNotes(NotesDataProvider.getNotes());
        });
        Log.d(NOTES_FRAG_TEST_DATA_ADDED, "addNotesTestData: ");
    }

    /*---------------------------GETTERS/SETTERS-----------------------------*/

    private LiveData<List<Terms>> getTerms() {
        return surferDatabase.termsDao().getAllTerms();
    }

    private LiveData<List<Courses>> getCourses() {
        return surferDatabase.coursesDao().getAllCourses();
    }

    private LiveData<List<PerformanceAssessments>> getPerfAssessments() {
        return surferDatabase.perfAssessmentDao().getAllPerfAssessments();
    }

    private LiveData<List<ObjectiveAssessments>> getObjAssessments() {
        return surferDatabase.objAssessmentDao().getAllObjAssessments();
    }

    private LiveData<List<Notes>> getNotes() {
        return surferDatabase.notesDao().getAllNotes();
    }

    /*---------------------------TERMS-----------------------------*/

    public Terms getTermById(int termId) {
        return surferDatabase.termsDao().getTermById(termId);
    }

    public void insertTerm(Terms term) {
        executor.execute(() -> surferDatabase.termsDao().insertTerm(term));
    }

    public void updateTerm(Terms term) {
        executor.execute(() -> surferDatabase.termsDao().updateTerm(term));
        Log.d(EDIT_TERM, "term updated.");
    }

    public void deleteTerm(Terms deletedTerm) {
        executor.execute(() -> {
            surferDatabase.termsDao().deleteTerm(deletedTerm);
        });
    }

    public Courses getCoursesByTermId(int termId) {
        return surferDatabase.coursesDao().getCoursesByTermId(termId);
    }

    /*---------------------------COURSES----------------------------*/

    public Courses getCoursesById(int courseId) {
        return surferDatabase.coursesDao().getCourseById(courseId);
    }

    public void insertCourse(Courses newCourse) {
        executor.execute(() -> surferDatabase.coursesDao().insertCourse(newCourse));
    }

    public void updateCourse(Courses course) {
        executor.execute(() -> surferDatabase.coursesDao().updateCourse(course));
    }

    public void deleteCourse(Courses course) {
        executor.execute(() -> surferDatabase.coursesDao().deleteCourse(course));
    }

    /*--------------------------PERFORMANCE ASSESSMENTS-----------------------------*/

    public PerformanceAssessments getPerfAssessmentsById(int per_Id) {
        return surferDatabase.perfAssessmentDao().getPerfAssessmentsById(per_Id);
    }

    public void insertPA(PerformanceAssessments newPA) {
        executor.execute(() -> {
            surferDatabase.perfAssessmentDao().insertPA(newPA);
        });
    }

    public void deletePA(PerformanceAssessments p) {
        executor.execute(() -> {
            surferDatabase.perfAssessmentDao().deletePA(p);
        });
    }

    /*-------------------------OBJECTIVE ASSESSMENTS---------------------------------*/

    public ObjectiveAssessments getObjAssessmentsById(int id) {
        return surferDatabase.objAssessmentDao().getObjAssessmentsById(id);
    }

    public void insertOA(ObjectiveAssessments newOA) {
        executor.execute(() -> {
            surferDatabase.objAssessmentDao().insertOA(newOA);
        });
    }

    public void deleteOA(ObjectiveAssessments o) {
        executor.execute(() -> {
            surferDatabase.objAssessmentDao().deleteOA(o);
        });
    }

    /*------------------------NOTES--------------------------------*/

    public Notes getNotesById(int noteId) {
        return surferDatabase.notesDao().getNotesById(noteId);
    }

    public void deleteNote(Notes note) {
        executor.execute(() -> {
            surferDatabase.notesDao().deleteNote(note);
        });
    }

    public void insertNote(Notes note) {
        executor.execute(() -> {
            surferDatabase.notesDao().insertNote(note);
        });
    }

}
