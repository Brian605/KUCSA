package com.return0.Kucsa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.return0.Kucsa.Adapters.NewMessageAdapter;
import com.return0.Kucsa.DatabaseManager;
import com.return0.Kucsa.Models.NewMessage;
import com.return0.Kucsa.R;

import java.util.ArrayList;
import java.util.List;

public class NewMessageActivity extends AppCompatActivity {
private DatabaseManager databaseManager;
List<String> phonesList;
AlertDialog.Builder builder;
Dialog dialog;
String messaGES;
    private DatabaseManager Dmanager;
    static ArrayList<String> numbers;
    static  BroadcastReceiver sentStatus, deleveredStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewMessageActivity.this));
        recyclerView.setHasFixedSize(true);
        FloatingActionButton fab=findViewById(R.id.fab);

       databaseManager=new DatabaseManager(NewMessageActivity.this);
       databaseManager.open();

        List<NewMessage> newMessages;
        newMessages=databaseManager.fetchNewMessage();
       final NewMessageAdapter adapter=new NewMessageAdapter(NewMessageActivity.this,newMessages);
       recyclerView.setAdapter(adapter);
       databaseManager.close();

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
         List<NewMessage> selected=adapter.getNewMessageList();
         phonesList=new ArrayList<>();
         for (NewMessage n:selected){
             if (n.getIselected()){
                 Log.e("Selected Numbers",n.getNumber());
                 phonesList.add(n.getNumber());
             }
         }

         if (phonesList.size()>0){
             sendBulkSms();
         }

           }
       });

    }

    private void sendBulkSms() {
        LayoutInflater inflater=LayoutInflater.from(NewMessageActivity.this) ;
        View view=inflater.inflate(R.layout.newmessage_dialog,null,false);
        builder=new AlertDialog.Builder(NewMessageActivity.this)  ;
        builder.setTitle("Send SMS");
        builder.setView(view);
        dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);

        final TextInputEditText mess;
        Button send,cancel;
        mess= view.findViewById(R.id.message)  ;
        send=view.findViewById(R.id.send);
        cancel=view.findViewById(R.id.cancel);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=mess.getText().toString();
                if (!message.equals("")){
                    messaGES=message;
                    new SmsSender().execute();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                return;
            }
        });

        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void addToDB(String number, String messaGES, String out) {
        Dmanager = new DatabaseManager(NewMessageActivity.this);
        Dmanager.open();
        Dmanager.insertMessage(number+"."+messaGES, "sent", out);
        Dmanager.close();


    }

    @Override
    protected void onResume() {
        registerReceiver(sentStatus,new IntentFilter("SMS_SENT"));
        registerReceiver(deleveredStatus,new IntentFilter("SMS_DELIVERED"));
        super.onResume();
    }
/*
    @Override
    protected void onPause() {
       /* super.onPause();
        super.onPause();
       // if (!registerReceiver(sentStatus,new IntentFilter("SMS_DELIVERED")))
        try {
            unregisterReceiver(sentStatus);
            unregisterReceiver(deleveredStatus);
        }catch (IllegalArgumentException e){
            Log.e("Receiver error",e.getMessage().toString());
        }

    }*/


    private void addToDB(String number, String messaGES) {
        Dmanager = new DatabaseManager(NewMessageActivity.this);
        Dmanager.open();
        Dmanager.insertMessage(number+"."+messaGES, "sent", "sentbox");
        Dmanager.close();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewMessageActivity.this, MainActivity.class));
    }

    private class SmsSender extends AsyncTask<Context, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Looper.prepare();
        }

        @Override
        protected Void doInBackground(final Context... strings) {

            Dmanager = new DatabaseManager(NewMessageActivity.this);
            Dmanager.open();
            numbers = Dmanager.fetchContacts();
            Dmanager.close();
            String status;
            String sent = "SMS_SENT";
            String delivered = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(sent), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(delivered), 0);

            for ( final String number : numbers){
                sentStatus = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (getResultCode() == AppCompatActivity.RESULT_OK) {
                            //
                            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
                        } else {
                            addToDB(number, messaGES, "outbox");
                        }

                    }
                };
                registerReceiver(sentStatus, new IntentFilter(sent));

                deleveredStatus = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (getResultCode() == AppCompatActivity.RESULT_OK) {
                            Toast.makeText(getApplicationContext(), "Message delivered", Toast.LENGTH_LONG).show();
                            addToDB(number, messaGES);
                        } else {
                            addToDB(number, messaGES, "outbox");
                        }

                    }
                };
                registerReceiver(deleveredStatus, new IntentFilter(delivered));

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, messaGES, sentPI, deliveredPI);
                // Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();

                for (int y = 1; y < 1000000; y++) {
                    //
                }
            }
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            unregisterReceiver(sentStatus);
            unregisterReceiver(deleveredStatus);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }

    }
}
