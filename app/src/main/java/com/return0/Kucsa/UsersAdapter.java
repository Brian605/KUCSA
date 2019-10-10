package com.return0.Kucsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {
private Context context;
private ArrayList<User> users;
private ArrayList<User> userArrayList;
private DatabaseManager manager;

public UsersAdapter(Context context,ArrayList<User> users){
    this.context=context;
    this.users=users;
    this.userArrayList=users;
    manager=new DatabaseManager(context);
}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    final User user=users.get(position);
    holder.idText.setText(String.valueOf(user.getId()));
    holder.phoneText.setText(user.getPhoneNumber());
    holder.regText.setText(user.getRegistrationNumber());
    holder.courseText.setText(user.getCourse());
    holder.yearText.setText(user.getYearOfStudy());
    holder.regDateText.setText(user.getRegDate());
    holder.userText.setText(user.getUserName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
