package com.fm.lvplay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class AllChannelAdapter extends RecyclerView.Adapter<AllChannelAdapter.VH> {

    private List<Channel> channelList;
    private Context mContext;

    public AllChannelAdapter(List<Channel> channelList, Context mContext) {
        this.channelList = channelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_channel, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        final Channel channel = channelList.get(position);
        holder.tvChannel.setText(channel.getName());
        if (channel.isPlaying()) {
            holder.jumpingView.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundResource(R.drawable.channel_bg_selected);
        } else {
            holder.jumpingView.setVisibility(View.GONE);
            holder.itemView.setBackgroundResource(R.drawable.channel_bg_selector);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.setOnClickListener(null);
        holder.itemView.setOnFocusChangeListener(null);
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private TextView tvChannel;
        private JumpingView jumpingView;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvChannel = itemView.findViewById(R.id.tv_channel);
            jumpingView = itemView.findViewById(R.id.jump_view);
            jumpingView.setColor(R.color.colorAccent);
        }
    }

}
