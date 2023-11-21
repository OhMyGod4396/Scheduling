package com.example.scheduling.entity;

import lombok.Data;


/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
@Data
public class Track {
    private Session morningSession;
    private Session lunchTime;
    private Session afternoonSession;
    private Session networkingEvent;
}