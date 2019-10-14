package com.return0.Kucsa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.return0.Kucsa.R;
import com.return0.Kucsa.SmsService;

public class MainActivity extends AppCompatActivity {

    TextView inbox,outbox,sentbox,events;
    Intent service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        service=new Intent(MainActivity.this, SmsService.class);
        MainActivity.this.startService(service);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigationView.findViewById(R.id.nav_home).setBackgroundColor(getResources().getColor(R.color.colorblue));
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorwhite));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       return false;
            }
        });

        if (toolbar.getOverflowIcon()!=null) {
            toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        }

        if (getSupportActionBar()!= null){
        getSupportActionBar().setTitle("");
        }
        inbox=findViewById(R.id.inbox);
        outbox=findViewById(R.id.outbox);
        sentbox=findViewById(R.id.sentbox);
        
        requestSMSPermission();

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoView("inbox");
            }
        });

        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoView("outbox");
            }
        });

        sentbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoView("sentbox");
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomnav);
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.colorwhite));
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.colorwhite));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==R.id.contacts){
                  gotoContacts();
                }else
                    if (id==R.id.new_message){
                        startActivity(new Intent(MainActivity.this, NewMessageActivity.class));
                    }

                return true;
            }
        });
    }

    private void requestSMSPermission() {
        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
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
                                   // finishAffinity();
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

    private void gotoView(String para) {
        Intent intent=new Intent(MainActivity.this, MessagesViewActivity.class);
        intent.putExtra("pool",para);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.bottom_nav_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.contacts){
            gotoContacts();
        }


        return super.onOptionsItemSelected(item);
    }

    public void gotoContacts(){
        startActivity(new Intent(MainActivity.this, Contacts.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_voting) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
