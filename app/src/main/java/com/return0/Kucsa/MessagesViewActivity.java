package com.return0.Kucsa;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesViewActivity extends AppCompatActivity {


    private DatabaseManager Dmanager;
    private ArrayList<Messages> messages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_view_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String pool = intent.getStringExtra("pool");
        TextView view = findViewById(R.id.toolbartext);
        view.setText(pool);

        messages=new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.recycler);
        LinearLayoutManager manager=new LinearLayoutManager(MessagesViewActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        Dmanager=new DatabaseManager(MessagesViewActivity.this);
        Dmanager.open();

        messages=Dmanager.fetchMessages(pool);
        if (messages.size()>0){
        recyclerView.setVisibility(View.VISIBLE);
        adapter=new MessageAdapter(MessagesViewActivity.this,messages);
        recyclerView.setAdapter(adapter);

        }else{
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.nomessages).setVisibility(View.VISIBLE);
        }


    }
}