package com.lexnarro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.Plans;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plans> plansList;
    private OnItemClickListener onItemClickListener;

    public PlanAdapter(List<Plans> plansList) {
        this.plansList = plansList;
    }

    @NonNull
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plans, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ViewHolder viewHolder, int i) {

        final Plans plans = plansList.get(i);
        viewHolder.plan.setText(plansList.get(i).getPlan());
        viewHolder.amount.setText(plansList.get(i).getAmount());
        viewHolder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(plans, "Clicked");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    public interface OnItemClickListener {

        void onItemClick(Plans plans, String type);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView plan, amount, subscribe;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            plan = itemView.findViewById(R.id.txt_plan);
            amount = itemView.findViewById(R.id.txt_amount);
            subscribe = itemView.findViewById(R.id.txt_subscribe);


        }
    }
}
