package com.kn.elephant.note.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.kn.elephant.note.Main;

import javafx.scene.control.Dialog;
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
	long period = 10000; //todo
	private Timer timer;
	@Setter
	private TimerTask task;
	private Date date;

    public Reminder(LocalDateTime time) {
        timer = new Timer();
        log.debug("Time:" + time.toString());
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        date = Date.from(instant);
        task = new RemindTask();
    }


	public void schedule() {
		timer.scheduleAtFixedRate(task, date, period);
	}

	class RemindTask extends TimerTask {
		public void run() {
			log.info("Start Task: Time's up!");
			Dialog<String> dialog = new Dialog<>();
			dialog.setTitle("Hello");
			dialog.setContentText("Hello");
			dialog.getDialogPane().getStyleClass().add("card");
			dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
			dialog.showAndWait();
			timer.cancel();
		}
	}
}
