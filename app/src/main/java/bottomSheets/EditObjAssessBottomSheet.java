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
import entity.ObjectiveAssessments;
import viewmodels.CourseViewModel;
import viewmodels.ObjAssessmentsVM;

import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.EDIT_OBJ_ASSESS;
import static Utilities.Constants.O_ID;
import static Utilities.Constants.O_OBJ_ID;
import static Utilities.Constants.O_STATUS_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class EditObjAssessBottomSheet extends BottomSheetDialogFragment {

    private static final String TEST = "TEST";

    private EditText oAssessName;
    private TextView oDueDate;
    private MaterialButton oEditAssessBtn;

    private int assess_Id, obj_Id, o_Id, status_Id;
    private String oaEnd;

    private ObjAssessmentsVM objAssessmentsVM;

    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private List<Courses> coursesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.obj_assess_edit_layout, container, false);

        oAssessName = view.findViewById(R.id.edit_o_assess_name);
        oDueDate = view.findViewById(R.id.end_edit_assess2);
        oEditAssessBtn = view.findViewById(R.id.edit_o_btn);

        oAssessName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        initEditObjAssessments();

        setEndDatePicker();
        clearNamesOnClick();
        editOADialog();

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

    private void initEditObjAssessments() {
        Log.d(EDIT_OBJ_ASSESS, "initEditObjAssessments: obj. assessment info set to edit.");
        objAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.mObjAssessments.observe(getViewLifecycleOwner(), oAssessments -> {
            if (getArguments() != null) {
                obj_Id = getArguments().getInt(O_OBJ_ID);
                o_Id = getArguments().getInt(O_ID);
            }
            for (ObjectiveAssessments o : oAssessments) {
                if (o.getObj_Id() == obj_Id && o.getId() == o_Id) {
                    oAssessName.setText(o.getObjAssesName());
                    oDueDate.setText(o.getObj_due_date());
                }
            }
        });
    }

    private void saveEditObjs() {
        String oName = oAssessName.getEditableText().toString().trim();
        String oEnd = oDueDate.getText().toString().trim();
        Bundle args = getArguments();
        if (args != null) {
            obj_Id = args.getInt(O_OBJ_ID);
            assess_Id = args.getInt(O_ID);
            status_Id = args.getInt(O_STATUS_ID);
        }
        objAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.addNewObjAssess(assess_Id, obj_Id, oName, oEnd, status_Id);
    }

    private void editOADialog() {
        oEditAssessBtn.setOnClickListener(v -> {
            AlertDialog.Builder addCourseAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addCourseAlert.setTitle("Please Confirm.");
            addCourseAlert.setMessage("Are you sure that you want to edit the objective assessment:"
                    + "\n\n" + oAssessName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    if (!assessmentToCourseEndDate()) {
                                        saveEditObjs();
                                        dismiss();
                                        Toast.makeText(getActivity(), "Successfully updated objective assessment: "
                                                + oAssessName.getText().toString(), Toast.LENGTH_SHORT).show();
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
        oDueDate.setOnClickListener(v -> {
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
            oaEnd = month + "-" + dayOfMonth + "-" + year;
            oDueDate.setText(oaEnd);
        };
    }

    private void clearNamesOnClick() {
        oAssessName.setOnClickListener(v -> oAssessName.getText().clear());
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(oAssessName.getEditableText().toString().trim())
                | TextUtils.isEmpty(oDueDate.getText().toString().trim()));
    }

    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(oDueDate.getText().toString())
                .before(formatStringToDate(currentDate)));
    }

    private boolean assessmentToCourseEndDate() {
        try {
            //Validation
            Date oEditGoalD = formatStringToDate(oDueDate.getText().toString());
            CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(CourseViewModel.class);
            courseViewModel.mCourses.observe(getViewLifecycleOwner(), courses -> {
                coursesList.addAll(courses);
            });
            Bundle args = getArguments();
            if (args != null) {
                obj_Id = args.getInt(O_OBJ_ID);
                Log.d(TEST, String.valueOf(obj_Id));
            }
            for (Courses c : coursesList) {
                if (c.getId() == obj_Id) {
                    return (oEditGoalD.after(formatStringToDate(c.getEndDate())) ||
                            oEditGoalD.before(formatStringToDate(c.getStartDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

}
