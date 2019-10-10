package com.return0.Kucsa;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView idText,phoneText,regText,userText,courseText,yearText,regDateText;
    public MyViewHolder(@NonNull View itemView) {
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
