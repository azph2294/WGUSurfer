package bottomSheets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Notes;
import entity.Terms;
import viewmodels.NotesViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.N_CREATEDATE;
import static Utilities.Constants.N_ID;
import static Utilities.Constants.N_NOTE_ID;

public class EditNoteBottomSheet extends BottomSheetDialogFragment {

    private static final String TEST = "EditNoteBottomSheet";

    private TextView note_Id_txt, createDateTxt, lastUpdateTxt;
    private EditText editNote;
    private MaterialButton saveNoteBtn;

    private NotesViewModel notesViewModel;
    private int note_Id, courseId;
    private String n_createDate;

    private List<Terms> termsList = new ArrayList<>();

    private Bundle args = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_edit_layout, container, false);

        note_Id_txt = view.findViewById(R.id.note_id_num);
        createDateTxt = view.findViewById(R.id.createDate_txt);
        lastUpdateTxt = view.findViewById(R.id.lastUpdate_txt);
        editNote = view.findViewById(R.id.note_edit_txt);

        editNote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editNote.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);

        ImageButton shareNoteBtn = view.findViewById(R.id.share_imgBtn);
        saveNoteBtn = view.findViewById(R.id.save_notes_btn);

        initTermViewModel();
        initNoteViewModel();
        clearNote();
        editNoteDialog();

        //Share notes via chooser..
        shareNoteBtn.setOnClickListener(v -> shareNotes());

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
        TermViewModel termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(getViewLifecycleOwner(), termsObserver);
    }

    private void initNoteViewModel() {
        notesViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NotesViewModel.class);
        notesViewModel.mNotes.observe(getViewLifecycleOwner(), notes -> {
            args = getArguments();
            if (args != null) {
                note_Id = args.getInt(N_ID);
                courseId = args.getInt(N_NOTE_ID);
                Log.d(TEST, "initNoteViewModel: " + args.toString());
            }
            for (Notes n : notes) {
                if (note_Id == n.getN_id() && courseId == n.getNote_Id()) {
                    note_Id_txt.setText(String.valueOf(n.getNote_Id()));
                    editNote.setText(n.getCourseNote());
                    createDateTxt.setText(n.getCreateDate());
                    lastUpdateTxt.setText(n.getLastUpdate());
                }
            }
        });
    }

    private void shareNotes() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String sharedNote = editNote.getEditableText().toString().trim();
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedNote);
        startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

    private void saveNote() {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(cal.getTime());

        String note = editNote.getText().toString().trim();
        args = getArguments();
        if (args != null) {
            note_Id = args.getInt(N_ID);
            courseId = args.getInt(N_NOTE_ID);
            n_createDate = args.getString(N_CREATEDATE);
        }
        notesViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NotesViewModel.class);
        notesViewModel.addNewNote(note_Id, courseId, note, n_createDate, currentDateTime);
    }

    private void editNoteDialog() {
        saveNoteBtn.setOnClickListener(v -> {
            AlertDialog.Builder addNoteAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addNoteAlert.setTitle("Please Confirm.");
            addNoteAlert.setMessage("Are you sure that you want to edit this note?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (!validateNote()) {
                            saveNote();
                            dismiss();
                            Toast.makeText(getActivity(), "Successfully updated this note.",
                                    Toast.LENGTH_SHORT).show();
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

    private boolean validateNote() {
        return (TextUtils.isEmpty(editNote.getEditableText().toString().trim()));
    }

    private void clearNote() {
        editNote.setOnClickListener(v -> editNote.setText(""));
    }

}
