package activities.splash_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import activities.terms_courses.AllTermsActivity;
import activities.terms_courses.CurrentTermActivity;
import adapters.TermRVAdapter;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import viewmodels.CourseViewModel;
import viewmodels.NotesViewModel;
import viewmodels.ObjAssessmentsVM;
import viewmodels.PerfAssessmentsVM;
import viewmodels.TermViewModel;

import static Utilities.Constants.TERM_ID;

public class MainActivity extends AppCompatActivity implements
        TermRVAdapter.OnTermClickListener {

    private Bundle args = new Bundle();
    private int termId;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = findViewById(R.id.currTerm_TB);
        setSupportActionBar(tb);
        setTitle(R.string.appName);

        if (dataAddedAlready()) {
            infoAlert();
        }

    }

    //Reminder to add data from database
    private void infoAlert() {
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(this,
                R.style.AlertDialogTheme);
        infoAlert.setMessage(R.string.alert_to_add_data);
        infoAlert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = infoAlert.create();
        alertDialog.show();
    }

    private boolean dataAddedAlready() {
        preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return !ranBefore;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.support) {
            Intent support = new Intent(this, SupportActivity.class);
            startActivity(support);
            finish();
            return true;
        }
        if (menuId == R.id.addTestData) {
            addTestAllData();
            addDataAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDataAlert() {
        AlertDialog.Builder dataAlert = new AlertDialog.Builder(this,
                R.style.AlertDialogTheme);
        dataAlert.setMessage("The information from my-surfer-database.db has been added.");
        final AlertDialog alert = dataAlert.create();
        alert.show();
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                alert.dismiss();
            }
        }.start();
    }

    //Add test data from database
    private void addTestAllData() {
        TermViewModel termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        termViewModel.addTermTestData();
        CourseViewModel courseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        courseViewModel.addCourseTestData();
        PerfAssessmentsVM perfAssessmentsVM = new ViewModelProvider(this)
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.addPerfAssesTestData();
        ObjAssessmentsVM objAssessmentsVM = new ViewModelProvider(this)
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.addObjAssesTestData();
        NotesViewModel notesViewModel = new ViewModelProvider(this)
                .get(NotesViewModel.class);
        notesViewModel.addNotesTestData();
    }

    //PORTRAIT - MAIN ACTIVITY
    public void displayCurrTerm(View view) {
        termId = args.getInt(TERM_ID);
        Intent currentTermScr = new Intent(this, CurrentTermActivity.class);
        currentTermScr.putExtra(TERM_ID, termId);
        startActivity(currentTermScr);
        finish();
    }

    public void displayAllTerms(View view) {
        termId = args.getInt(TERM_ID);
        Intent allTermsScr = new Intent(this, AllTermsActivity.class);
        allTermsScr.putExtra(TERM_ID, termId);
        startActivity(allTermsScr);
        finish();
    }

    public void displayTracker(View view) {
        termId = args.getInt(TERM_ID);
        Intent progTracker = new Intent(this, ProgTrackActivity.class);
        progTracker.putExtra(TERM_ID, termId);
        startActivity(progTracker);
        finish();
    }

    //LANDSCAPE - MAIN ACTIVITY
    public void displayCurrTerm_2(View view) {
        termId = args.getInt(TERM_ID);
        Intent currentTermScr = new Intent(this, CurrentTermActivity.class);
        currentTermScr.putExtra(TERM_ID, termId);
        startActivity(currentTermScr);
        finish();
    }

    public void displayAllTerms_2(View view) {
        termId = args.getInt(TERM_ID);
        Intent allTermsScr = new Intent(this, AllTermsActivity.class);
        allTermsScr.putExtra(TERM_ID, termId);
        startActivity(allTermsScr);
        finish();
    }

    public void displayTracker_2(View view) {
        termId = args.getInt(TERM_ID);
        Intent progTracker = new Intent(this, ProgTrackActivity.class);
        progTracker.putExtra(TERM_ID, termId);
        startActivity(progTracker);
        finish();
    }

    @Override
    public void onTermClick(int position) {
        this.termId = position;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
