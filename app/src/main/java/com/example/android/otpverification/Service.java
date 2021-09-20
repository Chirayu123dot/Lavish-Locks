package com.example.android.otpverification;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Service {
    // Each service will have a name, description, price range

    private final String Name;            // name of the service
    private final String Description;     // description of the service
    private final String PriceRange;       // price range of the service

    private Service(){
        Name = "me";
        Description = "me";
        PriceRange = "me";
    }

    public Service(String name, String description, String priceRange) {
        this.Name = name;
        this.Description = description;
        this.PriceRange = priceRange;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getPriceRange() {
        return PriceRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Name.equals(service.Name) && Description.equals(service.Description) && PriceRange.equals(service.PriceRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Description, PriceRange);
    }

    public static DiffUtil.ItemCallback<Service> itemCallback = new DiffUtil.ItemCallback<Service>() {
        @Override
        public boolean areItemsTheSame(@NonNull Service oldItem, @NonNull Service newItem) {
            return oldItem.Name.equals(newItem.Name);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Service oldItem, @NonNull Service newItem) {
            return oldItem.equals(newItem);
        }
    };
}
