package com.sepa.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.sepa.test.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Developer : Azeb Teklehaimanot
public class MainActivity extends AppCompatActivity {

    TextInputEditText phoneTextInputEditText;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneTextInputEditText = findViewById(R.id.input_edt_text_phone);
        continueButton = findViewById(R.id.btn_continue);
        CountryCodePicker countryCodePicker = findViewById(R.id.cpp);
        String countryCode = countryCodePicker.getSelectedCountryCode();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get phone number input from user
                if (phoneTextInputEditText.getText() == null) return;
                String phoneNumber = phoneTextInputEditText.getText().toString();
                // Validate phone number
                if (isValidPhoneNumber(phoneNumber)) {
                    //Make an api call to server that is sending the otp
                    Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                    intent.putExtra("PHONE_NUMBER", countryCode + phoneNumber);
                    startActivity(intent);
                } else {
                    // Display error message to user
                    phoneTextInputEditText.setError(getString(R.string.invalid_phone));
                }
            }
        });
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression for phone numbers
        // Allows for + symbol at the beginning of the number, and spaces, dots, hyphens, or parentheses as separators
        String regex = "^\\+?[\\d\\s.-]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}