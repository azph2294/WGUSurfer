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
import entity.PerformanceAssessments;

public class PerfAssessmentsVM extends AndroidViewModel {

    public LiveData<List<PerformanceAssessments>> mPerfAssessments;
    public MutableLiveData<PerformanceAssessments> perfLiveData =
            new MutableLiveData<>();
    private SurferRepository surferRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public PerfAssessmentsVM(@NonNull Application application) {
        super(application);
        surferRepository = SurferRepository.getInstance(getApplication());
        mPerfAssessments = surferRepository.mPerfAssessments;
    }

    public void addPerfAssesTestData() {
        surferRepository.addPerfAssesTestData();
    }

    public void setPerfAssessments(int per_Id) {
        executor.execute(() -> {
            PerformanceAssessments performanceAssessments =
                    surferRepository.getPerfAssessmentsById(per_Id);
            perfLiveData.postValue(performanceAssessments);
        });
    }

    public void addNewPerfAssess(int id, int per_Id, String paName, String paEnd, int active) {
        PerformanceAssessments newPA;
        TextUtils.isEmpty(paName.trim());
        newPA = new PerformanceAssessments(id, per_Id, paName, paEnd, active);
        surferRepository.insertPA(newPA);
    }

    public void deletePA(PerformanceAssessments p) {
        surferRepository.deletePA(p);
    }

}
