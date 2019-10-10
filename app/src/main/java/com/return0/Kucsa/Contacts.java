package com.return0.Kucsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private DatabaseManager Dmanager;
    private ArrayList<User> userList;
    private UsersAdapter adapter;
    String messaGES="KUCSA";
    private BroadcastReceiver sentStatus,deleveredStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        TextView noContacts=findViewById(R.id.nocontacts);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("");
        }

        RecyclerView recyclerView=findViewById(R.id.recycler);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        userList=new ArrayList<>();

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        Dmanager=new DatabaseManager(Contacts.this);

        Dmanager.open();
        userList=Dmanager.fetchUsers();

        Dmanager.close();
        if (userList.size()>0){
        recyclerView.setVisibility(View.VISIBLE);
        adapter=new UsersAdapter(Contacts.this,userList);
        recyclerView.setAdapter(adapter);

        }else{
         recyclerView.setVisibility(View.GONE);
         noContacts.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.addNew){
          startActivity(new Intent(Contacts.this,NewContactActivity.class))  ;

        }else if(
                id==R.id.bulksms
        ){
          sendBulkSms() ;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendBulkSms() {
      ArrayList<String> numbers=new ArrayList<>();
      Dmanager=new DatabaseManager(Contacts.this);
      Dmanager.open();
      numbers=Dmanager.fetchContacts();
      Dmanager.close();

      for(final String number:numbers){
   new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
           Dexter.withActivity(Contacts.this)
                   .withPermission(Manifest.permission.SEND_SMS)
                   .withListener(new PermissionListener() {
                       @Override
                       public void onPermissionGranted(PermissionGrantedResponse response) {
                           PendingIntent sentIntent=PendingIntent.getBroadcast(getApplicationContext(),0,new Intent("SMS_SENT"),0);
                           PendingIntent deliveredIntent=PendingIntent.getBroadcast(getApplicationContext(),0,new Intent("SMS_DELIVERED"),0);
                           SmsManager manager=SmsManager.getDefault();
                           manager.sendTextMessage(number,null,messaGES,sentIntent,deliveredIntent);

                       }

                       @Override
                       public void onPermissionDenied(PermissionDeniedResponse response) {
                           if (response.isPermanentlyDenied()) {
                               AlertDialog.Builder builder=new AlertDialog.Builder(Contacts.this);
                               builder.setTitle("Please Grant Permission");
                               builder.setMessage(getString(R.string.prompt));
                               builder.setPositiveButton("Take Me To Settings", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                       Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                       Uri uri=Uri.fromParts("package",getPackageName(),null);
                                       intent.setData(uri);
                                       startActivityForResult(intent,101);
                                   }
                               });
                               builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       System.exit(0);
                                   }
                               });
                               builder.show();

                           }

                       }

                       @Override
                       public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                           token.continuePermissionRequest();
                       }
                   }).withErrorListener(new PermissionRequestErrorListener() {
               @Override
               public void onError(DexterError error) {

               }
           }).check();


       }
   },5000) ;



      }

    }
    public void onResume(){
        super.onResume();
        sentStatus=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s="An Error Occured";
                switch (getResultCode()){
                    case Activity
                            .RESULT_OK  :s="Message Sent" ;
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s="Genereic failure";
                        break;
                    default:s="Sms Not Sent";
                        break;
                }
                if (s.equals("Message sent")){
               DatabaseManager manager=new DatabaseManager(Contacts.this);
               manager.insertMessage(messaGES,"sent","sentbox");

                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show(); ;

            }
        };

        deleveredStatus=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s="An Error Occured" ;
                switch (getResultCode()){
                    case Activity
                            .RESULT_OK  :s="Message Sent" ;
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s="Genereic failure";
                        break;
                    default:s="Sms Not Sent";
                        break;
                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(sentStatus,new IntentFilter("SMS_SENT"));
        registerReceiver(deleveredStatus,new IntentFilter("SMS_RECEIVED"));
    }

    public void onPause(){
        super.onPause();
        unregisterReceiver(sentStatus);
        unregisterReceiver(deleveredStatus);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Contacts.this,MainActivity.class));
    }
}
