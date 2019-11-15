package com.lexnarro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.TransactionRecord;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionRecord> recordList;
    private OnItemClickListener onItemClickListener;

    public TransactionAdapter(List<TransactionRecord> recordList) {
        this.recordList = recordList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_transaction, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.date.setText(recordList.get(position).getPayment_Date());
        holder.amount.setText(recordList.get(position).getAmount());
        holder.plan.setText(recordList.get(position).getPlanName());
        holder.invoice.setText(recordList.get(position).getInvoice_No());
        holder.end_date.setText(recordList.get(position).getEnd_Date());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(recordList.get(position),"Details");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {

        void onItemClick(TransactionRecord record, String type);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, amount, plan, invoice, end_date;
        LinearLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_payment_date);
            amount = itemView.findViewById(R.id.txt_amount);
            plan = itemView.findViewById(R.id.txt_plan);
            invoice = itemView.findViewById(R.id.txt_invoice);
            end_date = itemView.findViewById(R.id.txt_end_date);
            root = itemView.findViewById(R.id.transaction_root);
        }
    }
}
