package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Courses;

@Dao
public interface CoursesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCourses(List<Courses> courses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Courses course);

    @Update
    void updateCourse(Courses course);

    @Delete(entity = Courses.class)
    void deleteCourse(Courses course);

    @Query("SELECT * FROM courses WHERE id = :id")
    Courses getCourseById(int id);

    @Query("SELECT * FROM courses WHERE id = :termId")
    Courses getCoursesByTermId(int termId);

    @Query("SELECT * FROM courses")
    LiveData<List<Courses>> getAllCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int getCount();
}
