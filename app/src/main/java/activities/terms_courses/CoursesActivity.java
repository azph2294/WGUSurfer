package activities.terms_courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import fragments.AssessmentsFragment;
import fragments.HomeFragment;
import fragments.MentorFragment;
import fragments.NotesFragment;
import viewPager.ViewPagerAdapter;
import viewmodels.CourseViewModel;

import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.TERM_ID;

public class CoursesActivity extends AppCompatActivity {

    private static final String PAGE = "PageSelected";
    private static final String LIVE_DATA_TEST = "LiveDataTest";

    private ViewPager viewPager;
    private BottomNavigationView bottomNav;
    private MenuItem lastItem;
    private Toolbar tb;

    Bundle liveTerms = new Bundle();
    Bundle liveCourses = new Bundle();

    Bundle args = new Bundle();

    int termId, courseId;

    public CourseViewModel courseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        tb = findViewById(R.id.in_courses_TB);
        setSupportActionBar(tb);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToCourses());
        initCourseTBHeader();

        bottomNav = findViewById(R.id.course_bot_nav);
        viewPager = findViewById(R.id.c_frags_container);

        bottomNav.setOnNavigationItemSelectedListener(
                bottomNavigationItemSelectedListener);
        setupFragVP(getSupportFragmentManager(), viewPager);
        viewPager.addOnPageChangeListener(new PageChange());

    }

    private void initCourseTBHeader() {
        courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(this, courses -> {
            if (courses != null) {
                tb.setTitle(courses.getCourseName());
                Log.d(LIVE_DATA_TEST, courses.getCourseName());
            }
        });
        liveTerms = getIntent().getExtras();
        liveCourses = getIntent().getExtras();
        if (liveTerms != null && liveCourses != null) {
            termId = liveTerms.getInt(TERM_ID);
            courseId = liveCourses.getInt(COURSE_ID);
            courseViewModel.setTermCourses(termId);
            courseViewModel.setCoursesById(courseId);
        }
    }

    private void setupFragVP(FragmentManager fragmentManager, ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new AssessmentsFragment(), "Assessments");
        adapter.addFragment(new MentorFragment(), "Mentor");
        adapter.addFragment(new NotesFragment(), "Notes");
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            bottomNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.assessments:
                viewPager.setCurrentItem(1);
                break;
            case R.id.mentor:
                viewPager.setCurrentItem(2);
                break;
            case R.id.notes:
                viewPager.setCurrentItem(3);
                break;
        }
        return false;
    };

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        args = getIntent().getExtras();
        if (fragment instanceof HomeFragment) {
            HomeFragment home = (HomeFragment) fragment;
            home.setArguments(args);
        }
        if (fragment instanceof AssessmentsFragment) {
            AssessmentsFragment assessments = (AssessmentsFragment) fragment;
            assessments.setArguments(args);
        }
        if (fragment instanceof MentorFragment) {
            MentorFragment mentor = (MentorFragment) fragment;
            mentor.setArguments(args);
        }
        if (fragment instanceof NotesFragment) {
            NotesFragment notes = (NotesFragment) fragment;
            notes.setArguments(args);
        }
    }

    public class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (lastItem != null) {
                lastItem.setChecked(false);
            } else {
                bottomNav.getMenu().getItem(0).setChecked(false);
            }
            Log.d(PAGE, "onPageSelected: " + position);
            bottomNav.getMenu().getItem(position).setChecked(true);
            lastItem = bottomNav.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void backToCourses() {
        Intent backToSelectedTerm = new Intent(this, SelectedTermActivity.class);
        liveTerms = getIntent().getExtras();
        liveCourses = getIntent().getExtras();
        if (liveTerms != null && liveCourses != null) {
            termId = liveTerms.getInt(TERM_ID);
            courseId = liveCourses.getInt(COURSE_ID);
        }
        backToSelectedTerm.putExtra(TERM_ID, termId);
        backToSelectedTerm.putExtra(COURSE_ID, courseId);
        startActivity(backToSelectedTerm);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToCourses();
    }

}
