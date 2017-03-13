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

import com.lany.view.StateView;

public class MainActivity extends AppCompatActivity {
    private TestHandler mHandler = new TestHandler();
    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStateView = (StateView) findViewById(R.id.stateView);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                mStateView.setViewState(StateView.LOADING);
                Toast.makeText(getApplicationContext(), "retry clicked", Toast.LENGTH_SHORT).show();
                Message msg = mHandler.obtainMessage();
                msg.obj = mStateView;
                mHandler.sendMessageDelayed(msg, 3000);
            }
        });
        mStateView.setViewForState(R.layout.custom_empty_view, StateView.EMPTY, true);
        ListView list = (ListView) mStateView.findViewById(R.id.list);

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
                mStateView.setViewState(StateView.ERROR, "Some problems");
                return true;
            case R.id.empty:
                mStateView.setViewState(StateView.EMPTY, "no result");
                return true;
            case R.id.content:
                mStateView.setViewState(StateView.CONTENT);
                return true;
            case R.id.loading:
                mStateView.setViewState(StateView.LOADING, "waiting");
                return true;
            case R.id.network:
                mStateView.setViewState(StateView.NETWORK, "The network is not available");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof StateView) {
                ((StateView) msg.obj).setViewState(StateView.CONTENT);
            }

            super.handleMessage(msg);
        }
    }
}

