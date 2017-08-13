package com.lany.multistateview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lany.view.StateLayout;

public class MainActivity extends AppCompatActivity {
    private TestHandler mHandler = new TestHandler();
    private StateLayout mStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStateLayout = (StateLayout) findViewById(R.id.stateLayout);
        mStateLayout.setOnRetryClickListener(new StateLayout.OnRetryClickListener() {
            @Override
            public void onRetry(@StateLayout.State int state) {
                mStateLayout.showLoading();
                Toast.makeText(getApplicationContext(), "retry clicked", Toast.LENGTH_SHORT).show();
                Message msg = mHandler.obtainMessage();
                msg.obj = mStateLayout;
                mHandler.sendMessageDelayed(msg, 3000);
            }
        });
        mStateLayout.setEmptyView(R.layout.custom_empty_view);
        ListView list = (ListView) mStateLayout.findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "item" + i;
        }

        list.setAdapter(new ArrayAdapter<>(this, R.layout.item_listview, data));
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.error:
                mStateLayout.showError();
                return true;
            case R.id.empty:
                mStateLayout.showEmpty();
                return true;
            case R.id.content:
                mStateLayout.showContent();
                return true;
            case R.id.loading:
                mStateLayout.showLoading();
                return true;
            case R.id.network:
                mStateLayout.showNetwork();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof StateLayout) {
                ((StateLayout) msg.obj).showContent();
            }
            super.handleMessage(msg);
        }
    }
}

