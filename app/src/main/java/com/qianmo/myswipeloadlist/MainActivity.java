package com.qianmo.myswipeloadlist;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String[] strs = {"AAA", "BBB", "CCC", "DDD", "EEE", "AAA", "BBB", "CCC", "DDD", "EEE",
            "AAA", "BBB", "CCC", "DDD", "EEE", "AAA", "BBB", "CCC", "DDD", "EEE", "AAA", "BBB", "CCC", "DDD", "EEE"};
    private ArrayAdapter mAdapter;
    private LoadListView mListView;
    private ArrayList<String> mData;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < strs.length; i++) {
                mData.add(strs[i]);
            }
            mListView.onLoadComplete();
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = LayoutInflater.from(this).inflate(R.layout.load_view, null);
        mData = new ArrayList<String>();
        for (int i = 0; i < strs.length; i++) {
            mData.add(strs[i]);
        }

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
        mListView = (LoadListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mListView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void loadData() {
                mHandler.sendEmptyMessageDelayed(1, 5000);
            }
        });
    }
}
