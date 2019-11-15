
package com.lexnarro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lexnarro.R;

public class EditRecordDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);


    }

    private void inIt() {
        if (getIntent() != null && getIntent().getSerializableExtra("record") != null) {
            switch (getIntent().getStringExtra("type")) {

                case "edit":
                    break;
                case "detail":
                    break;
            }

        }
    }
}
