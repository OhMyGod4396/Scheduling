package com.example.scheduling.entity;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/7
 * @Description:
 */
@Data
@AllArgsConstructor
public class Talk implements Comparable<Talk> {
    private String title;
    private int duration;
    private LocalTime startTime;
   
    @Override
    public int compareTo(Talk other) {
        return other.duration - this.duration;
    }
}