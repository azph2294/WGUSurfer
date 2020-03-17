package viewmodels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import database.SurferRepository;
import entity.Terms;

public class TermViewModel extends AndroidViewModel {

    public LiveData<List<Terms>> mTerms;

    public MutableLiveData<Terms> termLiveData = new MutableLiveData<>();
    private SurferRepository surferRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermViewModel(@NonNull Application application) {
        super(application);
        surferRepository = SurferRepository.getInstance(getApplication());
        mTerms = surferRepository.mTerms;
    }

    public void addTermTestData() {
        surferRepository.addTermTestData();
    }

    public void setTerm(int termId) {
        executor.execute(() -> {
            Terms term = surferRepository.getTermById(termId);
            termLiveData.postValue(term);
        });
    }

    public void saveTerm(int termId, String tName, String startDate, String endDate) {
        Terms term = termLiveData.getValue();
        if (term != null) {
            term.setId(termId);
            term.setTermName(tName.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
        }
        surferRepository.updateTerm(term);
    }

    public void addNewTerm(String mTermName, String mStartDate, String mEndDate) {
        Terms term;
        if (TextUtils.isEmpty(mTermName.trim())) {
            return;
        }
        term = new Terms(mTermName.trim(), mStartDate, mEndDate);
        surferRepository.insertTerm(term);
    }

    public void deleteTerm(Terms deletedTerm) {
        surferRepository.deleteTerm(deletedTerm);
    }

    public void insertTermFlag(int termFlag) {
        Terms term;
        term = new Terms(termFlag);
        surferRepository.updateTerm(term);
    }

}
