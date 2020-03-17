package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Terms;

@Dao
public interface TermsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(Terms term);

    @Update
    void updateTerm(Terms term);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTerms(List<Terms> terms);

    @Delete(entity = Terms.class)
    void deleteTerm(Terms... term);

    @Query("SELECT * FROM terms WHERE id = :id")
    Terms getTermById(int id);

    @Query("SELECT * FROM terms")
    LiveData<List<Terms>> getAllTerms();

    @Query("SELECT COUNT(*) FROM terms")
    int getCount();

}
