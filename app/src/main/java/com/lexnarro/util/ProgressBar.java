package com.lexnarro.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.lexnarro.R;


/**
 * Created by ashwani on 8/5/17.
 */

public class ProgressBar extends Dialog {

    private static ProgressBar dialog;
    public ProgressBar(Context context) {
        super(context);
    }

    public ProgressBar(Context context, int theme) {
        super(context, theme);
    }

    public static synchronized ProgressBar showDialog(Context context, String message, boolean indeterminate, boolean cancelable) {
        if (dialog!=null && dialog.isShowing()){
            cancelDialog();
        }
        dialog = new ProgressBar(context, R.style.ProgressDialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_dialod);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        ((TextView) dialog.findViewById(R.id.tv_dialog_msg)).setText(message != null ? message : "");
        Animation operatingAnim = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        operatingAnim.setDuration(2200);

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);


        View view = dialog.getWindow().getDecorView();

        View circle = view.findViewById(R.id.img_progressbar_circle);
        circle.setAnimation(operatingAnim);
        return dialog;
    }
    public static synchronized void cancelDialog(){
        if (dialog==null){
            return;
        }
        dialog.cancel();
        View view = dialog.getWindow().getDecorView();
        View circle = view.findViewById(R.id.img_progressbar_circle);
        if(circle.getAnimation()!=null){
            circle.getAnimation().reset();
            circle.clearAnimation();
        }


    }



}
