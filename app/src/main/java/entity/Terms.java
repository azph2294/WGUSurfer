package entity;

import android.os.Bundle;

import androidx.annotation.BoolRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import converters.DateConverter;

import static Utilities.Constants.T_ACTIVE;
import static Utilities.Constants.T_ENDDATE;
import static Utilities.Constants.T_ID;
import static Utilities.Constants.T_STARTDATE;
import static Utilities.Constants.T_TERMFLAG;
import static Utilities.Constants.T_TERMNAME;

@Entity(tableName = "terms")
public class Terms {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @BoolRes
    private int termFlag;

    private String termName;

    @TypeConverters(DateConverter.class)
    private String startDate;

    @TypeConverters(DateConverter.class)
    private String endDate;

    @Ignore
    public Terms() {
    }

    public Terms(int id, int termFlag, String termName, String startDate, String endDate) {
        this.id = id;
        this.termFlag = termFlag;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Terms(String termName, String startDate, String endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Terms(int termFlag) {
        this.termFlag = termFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTermName() {
        return termName;
    }

    public void setTermName(@NonNull String termName) {
        this.termName = termName;
    }

    public int getTermFlag() {
        return termFlag;
    }

    public void setTermFlag(int termFlag) {
        this.termFlag = termFlag;
    }

    @NonNull
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull String startDate) {
        this.startDate = startDate;
    }

    @NonNull
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(@NonNull String endDate) {
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Terms{" +
                "id=" + id +
                ", termFlag=" + termFlag +
                ", termName='" + termName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    public Terms(Bundle t) {
        if (t != null) {
            this.id = t.getInt(T_ID);
            this.termFlag = t.getInt(T_TERMFLAG);
            this.termName = t.getString(T_TERMNAME);
            this.startDate = t.getString(T_STARTDATE);
            this.endDate = t.getString(T_ENDDATE);
        }
    }

    public Bundle termsToBundle() {
        Bundle b = new Bundle();
        b.putInt(T_ID, this.id);
        b.putInt(T_TERMFLAG, this.termFlag);
        b.putString(T_TERMNAME, this.termName);
        b.putString(T_STARTDATE, this.startDate);
        b.putString(T_ENDDATE, this.endDate);

        return b;
    }

}
