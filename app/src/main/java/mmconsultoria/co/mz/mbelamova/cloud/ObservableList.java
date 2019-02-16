package mmconsultoria.co.mz.mbelamova.cloud;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Lifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class ObservableList<E> {
    Observable<E> listObservable;
    private List<E> data;
    private Lifecycle lifecycle;

    private ObservableList() {
        data = new ArrayList<>();

        listObservable = Observable.create(this::emitterConfig);
    }

    private <T> void emitterConfig(ObservableEmitter<T> emitter) {

    }

    public ObservableList Builder(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;

        return new ObservableList();
    }


    public void add(E element) {
        if (hasActiveObservers()) data.add(element);

    }

    private boolean hasActiveObservers() {
        if (lifecycle == null) {
            return false;
        }

        if (lifecycle.getCurrentState().equals(Lifecycle.State.STARTED)) {
            return true;
        }

        if (lifecycle.getCurrentState().equals(Lifecycle.State.RESUMED)) {
            return true;
        }

        return false;
    }

    public List<E> getNewData() {

        return null;
    }


}
