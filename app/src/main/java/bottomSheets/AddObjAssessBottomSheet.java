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
import viewmodels.CourseViewModel;
import viewmodels.ObjAssessmentsVM;

import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.C_STATUS_ID;
import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.TERM_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class AddObjAssessBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton addObjBtn;
    private TextView objED;
    private EditText objName;

    private int obj_Id, status_Id, termId;
    private String oEnd;

    private ObjAssessmentsVM objAssessmentsVM;

    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private Bundle args = new Bundle();
    private List<Courses> coursesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_obj_assess_layout, container, false);

        addObjBtn = view.findViewById(R.id.addNewOBtn);
        objName = view.findViewById(R.id.oName_add);
        objED = view.findViewById(R.id.end_o_assess);
        initAddObjAssessment();

        setEndDatePicker();
        addOADialog();

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

    private void initAddObjAssessment() {
        objAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.objLiveData.observe(getViewLifecycleOwner(), oAssessments -> {
            oAssessments.setObjAssesName(objName.getText().toString().trim());
            oAssessments.setObj_due_date(objED.getText().toString());
        });
    }

    private void addOADialog() {
        addObjBtn.setOnClickListener(v -> {
            AlertDialog.Builder addOAAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addOAAlert.setTitle("Please Confirm.");
            addOAAlert.setMessage("Are you sure that you want to add the new objective assessment:"
                    + "\n\n" + objName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    if (!oaToCourseEndDate()) {
                                        addNewOA();
                                        dismiss();
                                        Toast.makeText(getActivity(), "Successfully added objective assessment: "
                                                + objName.getText().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "The goal date must be within the associated course.",
                                                Toast.LENGTH_LONG).show();
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
            addOAAlert.setPositiveButton("Yes", alertOnClickListener);
            addOAAlert.setNegativeButton("No", alertOnClickListener);
            addOAAlert.setCancelable(false);
            AlertDialog dialog = addOAAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void addNewOA() {
        String oaName = objName.getText().toString().trim();
        String oaEnd = objED.getText().toString();
        args = getArguments();
        if (args != null) {
            obj_Id = args.getInt(COURSE_ID);
            status_Id = args.getInt(C_STATUS_ID);
        }
        objAssessmentsVM.addNewObjAssess(0, obj_Id, oaName, oaEnd, status_Id);
    }

    private void setEndDatePicker() {
        objED.setOnClickListener(v -> {
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
            oEnd = month + "-" + dayOfMonth + "-" + year;
            objED.setText(oEnd);
        };
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(objName.getEditableText().toString().trim())
                | TextUtils.isEmpty(objED.getText().toString().trim()));
    }

    //Check to see if date after current date
    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(objED.getText().toString())
                .before(formatStringToDate(currentDate)));
    }

    //Check to see if date within course
    private boolean oaToCourseEndDate() {
        try {
            //Validation
            Date oAddGoalD = formatStringToDate(objED.getText().toString());
            CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(CourseViewModel.class);
            courseViewModel.mCourses.observe(getViewLifecycleOwner(), courses ->
                    coursesList.addAll(courses));
            args = getArguments();
            if (args != null) {
                termId = args.getInt(TERM_ID);
            }
            for (Courses c : coursesList) {
                if (c.getTermId() == termId) {
                    return (oAddGoalD.after(formatStringToDate(c.getEndDate())) ||
                            oAddGoalD.before(formatStringToDate(c.getStartDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
