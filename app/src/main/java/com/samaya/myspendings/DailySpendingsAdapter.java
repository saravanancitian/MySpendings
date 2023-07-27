package com.samaya.myspendings;

import static com.samaya.myspendings.Utils.sdf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;

public class DailySpendingsAdapter extends RecyclerView.Adapter<DailySpendingsAdapter.ViewHolder> {

    public static interface OnItemClickListener{

        public void onItemClick(View view, int position, Spendings spending);
    }

    OnItemClickListener listener;
    private List<Spendings> spendingsList;

    public void setSpendingsList(List<Spendings> spendingsList) {
        this.spendingsList = spendingsList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    private final LayoutInflater mInflater;

    public DailySpendingsAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.dailylistitem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(spendingsList != null){
            Spendings spending = spendingsList.get(position);
            holder.itmTxtAmt.setText(String.valueOf(spending.amount));
            holder.itmTxtDate.setText(sdf.format(spending.whendt));
            holder.itmTxtPaidto.setText(spending.paidto);
            holder.itemView.setOnClickListener(view -> DailySpendingsAdapter.this.listener.onItemClick(view, position, spending));
        }
    }
    public void removeItem(int position){
        spendingsList.remove(position);
        notifyDataSetChanged();
    }
    public Spendings getItem(int position){
        return spendingsList.get(position);
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
        private  final TextView itmTxtPaidto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itmTxtAmt = itemView.findViewById(R.id.itm_txt_amt);
            itmTxtPaidto = itemView.findViewById(R.id.itm_txt_paidto);
            itmTxtDate = itemView.findViewById(R.id.itm_txt_date);
        }
    }
}
