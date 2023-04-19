package com.sepa.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.sepa.test.R;
import com.sepa.test.broadcast.SmsReceiver;

public class OtpActivity extends AppCompatActivity {
    private static final String TAG = OtpActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_SMS = 123;

    private PinView otpPinView;


    private final BroadcastReceiver otpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SmsReceiver.SMS_INTENT_ACTION)) {
                String otpCode = intent.getStringExtra("otp_code");
                if (otpCode != null && !otpCode.isEmpty()) {
                    otpPinView.setText(otpCode);
                } else {
                    otpPinView.setText("");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpPinView = findViewById(R.id.otp_view);
        TextView phoneNumberTextView = findViewById(R.id.phone_number);

        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            phoneNumberTextView.setText(phoneNumber);
        }

        requestSmsPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(otpReceiver,
                new IntentFilter(SmsReceiver.SMS_INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(otpReceiver);
    }


    private void requestSmsPermission() {
        // Check if we have the necessary permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
            // We don't have the permissions, so request them
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    PERMISSION_REQUEST_SMS);
        }
    }

    // Override this method in your activity to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start listening for SMS messages
                LocalBroadcastManager.getInstance(this).registerReceiver(otpReceiver,
                        new IntentFilter(SmsReceiver.SMS_INTENT_ACTION));

            }
        }
    }
}