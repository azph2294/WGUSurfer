package fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import activities.splash_main.MainActivity;
import bottomSheets.EditCourseBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.Terms;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.COMPLETED;
import static Utilities.Constants.COURSEACTIVITY_IN_HOME;
import static Utilities.Constants.DROPPED;
import static Utilities.Constants.IN_PROGRESS;
import static Utilities.Constants.PLAN_TO_TAKE;

public class HomeFragment extends Fragment {

    private TextView cHeader, cStartDate, cEndDate,
            cDueDate, cStatus, cDescription;

    private List<Terms> termsList = new ArrayList<>();
    private Bundle args = new Bundle();

    private EditCourseBottomSheet editCourseBottomSheet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        cHeader = view.findViewById(R.id.course_Header_lbl);
        cStartDate = view.findViewById(R.id.startD_PH);
        cEndDate = view.findViewById(R.id.endD_PH);
        cDueDate = view.findViewById(R.id.dueD_PH);
        cStatus = view.findViewById(R.id.c_status_type);
        cDescription = view.findViewById(R.id.description_text);

        FloatingActionButton cEditFAB = view.findViewById(R.id.c_edit_FAB);
        cEditFAB.setOnClickListener(v -> {
            editCourseBottomSheet = new EditCourseBottomSheet();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            args = getArguments();
            editCourseBottomSheet.setArguments(args);
            editCourseBottomSheet.show(getActivity().getSupportFragmentManager(),
                    "EditCourseBottomSheet");
        });
        cDescription.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }

    //TODO:REFERENCE HERE FOR SETTING COLORS
    private void setStatusColor(Courses courses) {
        if (courses.getStatusId() == IN_PROGRESS) {
            cStatus.setTextColor(Color.GREEN);
        }
        if (courses.getStatusId() == PLAN_TO_TAKE) {
            cStatus.setTextColor(Color.YELLOW);
        }
        if (courses.getStatusId() == DROPPED) {
            cStatus.setTextColor(Color.RED);
        }
        if (courses.getStatusId() == COMPLETED) {
            cStatus.setTextColor(Color.CYAN);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(COURSEACTIVITY_IN_HOME, "onViewCreated: HomeFragment was created..");
        initTermViewModel();

        CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(),
                courses -> {
                    cHeader.setText(courses.getCourseName());
                    cStartDate.setText(courses.getStartDate());
                    cEndDate.setText(courses.getEndDate());
                    cStatus.setText(courses.getStatus());
                    setStatusColor(courses);
                    cDescription.setText(courses.getCourseDesc());
                    for (Terms t : termsList) {
                        if (courses.getTermId() == t.getId()) {
                            cDueDate.setText(t.getEndDate());
                        }
                    }
                });
    }

    private void initTermViewModel() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                };
        TermViewModel termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(getViewLifecycleOwner(), termsObserver);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tb_menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.home_tb) {
            Intent toMain = new Intent(getContext(), MainActivity.class);
            startActivity(toMain);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
