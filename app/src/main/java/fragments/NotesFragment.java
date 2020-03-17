package fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.NotesRVAdapter;
import bottomSheets.AddNoteBottomSheet;
import bottomSheets.EditNoteBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Notes;
import entity.Terms;
import helpers.RVNotesTouchHelper;
import listeners.RVNotesTouchHelperListener;
import viewmodels.CourseViewModel;
import viewmodels.NotesViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.NOTES_FRAGMENT;
import static Utilities.Constants.N_COURSENOTE;
import static Utilities.Constants.N_CREATEDATE;
import static Utilities.Constants.N_ID;
import static Utilities.Constants.N_LASTUPDATE;
import static Utilities.Constants.N_NOTE_ID;

public class NotesFragment extends Fragment implements
        NotesRVAdapter.OnNoteClickListener, RVNotesTouchHelperListener {

    private static final String TEST = "TEST";

    private NotesRVAdapter notesRVAdapter;
    private RecyclerView notesRV;

    private TextView courseHeaderLbl;

    private NotesViewModel notesViewModel;

    private int note_Id;

    private List<Notes> notesList = new ArrayList<>();
    private List<Terms> termsList = new ArrayList<>();

    private Bundle args = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_fragment, container, false);

        courseHeaderLbl = view.findViewById(R.id.course_Header_lbl5);
        MaterialButton addNoteBtn = view.findViewById(R.id.add_note_btn);
        ImageButton infoBtn = view.findViewById(R.id.info_btn3);
        notesRV = view.findViewById(R.id.notes_list_rv);

        addNoteBtn.setOnClickListener(v -> addNote());
        infoBtn.setOnClickListener(v -> infoAlert());

        initTermViewModel();
        initNotesRV();
        initNotesVM();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCourseHeader();
    }

    private void addNote() {
        Fragment addNoteFragment = new AddNoteBottomSheet();
        FragmentManager fm = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager();
        FragmentTransaction transaction;
        args = getArguments();
        addNoteFragment.setArguments(args);
        transaction = fm.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(addNoteFragment, "AddNoteBottomSheet").commit();
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

    private void initNotesVM() {
        final Observer<List<Notes>> noteObserver =
                notes -> {
                    args = getArguments();
                    if (args != null) {
                        note_Id = args.getInt(COURSE_ID);
                        notesList.clear();
                        for (Notes note : notes) {
                            if (note.getNote_Id() == note_Id) {
                                notesList.add(note);
                                notesViewModel.setNotes(note_Id);
                            }
                        }
                        Log.d(TEST, "initPerfAssessVM: " + notesList);
                    }
                    if (notesRVAdapter == null) {
                        notesRVAdapter = new NotesRVAdapter(notesList, getActivity(),
                                this);
                        notesRV.setAdapter(notesRVAdapter);
                    } else {
                        notesRVAdapter.notifyDataSetChanged();
                    }
                };
        notesViewModel = new ViewModelProvider(this)
                .get(NotesViewModel.class);
        notesViewModel.mNotes.observe(getViewLifecycleOwner(), noteObserver);
    }

    private void initCourseHeader() {
        CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(),
                courses -> courseHeaderLbl.setText(courses.getCourseName()));
    }

    private void initNotesRV() {
        Log.d(NOTES_FRAGMENT, "initNotesRV: init notes recyclerview");

        notesRV.setHasFixedSize(true);
        notesRVAdapter = new NotesRVAdapter(notesList, getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notesRV.setLayoutManager(layoutManager);
        notesRV.setItemAnimator(new DefaultItemAnimator());
        notesRV.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback simpleCallback = new RVNotesTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(notesRV);
        notesRV.setAdapter(notesRVAdapter);
    }

    private void deleteNotes(View v, int position) {
        final Notes note = notesRVAdapter.removeItem(position);
        final Snackbar undo = Snackbar.make(v, "Note from course ID: "
                + note.getNote_Id() + " has been deleted.", Snackbar.LENGTH_LONG);
        undo.setAction("UNDO", v1 -> notesRVAdapter.restoreItem(note, position));
        undo.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    notesRVAdapter.removeItem(position);
                    notesViewModel.deleteNote(note);
                }
            }
        });
        undo.setActionTextColor(Color.BLACK);
        undo.show();
    }

    private void infoAlert() {
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(Objects.requireNonNull(getContext()),
                R.style.AlertDialogTheme);
        infoAlert.setMessage(R.string.notes_info);
        infoAlert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = infoAlert.create();
        alertDialog.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        deleteNotes(this.notesRV, position);
    }

    @Override
    public void onNoteClick(int position, int id, int note_Id, String courseNote,
                            String createDate, String lastUpdate) {
        EditNoteBottomSheet editNoteBottomSheet =
                new EditNoteBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putInt(N_ID, id);
        bundle.putInt(N_NOTE_ID, note_Id);
        bundle.putString(N_COURSENOTE, courseNote);
        bundle.putString(N_CREATEDATE, createDate);
        bundle.putString(N_LASTUPDATE, lastUpdate);
        editNoteBottomSheet.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        editNoteBottomSheet.show(getActivity().getSupportFragmentManager(),
                "EditNoteBottomSheet");
    }
}
