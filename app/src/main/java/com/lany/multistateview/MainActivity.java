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

import com.lany.msv.MultiStateView;


public class MainActivity extends AppCompatActivity {

    private TestHandler mHandler = new TestHandler();

    private MultiStateView mMultiStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        mMultiStateView.setOnRetryClickListener(new MultiStateView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                mMultiStateView.setViewState(MultiStateView.STATE_LOADING);
                Toast.makeText(getApplicationContext(), "retry clicked", Toast.LENGTH_SHORT).show();
                Message msg = mHandler.obtainMessage();
                msg.obj = mMultiStateView;
                mHandler.sendMessageDelayed(msg, 3000);
            }
        });
        mMultiStateView.setViewForState(R.layout.custom_empty_view, MultiStateView.STATE_EMPTY,true);
        ListView list = (ListView) mMultiStateView.findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "simulation item" + i;
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
                mMultiStateView.setViewState(MultiStateView.STATE_ERROR, "Some problems");
                return true;
            case R.id.empty:
                mMultiStateView.setViewState(MultiStateView.STATE_EMPTY, "no result");
                return true;

            case R.id.content:
                mMultiStateView.setViewState(MultiStateView.STATE_CONTENT);
                return true;

            case R.id.loading:
                mMultiStateView.setViewState(MultiStateView.STATE_LOADING, "waiting");
                return true;
            case R.id.network:
                mMultiStateView.setViewState(MultiStateView.STATE_NETWORK, "The network is not available");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof MultiStateView) {
                ((MultiStateView) msg.obj).setViewState(MultiStateView.STATE_CONTENT);
            }

            super.handleMessage(msg);
        }
    }
}

