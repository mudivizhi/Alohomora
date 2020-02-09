package com.mudivizhi.alohomora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    LinearLayout signupLayout;
    ImageView mobileSubmit;
    EditText mobileNumberEt;
    private TextView state;
    private ProgressBar progressBar;
    private String countryCode,mobileNumber,fullNumber;
    private CountryCodePicker cpp;
    PhoneAuthProvider.ForceResendingToken token;
    private GoogleSignInClient googleSignInClient;

    String verificationID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupLayout = findViewById(R.id.signup_layout);
        mobileSubmit = findViewById(R.id.iv_mobileSubmit);
        //countryCodeSp = findViewById(R.id.sp_countryCode);
        mobileNumberEt = findViewById(R.id.et_mobileNumber);
        cpp = findViewById(R.id.ccp);
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);

        firebaseAuth = FirebaseAuth.getInstance();

        //countryCodeSp.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                //moveToNextActivity();
            }
        });

        mobileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countryCode = countryCodeSp.getText().toString();
                mobileNumber = mobileNumberEt.getText().toString();

                if(validateInput(mobileNumber)){
                    progressBar.setVisibility(View.VISIBLE);
                    state.setText("Sending OTP...");
                    state.setVisibility(View.VISIBLE);
                    countryCode = cpp.getSelectedCountryCode();
                    fullNumber = "+"+countryCode+mobileNumber;
                    sendOTP(fullNumber);


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {

                    }
                    break;

            }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("googleAccount", googleSignInAccount);
        intent.putExtra("signInMethod", "google");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendOTP(String fullNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(fullNumber, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = s;
                token = forceResendingToken;
                state.setText("OTP Successfully Sent!!!");
                moveToNextActivity();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Something went wrong!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void moveToNextActivity() {
        Intent intent = new Intent(this,OTPActivity.class);
        intent.putExtra("verificationID",verificationID);
        startActivity(intent);

    }

   private boolean validateInput(String mobileNumber){
        if(mobileNumber==null || mobileNumber.isEmpty()){
            mobileNumberEt.setError("Mobile Number Required");
            return false;
        }else if(mobileNumber.length()!=10){
            mobileNumberEt.setError("Enter a valid Mobile Number");
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("signInMethod", "phone");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            onLoggedIn(alreadyloggedAccount);
        }
    }
}
