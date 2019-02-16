package mmconsultoria.co.mz.mbelamova.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.fragment.LoginFragment;
import mmconsultoria.co.mz.mbelamova.fragment.SignUpFragment;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.model.SimpleCallback;

public class LoginActivity extends BaseActivity {

    private static final int PHONE_NUMBER_RC = 50;
    private LoginFragment loginFragment;
    private SimpleCallback<String> phoneNumberCallback;
    private GoogleApiClient googleApiClient;

    private SignUpFragment signUpFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = new LoginFragment();
        signUpFragment =new SignUpFragment();

        requestPhoneNumber();



        swapFragment(R.id.login_container,signUpFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void requestPhoneNumber(SimpleCallback<String> callback) {
        phoneNumberCallback = callback;
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), PHONE_NUMBER_RC, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Could not start hint picker Intent", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_NUMBER_RC) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (phoneNumberCallback != null){
                    phoneNumberCallback.onSuccess(cred.getId());
                    Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");                }
            }
            phoneNumberCallback = null;
        }
    }

    private void requestPhoneNumber(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        requestPhoneNumber(loginFragment);
    }
}