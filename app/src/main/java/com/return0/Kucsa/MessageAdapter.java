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

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> implements Filterable {
private Context context;
private ArrayList<Messages> messages;
private ArrayList<Messages> messagesArrayList;
private DatabaseManager manager;

public MessageAdapter(Context context, ArrayList<Messages> messages){
    this.context=context;
    this.messages=messages;
    this.messagesArrayList=messages;
    manager=new DatabaseManager(context);
}

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recycler,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

    final Messages messages1=messages.get(position);
    holder.idText.setText(String.valueOf(messages1.getId()));
    holder.phoneText.setText(messages1.getPhoneNumber());
    holder.messageText.setText(messages1.getMessage());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
