package activities.terms_courses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import activities.splash_main.MainActivity;
import adapters.CurrTermCourseAdapter;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.Terms;
import helpers.RVCurrTermCourseTouchHelper;
import listeners.RVCurrTermCourseTHListener;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.CURRENT_TERM;
import static Utilities.Constants.TERM_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class CurrentTermActivity extends AppCompatActivity implements
        CurrTermCourseAdapter.OnCourseClickListener, RVCurrTermCourseTHListener {

    private RecyclerView currCoursesRV;
    private CurrTermCourseAdapter courseRVAdapter;

    private CourseViewModel courseViewModel;

    private TextView termNameLbl, startDateLbl, endDateLbl;

    private List<Courses> coursesList = new ArrayList<>();
    private List<Terms> termsList = new ArrayList<>();

    private Bundle args = new Bundle();
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_term);
        Toolbar tb = findViewById(R.id.currTerm_TB);
        setSupportActionBar(tb);
        tb.setTitle(R.string.mainLbl1);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToMainActivity());

        termNameLbl = findViewById(R.id.termLbl);
        startDateLbl = findViewById(R.id.sdPicker_ct);
        endDateLbl = findViewById(R.id.edPicker_ct);

        ImageButton infoBtn = findViewById(R.id.info_btn4);
        infoBtn.setOnClickListener(v -> infoAlert());

        initCourseRecyclerView();
        initCurrentTermViewModel();

    }

    //TODO: MAYBE ALTER FOR CURRENT TERM
    private void initCurrentTermViewModel() {
        TermViewModel termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(this, terms -> {
            termsList.addAll(terms);
            Calendar cal = Calendar.getInstance();
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
            String currentDate = sdf.format(cal.getTime());
            final Observer<List<Courses>> coursesObserver =
                    courseEntities -> {
                        for (Terms term : termsList) {
                            try {
                                if (formatStringToDate(term.getStartDate()).before(formatStringToDate(currentDate))
                                        && formatStringToDate(term.getEndDate()).after(formatStringToDate(currentDate))) {
                                    termNameLbl.setText(term.getTermName());
                                    startDateLbl.setText(term.getStartDate());
                                    endDateLbl.setText(term.getEndDate());
                                    for (Courses c : courseEntities) {
                                        if (c.getTermId() == term.getId()) {
                                            coursesList.add(c);
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (courseRVAdapter == null) {
                            courseRVAdapter = new CurrTermCourseAdapter(coursesList, this,
                                    CurrentTermActivity.this);
                            currCoursesRV.setAdapter(courseRVAdapter);
                        } else {
                            courseRVAdapter.notifyDataSetChanged();
                        }
                    };
            courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
            courseViewModel.mCourses.observe(this, coursesObserver);
        });
    }

    private void initCourseRecyclerView() {
        Log.d(CURRENT_TERM, "initRecyclerView: init recyclerview");

        currCoursesRV = findViewById(R.id.course_recyclerView);
        currCoursesRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        currCoursesRV.setLayoutManager(layoutManager);
        currCoursesRV.setItemAnimator(null);
        currCoursesRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RVCurrTermCourseTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(currCoursesRV);
    }

    public void backToMainActivity() {
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    private void deleteCourse(View view, final int position) {
        final Courses course = courseRVAdapter.removeItem(position);
        final Snackbar undo = Snackbar.make(view, "Course: " + course.getCourseName()
                + " has been deleted.", Snackbar.LENGTH_LONG);
        undo.setAction("UNDO", v -> courseRVAdapter.restoreItem(course, position));
        undo.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    courseRVAdapter.removeItem(position);
                    courseViewModel.deleteCourse(course);
                }
            }
        });
        for (Courses c : coursesList) {
            if (c == null) {
                if (args != null) {
                    termId = args.getInt(TERM_ID);
                }
                TermViewModel termViewModel = new ViewModelProvider(this)
                        .get(TermViewModel.class);
                termViewModel.setTerm(termId);
            }
        }
        undo.setActionTextColor(Color.BLACK);
        undo.show();
    }

    private void infoAlert() {
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(this,
                R.style.AlertDialogTheme);
        infoAlert.setMessage(R.string.course_info);
        infoAlert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = infoAlert.create();
        alertDialog.show();
    }

    @Override
    public void onCourseClick(int position) {
        Intent selectCourseIntent = new Intent(this, CoursesActivity.class);
        startActivity(selectCourseIntent);
        finish();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        deleteCourse(this.currCoursesRV, position);
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

}
