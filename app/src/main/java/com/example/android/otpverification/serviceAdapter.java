package com.example.android.otpverification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class serviceAdapter extends FirebaseRecyclerAdapter<Service, serviceAdapter.serviceViewHolder> {

    private String userName;
    private String userPhoneNumber;
    Context context;

    public serviceAdapter(@NonNull FirebaseRecyclerOptions<Service> options, String userName, String userPhoneNumber, Context context) {
        super(options);
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.context = context;
    }



    @NonNull
    @Override
    public serviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card_layout, parent, false);
        return new serviceViewHolder(view);
    }

    @Override
    protected void onBindViewHolder (@NonNull serviceViewHolder holder,int position,
    @NonNull Service model){
        Service serviceItem = getItem(position);

        holder.serviceName.setText(serviceItem.getName());
        holder.serviceDescription.setText(serviceItem.getDescription());
        holder.priceRange.setText(serviceItem.getPriceRange());

        holder.serviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppointmentBooking.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userPhoneNumber", userPhoneNumber);
                intent.putExtra("serviceName", holder.serviceName.getText());
                context.startActivity(intent);
            }
        });
    }

    public class serviceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName;
        TextView serviceDescription;
        TextView priceRange;
        View serviceCard;

        public serviceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.service_card_layout_service_name);
            serviceDescription = itemView.findViewById(R.id.service_card_layout_service_description);
            priceRange = itemView.findViewById(R.id.service_card_layout_price_range);
            serviceCard = itemView.findViewById(R.id.service_card_layout_service_card);
        }
    }
}

