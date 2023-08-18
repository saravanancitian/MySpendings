package com.samaya.myspendings.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.samaya.myspendings.R;
import com.samaya.myspendings.utils.DateUtils;

import java.util.List;

public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.ViewHolder>{
    private static final String TAG = "DateListAdapter";
    public static interface OnItemClickListener{

        public void onItemClick(View view, int position, String datestr);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static final int DATE_LIST_ADAPTER_TYPE_MONTH_YEAR = 1;
    public static final int DATE_LIST_ADAPTER_TYPE_YEAR = 2;
    int type;

    OnItemClickListener listener;

    private List<String> dates;


    public void setDates(List<String> dates) {
        this.dates = dates;
    }


    private final LayoutInflater mInflater;

    public DateListAdapter(int type, LayoutInflater mInflater){
        this.mInflater = mInflater;
        this.type = type;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.datelistitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str = dates.get(position);
         holder.itemView.setOnClickListener(view-> DateListAdapter.this.listener.onItemClick(view, position, str));
        if(type == DATE_LIST_ADAPTER_TYPE_MONTH_YEAR){

            String[] m = str.split("-");
            holder.tv_month.setVisibility(View.VISIBLE);
            int month = Integer.parseInt(m[0]);
            holder.tv_month.setText(DateUtils.getShortMonths(month -1 ));
            holder.tv_year.setText(m[1]);

        } else if(type == DATE_LIST_ADAPTER_TYPE_YEAR){
            holder.tv_month.setVisibility(View.GONE);
            holder.tv_year.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public final MaterialTextView tv_month, tv_year;
        public View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_year = itemView.findViewById(R.id.tv_year);

        }
    }
}
