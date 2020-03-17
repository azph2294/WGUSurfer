package bottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Terms;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.ACTIVE;
import static Utilities.Constants.COMPLETED;
import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.DROPPED;
import static Utilities.Constants.EDIT_COURSE_IN_HOME;
import static Utilities.Constants.IN_ACTIVE;
import static Utilities.Constants.IN_PROGRESS;
import static Utilities.Constants.PLAN_TO_TAKE;
import static Utilities.Constants.TERM_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class EditCourseBottomSheet extends BottomSheetDialogFragment {

    private static final String TEST = "TEST";

    private MaterialButton editCourseBtn;
    private TextView cStartDate, cEndDate;
    private EditText cName, cDescription;
    private Spinner courseStatus;

    private String ncStart, ncEnd;
    private int termId, courseId, ncStatusId, active;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private CourseViewModel courseViewModel;

    private Bundle args = new Bundle();

    private List<Terms> termsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_course_layout, container, false);

        editCourseBtn = view.findViewById(R.id.editCBtn);
        cName = view.findViewById(R.id.cName_edit);
        cStartDate = view.findViewById(R.id.start_edit);
        cEndDate = view.findViewById(R.id.end_edit);
        courseStatus = view.findViewById(R.id.edit_status_picker);
        cDescription = view.findViewById(R.id.c_desc_edit);

        cDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cDescription.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);

        initTermViewModel();
        initEditCoursesViewModel();

        setStartDatePicker();
        setEndDatePicker();
        clearNamesOnClick();
        editCourseDialog();

        return view;
    }

    // Landscape view - expanded
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(dialog -> {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            BottomSheetBehavior behavior = BottomSheetBehavior.from(Objects.requireNonNull(bottomSheet));
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
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

    private void initEditCoursesViewModel() {
        Log.d(EDIT_COURSE_IN_HOME, "initEditCourses: course info set to edit.");
        courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            cName.setText(courses.getCourseName());
            cStartDate.setText(courses.getStartDate());
            cEndDate.setText(courses.getEndDate());
            cDescription.setText(courses.getCourseDesc());
        });
    }

    private void editCourseDialog() {
        editCourseBtn.setOnClickListener(v -> {
            AlertDialog.Builder addCourseAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addCourseAlert.setTitle("Please Confirm.");
            addCourseAlert.setMessage("Are you sure that you want to edit the course:"
                    + "\n\n" + cName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (!validateFields()) {
                            if (!courseToTermEndDate()) {
                                saveEditCourses();
                                dismiss();
                                Toast.makeText(getActivity(), "Successfully updated course: "
                                        + cName.getText().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Start and end dates must be within " +
                                        "the associated term.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    "Entry fields can't be empty.", Toast.LENGTH_LONG).show();
                        }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            addCourseAlert.setPositiveButton("Yes", alertOnClickListener);
            addCourseAlert.setNegativeButton("No", alertOnClickListener);
            addCourseAlert.setCancelable(false);
            AlertDialog dialog = addCourseAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void saveEditCourses() {
        //TermViewModel
        String ncStatus = courseStatus.getSelectedItem().toString();
        String ncName = cName.getText().toString().trim();
        String ncDesc = cDescription.getText().toString().trim();
        ncStart = cStartDate.getText().toString();
        ncEnd = cEndDate.getText().toString();
        args = getArguments();
        if (args != null) {
            termId = args.getInt(TERM_ID);
            courseId = args.getInt(COURSE_ID);
        }
        if (ncStatus.equals("In Progress")) {
            ncStatusId = IN_PROGRESS;
            active = ACTIVE;
        }
        if (ncStatus.equals("Plan To Take")) {
            ncStatusId = PLAN_TO_TAKE;
            active = IN_ACTIVE;
        }
        if (ncStatus.equals("Dropped")) {
            ncStatusId = DROPPED;
            active = ACTIVE;
        }
        if (ncStatus.equals("Completed")) {
            ncStatusId = COMPLETED;
            active = IN_ACTIVE;
        }
        courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            courseViewModel.saveCourse(courseId, termId, ncStatusId, active, ncName, ncStatus,
                    ncDesc, ncStart, ncEnd);
            courseViewModel.setCoursesById(courseId);
        });
    }

    private void setStartDatePicker() {
        cStartDate.setOnClickListener(v -> {
            //Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog sdPicker = new DatePickerDialog(Objects.requireNonNull(getActivity())
                    , R.style.DatePickerTheme, onDateSetListener, year, month, day);
            sdPicker.show();
        });
        onDateSetListener = (view1, year, month, dayOfMonth) -> {
            month++;
            Log.d(DATE_PICK_ADD_COURSE, "onDateSet: selected start date: " + month + "/" + dayOfMonth + "/" + year);
            ncStart = month + "-" + dayOfMonth + "-" + year;
            cStartDate.setText(ncStart);
        };
    }

    private void clearNamesOnClick() {
        cName.setOnClickListener(v -> cName.getText().clear());
        cDescription.setOnClickListener(v -> cDescription.getText().clear());
    }

    private void setEndDatePicker() {
        cEndDate.setOnClickListener(v -> {
            //Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog edPicker = new DatePickerDialog(Objects.requireNonNull(getActivity())
                    , R.style.DatePickerTheme, onDateSetListener2, year, month, day);
            edPicker.show();
        });
        onDateSetListener2 = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            Log.d(DATE_PICK_ADD_COURSE, "onDateSet: selected end date: " + month + "/" + dayOfMonth + "/" + year);
            ncEnd = month + "-" + dayOfMonth + "-" + year;
            cEndDate.setText(ncEnd);
        };
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(cName.getEditableText().toString().trim())
                | TextUtils.isEmpty(cStartDate.getText().toString().trim())
                | TextUtils.isEmpty(cEndDate.getText().toString().trim())
                | TextUtils.isEmpty(cDescription.getText().toString().trim()));
    }

    private boolean courseToTermEndDate() {
        try {
            //Validation
            Date cEditStart = formatStringToDate(cStartDate.getText().toString());
            Date cEditEnd = formatStringToDate(cEndDate.getText().toString());
            args = getArguments();
            if (args != null) {
                termId = args.getInt(TERM_ID);
                Log.d(TEST, String.valueOf(termId));
            }
            for (Terms t : termsList) {
                if (t.getId() == termId) {
                    return (cEditStart.before(formatStringToDate(t.getStartDate()))
                            | cEditEnd.after(formatStringToDate(t.getEndDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

}
