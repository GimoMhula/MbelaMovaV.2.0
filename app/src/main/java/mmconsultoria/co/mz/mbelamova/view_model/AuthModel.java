package mmconsultoria.co.mz.mbelamova.view_model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mmconsultoria.co.mz.mbelamova.cloud.CloudRepository;
import mmconsultoria.co.mz.mbelamova.cloud.DatabaseValue;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.model.Person;
import timber.log.Timber;

import static java.lang.String.valueOf;

public class AuthModel extends AndroidViewModel {
    private AuthService authService;
    private LiveData<Person> userData;

    public AuthModel(@NonNull Application application) {
        super(application);
        authService = AuthService.getInstance();
        userData = new MutableLiveData<>();

        queryCurrentUser(authService.getUserId());
    }

    private void queryCurrentUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            Timber.d(valueOf(userId));
            return;
        }

        CloudRepository<Person> repository = new CloudRepository<>(Person.class, DatabaseValue.People);
        repository.setSubPath(userId);
        repository.attachListener((CloudRepository.OnChildValueListener<Person>) (data, error, movement) -> {
            if (movement != CloudRepository.DatabaseMovement.Removal)
                ((MutableLiveData<Person>) userData).postValue(data);
        });
    }

    public Single<AuthService.AuthResult> signIn(String phoneNumber, FragmentActivity activity) {
        return authService.signIn(activity, phoneNumber)
                .subscribeOn(Schedulers.io())
                .doAfterSuccess(authResult -> loadUserData(activity));
    }

    public Single<AuthService.AuthResult> signUp(Person person, FragmentActivity activity) {
        return Single.<AuthService.AuthResult>create(emitter -> {
            signIn(person.getPhoneNumber(), activity)
                    .doAfterSuccess(authResult -> {
                        recordUserdataOnCloud(person, (BaseActivity) activity)
                                .subscribe(emitter::onSuccess, emitter::onError);
                    });
        }).subscribeOn(Schedulers.io());

    }

    public Single<AuthService.AuthResult> resendCode(String phoneNumber, FragmentActivity activity) {
        return null;

    }

    public Single<AuthService.AuthResult> recordUserdataOnCloud(Person person, BaseActivity activity) {
        person.setId(authService.getUserId());
        Timber.d(valueOf(person));

        return Single.<AuthService.AuthResult>create(emitter -> {
            CloudRepository<Person> repository = new CloudRepository<>(Person.class, DatabaseValue.People);
            repository.upload(person, activity).subscribe(taskError -> {
                Timber.d(person.toString());
                saveUserData(person, activity);
                emitter.onSuccess(AuthService.AuthResult.USER_CREATED);
            }, emitter::onError);
        }).subscribeOn(Schedulers.io());
    }

    public Single<AuthService.AuthResult> sendConfirmCode(String code) {
        return authService.verifySmsCode(code)
                .subscribeOn(Schedulers.io());
    }

    public Single searchUser(String phoneNumber) {
        Timber.d(phoneNumber);
        return Single.create(emitter -> {

            DatabaseReference dataRef = FirebaseDatabase.getInstance()
                    .getReference(DatabaseValue.People.name());
            dataRef.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Timber.d("Database_key: " + dataSnapshot.getKey() + valueOf(dataSnapshot.getValue()));
                    Object value = dataSnapshot.getValue();
                    if (value != null) {
                        emitter.onSuccess(value);
                    } else emitter.onSuccess(DatabaseValue.NULL);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        }).subscribeOn(Schedulers.io());

    }


    public LiveData<Person> getUser() {
        return userData;
    }


    private void saveUserData(Person person, FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(DatabaseValue.AuthData.name(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(DatabaseValue.first_name.name(), person.getName());
        editor.putString(DatabaseValue.family_name.name(), person.getLastName());
        editor.putString(DatabaseValue.phone_number.name(), person.getPhoneNumber());
        editor.putString(DatabaseValue.user_id.name(), person.getId());
        editor.putString(DatabaseValue.driver_id.name(), person.getDriverId());
        editor.apply();
    }

    public void loadUserData(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(DatabaseValue.AuthData.name(), Context.MODE_PRIVATE);
        Person person = new Person();

        String firstName = preferences.getString(DatabaseValue.first_name.name(), "");
        String lastName = preferences.getString(DatabaseValue.family_name.name(), "");
        String phoneNumber = preferences.getString(DatabaseValue.phone_number.name(), "");
        String userId = preferences.getString(DatabaseValue.user_id.name(), "");
        String driverId = preferences.getString(DatabaseValue.driver_id.name(), "");
        String photoUri = preferences.getString(DatabaseValue.photo_uri.name(), "");
        String email = preferences.getString(DatabaseValue.email.name(), "");

        person.setName(firstName);
        person.setLastName(lastName);
        person.setPhoneNumber(phoneNumber);
        person.setEmail(email);
        person.setId(userId);
        person.setDriverId(driverId);
        person.setPhotoUri(photoUri);
    }


    public boolean isUserSignedIn() {
        return authService.isUserSignedIn();
    }
}
