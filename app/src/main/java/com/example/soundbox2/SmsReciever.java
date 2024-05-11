package com.example.soundbox2;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SmsReciever extends BroadcastReceiver {

    private static SmsListener mListener;
    private String bankName;
    Boolean b=false;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (Object o : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
            String sender = smsMessage.getDisplayOriginatingAddress();

            String messageBody = smsMessage.getDisplayMessageBody();

            if (messageBody.contains("credited")||messageBody.contains("Credited")){
                if (rupees_in_word(messageBody)==0){
                    received_money(messageBody,sender,0);
                }else if (rupees_in_word(messageBody)==1) {
                    received_money(messageBody, sender, 1);
                }else {
                    received_money(messageBody,sender,-1);
                }

            }

        }
    }
    public int rupees_in_word(String message){
        if (message.contains("Rs.")) {
            return 0;
        } else if (message.contains("rs.")){
            return 1;
        } else {
            return -1;
        }
    }

    public void received_money(String messageBody , String sender , int rupee_sign){
        String message = messageBody.replaceAll(" ","");
        if (rupee_sign == 0) {
            Pattern pattern = Pattern.compile("Rs.(\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String match = matcher.group(1); // Extract the matched group
                double moneyInRupees = Double.parseDouble(match);
                mListener.messageReceived("received ₹" + Double.toString(moneyInRupees), sender);
            }
        }
        else if (rupee_sign == 1) {
            Pattern pattern = Pattern.compile("rs.(\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String match = matcher.group(1); // Extract the matched group
                double moneyInRupees = Double.parseDouble(match);
                mListener.messageReceived("received ₹" + Double.toString(moneyInRupees), sender);
            }
        }
        else {
            Pattern pattern = Pattern.compile("₹(\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String match = matcher.group(1); // Extract the matched group
                double moneyInRupees = Double.parseDouble(match);
                mListener.messageReceived("received ₹" + Double.toString(moneyInRupees), sender);
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}