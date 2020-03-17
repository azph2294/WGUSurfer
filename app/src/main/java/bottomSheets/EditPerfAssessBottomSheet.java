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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

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
import entity.Courses;
import entity.PerformanceAssessments;
import viewmodels.CourseViewModel;
import viewmodels.PerfAssessmentsVM;

import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.EDIT_PERF_ASSESS;
import static Utilities.Constants.P_ID;
import static Utilities.Constants.P_PER_ID;
import static Utilities.Constants.P_STATUS_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class EditPerfAssessBottomSheet extends BottomSheetDialogFragment {

    private static final String TEST = "TEST";

    private EditText pAssessName;
    private TextView pDueDate;
    private MaterialButton pEditAssessBtn;

    private int assess_Id, per_Id, p_Id, status_Id;
    private String paEnd;

    private PerfAssessmentsVM perfAssessmentsVM;

    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private List<Courses> coursesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perf_assess_edit_layout, container, false);

        pAssessName = view.findViewById(R.id.edit_p_assess_name);
        pDueDate = view.findViewById(R.id.end_edit_assess1);
        pEditAssessBtn = view.findViewById(R.id.edit_p_btn);

        pAssessName.setImeOptions(EditorInfo.IME_ACTION_DONE);

        initEditPerfAssessments();
        setEndDatePicker();
        clearNamesOnClick();
        editPADialog();

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

    private void initEditPerfAssessments() {
        Log.d(EDIT_PERF_ASSESS, "initEditPerfAssessments: perf. assessment info set to edit.");
        perfAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.mPerfAssessments.observe(getViewLifecycleOwner(), pAssessments -> {
            if (getArguments() != null) {
                per_Id = getArguments().getInt(P_PER_ID);
                p_Id = getArguments().getInt(P_ID);
            }
            for (PerformanceAssessments p : pAssessments) {
                if (p.getPer_Id() == per_Id && p.getId() == p_Id) {
                    pAssessName.setText(p.getPerAssesName());
                    pDueDate.setText(p.getPer_due_date());
                }
            }
        });
    }

    private void saveEditPerfs() {
        String pName = pAssessName.getEditableText().toString().trim();
        String pEnd = pDueDate.getText().toString().trim();
        Bundle args = getArguments();
        if (args != null) {
            per_Id = args.getInt(P_PER_ID);
            assess_Id = args.getInt(P_ID);
            status_Id = args.getInt(P_STATUS_ID);
        }
        perfAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.addNewPerfAssess(assess_Id, per_Id, pName, pEnd, status_Id);
    }

    private void editPADialog() {
        pEditAssessBtn.setOnClickListener(v -> {
            AlertDialog.Builder addCourseAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addCourseAlert.setTitle("Please Confirm.");
            addCourseAlert.setMessage("Are you sure that you want to edit the performance assessment:"
                    + "\n\n" + pAssessName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    if (!assessmentToCourseEndDate()) {
                                        saveEditPerfs();
                                        dismiss();
                                        Toast.makeText(getActivity(), "Successfully updated performance assessment: "
                                                + pAssessName.getText().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Assessment goal date must be within " +
                                                "the associated course.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Check your dates. i.e. Dates cannot be in the past.",
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

    private void setEndDatePicker() {
        pDueDate.setOnClickListener(v -> {
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
            month++;
            Log.d(DATE_PICK_ADD_COURSE, "onDateSet: selected end date: " + month + "/" + dayOfMonth + "/" + year);
            paEnd = month + "-" + dayOfMonth + "-" + year;
            pDueDate.setText(paEnd);
        };
    }

    private void clearNamesOnClick() {
        pAssessName.setOnClickListener(v -> pAssessName.getText().clear());
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(pAssessName.getEditableText().toString().trim())
                | TextUtils.isEmpty(pDueDate.getText().toString().trim()));
    }

    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(pDueDate.getText().toString())
                .before(formatStringToDate(currentDate)));
    }

    private boolean assessmentToCourseEndDate() {
        try {
            //Validation
            Date pEditGoalD = formatStringToDate(pDueDate.getText().toString());
            CourseViewModel courseViewModel = new ViewModelProvider(this)
                    .get(CourseViewModel.class);
            courseViewModel.mCourses.observe(getViewLifecycleOwner(),
                    courses -> coursesList.addAll(courses));
            Bundle args = getArguments();
            if (args != null) {
                per_Id = args.getInt(P_PER_ID);
                Log.d(TEST, String.valueOf(per_Id));
            }
            for (Courses c : coursesList) {
                if (c.getId() == per_Id) {
                    return (pEditGoalD.after(formatStringToDate(c.getEndDate())) ||
                            pEditGoalD.before(formatStringToDate(c.getStartDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

}
