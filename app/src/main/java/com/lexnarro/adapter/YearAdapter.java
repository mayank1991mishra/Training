package com.lexnarro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.FinancialYear;

import java.util.List;

public class YearAdapter extends ArrayAdapter<FinancialYear> {
    private Context mContext;
    private List<FinancialYear> years;

    public YearAdapter(Context context, int resource, List<FinancialYear> years) {
        super(context, resource, years);
        this.mContext = context;
        this.years = years;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getNothingSelectedView(parent, position);
    }

    private View getNothingSelectedView(ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinneritem, parent, false);
        TextView t = view.findViewById(R.id.txt_year);
        t.setText(years.get(position).getText());
        for (FinancialYear years : years) {
            if (years.getSelected())
                t.setText(years.getText());
        }
        return view;

    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinneritem, parent, false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.txt_year);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.mTextView.setText(years.get(position).getText());
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
    }
}