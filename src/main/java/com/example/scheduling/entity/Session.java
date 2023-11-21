package com.example.scheduling.entity;

import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
@Data
public class Session {
    private List<Talk> talks = new ArrayList<>();

    public LocalTime getEndTime() {
    if (getTalks().isEmpty()) {
        return null;
    }
    Talk lastTalk = getTalks().get(getTalks().size() - 1);
    return lastTalk.getStartTime().plusMinutes(lastTalk.getDuration());
}
}
