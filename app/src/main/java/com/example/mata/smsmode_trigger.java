package com.example.mata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

public class smsmode_trigger extends BroadcastReceiver {
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;
    MediaPlayer player;
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show();


        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            bundle = intent.getExtras();
            if (bundle != null) {

                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {

                    for (Object aObject : pdu_Objects) {

                        currentSMS = getIncomingMessage(aObject, bundle);

                        String senderNo = currentSMS.getDisplayOriginatingAddress();

                      String  message = currentSMS.getDisplayMessageBody();

                        Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_LONG).show();
                        if (message=="ring"){
                            //hello
                            player= MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
                            player.setLooping(true);
                            player.start();
                        }

                        intent.putExtra("sender_no",senderNo);
                        intent.putExtra("message",message);
                        Intent service = new Intent(context, smsmode.class);
                        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //for latest version of android
                            context.startForegroundService(service);
                        }
                        else {
//                for older version of android (before O)
                            context.startService(service);
                            //}
                        }

                        String gps_cmp="ring";
                        if (message==(gps_cmp)){


                            // function to send gps location;
                        }

                    }
                    this.abortBroadcast();
                    // End of loop
                }
            }
        } // bundle null
    }


    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }}