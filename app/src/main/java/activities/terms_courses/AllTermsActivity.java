package activities.terms_courses;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import activities.splash_main.MainActivity;
import adapters.TermRVAdapter;
import database.SurferDatabase;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;
import entity.Terms;
import fragments.AddTermFragment;
import helpers.RVTermTouchHelper;
import listeners.AddTermSwipeGestureListener;
import listeners.RVTermTouchHelperListener;
import viewmodels.CourseViewModel;
import viewmodels.TermViewModel;

import static Utilities.Constants.ALL_TERMS;
import static Utilities.Constants.IN_ACTIVE_TERM_FLAG;
import static Utilities.Constants.OPEN_ADD_TERM_FRAGMENT;
import static Utilities.Constants.TERM_ID;

public class AllTermsActivity extends AppCompatActivity implements RVTermTouchHelperListener,
        AddTermFragment.OnFragmentInteractionListener, TermRVAdapter.OnTermClickListener,
        TermRVAdapter.OnTermLongClickListener {

    private static final String TEST = "AllTermsTag";
    private static final String HIDE_FRAGMENT = "HideAddTermLandscape";

    private GestureDetector gestureDetector;
    private AddTermSwipeGestureListener addTermSwipeGestureListener =
            new AddTermSwipeGestureListener(this);

    private CardView tRootLayout;
    private RecyclerView allTermsRV;
    private TermRVAdapter termRVAdapter;

    private TermViewModel termViewModel;

    private Bundle args = new Bundle();
    private int termId;

    private boolean hasCourses;

    private List<Terms> termsList = new ArrayList<>();
    private List<Courses> coursesList = new ArrayList<>();
    private ArrayList<String> termNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_terms);
        Toolbar tb = findViewById(R.id.currTerm_TB);
        setSupportActionBar(tb);
        tb.setTitle(R.string.mainLbl2);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToMainActivity());

        addTermSwipeGestureListener.setActivity(this);
        gestureDetector = new GestureDetector(this,
                addTermSwipeGestureListener);

        ImageButton infoBtn = findViewById(R.id.info_btn);
        infoBtn.setOnClickListener(v -> infoAlert());

        initTermRecyclerView();
        initTermViewModel();

    }

    private void initTermViewModel() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                    for (Terms t : termsList) {
                        termNames.add(t.getTermName());
                    }
                    if (termRVAdapter == null) {
                        termRVAdapter = new TermRVAdapter(
                                termsList, this, this,
                                this);
                        allTermsRV.setAdapter(termRVAdapter);
                    } else {
                        termRVAdapter.notifyDataSetChanged();
                    }
                };
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.mTerms.observe(this, termsObserver);
    }

    private void initTermRecyclerView() {
        Log.d(ALL_TERMS, "initRecyclerView: init recyclerview");

        allTermsRV = findViewById(R.id.term_recyclerView);
        tRootLayout = findViewById(R.id.term_rv_card);

        allTermsRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        allTermsRV.setLayoutManager(layoutManager);
        allTermsRV.setItemAnimator(new DefaultItemAnimator());
        allTermsRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RVTermTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(allTermsRV);

    }

    private void infoAlert() {
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(this,
                R.style.AlertDialogTheme);
        infoAlert.setMessage(R.string.all_term_info);
        infoAlert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = infoAlert.create();
        alertDialog.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        deleteTermWithAlert(this.tRootLayout, position);
    }

    private void deleteTermWithAlert(View view, final int position) {
        final Terms term = termRVAdapter.removeItem(position);
        AlertDialog.Builder deleteTermAlert = new AlertDialog.Builder(this,
                R.style.AlertDialogTheme);
        deleteTermAlert.setTitle("Warning!");
        deleteTermAlert.setMessage("You are about to delete term '"
                + term.getTermName() + "' and all courses associated with it. \n"
                + "Do you want to proceed with deletion?");
        Log.d(TEST, "deleteTermWithAlert: " + term.getTermFlag());
        DialogInterface.OnClickListener deleteTermListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Flag term to empty
                    observeTermsVMOnDelete(term);
                    if (term.getTermFlag() == 0) {
                        termViewModel.deleteTerm(term);
                        Snackbar deleteDialog = Snackbar.make(view, "Term: " + term.getTermName() +
                                " has been deleted from the database.", Snackbar.LENGTH_LONG);
                        deleteDialog.setActionTextColor(Color.BLACK);
                        deleteDialog.show();
                    } else {
                        termRVAdapter.restoreItem(term, position);
                        Toast.makeText(this, "Can't delete terms with " +
                                "courses assigned to it.", Toast.LENGTH_LONG).show();
                        break;
                    }
                case DialogInterface.BUTTON_NEGATIVE:
                    termRVAdapter.restoreItem(term, position);
                    break;
            }
        };
        deleteTermAlert.setPositiveButton("Yes", deleteTermListener);
        deleteTermAlert.setNegativeButton("No", deleteTermListener);
        AlertDialog dialog = deleteTermAlert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void observeTermsVMOnDelete(Terms term) {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    for (Terms t : termEntities) {
                        if (notHaveCourses()) {
                            term.setTermFlag(IN_ACTIVE_TERM_FLAG);
                            termViewModel.insertTermFlag(IN_ACTIVE_TERM_FLAG);
                            Log.d(TEST, "initTermViewModel: " + t.getTermFlag());
                        }
                    }
                };
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.mTerms.observe(this, termsObserver);
    }

    //TODO: CHECK VALIDITY OF DELETED TERMS
    private boolean notHaveCourses() {
        CourseViewModel courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.mCourses.observe(this, courses -> {
            args = getIntent().getExtras();
            if (args != null) {
                termId = args.getInt(TERM_ID);
                for (Courses c : courses) {
                    if (c.getTermId() == termId) {
                        coursesList.clear();
                        coursesList.add(c);
                        hasCourses = coursesList.isEmpty();
                        Log.d(TEST, "hasCourses: coursesList is empty: " + hasCourses);
                    }
                }
            }
        });
        return hasCourses;
    }

    private void displayEditTermActivity() {
        Intent editTermIntent = new Intent(this, EditTermActivity.class);
        startActivity(editTermIntent);
        finish();
    }

    public void openAddTermFragment() {
        Bundle args = getIntent().getExtras();
        AddTermFragment fragment = new AddTermFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction openTransaction = fragmentManager.beginTransaction();
        openTransaction.addToBackStack(null);
        openTransaction.add(R.id.new_term_layout, fragment, "OPEN_ADD_TERM").commit();
        Log.i(OPEN_ADD_TERM_FRAGMENT, "openFragment: add term fragment swiped up.");
    }

    public void openAddTermLandscape(View view) {
        Bundle args = getIntent().getExtras();
        AddTermFragment fragment = new AddTermFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction openTransaction = fragmentManager.beginTransaction();
        openTransaction.addToBackStack(null);
        openTransaction.replace(R.id.new_term_layout, fragment, "OPEN_ADD_TERM").commit();
        Log.i(OPEN_ADD_TERM_FRAGMENT, "openFragment: add term fragment swiped up.");
    }

    public void hideAddTermFragment(View view) {
        FragmentTransaction hideTransaction = getSupportFragmentManager().beginTransaction();
        Fragment closeAddTerm = getSupportFragmentManager().findFragmentByTag("OPEN_ADD_TERM");
        if (closeAddTerm != null) {
            hideTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            hideTransaction.addToBackStack(null);
            hideTransaction.hide(closeAddTerm).commit();
        }
        Log.i(HIDE_FRAGMENT, "hideFragment: add term fragment swiped down.");
    }

    public void backToMainActivity() {
        Intent toMain = new Intent(AllTermsActivity.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    //OVERRIDED METHODS
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    @Override
    public void onTermClick(int position) {
        Intent selectTermIntent = new Intent(this, SelectedTermActivity.class);
        startActivity(selectTermIntent);
        finish();
    }

    @Override
    public void onTermLongClick(int position) {
        displayEditTermActivity();
    }

    @Override
    protected void onDestroy() {
        SurferDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}