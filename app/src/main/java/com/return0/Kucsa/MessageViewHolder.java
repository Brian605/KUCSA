package com.return0.Kucsa;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView idText,messageText,phoneText,statusText;
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        idText=itemView.findViewById(R.id.ID);
        phoneText=itemView.findViewById(R.id.phoneNumber);
        messageText=itemView.findViewById(R.id.message);

    }
}
