package mmconsultoria.co.mz.mbelamova.view_model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import mmconsultoria.co.mz.mbelamova.cloud.CloudRepository;
import mmconsultoria.co.mz.mbelamova.cloud.DatabaseValue;
import mmconsultoria.co.mz.mbelamova.cloud.TaskError;
import mmconsultoria.co.mz.mbelamova.model.VisitedPlace;

public class TimeLineViewModel extends AndroidViewModel {
    private MutableLiveData<List<VisitedPlace>> liveData;

    public TimeLineViewModel(@NonNull Application application) {
        super(application);
        liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>());

        init();
    }

    private void init() {
        CloudRepository<VisitedPlace> repository = new CloudRepository<>(VisitedPlace.class,DatabaseValue.VisitedPlacesTimeLine);
        List<VisitedPlace> list = liveData.getValue();
        repository.attachListener((CloudRepository.OnChildValueListener<VisitedPlace>) (data, error, movement) -> {
            if (error == TaskError.None && movement == CloudRepository.DatabaseMovement.Addition) {
                list.add(data);
            }
        });

    }


    public MutableLiveData<List<VisitedPlace>> getLiveData() {
        return liveData;
    }
}
