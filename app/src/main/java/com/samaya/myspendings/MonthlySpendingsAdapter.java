package com.samaya.myspendings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samaya.myspendings.db.entity.MonthlySpending;

import java.util.List;

public class MonthlySpendingsAdapter extends RecyclerView.Adapter<MonthlySpendingsAdapter.ViewHolder> {

    private List<MonthlySpending> spendingsList;

    public void setMonthlySpendingsList(List<MonthlySpending> spendingsList) {
        this.spendingsList = spendingsList;
        notifyDataSetChanged();
    }

    private final LayoutInflater mInflater;

    public MonthlySpendingsAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.monthlylistitem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(spendingsList != null){
            MonthlySpending spending = spendingsList.get(position);
            holder.itmTxtAmt.setText(String.valueOf(spending.amount));
            holder.itmTxtDate.setText(spending.month);
        }
    }
    @Override
    public int getItemCount() {
        if(spendingsList != null){
            return spendingsList.size();
        }
        return 0;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private final TextView itmTxtAmt;
        private final TextView itmTxtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itmTxtAmt = itemView.findViewById(R.id.itm_txt_month_amt);
            itmTxtDate = itemView.findViewById(R.id.itm_txt_month);
        }
    }
}
