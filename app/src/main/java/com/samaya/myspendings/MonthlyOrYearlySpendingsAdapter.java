package com.samaya.myspendings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samaya.myspendings.db.entity.MonthlyOrYearlySpending;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthlyOrYearlySpendingsAdapter extends RecyclerView.Adapter<MonthlyOrYearlySpendingsAdapter.ViewHolder> {

    final static int TYPE_YEARLY = 1;
    final static int TYPE_MONTHLY = 2;
    int type;

    private List<MonthlyOrYearlySpending> spendingsList;

    public void setMonthlySpendingsList(List<MonthlyOrYearlySpending> spendingsList) {
        this.spendingsList = spendingsList;
        notifyDataSetChanged();
    }

    private final LayoutInflater mInflater;

    public MonthlyOrYearlySpendingsAdapter(int type,LayoutInflater mInflater) {
        this.mInflater = mInflater;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.monthlyoryearlylistitem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(spendingsList != null){
            MonthlyOrYearlySpending spending = spendingsList.get(position);
            holder.itmTxtAmt.setText(String.valueOf(spending.amount));
            if(type == TYPE_MONTHLY){
                String monthvalstr[] = spending.monthoryear.split("-");
                int monthval = Integer.parseInt(monthvalstr[0]);
                int yearval = Integer.parseInt(monthvalstr[1]);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, monthval -1);
                cal.set(Calendar.YEAR, yearval);
                String month = new SimpleDateFormat("MMM").format(cal.getTime());
                holder.itmTxtDate.setText(month+ " "+ monthvalstr[1]);
            } else{
                holder.itmTxtDate.setText(spending.monthoryear);
            }

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
            itmTxtDate = itemView.findViewById(R.id.itm_txt_month_year);
        }
    }
}
