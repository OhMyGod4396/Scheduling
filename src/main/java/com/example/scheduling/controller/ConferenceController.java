package com.example.scheduling.controller;

import com.example.scheduling.entity.Conference;
import com.example.scheduling.exception.FileParseException;
import com.example.scheduling.service.ConferenceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
@RestController
public class ConferenceController {

    @Resource
    private ConferenceService conferenceService;

    /**
     * 上传会议信息文件并进行调度
     * @param file 包含会议信息的文件
     * @return 调度结果
     */
    @PostMapping("/schedule")
    public String schedule(@RequestParam("file") MultipartFile file) throws FileParseException {
        Conference conference = conferenceService.schedule(file);
        return conferenceService.formatConferenceSchedule(conference);
    }
}