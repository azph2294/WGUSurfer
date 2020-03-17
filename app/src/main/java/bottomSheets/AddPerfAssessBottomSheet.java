package bottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

import activities.splash_main.MainActivity;
import dataProvider.CourseDataProvider;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.PerformanceAssessments;
import viewmodels.CourseViewModel;
import viewmodels.PerfAssessmentsVM;

import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.C_STATUS_ID;
import static Utilities.Constants.DATE_PICK_ADD_COURSE;
import static Utilities.Constants.GOAL_D;
import static Utilities.TimeFiles.formatStringToDate;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class AddPerfAssessBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton addPerfBtn;
    private TextView perfED;
    private EditText perfName;

    private int per_Id, status_Id;
    private String paEnd;

    private PerfAssessmentsVM perfAssessmentsVM;

    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private Bundle args = new Bundle();
    private List<Courses> coursesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_perf_assess_layout, container, false);

        addPerfBtn = view.findViewById(R.id.addNewPBtn);
        perfName = view.findViewById(R.id.pName_add);
        perfED = view.findViewById(R.id.end_p_assess);
        initAddPerfAssessment();

        setEndDatePicker();
        addPADialog();

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

    private void initAddPerfAssessment() {
        perfAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.perfLiveData.observe(getViewLifecycleOwner(), pAssessments -> {
            pAssessments.setPerAssesName(perfName.getText().toString().trim());
            pAssessments.setPer_due_date(perfED.getText().toString());
        });
    }

    private void addPADialog() {
        addPerfBtn.setOnClickListener(v -> {
            AlertDialog.Builder addPAAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addPAAlert.setTitle("Please Confirm.");
            addPAAlert.setMessage("Are you sure that you want to add the new performance assessment:"
                    + "\n\n" + perfName.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    if (!paToCourseEndDate()) {
                                        addNewPA();
                                        dismiss();
                                        Toast.makeText(getActivity(), "Successfully added performance assessment: "
                                                + perfName.getText().toString(), Toast.LENGTH_SHORT).show();
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
            addPAAlert.setPositiveButton("Yes", alertOnClickListener);
            addPAAlert.setNegativeButton("No", alertOnClickListener);
            addPAAlert.setCancelable(false);
            AlertDialog dialog = addPAAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void addNewPA() {
        String paName = perfName.getText().toString().trim();
        String paEnd = perfED.getText().toString();
        args = getArguments();
        if (args != null) {
            per_Id = args.getInt(COURSE_ID);
            status_Id = args.getInt(C_STATUS_ID);
        }
        perfAssessmentsVM.addNewPerfAssess(0, per_Id, paName, paEnd, status_Id);
    }

    private void setEndDatePicker() {
        perfED.setOnClickListener(v -> {
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
            perfED.setText(paEnd);
        };
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(perfName.getEditableText().toString().trim())
                | TextUtils.isEmpty(perfED.getText().toString().trim()));
    }

    //Check to see if date after current date
    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(perfED.getText().toString())
                .before(formatStringToDate(currentDate)));
    }

    //Check to see if date within course
    private boolean paToCourseEndDate() {
        try {
            //Validation
            Date pAddGoalD = formatStringToDate(perfED.getText().toString());
            CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(CourseViewModel.class);
            courseViewModel.mCourses.observe(getViewLifecycleOwner(), courses ->
                    coursesList.addAll(courses));
            args = getArguments();
            if (args != null) {
                per_Id = args.getInt(COURSE_ID);
            }
            for (Courses c : coursesList) {
                if (c.getId() == per_Id) {
                    return (pAddGoalD.after(formatStringToDate(c.getEndDate())) ||
                            pAddGoalD.before(formatStringToDate(c.getStartDate())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
