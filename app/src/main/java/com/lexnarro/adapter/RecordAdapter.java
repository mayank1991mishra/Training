package com.lexnarro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.TrainingRecord;

import java.util.List;

/**
 * Created by SAURABH on 01-06-2017.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<TrainingRecord> recordList;
    private OnItemClickListener onItemClickListener;


    public RecordAdapter(List<TrainingRecord> dutyList) {
        this.recordList = dutyList;
    }


    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_record, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {

        holder.date.setText(recordList.get(position).getDate());
        holder.category.setText(recordList.get(position).getCategoryName());
        holder.activity.setText(recordList.get(position).getActivityName());
        holder.unit.setText(recordList.get(position).getUnits());
        holder.provider.setText(recordList.get(position).getProvider());
        holder.year.setText(recordList.get(position).getFinancial_Year());

        final TrainingRecord record = recordList.get(position);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(record, "edit");
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(record, "detail");

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(record, "delete");
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

        void onItemClick(TrainingRecord record, String type);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, category, activity, unit, provider, year, edit, details, delete;
        LinearLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            category = itemView.findViewById(R.id.txt_category);
            activity = itemView.findViewById(R.id.txt_activity);
            unit = itemView.findViewById(R.id.txt_unit);
            provider = itemView.findViewById(R.id.txt_provider);
            year = itemView.findViewById(R.id.txt_year);
            edit = itemView.findViewById(R.id.txt_edit);
            details = itemView.findViewById(R.id.txt_details);
            delete = itemView.findViewById(R.id.txt_delete);
            root = itemView.findViewById(R.id.record_root);
        }
    }

}
