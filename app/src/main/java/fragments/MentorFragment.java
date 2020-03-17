package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import bottomSheets.EditMentorBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import viewmodels.CourseViewModel;

public class MentorFragment extends Fragment {

    private static final String TEST = "Mentor_args";
    private TextView cHeader, mName, mEmail, mPhone;

    private Bundle args = new Bundle();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mentor_fragment, container, false);

        mName = view.findViewById(R.id.mentorName_txt);
        mEmail = view.findViewById(R.id.mentorEmail_txt);
        mPhone = view.findViewById(R.id.mentorPhone_txt);
        cHeader = view.findViewById(R.id.course_Header_lbl_m);

        FloatingActionButton mEditFAB = view.findViewById(R.id.mentor_edit_FAB);
        mEditFAB.setOnClickListener(v -> {
            EditMentorBottomSheet editMentorBottomSheet =
                    new EditMentorBottomSheet();
            args = getArguments();
            if (args != null) {
                Log.d(TEST, "Mentor_Args: " + args.toString());
            }
            editMentorBottomSheet.setArguments(args);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            editMentorBottomSheet.show(getActivity().getSupportFragmentManager(),
                    "EditMentorBottomSheet");
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(), courses -> {
            mName.setText(courses.getMentorName());
            mEmail.setText(courses.getMentorEmail());
            mPhone.setText(courses.getMentorPhone());
            cHeader.setText(courses.getCourseName());
        });

    }

}
