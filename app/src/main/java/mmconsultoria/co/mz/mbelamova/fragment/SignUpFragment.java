package mmconsultoria.co.mz.mbelamova.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.activity.DriverMapsActivity;
import mmconsultoria.co.mz.mbelamova.cloud.DatabaseValue;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;
import mmconsultoria.co.mz.mbelamova.model.Person;
import mmconsultoria.co.mz.mbelamova.view_model.AuthModel;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment {
    private static final int RC_SIGN_IN = 9001;
    public final int requestCode = 100;

    @BindView(R.id.sign_up_is_driver_check)
    public CheckBox driverCheck;
    @BindView(R.id.sign_up_image)
    public ImageView userImage;
    @BindView(R.id.sign_up_name_text)
    public EditText nameTxt;
    @BindView(R.id.sign_up_surname_text)
    public EditText surnameTxt;
    @BindView(R.id.sign_up_email_text)
    public EditText emailTxt;
    private Uri imageUri;
    private String phoneNumber;

    //Google account
    GoogleSignInAccount acct;
    GoogleSignInClient mGoogleSignInClient;

    public static SignUpFragment newInstance(String phoneNumber) {


        Bundle args = new Bundle();
        args.putString(DatabaseValue.phone_number.name(), phoneNumber);
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        signIn();

        if (getArguments() != null) {
            phoneNumber = getArguments().getString(DatabaseValue.phone_number.name());
        }



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }


    @OnClick(R.id.sign_up_btn)
    public void signUpPerson(View view) {
        if (!isFieldsValid()) {
            return;
        }

        Person person = createPerson();


        if (person != null) {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.show();

            Timber.d(person.toString());
            AuthModel authentication = ViewModelProviders
                    .of(getActivity())
                    .get(AuthModel.class);

            authentication.recordUserdataOnCloud(person, (BaseActivity) getActivity())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(next -> {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Usuario Criado com sucesso", Toast.LENGTH_SHORT).show();
                                if (driverCheck.isChecked()) {
                                    swapFragment(R.id.login_container, new VehicleSignUpFragment());
                                    return;
                                }
                                startActivity(DriverMapsActivity.class, null, null);
                            },
                            throwable -> {
                                Timber.e(throwable);
                                dialog.dismiss();
                            });
        }
    }

    @OnClick(R.id.sign_up_image)
    public void callImage(View view) {
        callImageFromStorage(requestCode);
    }

    private Person createPerson() {
        Person person = new Person();
        person.setName(nameTxt.getText().toString().trim());
        person.setLastName(surnameTxt.getText().toString().trim());
        person.setEmail(emailTxt.getText().toString().trim());
        person.setPhoneNumber(phoneNumber);

        if (phoneNumber != null) {
            person.setId(phoneNumber);
        }

        if (imageUri != null) {
            person.setPhotoUri(imageUri.toString());
        }

        return person;

    }

    private boolean isFieldsValid() {
        if (nameTxt.getText().toString().trim().isEmpty()) {
            nameTxt.setError(getString(R.string.cannot_be_empty));
            return false;
        }

        if (surnameTxt.getText().toString().trim().isEmpty()) {
            surnameTxt.setError(getString(R.string.cannot_be_empty));
            return false;
        }

        if (emailTxt.getText().toString().trim().isEmpty()) {
            emailTxt.setError(getString(R.string.cannot_be_empty));
            return false;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == this.requestCode && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                userImage.setImageBitmap(imageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
