package com.return0.Kucsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView inbox,outbox,sentbox,events;
    Intent service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        service=new Intent(MainActivity.this,SmsService.class);
        MainActivity.this.startService(service);

        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        if (getSupportActionBar()!= null){
        getSupportActionBar().setTitle("");
        }
        inbox=findViewById(R.id.inbox);
        outbox=findViewById(R.id.outbox);
        sentbox=findViewById(R.id.sentbox);

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
                }

                return true;
            }
        });
    }

    private void gotoView(String para) {
        Intent intent=new Intent(MainActivity.this,MessagesViewActivity.class);
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
        startActivity(new Intent(MainActivity.this,Contacts.class));
    }
}
