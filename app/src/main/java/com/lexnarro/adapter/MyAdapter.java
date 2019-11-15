package com.lexnarro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.MyCategory;
import com.lexnarro.response.StateName;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private int id;
    private boolean selected;
    private String set_id;
    List<String> states;
    List<MyCategory> categories;
    List<StateName> stateNames;
    private boolean profile;
    private OnItemClickListener onItemClickListener;


    public MyAdapter(Context context, List<String> states) {
        this.mContext = context;
        this.states = states;
    }

    public MyAdapter(List<MyCategory> categories, Context context, int id) {
        this.id = id;
        this.mContext = context;
        this.categories = categories;
        List<String> states = new ArrayList<>();
        for (MyCategory state : categories) {
            states.add(state.getName());
        }
        this.states = states;
    }

    public MyAdapter(List<StateName> stateNames, Context context) {
        this.mContext = context;
        this.stateNames = stateNames;
        List<String> states = new ArrayList<>();
        for (StateName state : stateNames) {
            states.add(state.getName());
        }
        this.states = states;
    }
    public MyAdapter(List<StateName> stateNames, Context context,boolean profile, String id) {
        this.mContext = context;
        this.stateNames = stateNames;
        this.profile = profile;
        this.set_id = id;
        List<String> states = new ArrayList<>();
        for (StateName state : stateNames) {
            states.add(state.getName());
        }
        this.states = states;
    }

    public MyAdapter(List<MyCategory> categories, Context context, int id, boolean selected, String set_id) {
        this.id = id;
        this.mContext = context;
        this.selected = selected;
        this.set_id = set_id;
        this.categories = categories;
        List<String> states = new ArrayList<>();
        for (MyCategory state : categories) {
            states.add(state.getName());
        }
        this.states = states;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public Object getItem(int i) {
        return states.get(i);
    }

    @Override
    public long getItemId(int i) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getSelectedView(parent, position);

    }

    private View getSelectedView(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_no_selection, parent, false);
        TextView t = (TextView) view.findViewById(R.id.txt_language);

        if (profile){
            for (StateName name : stateNames) {
                if (name.getId().equalsIgnoreCase(set_id)) {
                    t.setText(name.getName());
                    t.setTag(name.getId());
                    profile=false;
                }
            }
        } else if (selected && position==0) {
            for (MyCategory category : categories) {
                if (category.getId().equalsIgnoreCase(set_id)) {
                    t.setText(category.getName());
                    t.setTag(category.getId());
                }
            }
        } else {
            t.setText(states.get(position));
            if (categories!=null){
                t.setTag(categories.get(position).getId());
            }

        }
        return view;

    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.textof);
            holder.root_f = (LinearLayout) convertView.findViewById(R.id.root_f);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.mTextView.setText(states.get(position));


        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {


        void onItemClick(View view, String state);


    }

    private class ViewHolder {
        private TextView mTextView;
        private LinearLayout root_f;
    }


}