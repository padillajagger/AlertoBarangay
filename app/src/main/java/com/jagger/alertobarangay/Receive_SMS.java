package com.jagger.alertobarangay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Receive_SMS extends BroadcastReceiver {
    private static EditText editText;

    public void setEditText(EditText editText)
    {
        Receive_SMS.editText=editText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage: smsMessages) {
            String message_body =smsMessage.getMessageBody();
            String code = message_body.substring(0,6);
            editText.setText(code);

        }
    }
}
