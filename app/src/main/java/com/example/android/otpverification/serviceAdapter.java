package com.example.android.otpverification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class serviceAdapter extends FirebaseRecyclerAdapter<Service, serviceAdapter.serviceViewHolder> {

    public serviceAdapter(@NonNull FirebaseRecyclerOptions<Service> options) {
        super(options);
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
    }

    public class serviceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName;
        TextView serviceDescription;
        TextView priceRange;

        public serviceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.service_name);
            serviceDescription = itemView.findViewById(R.id.service_description);
            priceRange = itemView.findViewById(R.id.price_range);
        }
    }
}

