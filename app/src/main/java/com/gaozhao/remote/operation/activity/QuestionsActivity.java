package com.gaozhao.remote.operation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaozhao.remote.R;
import com.gaozhao.remote.RemotePaymentBaseActivity;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * Date on 2020/7/22.
 * https://me.csdn.net/gao511147456
 */
public class QuestionsActivity extends RemotePaymentBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operational_problem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("疑难问题");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
