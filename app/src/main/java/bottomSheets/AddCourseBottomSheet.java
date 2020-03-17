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
import com.santalu.maskedittext.MaskEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Terms;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.ACTIVE;
import static Utilities.Constants.ACTIVE_TERM_FLAG;
import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.IN_ACTIVE;
import static Utilities.Constants.IN_PROGRESS;
import static Utilities.Constants.PLAN_TO_TAKE;
import static Utilities.Constants.TERM_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class AddCourseBottomSheet extends BottomSheetDialogFragment {

    private static final String TEST = "TEST_ADD_COURSE";

    private MaterialButton addNewCourseBtn;
    private TextView cStartDate, cEndDate;
    private EditText cName, mentorName, mentorEmail, cDescription;
    private MaskEditText mentorPhone;
    private Spinner courseStatus;

    private String ncStart;
    private String ncEnd;
    private String ncStatus;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private int ncStatusId, active;
    private int termId;

    private List<Terms> termsList = new ArrayList<>();
    private Bundle args = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_course_layout, container, false);

        addNewCourseBtn = view.findViewById(R.id.addNewCBtn);
        cName = view.findViewById(R.id.cName_add);
        cStartDate = view.findViewById(R.id.start_add);
        cEndDate = view.findViewById(R.id.end_add);
        mentorName = view.findViewById(R.id.mentorName_add);
        mentorEmail = view.findViewById(R.id.mentorEmail_add);
        mentorPhone = view.findViewById(R.id.mentorPhone_add);
        courseStatus = view.findViewById(R.id.status_picker);
        cDescription = view.findViewById(R.id.c_desc);

        cDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cDescription.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);

        setStartDatePicker();
        setEndDatePicker();
        addCourseDialog();

        initAddNewCourses();

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

    //TODO: WORK ON REMOVING FLAG FOR DELETING TERMS
    private void observeTermsVM() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                    for (Terms t : termsList) {
                        if (t.getTermFlag() == 0) {
                            t.setTermFlag(ACTIVE_TERM_FLAG);
                            termViewModel.insertTermFlag(ACTIVE_TERM_FLAG);
                            Log.d(TEST, "TERM_FLAGGED: " + t.getTermFlag());
                        }
                    }
                };
        termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(getViewLifecycleOwner(), termsObserver);
    }

    private void initAddNewCourses() {
        courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            ncStatus = courseStatus.getSelectedItem().toString();
            if (ncStatus.equals("In Progress")) {
                ncStatusId = IN_PROGRESS;
                active = ACTIVE;
            } else if (ncStatus.equals("Plan To Take")) {
                ncStatusId = PLAN_TO_TAKE;
                active = IN_ACTIVE;
            }
            courses.setStatusId(ncStatusId);
            courses.setActive(active);
            courses.setCourseName(cName.getText().toString().trim());
            courses.setStatus(ncStatus);
            courses.setCourseDesc(cDescription.getText().toString());
            courses.setMentorName(mentorName.getText().toString().trim());
            courses.setMentorPhone(Objects.requireNonNull(mentorPhone.getText()).toString().trim());
            courses.setMentorEmail(mentorEmail.getText().toString().trim());
            courses.setStartDate(cStartDate.getText().toString().trim());
            courses.setEndDate(cEndDate.getText().toString().trim());
        });
    }

    private void addCourseDialog() {
        addNewCourseBtn.setOnClickListener(v -> {
            AlertDialog.Builder addCourseAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addCourseAlert.setTitle("Please Confirm.");
            addCourseAlert.setMessage("Are you sure that you want to add the new course:"
                    + "\n\n" + cName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    if (!courseToTermEndDate()) {
                                        addNewCourse();
                                        //Flag term to course holder
                                        observeTermsVM();
                                        dismiss();
                                        Toast.makeText(getActivity(), "Successfully added course: "
                                                + cName.getText().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Start and end dates must be within " +
                                                "the associated term.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Check your dates. i.e. New course start date must be a future date.",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(),
                                        "Entry fields can't be empty.", Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
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

    private void addNewCourse() {
        ncStatus = courseStatus.getSelectedItem().toString();
        if (ncStatus.equals("In Progress")) {
            ncStatusId = IN_PROGRESS;
            active = ACTIVE;
        }
        if (ncStatus.equals("Plan To Take")) {
            ncStatusId = PLAN_TO_TAKE;
            active = IN_ACTIVE;
        }
        if (args != null) {
            termId = args.getInt(TERM_ID);
        }
        String ncName = cName.getText().toString().trim();
        String ncDesc = cDescription.getText().toString().trim();
        String ncMentorName = mentorName.getText().toString().trim();
        String ncMentorEmail = mentorEmail.getText().toString().trim();
        String ncMentorPhone = Objects.requireNonNull(mentorPhone.getText()).toString().trim();
        ncStart = cStartDate.getText().toString();
        ncEnd = cEndDate.getText().toString();
        courseViewModel.addNewCourse(0, termId, ncStatusId, active, ncName, ncStatus,
                ncDesc, ncMentorName, ncMentorPhone, ncMentorEmail, ncStart, ncEnd);

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
            month = month + 1;
            Log.d(DATE_PICK_ADD_COURSE, "onDateSet: selected start date: " + month + "/" + dayOfMonth + "/" + year);
            ncStart = month + "-" + dayOfMonth + "-" + year;
            cStartDate.setText(ncStart);
        };
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
                | TextUtils.isEmpty(mentorName.getText().toString().trim())
                | TextUtils.isEmpty(mentorEmail.getText().toString())
                | TextUtils.isEmpty(Objects.requireNonNull(mentorPhone
                .getText()).toString().trim())
                | TextUtils.isEmpty(cDescription.getText().toString().trim()));
    }

    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(cStartDate.getText().toString())
                .before(formatStringToDate(currentDate)) | formatStringToDate(cEndDate.getText().toString())
                .before(formatStringToDate(currentDate))) | formatStringToDate(cStartDate.getText().toString())
                .after(formatStringToDate(cEndDate.getText().toString()));
    }

    private boolean courseToTermEndDate() {
        try {
            //Validation
            Date cStart = formatStringToDate(cStartDate.getText().toString());
            Date cEnd = formatStringToDate(cEndDate.getText().toString());
            termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(TermViewModel.class);
            termViewModel.mTerms.observe(getViewLifecycleOwner(), terms -> {
                termsList.addAll(terms);
            });
            args = getArguments();
            if (args != null) {
                termId = args.getInt(TERM_ID);
            }
            Log.d(TEST, "Term ID: " + termId);
            for (Terms t : termsList) {
                if (t.getId() == termId) {
                    return (cStart.before(formatStringToDate(t.getStartDate()))
                            | cStart.after(formatStringToDate(t.getEndDate()))
                            | cEnd.after(formatStringToDate(t.getEndDate()))
                            | cEnd.before(formatStringToDate(t.getStartDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
