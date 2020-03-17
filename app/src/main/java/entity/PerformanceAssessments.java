package entity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static Utilities.Constants.P_ID;
import static Utilities.Constants.P_PERF_DUE_DATE;
import static Utilities.Constants.P_PER_ASSESS;
import static Utilities.Constants.P_PER_ID;
import static Utilities.Constants.P_STATUS_ID;

@Entity(tableName = "performance_assessments")
public class PerformanceAssessments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Courses.class, parentColumns = "id", childColumns = "per_Id")
    private int per_Id;

    private String perAssesName;

    private String per_due_date;

    private int status_Id;

    public PerformanceAssessments(int id, int per_Id, String perAssesName,
                                  String per_due_date, int status_Id) {
        this.id = id;
        this.per_Id = per_Id;
        this.perAssesName = perAssesName;
        this.per_due_date = per_due_date;
        this.status_Id = status_Id;
    }

    @Ignore
    public PerformanceAssessments() {
    }

    @Ignore
    public PerformanceAssessments(String perAssesName, String per_due_date) {
        this.perAssesName = perAssesName;
        this.per_due_date = per_due_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPer_Id() {
        return per_Id;
    }

    public void setPer_Id(int per_Id) {
        this.per_Id = per_Id;
    }

    public String getPerAssesName() {
        return perAssesName;
    }

    public void setPerAssesName(String perAssesName) {
        this.perAssesName = perAssesName;
    }

    public String getPer_due_date() {
        return per_due_date;
    }

    public void setPer_due_date(String per_due_date) {
        this.per_due_date = per_due_date;
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
        return "PerformanceAssessments{" +
                "id=" + id +
                ", per_Id=" + per_Id +
                ", perAssesName='" + perAssesName + '\'' +
                ", per_due_date='" + per_due_date + '\'' +
                ", status_Id=" + status_Id +
                '}';
    }

    public PerformanceAssessments(Bundle p) {
        if (p != null) {
            this.id = p.getInt(P_ID);
            this.per_Id = p.getInt(P_PER_ID);
            this.perAssesName = p.getString(P_PER_ASSESS);
            this.per_due_date = p.getString(P_PERF_DUE_DATE);
            this.status_Id = p.getInt(P_STATUS_ID);
        }
    }

    public Bundle perfAssessToBundle() {
        Bundle b = new Bundle();
        b.putInt(P_ID, this.id);
        b.putInt(P_PER_ID, this.per_Id);
        b.putString(P_PER_ASSESS, this.perAssesName);
        b.putString(P_PERF_DUE_DATE, this.per_due_date);
        b.putInt(P_STATUS_ID, this.status_Id);

        return b;
    }

}
