package activities.terms_courses;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Terms;
import viewmodels.TermViewModel;

import static Utilities.Constants.DATE_PICKER_EDIT_TERM;
import static Utilities.Constants.TERM_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class EditTermActivity extends AppCompatActivity {

    private TextInputEditText termName;
    private TextView termHeading, selectStart, selectEnd, popupSelSD, popupSelED;
    private MaterialButton editBtn;

    private int termId;

    private TermViewModel termViewModel;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private List<Terms> termsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar tb = findViewById(R.id.currTerm_TB);
        tb.setTitle(R.string.editTerm);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToAllTerms());

        termHeading = findViewById(R.id.editTermHeading);
        selectStart = findViewById(R.id.editSelectSD);
        selectEnd = findViewById(R.id.editSelectED);
        termName = findViewById(R.id.editTermName2);
        editBtn = findViewById(R.id.editTermBtn);
        popupSelSD = findViewById(R.id.editPopup_SD);
        popupSelED = findViewById(R.id.editPopup_ED);

        initTermViewModel();
        initEditTermViewModel();

        setStartDatePicker();
        setEndDatePicker();
        clearTermNameOnClick();
        editTermDialog();

    }

    private void initTermViewModel() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                };
        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.mTerms.observe(this, termsObserver);
    }

    private void initEditTermViewModel() {
        termViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        termViewModel.termLiveData.observe(this, term -> {
            if (term != null) {
                termHeading.setText(term.getTermName());
                selectStart.setText(term.getStartDate());
                selectEnd.setText(term.getEndDate());
                termName.setText(term.getTermName());
                popupSelSD.setText(R.string.popUpStart);
                popupSelED.setText(R.string.popUpEnd);
            }
        });
        Bundle liveTerms = getIntent().getExtras();
        if (liveTerms != null) {
            int termId = liveTerms.getInt(TERM_ID);
            termViewModel.setTerm(termId);
        }
    }

    private void editTermDialog() {
        editBtn.setOnClickListener(v -> {
            AlertDialog.Builder editTermAlert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            editTermAlert.setTitle("Please Confirm.");
            editTermAlert.setMessage("Are you sure that you want to edit term:" + "\n\n" +
                    termName.getText());
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    saveTerm();
                                    Toast.makeText(this, "Successfully updated term: " +
                                                    Objects.requireNonNull(termName.getText()).toString(),
                                            Toast.LENGTH_SHORT).show();
                                    backToAllTerms();
                                } else {
                                    Toast.makeText(this,
                                            "Check your dates.",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this,
                                        "Entry fields can't be empty.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            editTermAlert.setPositiveButton("Yes", alertOnClickListener);
            editTermAlert.setNegativeButton("No", alertOnClickListener);
            editTermAlert.setCancelable(false);
            AlertDialog dialog = editTermAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void saveTerm() {
        String tName = Objects.requireNonNull(termName.getText()).toString();
        String startDate = selectStart.getText().toString();
        String endDate = selectEnd.getText().toString();
        Bundle args = getIntent().getExtras();
        if (args != null) {
            termId = args.getInt(TERM_ID);
        }
        termViewModel.saveTerm(termId, tName, startDate, endDate);
    }

    private void setStartDatePicker() {
        selectStart.setOnClickListener(v -> {
            //Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog sdPicker = new DatePickerDialog(this,
                    R.style.DatePickerTheme, onDateSetListener, year, month, day);
            sdPicker.show();
        });
        onDateSetListener = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            Log.d(DATE_PICKER_EDIT_TERM, "onDateSet: edit selected start date: " + month + "/" + dayOfMonth + "/" + year);
            String startDate = month + "-" + dayOfMonth + "-" + year;
            selectStart.setText(startDate);
            popupSelSD.setText(R.string.popUpStart);
        };
    }

    private void setEndDatePicker() {
        selectEnd.setOnClickListener(v -> {
            //Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog edPicker = new DatePickerDialog(this,
                    R.style.DatePickerTheme, onDateSetListener2, year, month, day);
            edPicker.show();
        });
        onDateSetListener2 = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            Log.d(DATE_PICKER_EDIT_TERM, "onDateSet: edit selected end date: " + month + "/" + dayOfMonth + "/" + year);
            String endDate = month + "-" + dayOfMonth + "-" + year;
            selectEnd.setText(endDate);
            popupSelED.setText(R.string.popUpEnd);
        };
    }

    private void clearTermNameOnClick() {
        termName.setOnClickListener(v -> termName.setText(""));
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(termName.getEditableText().toString().trim())
                | TextUtils.isEmpty(selectStart.getText().toString().trim())
                | TextUtils.isEmpty(selectEnd.getText().toString().trim()));
    }

    private boolean dates() throws ParseException {
        return (formatStringToDate(selectEnd.getText().toString())
                .before(formatStringToDate(selectStart.getText().toString()))
                | formatStringToDate(selectStart.getText().toString())
                .after(formatStringToDate(selectEnd.getText().toString()))
                | formatStringToDate(selectStart.getText().toString())
                .equals(formatStringToDate(selectEnd.getText().toString())));
    }

    private void backToAllTerms() {
        Intent toAllTerms = new Intent(this, AllTermsActivity.class);
        startActivity(toAllTerms);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToAllTerms();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
