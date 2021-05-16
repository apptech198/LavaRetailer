package com.apptech.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivitySocialBinding;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialActivity extends AppCompatActivity {

    ActivitySocialBinding binding;
    private static final String TAG = "SocialActivity";
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    private static GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(this);



        /* get User number auto fetch */
        try {
            requestHint();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            binding.EditEmail.setEnabled(true);
        }

        findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {});
                    signIn();
                    break;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.SendotpBtn.setOnClickListener(v -> {
            if(new NetworkCheck().haveNetworkConnection(this)){
                if(mobileValidation(binding.EditEmail.getText().toString().trim())){
                    binding.progressbar.setVisibility(View.VISIBLE);
                    SendOTP();
                }
                return;
            }
            Toast.makeText(SocialActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        });

        binding.forgotBtn.setOnClickListener(v -> startActivity(new Intent(SocialActivity.this , ForgotActivity.class)));



        binding.EditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.NumberInputLayoutError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void SendOTP() {

        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.SEND_OTP(binding.EditEmail.getText().toString().trim()).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {
                        String otp = jsonObject.getString("otp");
                        startActivity(new Intent(SocialActivity.this , OtpActivity.class).putExtra("otp" , otp).putExtra("mobile" , binding.EditEmail.getText().toString().trim()));
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(SocialActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressbar.setVisibility(View.GONE);
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SocialActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(SocialActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }


    private boolean mobileValidation(String s){
        if(s.isEmpty()){
            binding.EditEmail.setError(getResources().getString(R.string.field_required));
            binding.NumberInputLayoutError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.EditEmail.setError(null);
        binding.NumberInputLayoutError.setVisibility(View.GONE);
        return true;
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: " + requestCode );

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }



        switch (requestCode) {
            case CREDENTIAL_PICKER_REQUEST:
                // Obtain the phone number from the result
                if (resultCode == RESULT_OK) {
                    if (resultCode == RESULT_OK) {
                        Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                        String number = credential.getId();  /*<-- will need to process phone number string -->*/
                        if (number.startsWith("+")) {
                            if (number.startsWith("+")) {
                                if (number.length() == 13) {
                                    String str_getMOBILE = number.substring(3);
                                    binding.EditEmail.setText(str_getMOBILE);
                                    binding.EditEmail.setEnabled(false);
                                } else if (number.length() == 14) {
                                    String str_getMOBILE = number.substring(4);
                                    binding.EditEmail.setText(str_getMOBILE);
                                    binding.EditEmail.setEnabled(false);
                                }
                            } else {
                                binding.EditEmail.setText(number);
                                binding.EditEmail.setEnabled(false);
                            }
                            SendOTP();


                        }
                        break;
                        // ...
                    }
                }
        }


/*        if(requestCode == CREDENTIAL_PICKER_REQUEST) {
            Log.e(TAG, "onActivityResult: " + requestCode);
            Log.e(TAG, "onActivityResult: " + RESULT_OK);
            if (requestCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                String number = credential.getId();
                Log.e(TAG, "onActivityResult: " + number);
                if (number.startsWith("+")) {
                    if (number.length() == 13) {
                        String str_getMOBILE = number.substring(3);
                        binding.EditEmail.setText(str_getMOBILE);
                        binding.EditEmail.setEnabled(false);
                    } else if (number.length() == 14) {
                        String str_getMOBILE = number.substring(4);
                        binding.EditEmail.setText(str_getMOBILE);
                        binding.EditEmail.setEnabled(false);
                    }
                } else {
                    binding.EditEmail.setText(number);
                    binding.EditEmail.setEnabled(false);
                }
                SendOTP();

            }

        }*/

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            emailVerify(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void emailVerify(String email){
        startActivity(new Intent(SocialActivity.this , SignUpActivity.class).putExtra("TYPE" , "GOOGLE"));
    }





    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0);
    }



}






















































