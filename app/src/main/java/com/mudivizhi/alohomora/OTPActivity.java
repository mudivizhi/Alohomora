package com.mudivizhi.alohomora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity extends AppCompatActivity {


    private EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private ImageView verify;
    private  String otp,verificationID;
    FirebaseAuth firebaseAuth;
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

        Intent intent = getIntent();
        verificationID = intent.getStringExtra("verificationID");
        //Toast.makeText(this, verificationID, Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();


        otp1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp1.getText().length() == 1)
                    otp2.requestFocus();
                return false;
            }
        });
        otp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp2.getText().length() == 1)
                    otp3.requestFocus();
                return false;
            }
        });
        otp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp3.getText().length() == 1)
                    otp4.requestFocus();
                return false;
            }
        });
        otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp4.getText().length() == 1)
                    otp5.requestFocus();
                return false;
            }
        });
        otp5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp5.getText().length() == 1)
                    otp6.requestFocus();
                return false;
            }
        });
        otp6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(otp6.getText().length() == 1)
                    verify.requestFocus();
                return false;
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidOTP()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,otp);
                    verifyAuth(credential);

                }else{
                    Toast.makeText(OTPActivity.this, "Enter Valid OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void verifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(OTPActivity.this,MainActivity.class);
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
}
