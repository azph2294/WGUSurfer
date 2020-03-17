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
import entity.Courses;

public class CourseViewModel extends AndroidViewModel {

    public LiveData<List<Courses>> mCourses;
    public MutableLiveData<Courses> courseLiveData = new MutableLiveData<>();
    private SurferRepository surferRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        surferRepository = SurferRepository.getInstance(getApplication());
        mCourses = surferRepository.mCourses;
    }

    public void addCourseTestData() {
        surferRepository.addCoursesTestData();
    }

    public void setTermCourses(int termId) {
        executor.execute(() -> {
            Courses courses = surferRepository.getCoursesByTermId(termId);
            courseLiveData.postValue(courses);
        });
    }

    public void deleteCourse(Courses course) {
        surferRepository.deleteCourse(course);
    }

    public void addNewCourse(int id, int termId, int statusId, int active,
                             String courseName, String status, String courseDesc, String mentorName,
                             String mentorPhone, String mentorEmail, String startDate,
                             String endDate) {
        Courses newCourse;
        if (TextUtils.isEmpty(courseName.trim()) | TextUtils.isEmpty(mentorName.trim())
                | TextUtils.isEmpty(mentorEmail.trim()) | TextUtils.isEmpty(mentorPhone.trim())) {
            return;
        }
        newCourse = new Courses(id, termId, statusId, active, courseName.trim(), status, courseDesc.trim(),
                mentorName.trim(), mentorPhone.trim(), mentorEmail.trim(), startDate, endDate);
        surferRepository.insertCourse(newCourse);

    }

    public void setCoursesById(int courseId) {
        executor.execute(() -> {
            Courses courses = surferRepository.getCoursesById(courseId);
            courseLiveData.postValue(courses);
        });
    }

    public void saveCourse(int courseId, int termId, int ncStatusId, int active, String ncName, String ncStatus,
                           String ncDesc, String ncStart, String ncEnd) {
        Courses course = courseLiveData.getValue();
        if (course != null) {
            course.setId(courseId);
            course.setTermId(termId);
            course.setStatusId(ncStatusId);
            course.setActive(active);
            course.setCourseName(ncName.trim());
            course.setStatus(ncStatus);
            course.setCourseDesc(ncDesc.trim());
            course.setStartDate(ncStart);
            course.setEndDate(ncEnd);
        }
        surferRepository.updateCourse(course);
    }

    public void saveMentor(int courseId, int termId, String e_mName, String e_mEmail, String e_mPhone) {
        Courses course = courseLiveData.getValue();
        if (course != null) {
            course.setId(courseId);
            course.setTermId(termId);
            course.setMentorName(e_mName);
            course.setMentorEmail(e_mEmail);
            course.setMentorPhone(e_mPhone);
        }
        surferRepository.updateCourse(course);
    }

}
