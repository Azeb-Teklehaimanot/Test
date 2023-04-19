package com.sepa.test.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_INTENT_ACTION = "NEW_OTP_CODE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                        String otpCode = extractOtpCode(message.getMessageBody());
                        if (otpCode != null && !otpCode.isEmpty()) {
                            Intent otpIntent = new Intent(SMS_INTENT_ACTION);
                            otpIntent.putExtra("otp_code", otpCode);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(otpIntent);
                        }
                    }
                }
            }
        }
    }

    private String extractOtpCode(String messageBody) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
