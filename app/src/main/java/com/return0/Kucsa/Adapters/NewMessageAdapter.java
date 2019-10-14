package com.return0.Kucsa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.return0.Kucsa.Models.NewMessage;
import com.return0.Kucsa.R;

import java.util.List;

public class NewMessageAdapter extends RecyclerView.Adapter<NewMessageAdapter.MessageHolder> {
Context context;
List<NewMessage> newMessageList;

public NewMessageAdapter(Context context,List<NewMessage> newMessageList){
    this.context=context;
    this.newMessageList=newMessageList;
}

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.bulk_recycler,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageHolder holder, final int position) {
    NewMessage message=newMessageList.get(position);
    holder.username.setText(message.getName());
    holder.year.setText(message.getYear());
    holder.phone.setText(message.getNumber());

    holder.checkBox.setChecked(message.getIselected());
    holder.checkBox.setTag(newMessageList.get(position));
    holder.checkBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NewMessage newMessage=(NewMessage)holder.checkBox.getTag();
            newMessage.setIselected(holder.checkBox.isChecked());
            newMessageList.get(position).setIselected(holder.checkBox.isChecked());
        }
    });

    }

    @Override
    public int getItemCount() {
        return newMessageList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
    TextView username,phone,year;
    AppCompatCheckBox checkBox;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            phone=itemView.findViewById(R.id.phone);
            year=itemView.findViewById(R.id.sem);
            checkBox=itemView.findViewById(R.id.tosend);

        }
    }
    public List<NewMessage> getNewMessageList(){
    return newMessageList;
    }
}
