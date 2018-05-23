package com.lany.state.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lany.state.StateLayout;
import com.lany.state.ViewType;

public class MainActivity extends AppCompatActivity {
    private StateLayout mStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showContent();
            }
        });
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showError("发现一些问题");
            }
        });
        findViewById(R.id.empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showEmpty("找不到内容");
            }
        });
        findViewById(R.id.network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showNetwork("没有网络啦");
            }
        });
        findViewById(R.id.loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showLoading();
            }
        });


        mStateLayout = findViewById(R.id.stateLayout);
        mStateLayout.setOnStateListener(new StateLayout.OnStateListener() {
            @Override
            public void onStateChanged(@ViewType int state) {
                Log.i("MainActivity", "onStateChanged: state==" + state);
            }
        });
        mStateLayout.setOnRetryListener(new StateLayout.OnRetryListener() {
            @Override
            public void onRetry() {
                mStateLayout.showLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStateLayout.showContent();
                        Toast.makeText(getApplicationContext(), "load data finish", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);


            }
        });

        ListView list = findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "Item " + i;
        }
        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, data));
    }
}

