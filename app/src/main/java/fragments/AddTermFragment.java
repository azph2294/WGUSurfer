package fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Terms;
import viewmodels.TermViewModel;

import static Utilities.TimeFiles.formatStringToDate;

public class AddTermFragment extends Fragment {

    private static final String HIDE_FRAGMENT = "CloseFragment";
    private static final String DATE_PICKER_ADD_TERM = "AddTermDatePicker";

    private TextInputEditText termName;
    private TextView selectStart, selectEnd, popupSelSD, popupSelED;
    private MaterialButton addBtn;

    private String startDate, endDate;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    private TermViewModel termViewModel;

    private List<Terms> termsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_add_term, container, false);

        final GestureDetector gestureDetector = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        return false;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onContextClick(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        final int MIN_SWIPE_DISTANCE_Y = 0;
                        final int MAX_SWIPE_DISTANCE_Y = 1000;
                        float deltaY = e1.getY() - e2.getY();
                        float deltaYAbs = Math.abs(deltaY);
                        try {
                            if (deltaYAbs >= MIN_SWIPE_DISTANCE_Y && deltaYAbs <= MAX_SWIPE_DISTANCE_Y) {
                                if (deltaY < 0) {
                                    hideAddTermFragment();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
        view.setOnTouchListener((v, event) -> {
            if (v.getWindowId().isFocused()) {
                return gestureDetector.onTouchEvent(event);
            }
            return true;
        });

        selectStart = view.findViewById(R.id.selectSD);
        selectEnd = view.findViewById(R.id.selectED);
        termName = view.findViewById(R.id.editTermName);
        addBtn = view.findViewById(R.id.addTermBtn);
        popupSelSD = view.findViewById(R.id.popup_SD);
        popupSelED = view.findViewById(R.id.popup_ED);

        initTermViewModel();
        initAddTermViewModel();

        setStartDatePicker();
        setEndDatePicker();
        clearTermNameOnClick();
        addTermDialog();

        return view;
    }

    private void initTermViewModel() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                };
        termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(getViewLifecycleOwner(), termsObserver);
    }

    private void initAddTermViewModel() {
        termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(TermViewModel.class);
        termViewModel.termLiveData.observe(getViewLifecycleOwner(), term -> {
            term.setTermName(termName.getEditableText().toString());
            term.setStartDate(selectStart.getText().toString());
            term.setEndDate(selectEnd.getText().toString());
        });
    }

    private void addTermDialog() {
        addBtn.setOnClickListener(v -> {
            AlertDialog.Builder addTermAlert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),
                    R.style.AlertDialogTheme);
            addTermAlert.setTitle("Please Confirm.");
            addTermAlert.setMessage("Are you sure that you want to add the new term:" + "\n\n" +
                    termName.getText() + "?");
            DialogInterface.OnClickListener alertOnClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (!validateFields()) {
                                if (!dates()) {
                                    addNewTerm();
                                    Toast.makeText(this.getActivity(), "Successfully added term: " +
                                                    Objects.requireNonNull(termName.getText()).toString(),
                                            Toast.LENGTH_LONG).show();
                                    hideAddTermFragment();
                                } else {
                                    Toast.makeText(this.getActivity(),
                                            "Check your dates. i.e. New term start date must be a future date.",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this.getActivity(),
                                        "Entry fields can't be empty.", Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            addTermAlert.setPositiveButton("Yes", alertOnClickListener);
            addTermAlert.setNegativeButton("No", alertOnClickListener);
            addTermAlert.setCancelable(false);
            AlertDialog dialog = addTermAlert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void addNewTerm() {
        String mTermName = termName.getEditableText().toString();
        String mStartDate = selectStart.getText().toString();
        String mEndDate = selectEnd.getText().toString();
        termViewModel.addNewTerm(mTermName, mStartDate, mEndDate);
    }

    private void setStartDatePicker() {
        selectStart.setOnClickListener(v -> {
            //Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog sdPicker = new DatePickerDialog(Objects.requireNonNull(getActivity())
                    , R.style.DatePickerTheme, onDateSetListener, year, month, day);
            sdPicker.show();
        });
        onDateSetListener = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            Log.d(DATE_PICKER_ADD_TERM, "onDateSet: selected start date: " + month + "/" + dayOfMonth + "/" + year);
            startDate = month + "-" + dayOfMonth + "-" + year;
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

            DatePickerDialog edPicker = new DatePickerDialog(Objects.requireNonNull(getActivity())
                    , R.style.DatePickerTheme, onDateSetListener2, year, month, day);
            edPicker.show();
        });
        onDateSetListener2 = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            Log.d(DATE_PICKER_ADD_TERM, "onDateSet: selected end date: " + month + "/" + dayOfMonth + "/" + year);
            endDate = month + "-" + dayOfMonth + "-" + year;
            selectEnd.setText(endDate);
            popupSelED.setText(R.string.popUpEnd);
        };
    }

    private void clearTermNameOnClick() {
        termName.setOnClickListener(v -> termName.getEditableText().clear());
    }

    private void hideAddTermFragment() {
        FragmentTransaction hideTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        Fragment closeAddTerm = getActivity().getSupportFragmentManager().findFragmentByTag("OPEN_ADD_TERM");
        if (closeAddTerm != null) {
            hideTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            hideTransaction.addToBackStack(null);
            hideTransaction.hide(closeAddTerm).commit();
        }
        Log.i(HIDE_FRAGMENT, "hideFragment: add term fragment swiped down.");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private boolean validateFields() {
        return (TextUtils.isEmpty(termName.getEditableText().toString().trim())
                | TextUtils.isEmpty(selectStart.getText().toString().trim())
                | TextUtils.isEmpty(selectEnd.getText().toString().trim()));
    }

    private boolean dates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());

        return (formatStringToDate(selectStart.getText().toString())
                .before(formatStringToDate(currentDate)) | formatStringToDate(selectEnd.getText().toString())
                .before(formatStringToDate(currentDate))) | formatStringToDate(selectStart.getText().toString())
                .after(formatStringToDate(selectEnd.getText().toString()));
    }

}