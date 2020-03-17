package activities.terms_courses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import activities.splash_main.MainActivity;
import adapters.CourseRVAdapter;
import bottomSheets.AddCourseBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import helpers.RVCourseTouchHelper;
import listeners.RVCourseTouchHelperListener;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.SELECTED_TERM;
import static Utilities.Constants.TERM_ID;

public class SelectedTermActivity extends AppCompatActivity implements RVCourseTouchHelperListener,
        CourseRVAdapter.OnCourseClickListener, CourseRVAdapter.OnCourseLongClickListener {

    private Toolbar tb;
    private RecyclerView selCoursesRV;
    private CourseRVAdapter courseRVAdapter;
    private CourseViewModel courseViewModel;

    private TextView termNameLbl, startDateLbl, endDateLbl;

    private Bundle liveTerms = new Bundle();
    private Bundle args = new Bundle();
    private int termId;

    public List<Courses> coursesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_term);
        tb = findViewById(R.id.currTerm_TB);
        setSupportActionBar(tb);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToAllTerms());

        termNameLbl = findViewById(R.id.termLbl2);
        startDateLbl = findViewById(R.id.sdPicker_ct2);
        endDateLbl = findViewById(R.id.edPicker_ct2);

        ImageButton infoBtn = findViewById(R.id.info_btn3);
        infoBtn.setOnClickListener(v -> infoAlert());

        //For add course bottom sheet
        ImageButton addCourseBtn = findViewById(R.id.coursesAddButton);
        addCourseBtn.setOnClickListener(v -> {
            liveTerms = getIntent().getExtras();
            AddCourseBottomSheet addCourseBottomSheet = new
                    AddCourseBottomSheet();
            addCourseBottomSheet.setArguments(liveTerms);
            addCourseBottomSheet.show(getSupportFragmentManager(), "AddCourseBottomSheet");
        });

        initCourseRecyclerView();
        initSetTermViewModel();
        initSelTermCourseViewModel();

    }

    private void initSetTermViewModel() {
        TermViewModel termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        termViewModel.termLiveData.observe(this, terms -> {
            if (terms != null) {
                termNameLbl.setText(terms.getTermName());
                startDateLbl.setText(terms.getStartDate());
                endDateLbl.setText(terms.getEndDate());
                tb.setTitle(terms.getTermName());
            }
        });
        liveTerms = getIntent().getExtras();
        if (liveTerms != null) {
            termId = liveTerms.getInt(TERM_ID);
            termViewModel.setTerm(termId);
        }
    }

    private void initSelTermCourseViewModel() {
        final Observer<List<Courses>> coursesObserver =
                courseEntities -> {
                    liveTerms = SelectedTermActivity.this.getIntent().getExtras();
                    if (liveTerms != null) {
                        termId = liveTerms.getInt(TERM_ID);
                        coursesList.clear();
                        for (Courses c : courseEntities) {
                            if (c.getTermId() == termId) {
                                courseViewModel.setTermCourses(termId);
                                coursesList.add(c);
                            }
                        }
                    }
                    if (courseRVAdapter == null) {
                        courseRVAdapter = new CourseRVAdapter(
                                coursesList, SelectedTermActivity.this, SelectedTermActivity.this,
                                SelectedTermActivity.this);
                        selCoursesRV.setAdapter(courseRVAdapter);
                    } else {
                        courseRVAdapter.notifyDataSetChanged();
                    }
                };
        courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.mCourses.observe(this, coursesObserver);
    }

    private void initCourseRecyclerView() {
        Log.d(SELECTED_TERM, "initRecyclerView: init recyclerview");

        selCoursesRV = findViewById(R.id.course_recyclerView2);
        selCoursesRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        selCoursesRV.setLayoutManager(layoutManager);
        selCoursesRV.setItemAnimator(new DefaultItemAnimator());
        selCoursesRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RVCourseTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(selCoursesRV);

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

    //TODO: EXTRA FEATURE COURSES LIST (NOT IMPLEMENTED)
    @Override
    public void onCourseLongClick(int position) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        deleteCourse(this.selCoursesRV, position);
    }

    //TODO: SET THIS TO TERM DELETION I.E. FLAG
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

    private void backToAllTerms() {
        Intent toAllTerms = new Intent(this, AllTermsActivity.class);
        startActivity(toAllTerms);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToAllTerms();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.home_tb) {
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
