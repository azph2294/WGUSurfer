package activities.splash_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.Terms;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.COMPLETED;
import static Utilities.Constants.DROPPED;
import static Utilities.Constants.IN_PROGRESS;
import static Utilities.Constants.PLAN_TO_TAKE;
import static Utilities.TimeFiles.formatStringToDate;

public class ProgTrackActivity extends AppCompatActivity {

    private static final String TEST = "ProgTrackActivity";

    private TextView daysLeftTxt;

    private ArrayList<String> in_prog_cList = new ArrayList<>();
    private ArrayList<String> plan_to_take_list = new ArrayList<>();
    private ArrayList<String> completed_list = new ArrayList<>();
    private ArrayList<String> dropped_list = new ArrayList<>();

    private ListView inProgLV, planToTakeLV, completedLV, droppedLV;

    private Bundle liveCourses = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog_track);
        Toolbar tb = findViewById(R.id.toolbar_prog);
        setSupportActionBar(tb);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToMainActivity());

        inProgLV = findViewById(R.id.in_prog_list);
        planToTakeLV = findViewById(R.id.plan_to_take_list);
        completedLV = findViewById(R.id.completed_list);
        droppedLV = findViewById(R.id.dropped_list);
        daysLeftTxt = findViewById(R.id.t_days_left);

        initInProgressData();
        daysLeftInCurrTerm();

    }

    //Get days left in current term
    private void daysLeftInCurrTerm() {
        TermViewModel termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());
        Log.d(TEST, "current date: " + currentDate);
        termViewModel.mTerms.observe(this, terms -> {
            for (Terms term : terms) {
                try {
                    if (formatStringToDate(currentDate).after(formatStringToDate(term.getStartDate()))
                            && formatStringToDate(currentDate).before(formatStringToDate(term.getEndDate()))) {
                        long diffMS = formatStringToDate(term.getEndDate()).getTime() - Calendar.getInstance().getTimeInMillis();
                        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffMS);
                        daysLeftTxt.setText(String.valueOf(diffInDays + 1));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initInProgressData() {
        final Observer<List<Courses>> coursesObserver =
                courses -> {
                    liveCourses = getIntent().getExtras();
                    if (liveCourses != null) {
                        for (Courses c : courses) {
                            if (c.getStatusId() == IN_PROGRESS) {
                                in_prog_cList.add(c.getCourseName());
                                ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1, in_prog_cList);
                                inProgLV.setAdapter(adapter_1);
                                inProgLV.setNestedScrollingEnabled(true);
                            }
                            if (c.getStatusId() == PLAN_TO_TAKE) {
                                plan_to_take_list.add(c.getCourseName());
                                ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1, plan_to_take_list);
                                planToTakeLV.setAdapter(adapter_2);
                                planToTakeLV.setNestedScrollingEnabled(true);
                            }
                            if (c.getStatusId() == COMPLETED) {
                                completed_list.add(c.getCourseName());
                                ArrayAdapter<String> adapter_3 = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1, completed_list);
                                completedLV.setAdapter(adapter_3);
                                completedLV.setNestedScrollingEnabled(true);
                            }
                            if (c.getStatusId() == DROPPED) {
                                dropped_list.add(c.getCourseName());
                                ArrayAdapter<String> adapter_4 = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1, dropped_list);
                                droppedLV.setAdapter(adapter_4);
                                droppedLV.setNestedScrollingEnabled(true);
                            }
                        }
                    }
                };
        CourseViewModel courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.mCourses.observe(this, coursesObserver);
    }

    private void backToMainActivity() {
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }
}
