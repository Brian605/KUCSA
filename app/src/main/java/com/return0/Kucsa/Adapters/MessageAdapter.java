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
import com.return0.Kucsa.Models.Messages;
import com.return0.Kucsa.R;
import com.return0.Kucsa.RecyclerOnclickListener;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements Filterable {
private Context context;
private ArrayList<Messages> messages;
private ArrayList<Messages> messagesArrayList;
private DatabaseManager manager;
private RecyclerOnclickListener listener;

public MessageAdapter(Context context, ArrayList<Messages> messages,RecyclerOnclickListener listener){
    this.context=context;
    this.messages=messages;
    this.messagesArrayList=messages;
    manager=new DatabaseManager(context);
    this.listener=listener;
}

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recycler,parent,false);
        final MessageViewHolder viewHolderV=new MessageViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               listener.onclick(view,viewHolderV.getAdapterPosition());
               return true;
            }
        });

        return viewHolderV;
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


    class MessageViewHolder extends RecyclerView.ViewHolder {

         TextView idText,messageText,phoneText,statusText;
         MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            idText=itemView.findViewById(R.id.ID);
            phoneText=itemView.findViewById(R.id.phoneNumber);
            messageText=itemView.findViewById(R.id.message);

        }
    }

}
