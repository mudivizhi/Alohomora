package com.mudivizhi.alohomora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class offer_detail extends AppCompatActivity {

    TextView coupon_text;

    TextView copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        copy = (TextView) findViewById(R.id.coupon_code);
        copy.setOnClickListener((view) -> {
            copycode();
        });
    }
//
    private void copycode(){
            ClipboardManager manager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            TextView coupon=(TextView)findViewById(R.id.coupon_code);
            String coupon_text=coupon.getText().toString();
            ClipData clipData=ClipData.newPlainText("text",coupon_text);
            manager.setPrimaryClip(clipData);
    }
}
