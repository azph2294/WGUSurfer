package bottomSheets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import viewmodels.CourseViewModel;

import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.TERM_ID;

public class EditMentorBottomSheet extends BottomSheetDialogFragment {

    private EditText m_name, m_email, m_phone;
    private MaterialButton editMentorBtn;

    private int termId, courseId;

    private CourseViewModel courseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_mentor_layout, container, false);

        m_name = view.findViewById(R.id.mentorName_edit);
        m_email = view.findViewById(R.id.mentorEmail_edit);
        m_phone = view.findViewById(R.id.mentorPhone_edit);
        editMentorBtn = view.findViewById(R.id.edit_m_btn);

        initEditMentor();
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

    private void initEditMentor() {
        courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            m_name.setText(courses.getMentorName());
            m_email.setText(courses.getMentorEmail());
            m_phone.setText(courses.getMentorPhone());
        });
    }

    private void editCourseDialog() {
        editMentorBtn.setOnClickListener(v -> {
            AlertDialog.Builder addCourseAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addCourseAlert.setTitle("Please Confirm.");
            addCourseAlert.setMessage("Are you sure that you want to edit the mentor to:"
                    + "\n\n" + m_name.getText().toString() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveEditMentor();
                        dismiss();
                        Toast.makeText(getActivity(), "Successfully updated mentor: "
                                + m_name.getText().toString(), Toast.LENGTH_SHORT).show();
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

    private void saveEditMentor() {
        String e_mName = m_name.getEditableText().toString().trim();
        String e_mEmail = m_email.getEditableText().toString().trim();
        String e_mPhone = m_phone.getEditableText().toString().trim();
        Bundle args = getArguments();
        if (args != null) {
            termId = args.getInt(TERM_ID);
            courseId = args.getInt(COURSE_ID);
        }
        courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            courseViewModel.saveMentor(courseId, termId, e_mName, e_mEmail, e_mPhone);
            courseViewModel.setCoursesById(courseId);
        });
    }

}
