package com.fm.lvplay;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private int id;
    private String name;

    private boolean inPlaying = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying() {
        return inPlaying;
    }

    public void setPlaying(boolean playing) {
        inPlaying = playing;
    }

    public static List<Channel> initList(int count) {
        List<Channel> list = new ArrayList<>();

        for (int i = 1; i < count; i++) {
            Channel channel = new Channel();
            channel.setId(i);
            channel.setName("channel_" + i);
            channel.setPlaying(false);
            list.add(channel);
        }

        return list;
    }
}
