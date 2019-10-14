package com.return0.Kucsa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.return0.Kucsa.DatabaseManager;
import com.return0.Kucsa.R;

public class NewContactActivity extends AppCompatActivity {
 private TextInputEditText phoneText,regText,userText,courseText,yearText,regDateText;
 private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneText=findViewById(R.id.phoneno);
        regText=findViewById(R.id.reg);
        userText=findViewById(R.id.user);
        courseText=findViewById(R.id.course);
        yearText=findViewById(R.id.yearof);
        regDateText=findViewById(R.id.datereg);
        floatingActionButton=findViewById(R.id.save);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone,reg,user,course,year,date;
                phone=phoneText.getText().toString();
                reg=regText.getText().toString();
                user=userText.getText().toString();
                course=courseText.getText().toString();
                year=yearText.getText().toString();
                date=regDateText.getText().toString();

                if(!TextUtils.isEmpty(phone) &&!TextUtils.isEmpty(reg) &&!TextUtils.isEmpty(user) &&
                        !TextUtils.isEmpty(course) &&!TextUtils.isEmpty(year) &&!TextUtils.isEmpty(date) ) {

                 DatabaseManager manager=new DatabaseManager(NewContactActivity.this);
                 manager.open();
                 manager.insert(phone,reg,user,course,year,date);

                 startActivity(new Intent(NewContactActivity.this, Contacts.class));

                }

            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }


    }
}

