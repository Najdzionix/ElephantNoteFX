package com.kn.elephant.note.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 13-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
@Getter
public class Reminder {
	private Timer timer;
	@Setter
	private TimerTask task;
	private Date date;
	@Setter
	private long period = 100000; //todo

    public Reminder(LocalDateTime time) {
        timer = new Timer();
        log.debug("Time:" + time.toString());
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        date = Date.from(instant);
    }


	public void schedule() {
		timer.scheduleAtFixedRate(task, date, period);
	}
}
