package com.fm.lvplay;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    MenuRecyclerView rvAllChannel;
    MenuRecyclerView rvAllChannel2;
    private Button btnStop;
    private Button btnChangeChannel;

    List<Channel> channelList;
    List<Channel> channelList2;

    private int mCurrentPlayPostion = 10;
    private int mCurrentPlayPostion2 = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvAllChannel = findViewById(R.id.rv_all_channel);
        rvAllChannel.setViewName("recycler");
        rvAllChannel2 = findViewById(R.id.rv_all_channel2);
        rvAllChannel2.setViewName("recycler2");

        btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("main", "current:" + rvAllChannel.getFocusedPosition());
                Log.d("main", "current:" + rvAllChannel.getActivatedPosition());
                Log.d("main", "current:" + rvAllChannel2.getFocusedPosition());
                Log.d("main", "current:" + rvAllChannel2.getActivatedPosition());

                rvAllChannel.smoothScrollToPosition(16);
                rvAllChannel2.scrollToPosition(22);
            }
        });

        btnChangeChannel = findViewById(R.id.btn_change_channel);
        btnChangeChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelList.get(16).setPlaying(false);
                channelList.get(27).setPlaying(true);
            }
        });

        channelList = Channel.initList(36);

        channelList.get(mCurrentPlayPostion).setPlaying(true);
        rvAllChannel.setLayoutManager(new V7LinearLayoutManager(this));
        final AllChannelAdapter allChannelAdapter = new AllChannelAdapter(channelList, this);
        rvAllChannel.setAdapter(allChannelAdapter);
        rvAllChannel.setCurrentPosition(3);
        rvAllChannel.setItemActivatedListener(new MenuRecyclerView.ItemActivatedListener() {
            @Override
            public void onItemActivated(View itemView, int position, boolean activated) {
                Log.d("main", "onItemActivated:" + position + ",activated:" + activated);
                if (channelList.get(position).isPlaying()) {
                    return;
                }
                TextView tvChannel = itemView.findViewById(R.id.tv_channel);
                if (activated) {
                    tvChannel.setTextColor(Color.parseColor("#00FF33"));
                } else {
                    tvChannel.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        rvAllChannel.setOnItemClickListener(new MenuRecyclerView.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.d("main", "onItemClick:" + position);
                if (position == mCurrentPlayPostion) {
                    return;
                }
                int oldPostion = mCurrentPlayPostion;
                mCurrentPlayPostion = position;
                channelList.get(oldPostion).setPlaying(false);
                channelList.get(mCurrentPlayPostion).setPlaying(true);
                allChannelAdapter.notifyItemChanged(oldPostion);
                allChannelAdapter.notifyItemChanged(mCurrentPlayPostion);
            }
        });

        rvAllChannel.setOnItemFocusListener(new MenuRecyclerView.ItemFocusListener() {
            @Override
            public void onItemFocusChange(View itemView, int position, boolean hasFocus) {
                Log.d("main", "onItemFocusChange:" + position + ",hasFocus:" + hasFocus);
            }
        });

        rvAllChannel.setOnItemLongClickListener(new MenuRecyclerView.ItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                Log.d("main", "onItemLongClick:" + position);
                return true;
            }
        });

        rvAllChannel.setFocusLostListener(new MenuRecyclerView.FocusLostListener() {
            @Override
            public void onFocusLost(View lastFocusChild, int direction) {
                Log.e("main", "onFocusLost direction:" + direction);
            }
        });


        rvAllChannel2.setCanFocusOutHorizontal(true);  //让水平方向无法移出控件
        rvAllChannel2.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        channelList2 = Channel.initList(54);
        final AllChannelAdapter allChannelAdapter2 = new AllChannelAdapter(channelList2, this);
        rvAllChannel2.setAdapter(allChannelAdapter2);


        rvAllChannel2.setItemActivatedListener(new MenuRecyclerView.ItemActivatedListener() {
            @Override
            public void onItemActivated(View itemView, int position, boolean activated) {
                Log.d("main2", "onItemActivated:" + position + ",activated:" + activated);
            }
        });

        rvAllChannel2.setOnItemClickListener(new MenuRecyclerView.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.d("main2", "onItemClick:" + position);
                if (position == mCurrentPlayPostion2) {
                    return;
                }
                int oldPostion = mCurrentPlayPostion2;
                mCurrentPlayPostion2 = position;
                channelList2.get(oldPostion).setPlaying(false);
                channelList2.get(mCurrentPlayPostion2).setPlaying(true);
                allChannelAdapter2.notifyItemChanged(oldPostion);
                allChannelAdapter2.notifyItemChanged(mCurrentPlayPostion2);
            }
        });

        rvAllChannel2.setOnItemFocusListener(new MenuRecyclerView.ItemFocusListener() {
            @Override
            public void onItemFocusChange(View itemView, int position, boolean hasFocus) {
                Log.d("main2", "onItemFocusChange:" + position + ",hasFocus:" + hasFocus);
            }
        });

        rvAllChannel2.setOnItemLongClickListener(new MenuRecyclerView.ItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                Log.d("main2", "onItemLongClick:" + position);
                return true;
            }
        });

        rvAllChannel2.setFocusLostListener(new MenuRecyclerView.FocusLostListener() {
            @Override
            public void onFocusLost(View lastFocusChild, int direction) {
                Log.e("main", "onFocusLost1 direction:" + direction);
            }
        });

    }
}
