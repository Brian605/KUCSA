package com.return0.Kucsa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.return0.Kucsa.DatabaseManager;
import com.return0.Kucsa.R;
import com.return0.Kucsa.RecyclerOnclickListener;
import com.return0.Kucsa.Models.User;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> implements Filterable {
private Context context;
private ArrayList<User> users;
private ArrayList<User> userArrayList;
private DatabaseManager manager;
private RecyclerOnclickListener listener;

public ContactsAdapter(Context context, ArrayList<User> users, RecyclerOnclickListener listener){
    this.context=context;
    this.users=users;
    this.userArrayList=users;
    manager=new DatabaseManager(context);
   this. listener=listener;
}

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_recycler,parent,false);
        final ContactsViewHolder viewHolder=new ContactsViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onclick(view,viewHolder.getAdapterPosition());
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {

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

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView idText,phoneText,regText,userText,courseText,yearText,regDateText;
        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            idText=itemView.findViewById(R.id.ID);
            phoneText=itemView.findViewById(R.id.phone);
            regText=itemView.findViewById(R.id.regNo);
            userText=itemView.findViewById(R.id.userName);
            courseText=itemView.findViewById(R.id.course);
            yearText=itemView.findViewById(R.id.Year);
            regDateText=itemView.findViewById(R.id.regDate);

        }
    }
}
