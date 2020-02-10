package com.mudivizhi.alohomora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

//TODO Need to automatically verfiy the OTP if all 6 numbers are entered
public class OTPActivity extends AppCompatActivity {


    private EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private ImageView verify;
    private  String otp,verificationID,phoneNo;
    FirebaseAuth firebaseAuth;
    TextView resend;
    CountDownTimer cTimer = null;
    private boolean timerFinished = false;
    PhoneAuthProvider.ForceResendingToken token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        verify = findViewById(R.id.verifyIv);
        resend = findViewById(R.id.resendText);
        startTimer();

        Intent intent = getIntent();
        verificationID = intent.getStringExtra("verificationID");
        token = (PhoneAuthProvider.ForceResendingToken) intent.getExtras().get("token");
        phoneNo = intent.getStringExtra("phone");
        //Toast.makeText(this, verificationID, Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();

        otp1.addTextChangedListener(new GenericTextWatcher(otp2, otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(otp3, otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(otp4, otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(otp5, otp4));
        otp5.addTextChangedListener(new GenericTextWatcher(otp6, otp5));

        //Not Working need to be updated
        otp6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp6.getText().length() == 1){
                    verify.requestFocus();
                    validateAndVerifyOTP();
                }
                return false;
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndVerifyOTP();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerFinished){
                    resendVerificationCode(phoneNo,token);
                    startTimer();
                }
            }
        });

    }
    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend.setText("00 : " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timerFinished = true;
                resend.setText("Resend OTP");
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void validateAndVerifyOTP(){
        if(isValidOTP()){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,otp);
            verifyAuth(credential);

        }else{
            Toast.makeText(OTPActivity.this, "Enter Valid OTP!", Toast.LENGTH_SHORT).show();
        }
    }
    private void validateAndVerifyOTP(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        verifyAuth(credential);
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(OTPActivity.this,MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.putExtra("signInMethod", "phone");
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(OTPActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidOTP() {
        if(isEmpty(otp1) || isEmpty(otp2 ) ||isEmpty(otp3) ||isEmpty(otp4) ||isEmpty(otp5) ||isEmpty(otp6)){
            return false;
        }
        otp = getStringFromET(otp1)+getStringFromET(otp2)+getStringFromET(otp3)+getStringFromET(otp4)+getStringFromET(otp5)+getStringFromET(otp6);
        return true;
    }
    private boolean isEmpty(EditText ed){
        if(ed.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
    private String getStringFromET(EditText ed){
        return  ed.getText().toString();
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Toast.makeText(OTPActivity.this, "OTP Sent!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        validateAndVerifyOTP(code);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                },         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    public class GenericTextWatcher implements TextWatcher {
        private EditText etPrev;
        private EditText etNext;

        public GenericTextWatcher(EditText etNext, EditText etPrev) {
            this.etPrev = etPrev;
            this.etNext = etNext;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (text.length() == 1)
                etNext.requestFocus();
            else if (text.length() == 0)
                etPrev.requestFocus();
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}

