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
import entity.ObjectiveAssessments;

public class ObjAssessmentsVM extends AndroidViewModel {

    public LiveData<List<ObjectiveAssessments>> mObjAssessments;
    public MutableLiveData<ObjectiveAssessments> objLiveData =
            new MutableLiveData<>();
    private SurferRepository surferRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ObjAssessmentsVM(@NonNull Application application) {
        super(application);
        surferRepository = SurferRepository.getInstance(getApplication());
        mObjAssessments = surferRepository.mObjAssessments;
    }

    public void addObjAssesTestData() {
        surferRepository.addObjAssesTestData();
    }

    public void setObjAssessments(int id) {
        executor.execute(() -> {
            ObjectiveAssessments objectiveAssessments =
                    surferRepository.getObjAssessmentsById(id);
            objLiveData.postValue(objectiveAssessments);
        });
    }

    public void addNewObjAssess(int id, int obj_id, String oaName, String oaEnd, int active) {
        ObjectiveAssessments newOA;
        TextUtils.isEmpty(oaName.trim());
        newOA = new ObjectiveAssessments(id, obj_id, oaName, oaEnd, active);
        surferRepository.insertOA(newOA);
    }

    public void deleteOA(ObjectiveAssessments o) {
        surferRepository.deleteOA(o);
    }

}
