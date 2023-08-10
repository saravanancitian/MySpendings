package com.samaya.myspendings;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class ReportRVListAdapter extends RecyclerView.Adapter<ReportRVListAdapter.ViewHolder>{

    private static final String TAG = "ReportRVListAdapter";

    public void setData(String[] data) {
        this.data = data;
    }

    private String data[];
    private final LayoutInflater mInflater;

    public ReportRVListAdapter(String[] data, LayoutInflater mInflater){
        this.data = data;
        this.mInflater = mInflater;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.datelistitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str = data[position];
        String m[] = str.split("-");
        holder.tv_month.setText(m[0]);
        holder.tv_year.setText(m[1]);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public final MaterialTextView tv_month, tv_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            tv_month = (MaterialTextView) itemView.findViewById(R.id.tv_month);
            tv_year = (MaterialTextView) itemView.findViewById(R.id.tv_year);

        }
    }
}
