package bottomSheets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import viewmodels.NotesViewModel;

import static Utilities.Constants.COURSE_ID;

public class AddNoteBottomSheet extends BottomSheetDialogFragment {

    private EditText newNote;
    private MaterialButton saveNoteBtn;

    private NotesViewModel notesViewModel;

    private int note_Id;

    private Bundle args = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_add_layout, container, false);

        newNote = view.findViewById(R.id.note_add);
        saveNoteBtn = view.findViewById(R.id.addNewNoteBtn);

        newNote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        newNote.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);
        initAddNote();
        addNoteDialog();

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

    private void addNoteDialog() {
        saveNoteBtn.setOnClickListener(v -> {
            AlertDialog.Builder addNoteAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addNoteAlert.setTitle("Please Confirm.");
            addNoteAlert.setMessage("Are you sure that you want to add this note?");
            args = getArguments();
            if (args != null) {
                note_Id = args.getInt(COURSE_ID);
            }
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (!validateNote()) {
                            addNewNote();
                            dismiss();
                            Toast.makeText(getActivity(), "Successfully added note to course ID: "
                                    + note_Id, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Entry fields can't be empty.", Toast.LENGTH_LONG).show();
                        }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            addNoteAlert.setPositiveButton("Yes", alertOnClickListener);
            addNoteAlert.setNegativeButton("No", alertOnClickListener);
            addNoteAlert.setCancelable(false);
            AlertDialog dialog = addNoteAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void initAddNote() {
        notesViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NotesViewModel.class);
        notesViewModel.noteLiveData.observe(getViewLifecycleOwner(), notes ->
                notes.setCourseNote(newNote.getEditableText().toString().trim()));
    }

    private void addNewNote() {
        String n_note = newNote.getEditableText().toString().trim();
        args = getArguments();
        if (args != null) {
            note_Id = args.getInt(COURSE_ID);
        }
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(cal.getTime());
        notesViewModel.addNewNote(0, note_Id, n_note, currentDateTime, currentDateTime);
    }

    private boolean validateNote() {
        return (TextUtils.isEmpty(newNote.getEditableText().toString().trim()));
    }

}
