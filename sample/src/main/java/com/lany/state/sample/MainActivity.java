package com.lany.state.sample;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lany192.view.StateLayout;


public class MainActivity extends AppCompatActivity {
    private StateLayout mStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.content).setOnClickListener(view -> mStateLayout.showContent());
        findViewById(R.id.error).setOnClickListener(view -> mStateLayout.showError("发现一些问题"));
        findViewById(R.id.empty).setOnClickListener(view -> mStateLayout.showEmpty("找不到内容"));
        findViewById(R.id.network).setOnClickListener(view -> mStateLayout.showNetwork("没有网络啦"));
        findViewById(R.id.loading).setOnClickListener(view -> mStateLayout.showLoading());


        mStateLayout = findViewById(R.id.stateLayout);
        mStateLayout.setOnStateListener(state -> Log.i("MainActivity", "onStateChanged: state==" + state));
        mStateLayout.setOnRetryListener(() -> {
            mStateLayout.showLoading();
            new Handler().postDelayed(() -> {
                mStateLayout.showContent();
                Toast.makeText(getApplicationContext(), "load data finish", Toast.LENGTH_SHORT).show();
            }, 3000);


        });

        ListView list = findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "Item " + i;
        }
        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, data));
    }
}

