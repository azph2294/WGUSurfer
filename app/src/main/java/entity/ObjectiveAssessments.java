package entity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static Utilities.Constants.O_STATUS_ID;
import static Utilities.Constants.O_ID;
import static Utilities.Constants.O_OBJ_ASSESS;
import static Utilities.Constants.O_OBJ_DUE_DATE;
import static Utilities.Constants.O_OBJ_ID;

@Entity(tableName = "objective_assessments")
public class ObjectiveAssessments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Courses.class, parentColumns = "id", childColumns = "obj_Id")
    private int obj_Id;

    private String objAssesName;

    private String obj_due_date;

    private int status_Id;

    public ObjectiveAssessments(int id, int obj_Id, String objAssesName,
                                String obj_due_date, int status_Id) {
        this.id = id;
        this.obj_Id = obj_Id;
        this.objAssesName = objAssesName;
        this.obj_due_date = obj_due_date;
        this.status_Id = status_Id;
    }

    @Ignore
    public ObjectiveAssessments() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObj_Id() {
        return obj_Id;
    }

    public void setObj_Id(int obj_Id) {
        this.obj_Id = obj_Id;
    }

    public String getObjAssesName() {
        return objAssesName;
    }

    public void setObjAssesName(String objAssesName) {
        this.objAssesName = objAssesName;
    }

    public String getObj_due_date() {
        return obj_due_date;
    }

    public void setObj_due_date(String obj_due_date) {
        this.obj_due_date = obj_due_date;
    }

    public int getStatus_Id() {
        return status_Id;
    }

    public void setStatus_Id(int status_Id) {
        this.status_Id = status_Id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ObjectiveAssessments{" +
                "id=" + id +
                ", obj_Id=" + obj_Id +
                ", objAssesName='" + objAssesName + '\'' +
                ", obj_due_date='" + obj_due_date + '\'' +
                ", status_Id=" + status_Id +
                '}';
    }

    public ObjectiveAssessments(Bundle o) {
        if (o != null) {
            this.id = o.getInt(O_ID);
            this.obj_Id = o.getInt(O_OBJ_ID);
            this.objAssesName = o.getString(O_OBJ_ASSESS);
            this.obj_due_date = o.getString(O_OBJ_DUE_DATE);
            this.status_Id = o.getInt(O_STATUS_ID);
        }
    }

    public Bundle objAssessToBundle() {
        Bundle b = new Bundle();
        b.putInt(O_ID, this.id);
        b.putInt(O_OBJ_ID, this.obj_Id);
        b.putString(O_OBJ_ASSESS, this.objAssesName);
        b.putString(O_OBJ_DUE_DATE, this.obj_due_date);
        b.putInt(O_STATUS_ID, this.status_Id);

        return b;
    }

}
