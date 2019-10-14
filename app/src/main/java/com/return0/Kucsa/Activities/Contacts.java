package com.return0.Kucsa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.return0.Kucsa.Adapters.ContactsAdapter;
import com.return0.Kucsa.DatabaseManager;
import com.return0.Kucsa.Models.User;
import com.return0.Kucsa.R;
import com.return0.Kucsa.RecyclerOnclickListener;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private DatabaseManager Dmanager;
    private ArrayList<User> userList;
    static ArrayList<String> numbers;
    private ContactsAdapter adapter;
    RecyclerOnclickListener listener;

    String messaGES ;
  static  BroadcastReceiver sentStatus, deleveredStatus;
  AlertDialog.Builder builder;
  Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        TextView noContacts = findViewById(R.id.nocontacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        userList = new ArrayList<>();

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        Dmanager = new DatabaseManager(Contacts.this);

        Dmanager.open();
        userList = Dmanager.fetchUsers();

        Dmanager.close();
        if (userList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ContactsAdapter(Contacts.this, userList, new RecyclerOnclickListener() {
                @Override
                public void onclick(View parent, int position) {
                showConfirmDialog(parent,position);

                }
            });
            recyclerView.setAdapter(adapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            noContacts.setVisibility(View.VISIBLE);

        }


    }

    private void showConfirmDialog(final View parent, final int position) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Contacts.this);
        builder.setMessage("Select an action");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             deleteContact(parent,position);
            }
        });

        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              editContacts(parent,position,builder);
            }
        });
        builder.create();
        builder.show();
    }

    private void editContacts(View itemView, int position,AlertDialog.Builder builder) {
        TextView idText,phoneText,regText,userText,courseText,yearText,regDateText;

        idText=itemView.findViewById(R.id.ID);
        phoneText=itemView.findViewById(R.id.phone);
        regText=itemView.findViewById(R.id.regNo);
        userText=itemView.findViewById(R.id.userName);
        courseText=itemView.findViewById(R.id.course);
        yearText=itemView.findViewById(R.id.Year);
        regDateText=itemView.findViewById(R.id.regDate);

        Intent intent=new Intent(Contacts.this, EditContacts.class);
        intent.putExtra("id",idText.getText().toString());
        intent.putExtra("phone",phoneText.getText().toString());
        intent.putExtra("reg",regText.getText().toString());
        intent.putExtra("user",userText.getText().toString());
        intent.putExtra("course",courseText.getText().toString());
        intent.putExtra("year",yearText.getText().toString());
        intent.putExtra("regdate",regDateText.getText().toString());


        startActivity(intent);





    }

    private void deleteContact(View parent, int position) {
        TextView id=parent.findViewById(R.id.ID);
        int UserId=Integer.parseInt(id.getText().toString());
        Dmanager=new DatabaseManager(Contacts.this);
        Dmanager.open();
        Dmanager.delete(UserId);
        Dmanager.close();
        Toast.makeText(getApplicationContext(), "Contact Deleted", Toast.LENGTH_LONG).show();
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addNew) {
            startActivity(new Intent(Contacts.this, NewContactActivity.class));

        } else if (id == R.id.bulksms) {
            sendBulkSms();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendBulkSms() {
       // Context context=Contacts.this;

        LayoutInflater inflater=LayoutInflater.from(Contacts.this) ;
        View view=inflater.inflate(R.layout.newmessage_dialog,null,false);
        builder=new AlertDialog.Builder(Contacts.this)  ;
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

    private void addToDB(String number, String messaGES, String out) {
        Dmanager = new DatabaseManager(Contacts.this);
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
        Dmanager = new DatabaseManager(Contacts.this);
        Dmanager.open();
        Dmanager.insertMessage(number+"."+messaGES, "sent", "sentbox");
        Dmanager.close();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Contacts.this, MainActivity.class));
    }

   private class SmsSender extends AsyncTask<Context, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Looper.prepare();
        }

        @Override
        protected Void doInBackground(final Context... strings) {

            Dmanager = new DatabaseManager(Contacts.this);
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
