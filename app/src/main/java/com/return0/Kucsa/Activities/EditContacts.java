package com.return0.Kucsa.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.return0.Kucsa.DatabaseManager;
import com.return0.Kucsa.R;

public class EditContacts extends AppCompatActivity {
    private TextInputEditText phoneText,regText,userText,courseText,yearText,regDateText;
   private String ids;
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

        Intent intent=getIntent();
        ids=intent.getStringExtra("id");
        String phone = intent.getStringExtra("phone");
        String reg = intent.getStringExtra("reg");
        String user = intent.getStringExtra("user");
        String course = intent.getStringExtra("course");
        String year = intent.getStringExtra("year");
        final String regDate = intent.getStringExtra("regdate");

        phoneText.setText(phone);
        regText.setText(reg);
        userText.setText(user);
        courseText.setText(course);
        yearText.setText(year);
        regDateText.setText(regDate);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userid=Integer.parseInt(ids);
                DatabaseManager db=new DatabaseManager(EditContacts.this);
                db.open();
                String phone,reg,user,course,year,date;
                phone=phoneText.getText().toString();
                reg=regText.getText().toString();
                user=userText.getText().toString();
                course=courseText.getText().toString();
                year=yearText.getText().toString();
                date=regDateText.getText().toString();

                db.update(userid,phone,reg,user,course,year,date);
                Toast.makeText(getApplicationContext(), "Contact Updated", Toast.LENGTH_LONG).show();
                db.close();
                startActivity(new Intent(EditContacts.this,Contacts.class));

            }
        });

    }
}
