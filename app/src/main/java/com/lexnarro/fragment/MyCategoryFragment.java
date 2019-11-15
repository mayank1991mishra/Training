package com.lexnarro.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.response.MyCategory;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCategoryFragment} interface
 * to handle interaction events.
 * Use the {@link MyCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private ArrayList<MyCategory> mParam1;
    private double mParam2;

    private View view;
    private ProgressBar progress;
    private TextView txt_completed;
    private TextView txt_unit_completed;
    private TextView txt_cat_completed;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private ArrayList<MyCategory> mParam3;
    private int total_cat;
    private LinearLayout linearLayout;

    public MyCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCategoryFragment newInstance(ArrayList<MyCategory> param1, double param2, ArrayList<MyCategory> param3) {
        MyCategoryFragment fragment = new MyCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ArrayList<MyCategory>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            mParam3 = (ArrayList<MyCategory>) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_progress_fragmanet, container, false);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        txt_completed = (TextView) view.findViewById(R.id.txt_completed);
        txt_unit_completed = (TextView) view.findViewById(R.id.txt_unit_completed);
        txt_unit_completed.setText(String.valueOf(mParam2) + " Units completed");
        txt_cat_completed = (TextView) view.findViewById(R.id.txt_cat_completed);

//        showUnitProgress((int)Math.round(mParam2));
        showUnitProgress(mParam2);
//        txt_completed.setText((int)Math.round(mParam2)+"/10");
        showCourses(mParam1, mParam3);
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

    private void showUnitProgress(final double total_progress) {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < total_progress * 10) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
//                            circularSeekbar.setProgress(progressStatus);
                            progress.setProgress(progressStatus);
                            txt_completed.setText(progressStatus / 10 + "/" + 100 / 10);
//                            if (progressStatus == total_progress * 10) {
//                                txt_completed.setText(String.valueOf(mParam2) + "/" + 100 / 10);
//                            }

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }


    private void showCourses(ArrayList<MyCategory> myCategories, ArrayList<MyCategory> myExMyCategories) {

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.courses);


        for (int i = 0; i < myCategories.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(30, 20, 30, 20);
            TextView textView = new TextView(getActivity());
            textView.setTextSize(16);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(layoutParams);
            if (myExMyCategories.get(i).getUnits_Done().equalsIgnoreCase("0")) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.redcircle), null, null);
                textView.setCompoundDrawablePadding(5);

            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.round_check), null, null);
                textView.setCompoundDrawablePadding(5);

                txt_cat_completed.setText(String.format(Locale.getDefault(), "%d/%d Cats Completed", ++total_cat, myCategories.size()));

            }
            if (textView.getParent() != null) {
                ((ViewGroup) textView.getParent()).removeView(textView); // <- fix
            }
            textView.setText(myCategories.get(i).getMyShortName());
            linearLayout.addView(textView);
        }

    }



}
