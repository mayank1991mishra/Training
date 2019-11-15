package com.lexnarro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.activity.CarryOverActivity;
import com.lexnarro.bean.CarryOverData;
import com.lexnarro.response.TrainingRecord;

import org.w3c.dom.Text;

import java.util.List;
import java.util.regex.Pattern;

public class CarryAdapter extends RecyclerView.Adapter<CarryAdapter.ViewHolder> {
    private final double unit;
    private final Context context;
    private List<TrainingRecord> carryList;
    private OnEditListener onEditListener;
    private double before_change;
    private int after;
    private double entered;


    public CarryAdapter(List<TrainingRecord> carryList, double allow_unit, Context context) {
        this.carryList = carryList;
        this.unit = allow_unit;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_carry, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if((Double.parseDouble(carryList.get(position).getUnits())-1)<holder.allowUnit){
            holder.allowUnit=(Double.parseDouble(carryList.get(position).getUnits())-1);
        }


        holder.date.setText(carryList.get(position).getDate());
        holder.category.setText(carryList.get(position).getCategoryName());
        holder.activity.setText(carryList.get(position).getActivityName());
        holder.unit.setText(carryList.get(position).getUnits());
        holder.provider.setText(carryList.get(position).getProvider());
        holder.year.setText(carryList.get(position).getFinancial_Year());

        final TrainingRecord record = carryList.get(position);
        if (onEditListener != null) {
            onEditListener.setListener(holder);
        }
        holder.edt_unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {





            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 0 && s.length() > 0 && s.toString().equalsIgnoreCase(".")) {
                    int maxLength = 2;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.append("5");
                } else if (start == 0 && s.length() > 0 && Double.parseDouble(String.valueOf(s.charAt(0))) < holder.allowUnit) {
                    int maxLength = 2;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("."));
                } else if (start == 0 && s.length() > 0 && Double.parseDouble(String.valueOf(s.charAt(0))) > holder.allowUnit) {
                    int maxLength = 1;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.getText().clear();
                    holder.edt_unit.append(Double.toString(holder.allowUnit));
                    Toast.makeText(context, "Carry over unit can not be more than allowed units or unit available in the category", Toast.LENGTH_SHORT).show();
                } else if (start == 1 && s.length() > 1 && String.valueOf(s.charAt(1)).equalsIgnoreCase(".")) {
                    int maxLength = 3;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("5"));
                    holder.edt_unit.append("5");
                } else if (start == 1 && before == 1 && s.length() > 0 && String.valueOf(s.charAt(0)).equalsIgnoreCase(".")) {
                    int maxLength = 1;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.getText().clear();
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("123456789."));
                } else if (start == 1 && before == 1 && s.length() > 0 && Double.parseDouble(String.valueOf(s.charAt(0))) < holder.allowUnit) {
                    int maxLength = 2;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("."));
                } else if (start == 2 && before == 1 && s.length() > 1 && String.valueOf(s.charAt(1)).equalsIgnoreCase(".")) {
                    int maxLength = 2;
                    InputFilter[] fArray = new InputFilter[1];
                    String  temp=String.valueOf(s.charAt(0));
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("."));
                    holder.edt_unit.getText().clear();
                    holder.edt_unit.append(temp);
                } else {
                    int maxLength = 1;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edt_unit.setFilters(fArray);
                    holder.edt_unit.setKeyListener(DigitsKeyListener.getInstance("123456789."));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s!=null && !s.toString().trim().equals("") && Double.parseDouble(s.toString())>=Double.parseDouble(record.getUnits())){
                    holder.edt_unit.getText().clear();
                    Toast.makeText(context, "Entered unit can not be more than available unit!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Pattern.matches("[0-9]{1}", s.toString()) || Pattern.matches("[.]{1}[5]{1}", s.toString()) || Pattern.matches("[1-9]{1}[.]{1}[5]{1}", s.toString())) {
                    Log.e("OK", "OK");
                    if (Double.parseDouble(s.toString()) <= holder.allowUnit) {
                        carryList.get(position).setEntered_unit(s.toString());
                        if (onEditListener!=null){
                            onEditListener.onUnitEnter(position,Double.parseDouble(s.toString()),true,carryList);
                        }

                    } else {
                        carryList.get(position).setEntered_unit(null);
                        if (onEditListener!=null){
                            onEditListener.onUnitEnter(position,Double.parseDouble(s.toString()),false,null);
                        }
                        Toast.makeText(context, "Carry over unit can not be more than allowed units or unit available in the category", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.e("OK", "Not Ok");
                }


            }
        });

        holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.edt_unit.setEnabled(true);
                    holder.edt_unit.requestFocus();
                    carryList.get(position).setSelected(true);
                } else {
                    holder.edt_unit.setEnabled(false);
                    holder.edt_unit.setText("");
                    onEditListener.onUnitEnter(position ,0,false,null);
                    carryList.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carryList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements CarryOverActivity.OnUnitChangeListener {
        TextView date, category, activity, unit, provider, year;
        EditText edt_unit;
        LinearLayout root;
        CheckBox select;
        double allowUnit;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            category = itemView.findViewById(R.id.txt_category);
            activity = itemView.findViewById(R.id.txt_activity);
            unit = itemView.findViewById(R.id.txt_unit);
            provider = itemView.findViewById(R.id.txt_provider);
            year = itemView.findViewById(R.id.txt_year);
            edt_unit = itemView.findViewById(R.id.edt_unit);
            edt_unit.setEnabled(false);
            edt_unit.setKeyListener(DigitsKeyListener.getInstance("123456789."));
            select = itemView.findViewById(R.id.chk_select);
            root = itemView.findViewById(R.id.record_root);
        }

        @Override
        public void onUnitChange(double unit) {
            this.allowUnit = unit;

        }
    }

    public interface OnEditListener {

        void onUnitEnter(int position, double unit, boolean add,List<TrainingRecord> list);
        void onUnitDeleted(double unit);

        void setListener(CarryOverActivity.OnUnitChangeListener listener);

    }

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

}