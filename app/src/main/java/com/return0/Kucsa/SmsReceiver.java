package com.return0.Kucsa;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {
    private DatabaseManager manager;
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras=intent.getExtras();
            if (extras!=null){
                Object[] pdus=(Object[])extras.get("pdus") ;
                if (pdus !=null){
                    for (Object pdu:pdus){
                        SmsMessage smsMessage=getSmsMessage(pdu,extras);
                        manager=new DatabaseManager(context);

                        String message = smsMessage.getMessageBody();
                        if (message.startsWith("KUCSA")){
                            String phoneNumber = smsMessage.getDisplayOriginatingAddress().trim().replace("+254","0");
                            String[] regMessage= message.split("\\.");
                            String registrationNumber = regMessage[1];
                            String username = regMessage[2];
                            String course = regMessage[3];
                            String yearOfStudy = regMessage[4]+"."+regMessage[5];

                            Log.e("data","phonenumber"+phoneNumber+" regNo"+registrationNumber+" Username"+username+" course" +
                                    course+" Yearof "+yearOfStudy);

                             manager.open();

                            if (isValidReg(registrationNumber) && isValidUser(username) && isValidCourse(course) && isValidYear(yearOfStudy)){
                                SimpleDateFormat format=new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
                                Date date=new Date();
                                String formattedDate=format.format(date);
                                manager.insert(phoneNumber, registrationNumber, username, course, yearOfStudy,formattedDate);
                                manager.insertMessage(phoneNumber+"."+message,"complete","inbox");
                            }else{
                                manager.insertMessage(phoneNumber+"."+message,"pending","inbox");
                            }


                        }
                    }

                }

            }

        }

        private SmsMessage getSmsMessage(Object object,Bundle bundle){
            SmsMessage message;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                String format=bundle.getString("format") ;
                message=SmsMessage.createFromPdu((byte[])object,format);
            }else {
                message=SmsMessage.createFromPdu((byte[])object);
                Log.e("Message",message.getDisplayMessageBody());
            }
            return message;
        }

        private boolean isValidYear(String yearOfStudy) {
            String[] years=new String[]{"1.1","1.2","2.1","2.2","3.1","3.2","4.1","4.2"};
            boolean status=false;
            for(String year:years){
                if (year.equals(yearOfStudy.trim())){
                    status=true;
                }
            }
return status;

        }

        private boolean isValidCourse(String course) {
            return  course!=null && course.length()>=4;
        }

        private boolean isValidUser(String username) {
            return username !=null && username.length()>=4;

        }

        private boolean isValidReg(String registrationNumber) {
            return registrationNumber.contains("/") && registrationNumber.length()<14 && registrationNumber.length()>12;
        }

    }

