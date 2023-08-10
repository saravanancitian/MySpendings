package com.samaya.myspendings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samaya.myspendings.db.entity.DMYSpending;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DMYSpendingsAdapter extends RecyclerView.Adapter<DMYSpendingsAdapter.ViewHolder> {

    final static int TYPE_YEARLY = 1;
    final static int TYPE_MONTHLY = 2;
    final static int TYPE_DAILY = 3;
    int type;

    private List<DMYSpending> spendingsList;

    public void setMonthlySpendingsList(List<DMYSpending> spendingsList) {
        this.spendingsList = spendingsList;
        notifyDataSetChanged();
    }

    private final LayoutInflater mInflater;

    public DMYSpendingsAdapter(int type, LayoutInflater mInflater) {
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
            DMYSpending spending = spendingsList.get(position);
            holder.itmTxtAmt.setText(String.valueOf(spending.amount));
            if(type == TYPE_MONTHLY){
                String monthvalstr[] = spending.dmyDate.split("-");
                int monthval = Integer.parseInt(monthvalstr[0]);
                int yearval = Integer.parseInt(monthvalstr[1]);
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.MONTH, monthval -1);
//                cal.set(Calendar.YEAR, yearval);
//                String month = new SimpleDateFormat("MMM").format(cal.getTime());
                String month = Utils.getMonth(monthval - 1);
                holder.itmTxtDate.setText(month+ " "+ monthvalstr[1]);
            } else if(type == TYPE_YEARLY) {
                holder.itmTxtDate.setText(spending.dmyDate);
            } else if(type == TYPE_DAILY){
                String monthvalstr[] = spending.dmyDate.split("-");
                int dayval = Integer.parseInt(monthvalstr[0]);
                int monthval = Integer.parseInt(monthvalstr[1]);
                int yearval = Integer.parseInt(monthvalstr[2]);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, monthval -1);
                cal.set(Calendar.YEAR, yearval);
                cal.set(Calendar.DAY_OF_MONTH, dayval);
                holder.itmTxtDate.setText(Utils.sdf.format(cal.getTime()));
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
