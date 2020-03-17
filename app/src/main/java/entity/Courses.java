package entity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import converters.DateConverter;

import static Utilities.Constants.C_ACTIVE;
import static Utilities.Constants.C_COURSENAME;
import static Utilities.Constants.C_DESCRIPTION;
import static Utilities.Constants.C_ENDDATE;
import static Utilities.Constants.C_ID;
import static Utilities.Constants.C_MENTOREMAIL;
import static Utilities.Constants.C_MENTORNAME;
import static Utilities.Constants.C_MENTORPHONE;
import static Utilities.Constants.C_STARTDATE;
import static Utilities.Constants.C_STATUS;
import static Utilities.Constants.C_STATUS_ID;
import static Utilities.Constants.C_TERM_ID;

@Entity(tableName = "courses")
public class Courses {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Terms.class, parentColumns = "id", childColumns = "termId")
    private int termId;

    private int statusId;

    private int active;

    private String courseName;

    private String status;

    private String courseDesc;

    private String mentorName;

    private String mentorPhone;

    private String mentorEmail;

    private String startDate;

    private String endDate;

    public Courses(int id, int termId, int statusId, int active, @NonNull String courseName, @NonNull String status,
                   @NonNull String courseDesc, @NonNull String mentorName, @NonNull String mentorPhone,
                   @NonNull String mentorEmail, @NonNull String startDate, @NonNull String endDate) {
        this.id = id;
        this.termId = termId;
        this.statusId = statusId;
        this.active = active;
        this.courseName = courseName;
        this.status = status;
        this.courseDesc = courseDesc;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Courses(String mentorName, String mentorPhone, String mentorEmail) {
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
    }

    @Ignore
    public Courses(int termId, int statusId, int active, String courseName, String status,
                   String courseDesc, String startDate, String endDate) {
        this.termId = termId;
        this.statusId = statusId;
        this.active = active;
        this.courseName = courseName;
        this.status = status;
        this.courseDesc = courseDesc;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @NonNull
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(@NonNull String courseName) {
        this.courseName = courseName;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(@NonNull String courseDesc) {
        this.courseDesc = courseDesc;
    }

    @NonNull
    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(@NonNull String mentorName) {
        this.mentorName = mentorName;
    }

    @NonNull
    public String getMentorPhone() {
        return mentorPhone;
    }

    public void setMentorPhone(@NonNull String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    @NonNull
    public String getMentorEmail() {
        return mentorEmail;
    }

    public void setMentorEmail(@NonNull String mentorEmail) {
        this.mentorEmail = mentorEmail;
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

    public Courses(Bundle c) {
        if (c != null) {
            this.id = c.getInt(C_ID);
            this.termId = c.getInt(C_TERM_ID);
            this.statusId = c.getInt(C_STATUS_ID);
            this.active = c.getInt(C_ACTIVE);
            this.courseName = c.getString(C_COURSENAME);
            this.status = c.getString(C_STATUS);
            this.courseDesc = c.getString(C_DESCRIPTION);
            this.mentorName = c.getString(C_MENTORNAME);
            this.mentorPhone = c.getString(C_MENTORPHONE);
            this.mentorEmail = c.getString(C_MENTOREMAIL);
            this.startDate = c.getString(C_STARTDATE);
            this.endDate = c.getString(C_ENDDATE);
        }
    }

    public Bundle coursesToBundle() {
        Bundle b = new Bundle();
        b.putInt(C_ID, this.id);
        b.putInt(C_TERM_ID, this.termId);
        b.putInt(C_STATUS_ID, this.statusId);
        b.putInt(C_ACTIVE, this.active);
        b.putString(C_COURSENAME, this.courseName);
        b.putString(C_STATUS, this.status);
        b.putString(C_DESCRIPTION, this.courseDesc);
        b.putString(C_MENTORNAME, this.mentorName);
        b.putString(C_MENTORPHONE, this.mentorPhone);
        b.putString(C_MENTOREMAIL, this.mentorEmail);
        b.putString(C_STARTDATE, this.startDate);
        b.putString(C_ENDDATE, this.endDate);

        return b;
    }

    @NonNull
    @Override
    public String toString() {
        return "Courses{" +
                "id=" + id +
                ", termId=" + termId +
                ", statusId=" + statusId +
                ", active=" + active +
                ", courseName='" + courseName + '\'' +
                ", status='" + status + '\'' +
                ", courseDesc='" + courseDesc + '\'' +
                ", mentorName='" + mentorName + '\'' +
                ", mentorPhone='" + mentorPhone + '\'' +
                ", mentorEmail='" + mentorEmail + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
