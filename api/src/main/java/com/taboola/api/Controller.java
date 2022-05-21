package com.taboola.api;

import java.time.Instant;
import java.util.Map;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class Controller {

    @Autowired
    Dao dao;

    @GetMapping("/currentTime")
    public long time() {
        return Instant.now().toEpochMilli();
    }

    @GetMapping("/counters/time/{time}")
    public Map<Integer, Integer> getEvents(@PathVariable String time) {
        Timestamp timestamp = new Timestamp(Long.parseLong(time));
        return dao.getEventsByTime(timestamp);
    }

    @GetMapping("/counters/time/{time}/eventId/{eventId}")
    public int getEventCounts(@PathVariable String time, @PathVariable String eventId) {
        Timestamp timestamp = new Timestamp(Long.parseLong(time));
        return dao.getEventCountsByTimeAndEventId(timestamp, Integer.parseInt(eventId));
    }

    @GetMapping("/counters/eventId/{eventId}")
    public Map<Timestamp, Integer> getEventsByEventId(@PathVariable String eventId) {
        return dao.getEventsByEventId(Integer.parseInt(eventId));
    }

}
