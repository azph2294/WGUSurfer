package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;

@Dao
public interface ObjAssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllObjAssessments(List<ObjectiveAssessments> objectiveAssessments);

    @Query("SELECT * FROM objective_assessments")
    LiveData<List<ObjectiveAssessments>> getAllObjAssessments();

    @Query("SELECT * FROM objective_assessments WHERE obj_Id = :id")
    ObjectiveAssessments getObjAssessmentsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOA(ObjectiveAssessments o);

    @Update
    void updateOA(ObjectiveAssessments o);

    @Delete(entity = ObjectiveAssessments.class)
    void deleteOA(ObjectiveAssessments o);

}
