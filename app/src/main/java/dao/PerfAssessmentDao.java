package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.PerformanceAssessments;

@Dao
public interface PerfAssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPerfAssessments(List<PerformanceAssessments> performanceAssessments);

    @Query("SELECT * FROM performance_assessments")
    LiveData<List<PerformanceAssessments>> getAllPerfAssessments();

    @Query("SELECT * FROM performance_assessments WHERE per_Id = :id")
    PerformanceAssessments getPerfAssessmentsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPA(PerformanceAssessments p);

    @Update
    void updatePA(PerformanceAssessments p);

    @Delete(entity = PerformanceAssessments.class)
    void deletePA(PerformanceAssessments p);

}
