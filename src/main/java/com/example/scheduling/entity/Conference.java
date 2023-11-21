package com.example.scheduling.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
@Data
public class Conference {
    private List<Track> tracks = new ArrayList<>();
}
