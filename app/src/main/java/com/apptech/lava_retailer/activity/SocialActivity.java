package com.apptech.lava_retailer.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivitySocialBinding;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    List<Country_list> countryLists = new ArrayList<>();
    PopupMenu popupMenu;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        Context wrapper1 = new ContextThemeWrapper(this, R.style.YOURSTYLE);
        popupMenu = new PopupMenu(wrapper1, binding.SelectCountry);


        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        binding.loginButton.setReadPermissions(Arrays.asList(EMAIL));


        if (sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME") != null){
            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                case "en":
                case "fr":
                    binding.countryName.setText(sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME"));
                    break;
                case "ar":
                    binding.countryName.setText(sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME_AR"));
                    break;
            }
        }


        if (new NetworkCheck().haveNetworkConnection(this)){
            getCountry();
        }else {
            Toast.makeText(this, "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }



        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e(TAG, "onSuccess: " );
            }

            @Override
            public void onCancel() {
                // App code
                Log.e(TAG, "onCancel: " );
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG, "onError: ");
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile profile = Profile.getCurrentProfile();

                        Log.e(TAG, "onSuccess: " + accessToken.getUserId() );

                        startActivity(new Intent(SocialActivity.this , SignUpActivity.class).putExtra("TYPE" , "FACEBOOK").putExtra("UserId" , String.valueOf(accessToken.getUserId())));

                    }

                    @Override
                    public void onCancel() {
                        // App code

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e(TAG, "onError: " + exception.getMessage());
                    }
        });
        

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
                    binding.progressbar.setVisibility(View.VISIBLE);
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

        binding.SelectCountry.setOnClickListener(v -> {
            popupMenu.setOnMenuItemClickListener(item -> {

                int pos = item.getGroupId();

                sessionManage.LOGIN_COUNTRY(String.valueOf(item.getItemId()) , countryLists.get(pos).getName() , countryLists.get(pos).getCurrency()
                        , countryLists.get(pos).getCurrency_symbol() , countryLists.get(pos).getName_ar());

                switch (sessionManage.getUserDetails().get("LANGUAGE")){
                    case "en":
                    case "fr":
                        binding.countryName.setText(countryLists.get(pos).getName());
                        break;
                    case "ar":
                        String ar = countryLists.get(pos).getName_ar();
                        binding.countryName.setText(ar);
                        break;
                }

                return false;
            });
            popupMenu.show();

        });




    }


    private void getCountry(){


        lavaInterface.Country().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray array = jsonObject.getJSONArray("country_list");

                        for (int i=0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            countryLists.add(new Country_list(
                                    object.getString("id")
                                    ,object.getString("name")
                                    ,object.getString("name_ar")
                                    ,object.optString("name_fr")
                                    ,object.getString("time")
                                    ,object.getString("currency")
                                    ,object.optString("currency_symbol")
                            ));

                            int id = Integer.parseInt(object.getString("id"));

                            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                                case "en":
                                    popupMenu.getMenu().add(i, id, i ,object.getString("name"));
                                    break;
                                case "fr":
                                    if(countryLists.get(0).getName_fr().isEmpty()){
                                        popupMenu.getMenu().add(i, id, i ,object.getString("name"));
                                    }else {
                                        popupMenu.getMenu().add(i, id, i ,object.getString("name_fr"));
                                    }
                                    break;
                                case "ar":
                                    popupMenu.getMenu().add(i, id, i ,object.getString("name_ar"));
                                    break;
                            }


                        }


                        try {
                            if (sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME") == null){

                                switch (sessionManage.getUserDetails().get("LANGUAGE")){
                                    case "en":
                                    case "fr":
                                        binding.countryName.setText(countryLists.get(0).getName());
                                        break;
                                    case "ar":
                                        binding.countryName.setText(countryLists.get(0).getName_fr());
                                        break;
                                }


                            }
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                        progressDialog.cancel();
                        return;
                    }
                    Toast.makeText(SocialActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(SocialActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }


    private void SendOTP() {
        binding.SendotpBtn.setEnabled(false);
        binding.progressbar.setVisibility(View.VISIBLE);

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);
        String login_country_name = sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME");


        lavaInterface.SEND_OTP(binding.EditEmail.getText().toString().trim() ,login_country_name , country_id , country_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    binding.SendotpBtn.setEnabled(true);
                    if (error.equalsIgnoreCase("false")) {
                        String otp = jsonObject.getString("otp");
                        startActivity(new Intent(SocialActivity.this , OtpActivity.class).putExtra("otp" , otp).putExtra("mobile" , binding.EditEmail.getText().toString().trim()));
                        binding.SendotpBtn.setEnabled(true);
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(SocialActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.SendotpBtn.setEnabled(true);
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.SendotpBtn.setEnabled(true);
                    binding.progressbar.setVisibility(View.GONE);
                }
                binding.SendotpBtn.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SocialActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.SendotpBtn.setEnabled(true);
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

        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            emailVerify(account.getEmail());
            binding.progressbar.setVisibility(View.GONE);
        } catch (ApiException e) {
            Log.e(TAG, "handleSignInResult: " + e.getMessage() );
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            binding.progressbar.setVisibility(View.GONE);
            ErrorDilaog("Something went wrong Pleace try again");
        }
    }


    private void emailVerify(String email){
        GmailCheck(email);
    }

    private void ErrorDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
//        builder.setNegativeButton("" , (dialog, which) -> {
//            dialog.dismiss();
//        });
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0);
    }



    private void GmailCheck(String email){

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        lavaInterface.EMAIL_CHECK(email , country_id , country_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        binding.SendotpBtn.setEnabled(true);
                        if (error.equalsIgnoreCase("false")) {
                            int Msgerror = R.string.Already_exists;
                            AlertDialogfailure(Msgerror);
                            return;
                        }
                        startActivity(new Intent(SocialActivity.this , SignUpActivity.class).putExtra("TYPE" , "GOOGLE"));
//                        Toast.makeText(SocialActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SocialActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Toast.makeText(SocialActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }



    private void AlertDialogfailure(int msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_imei_not_exits , null );
        builder.setView(v);

        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView des = v.findViewById(R.id.des);
        MaterialTextView Title = v.findViewById(R.id.Title);
        Title.setText("Alert");
        des.setText(msg);


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

        } else {

        }


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }








}






















































