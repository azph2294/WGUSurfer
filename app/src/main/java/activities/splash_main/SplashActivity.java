package activities.splash_main;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;
import notifications.NotificationHelper;
import viewmodels.CourseViewModel;
import viewmodels.ObjAssessmentsVM;
import viewmodels.PerfAssessmentsVM;

import static Utilities.Constants.GOAL_D;
import static Utilities.TimeFiles.formatStringToDate;

public class SplashActivity extends AppCompatActivity {

    private static final String TEST = "_ALARM";

    ImageView wgu_surfer, wguText;
    Animation from_Left, from_bottom;

    NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        wguText = findViewById(R.id.wguText);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.splash_from_bottom);
        wguText.setAnimation(from_bottom);

        wgu_surfer = findViewById(R.id.surfer);
        from_Left = AnimationUtils.loadAnimation(this, R.anim.splash_from_left);
        wgu_surfer.setAnimation(from_Left);

        int SPLASH_TIMEOUT = 5000;
        new Handler().postDelayed(() -> {
            Intent splash = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(splash);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, SPLASH_TIMEOUT);

        notificationHelper = new NotificationHelper(this);

        //Notifications for assessment goal dates
        createNotificationChannel();
        perfGoalDateAlarm();
        objGoalDateAlarm();
        courseStart_EndAlarm();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(GOAL_D, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannelGroup(new
                    NotificationChannelGroup(description, name));
        }
    }

    /*Create notifications for 2 weeks before, 5 days before and CURRENT DATE
    FOR PERF. ASSESSMENTS, OBJ. ASSESSMENTS, COURSE START/ END DATES
     */
    private void perfGoalDateAlarm() {
        PerfAssessmentsVM perfAssessmentsVM = new ViewModelProvider(this)
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.mPerfAssessments.observe(this,
                pAssessments -> {
                    for (PerformanceAssessments p : pAssessments) {
                        try {
                            Date pGoalDate = formatStringToDate(p.getPer_due_date());
                            Calendar cal = Calendar.getInstance();
                            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                            String currentDate = sdf.format(cal.getTime());
                            Date due_today = formatStringToDate(currentDate);
                            //Get 2-week long value
                            long long_2_weeks = formatStringToDate(currentDate).getTime() + 14 * 24 * 60 * 60 * 1000;
                            Date date_2_weeks = new Date(long_2_weeks);

                            //Get 5-day long value
                            long long_5_days = formatStringToDate(currentDate).getTime() + 5 * 24 * 60 * 60 * 1000;
                            Date date_5_days = new Date(long_5_days);

                            Log.d(TEST, "NOTIFICATION DATES: " + pGoalDate + "\n" + date_5_days);

                            //2-week notification
                            if (pGoalDate.equals(date_2_weeks)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Performance Assessment: " + p.getPerAssesName() +
                                                " is due in 2 weeks!", MainActivity.class);
                            }
                            //5-day notification
                            else if (pGoalDate.equals(date_5_days)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Performance Assessment: " + p.getPerAssesName() +
                                                " is due in 5 days!", MainActivity.class);
                            }
                            //Current day notification
                            else if (pGoalDate.equals(due_today)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Performance Assessment: " + p.getPerAssesName() +
                                                " is due today!", MainActivity.class);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void objGoalDateAlarm() {
        ObjAssessmentsVM objAssessmentsVM = new ViewModelProvider(this)
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.mObjAssessments.observe(this,
                oAssessments -> {
                    for (ObjectiveAssessments o : oAssessments) {
                        try {
                            Date oGoalDate = formatStringToDate(o.getObj_due_date());
                            Calendar cal = Calendar.getInstance();
                            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                            String currentDate = sdf.format(cal.getTime());
                            Date due_today = formatStringToDate(currentDate);
                            //Get 2-week long value
                            long long_2_weeks = formatStringToDate(currentDate).getTime() + 14 * 24 * 60 * 60 * 1000;
                            Date date_2_weeks = new Date(long_2_weeks);

                            //Get 5-day long value
                            long long_5_days = formatStringToDate(currentDate).getTime() + 5 * 24 * 60 * 60 * 1000;
                            Date date_5_days = new Date(long_5_days);

                            Log.d(TEST, "NOTIFICATION DATES: " + oGoalDate + "\n" + date_5_days);

                            //2-week notification
                            if (oGoalDate.equals(date_2_weeks)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Objective Assessment: " + o.getObjAssesName() +
                                                " is due in 2 weeks!", MainActivity.class);
                            }
                            //5-day notification
                            else if (oGoalDate.equals(date_5_days)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Objective Assessment: " + o.getObjAssesName() +
                                                " is due in 5 days!", MainActivity.class);
                            }
                            //Current day notification
                            else if (oGoalDate.equals(due_today)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Objective Assessment: " + o.getObjAssesName() +
                                                " is due in today!", MainActivity.class);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void courseStart_EndAlarm() {
        CourseViewModel courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.mCourses.observe(this,
                courses -> {
                    for (Courses c : courses) {
                        try {
                            Date cStart = formatStringToDate(c.getStartDate());
                            Calendar cal = Calendar.getInstance();
                            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                            String currentDate = sdf.format(cal.getTime());
                            Date due_today = formatStringToDate(currentDate);
                            //Get 2-week long value
                            long long_2_weeks = formatStringToDate(currentDate).getTime() + 14 * 24 * 60 * 60 * 1000;
                            Date date_2_weeks = new Date(long_2_weeks);

                            //Get 5-day long value
                            long long_5_days = formatStringToDate(currentDate).getTime() + 5 * 24 * 60 * 60 * 1000;
                            Date date_5_days = new Date(long_5_days);

                            Log.d(TEST, "NOTIFICATION DATES: " + cStart + "\n" + date_5_days);

                            //2-week notification
                            if (cStart.equals(date_2_weeks)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Course: " + c.getCourseName() +
                                                " starts in 2 weeks!", MainActivity.class);
                            }
                            //5-day notification
                            else if (cStart.equals(date_5_days)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Course: " + c.getCourseName() +
                                                " starts in 5 days!", MainActivity.class);
                            }
                            //Current day notification
                            else if (cStart.equals(due_today)) {
                                notificationHelper.sendHighPriorityNotification("WGU Surfer",
                                        "Course: " + c.getCourseName() +
                                                " starts today!", MainActivity.class);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}
