package Utilities;

public class Constants {

    //Testing-Database ! use to test database conn.
    public static final String LIVE_DATA_TEST = "LiveDataTest";

    //broadcast - notifications
    public static final String GOAL_D = "goal_date";
    public static final String GROUP_KEY_NOTIFICATIONS = "dev.alvatech.c196_wgusurfer_ericbsmith.NOTIFICATIONS";
    public static final int FIVE_DAY1 = 1;
    public static final int TWO_WEEK1 = 2;

    //surfer repository - all activities
    public static final String ALLTERMS_TEST_DATA_ADDED = "SurferRepository";
    public static final String CURRTERM_TEST_DATA_ADDED = "SurferRepository";
    public static final String PERF_FRAG_TEST_DATA_ADDED = "SurferRepository";
    public static final String OBJ_FRAG_TEST_DATA_ADDED = "SurferRepository";
    public static final String NOTES_FRAG_TEST_DATA_ADDED = "SurferRepository";

    //adapters
    public static final String PERF_ASSESS__RV = "PerfAssessRVAdapter";
    public static final String OBJ_ASSESS__RV = "ObjAssessRVAdapter";

    //all terms activity
    public static final String ALL_TERMS = "AllTermsActivity";
    public static final String ALL_TERMS_RV = "TermRVAdapter";
    public static final String OPEN_ADD_TERM_FRAGMENT = "OpenAddTermFragment";

    //current term activity
    public static final String CURRENT_TERM = "CurrentTermActivity";
    public static final String COURSE_RV_SELECTED_TERM = "CourseRVAdapter";

    //selected term activity
    public static final String SELECTED_TERM = "SelectedTermActivity";

    //edit term activity
    public static final String EDIT_TERM = "EditTermActivity";
    public static final String DATE_PICKER_EDIT_TERM = "EditTermActivity";

    //add course bottom sheet
    public static final String DATE_PICK_ADD_COURSE = "AddCourseActivity";

    /*-----------------ROOM/DATABASE CONSTANTS-------------*/
    public static final String TERM_ID = "id";
    public static final String TERM_ACTIVE = "active";
    public static final String COURSE_ID = "termId";

    /*-------------NUMERIC CONSTANTS----------------------------*/
    //Status id
    public static final int IN_PROGRESS = 1;
    public static final int PLAN_TO_TAKE = 2;
    public static final int DROPPED = 3;
    public static final int COMPLETED = 4;

    public static final int ACTIVE = 1;
    public static final int IN_ACTIVE = 0;
    public static final int ACTIVE_TERM_FLAG = 1;
    public static final int IN_ACTIVE_TERM_FLAG = 0;

    /*---------------------------FRAGMENTS-------------------------*/
    public static final String COURSEACTIVITY_IN_HOME = "HomeFragment";
    public static final String EDIT_COURSE_IN_HOME = "EditCourseBottomSheet";
    public static final String COURSEACTIVITY_IN_ASSESS = "AssessmentsFragment";
    public static final String ASSESSMENTS_FRAGMENT = "AssessmentsFragment";
    public static final String NOTES_FRAGMENT = "NotesFragment";

    /*--------------BOTTOM SHEETS------------------------------*/
    public static final String EDIT_PERF_ASSESS = "EditPerfBottomSheet";
    public static final String EDIT_OBJ_ASSESS = "EditObjBottomSheet";

    /*--------------TERMS--------------------------------*/
    public static final String T_ID = "id";
    public static final String T_TERMFLAG = "termFlag";
    public static final String T_TERMNAME = "termName";
    public static final String T_STARTDATE = "startDate";
    public static final String T_ENDDATE = "endDate";
    public static final String T_ACTIVE = "active";

    /*---------------COURSES----------------------------------------*/
    public static final String C_ID = "id";
    public static final String C_TERM_ID = "termId";
    public static final String C_STATUS_ID = "statusId";
    public static final String C_ACTIVE = "active";
    public static final String C_COURSENAME = "courseName";
    public static final String C_STATUS = "status";
    public static final String C_DESCRIPTION = "courseDesc";
    public static final String C_MENTORNAME = "mentorName";
    public static final String C_MENTORPHONE = "mentorPhone";
    public static final String C_MENTOREMAIL = "mentorEmail";
    public static final String C_STARTDATE = "startDate";
    public static final String C_ENDDATE = "endDate";

    /*--------------------OBJ ASSESSMENTS------------------------------*/
    public static final String O_ID = "id";
    public static final String O_OBJ_ID = "obj_id";
    public static final String O_OBJ_ASSESS = "objAssesName";
    public static final String O_OBJ_DUE_DATE = "obj_due_date";
    public static final String O_STATUS_ID = "status_Id";

    /*--------------------PERF ASSESSMENTS------------------------*/
    public static final String P_ID = "id";
    public static final String P_PER_ID = "per_Id";
    public static final String P_PER_ASSESS = "perAssesName";
    public static final String P_PERF_DUE_DATE = "per_sched_date";
    public static final String P_STATUS_ID = "status_Id";

    /*--------------------NOTES-----------------------------------------*/

    public static final String N_ID = "id";
    public static final String N_NOTE_ID = "note_Id";
    public static final String N_COURSENOTE = "courseNote";
    public static final String N_CREATEDATE = "createDate";
    public static final String N_LASTUPDATE = "lastUpdate";

}
