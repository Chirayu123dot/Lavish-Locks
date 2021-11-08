package com.example.android.otpverification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CalendarAdapter extends ListAdapter<CalendarDate, CalendarAdapter.ViewHolder> {

    private Context context;
    private TextView month;
    private View previousDateCard = null;
    private TextView previousDateTextField = null;
    private dateOnClickInterface onClickInterface;

    protected CalendarAdapter(Context context, @NonNull DiffUtil.ItemCallback<CalendarDate> diffCallback, TextView month, dateOnClickInterface onClickInterface) {
        super(diffCallback);
        this.context = context;
        this.month = month;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_date_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarDate calendarDate = getItem(position);

        holder.dayField.setText(calendarDate.getDayName());
        holder.dateField.setText(calendarDate.getDate());

        holder.dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.setText(calendarDate.getMonthName());
                if(previousDateCard != null){
                    previousDateCard.setBackground(null);
                    previousDateTextField.setTextColor(context.getResources().getColor(R.color.edit_text_outline));
                }
                holder.dateCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.selected_date_background));
                holder.dateField.setTextColor(context.getResources().getColor(R.color.white));

                previousDateCard = holder.dateCard;
                previousDateTextField = holder.dateField;

                onClickInterface.setClick(position);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView dayField;
        TextView dateField;
        View dateCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayField = itemView.findViewById(R.id.calendar_date_card_day);
            dateField = itemView.findViewById(R.id.calendar_date_card_date);
            dateCard = itemView.findViewById(R.id.calendar_date_card_date_card);
        }
    }
}
