package com.example.scheduling.service;

import com.example.scheduling.entity.Conference;
import com.example.scheduling.entity.Session;
import com.example.scheduling.entity.Talk;
import com.example.scheduling.entity.Track;
import com.example.scheduling.exception.FileParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
@Service
public class ConferenceService {

    private static final LocalTime MORNING_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime MORNING_END_TIME = LocalTime.of(12, 0);
    private static final LocalTime AFTERNOON_START_TIME = LocalTime.of(13, 0);
    private static final LocalTime AFTERNOON_END_TIME = LocalTime.of(16, 0);
    /**
     * 主要的调度方法，接收一个文件，解析出Talks，然后安排会议
     */
    public Conference schedule(MultipartFile file) throws FileParseException {
        List<Talk> talks;
        try {
            talks = parseFile(file);
        } catch (IOException e) {
            throw new FileParseException("文件解析失败: " + e.getMessage(), e);
        }
        return scheduleConference(talks);
    }

    /**
     * 解析文件，将每一行转换为一个Talk对象，并处理可能遇到的问题
     */
    private List<Talk> parseFile(MultipartFile file) throws IOException {
        List<Talk> talks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int lastSpaceIndex = line.lastIndexOf(" ");
                if (lastSpaceIndex == -1) {
                    throw new IOException("无效的行格式: " + line);
                }
                String title = line.substring(0, lastSpaceIndex);
                String durationStr = line.substring(lastSpaceIndex + 1);
                int duration;
                try {
                    duration = "lightning".equals(durationStr) ? 5 : Integer.parseInt(durationStr.replace("min", ""));
                } catch (NumberFormatException e) {
                    throw new IOException("无法解析时长: " + durationStr);
                }
                talks.add(new Talk(title, duration, null));
            }
        } catch (IOException e) {
            throw new IOException("文件读取失败: " + e.getMessage());
        }
        if (talks.isEmpty()) {
            throw new IOException("文件为空或者没有有效的行");
        }
        return talks;
    }

    /**
     * 处理会议，将Talks分配到不同的Track和Session中
     */
    private Conference scheduleConference(List<Talk> talks) {
        Conference conference = new Conference();
        while (!talks.isEmpty()) {
            Track track = new Track();
            track.setMorningSession(scheduleSession(new Session(), MORNING_START_TIME, MORNING_END_TIME, talks));
            track.setAfternoonSession(scheduleSession(new Session(), AFTERNOON_START_TIME, AFTERNOON_END_TIME, talks));
            LocalTime networkingEventStartTime = track.getAfternoonSession().getEndTime().isBefore(AFTERNOON_END_TIME)
                    ? AFTERNOON_END_TIME
                    : track.getAfternoonSession().getEndTime();
            track.setNetworkingEvent(
                    scheduleSession(new Session(), networkingEventStartTime, LocalTime.of(17, 0), talks));
            conference.getTracks().add(track);
        }
        return conference;
    }

    /**
     * 安排一个Session，将Talks分配到指定的时间段
     */
    private Session scheduleSession(Session session, LocalTime startTime, LocalTime endTime, List<Talk> talks) {
        LocalTime currentTime = startTime;
        Iterator<Talk> iterator = talks.iterator();
        while (iterator.hasNext()) {
            Talk talk = iterator.next();
            int totalDuration = session.getTalks().stream().mapToInt(Talk::getDuration).sum();
            if (totalDuration + talk.getDuration() < Duration.between(startTime, endTime).toMinutes()) {
                talk.setStartTime(currentTime);
                session.getTalks().add(talk);
                iterator.remove();
                currentTime = currentTime.plusMinutes(talk.getDuration());
            }
        }
        fillIdleTime(session, currentTime, endTime, talks);
        return session;
    }

    /**
     * 填充空闲时间，将剩余的Talks分配到空闲时间
     */
    private void fillIdleTime(Session session, LocalTime currentTime, LocalTime endTime, List<Talk> talks) {
        int idleTime = (int) Duration.between(currentTime, endTime).toMinutes();
        Iterator<Talk> iterator = talks.iterator();
        while (iterator.hasNext()) {
            Talk talk = iterator.next();
            if (talk.getDuration() <= idleTime) {
                talk.setStartTime(currentTime);
                session.getTalks().add(talk);
                iterator.remove();
                currentTime = currentTime.plusMinutes(talk.getDuration());
                idleTime -= talk.getDuration();
            }
        }
    }

    /**
     * 格式化会议日程，
     */
    public String formatConferenceSchedule(Conference conference) {
        StringBuilder schedule = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
        int trackNumber = 1;

        for (Track track : conference.getTracks()) {
            schedule.append("会场").append(trackNumber++).append(":\n");

            if (!track.getMorningSession().getTalks().isEmpty()) {
                appendTalks(schedule, track.getMorningSession().getTalks(), formatter);
                schedule.append("12:00下午 Lunch\n");
            }

            if (!track.getAfternoonSession().getTalks().isEmpty()) {
                appendTalks(schedule, track.getAfternoonSession().getTalks(), formatter);
            }

            if (!track.getNetworkingEvent().getTalks().isEmpty()) {
                appendTalks(schedule, track.getNetworkingEvent().getTalks(), formatter);
                Talk lastTalk = track.getNetworkingEvent().getTalks()
                        .get(track.getNetworkingEvent().getTalks().size() - 1);
                LocalTime networkingEventStartTime = lastTalk.getStartTime().plusMinutes(lastTalk.getDuration());
                schedule.append(networkingEventStartTime.format(formatter)).append(" Networking Event\n");
            }
        }

        return schedule.toString();
    }

    private void appendTalks(StringBuilder schedule, List<Talk> talks, DateTimeFormatter formatter) {
        for (Talk talk : talks) {
            schedule.append(talk.getStartTime().format(formatter))
                    .append(" ")
                    .append(talk.getTitle())
                    .append(" ")
                    .append(talk.getDuration())
                    .append("min\n");
        }
    }

}