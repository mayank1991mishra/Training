package com.lexnarro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.MyCategory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Category} interface
 * to handle interaction events.
 * Use the {@link Category#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Category extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3= "param3";
    // TODO: Rename and change types of parameters
    private View view;
    private ProgressBar progress;
    private TextView txt_unitsCompleted;
    private TextView txt_units_Completed;
    private TextView txt_short_name;
    private TextView txt_unit_name;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private MyCategory mParam1;
    int units=0;
    Double unitsDone=0.0;



    public Category() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Category.
     */
    // TODO: Rename and change types and number of parameters
    public static Category newInstance(MyCategory param1) {
        Category fragment = new Category();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (MyCategory) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_e, container, false);
        progress = (ProgressBar) view.findViewById(R.id.progressbar);

        txt_unitsCompleted = (TextView) view.findViewById(R.id.txt_unitsCompleted);
        txt_units_Completed = (TextView) view.findViewById(R.id.txt_units_Completed);
        txt_short_name = (TextView) view.findViewById(R.id.txt_short_name);
        txt_unit_name = (TextView) view.findViewById(R.id.txt_units_name);
        txt_short_name.setText(mParam1.getExistingShort_Name());
        txt_unit_name.setText(mParam1.getCategory_Name());

        txt_units_Completed.setText(mParam1.getUnits_Done()+" Units completed");
        unitsDone =  Double.parseDouble(mParam1.getUnits_Done());
        units = (int)Math.round(unitsDone);
        txt_unitsCompleted.setText(String.valueOf(units)+"/1");

        if (units>0){
            progress.setProgress(100);

        }else {
            progress.setProgress(0);

        }



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
