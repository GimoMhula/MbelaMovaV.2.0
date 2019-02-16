package mmconsultoria.co.mz.mbelamova.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import mmconsultoria.co.mz.mbelamova.model.Person;

public class UserViewModel extends AndroidViewModel {
    LiveData<Person> personLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void signIn(CharSequence phoneNumber){

    }

    public void login(){

    }

    public void signOut(){

    }

    public void updateUser(Person person){

    }
}
