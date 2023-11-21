package com.example.scheduling;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */

import com.example.scheduling.entity.Conference;
import com.example.scheduling.service.ConferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConferenceServiceTest {

    @Autowired
    private ConferenceService conferenceService;

    @Test
    public void testSchedule() throws Exception {
        String content = "1 Deep dive into probabilistic machine learning 60min\n" +
                "2 High-throughput single-shot multibox object detection on edge devices using FPGAS 45min\n" +
                "3 Containers and the intelligent application revolution 30min\n" +
                "4 AI and the future of work 45min\n" +
                "5 Innovations in explainable AI in the context of real-world business applications 45min\n" +
                "6 Hyperparameter Optimization in practice lightning\n" +
                "7 Humanizing technology: Emotion detection from face and voice 60min\n" +
                "8 Computational creativity: Making music with AI technologies 45min";
        MultipartFile file = new MockMultipartFile("file", "talks.txt", "text/plain", content.getBytes());

        Conference conference = conferenceService.schedule(file);


        assertNotNull(conference);
        System.out.println(conference);
        // 这里可以添加更多的断言来验证conference的内容
    }
}