package com.kn.elephant.note;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventContentDto;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.model.Interval;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.service.Reminder;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 25-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SchedulerEvents {

    @Inject
    private EventService eventService;

    @Getter
    private List<TimerTask> tasks;

    SchedulerEvents() {
        GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));
        context.init();
        tasks = new ArrayList<>();

    }

    public void start() {
		eventService.getAllEvents().stream()
			 .filter(this::isCanRun)
			 .forEach(this::scheduleEvent);
	 }

    private boolean isCanRun(EventDto eventDto) {
        if (eventDto.isDeleted()) {
            return false;
        }
        if (eventDto.getStartDate().isBefore(LocalDateTime.now()) && eventDto.getRepeat() == null) {
            return false;
        }
        return true;
    }

    private void scheduleEvent(EventDto eventDto) {
    	log.info("Schedule event: {}",  eventDto);
		Reminder reminder;
		if(eventDto.getStartDate().isBefore(LocalDateTime.now())) {
			LocalDateTime startTime = getStartTime(eventDto.getStartDate(), eventDto.getRepeat());
			log.info("New start date: {}", startTime);
			eventDto.setStartDate(startTime);
			eventService.saveEvent(eventDto);
			reminder = new Reminder(startTime);
		} else {
    		reminder = new Reminder(eventDto.getStartDate());
		}

        if(eventDto.getRepeat() != null) {
			reminder.setPeriod(getPeriodFromInterval(eventDto.getRepeat()));
		}

        TimerTask timerTask = new TimerTask() {
            public void run() {
            	
                Platform.runLater(() -> {
					createReminderDialog(eventDto, reminder);
				});
            }
        };

		tasks.add(timerTask);
        reminder.setTask(timerTask);
        reminder.schedule();
    }

    protected static LocalDateTime getStartTime(LocalDateTime dateTime, Interval interval) {
    	LocalDateTime startTime = LocalDateTime.now();
    	if(interval == Interval.HOUR) {
			LocalTime time;
    		if(LocalTime.now().isAfter(LocalTime.of(LocalTime.now().getHour(), dateTime.getMinute(), dateTime.getSecond()))) {
				time = LocalTime.of(LocalTime.now().plusHours(1L).getHour(), dateTime.getMinute(), dateTime.getSecond());
			} else {
				time = LocalTime.of(LocalTime.now().getHour(), dateTime.getMinute(), dateTime.getSecond());
			}
			startTime = LocalDateTime.of(LocalDate.now(), time);
		} else if (interval == Interval.DAY) {
			if(LocalDateTime.now().isAfter(dateTime)) {
				LocalDate day = LocalDate.now().plusDays(1L);
				LocalTime time = LocalTime.of(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
				startTime = LocalDateTime.of(day, time);
			} else {
			   startTime = dateTime;
			}
		} else if ( interval == Interval.WEEK) {
			if(LocalDateTime.now().isAfter(dateTime)) {
				long days = Duration.between(dateTime, LocalDateTime.now()).toDays();
				long weeks = (long)Math.max(Math.ceil(days/7.0), 1);
				startTime = dateTime.plusWeeks(weeks);
			}  else {
				startTime = dateTime;
			}
		}

	   return startTime;
	}

    private long getPeriodFromInterval(Interval interval) {
    	long timeInMilliseconds;
    	switch (interval) {
			case HOUR:
				timeInMilliseconds = DateUtils.MILLIS_PER_HOUR;
				break;
			case DAY:
				timeInMilliseconds = DateUtils.MILLIS_PER_DAY;
				break;
			case WEEK:
				timeInMilliseconds = DateUtils.MILLIS_PER_DAY * 7;
				break;
			default:
				throw new IllegalArgumentException("Not recognize interval:" + interval);
		}
		return timeInMilliseconds;
	}

    private void createReminderDialog(EventDto eventDto, Reminder reminder) {
		Dialog<EventContentDto> dialog = new Dialog<>();
		dialog.setResizable(false);
		dialog.setTitle("Reminder");
		dialog.setHeaderText("Event is start! ->  " + eventDto.getName());
		dialog.getDialogPane().getStyleClass().add("card");
		dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());

		TextField contentText = new TextField();
		contentText.setId("dialogNoteTitleText");
		Platform.runLater(contentText::requestFocus);
		dialog.getDialogPane().setContent(contentText);

		ButtonType buttonTypeOk = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			EventContentDto eventContentDto = new EventContentDto().setContent(contentText.getText()).setDate(LocalDateTime.now());
			eventDto.addEventContentDto(eventContentDto);
			eventService.saveEvent(eventDto);
		});

        dialog.setOnCloseRequest(event -> reminder.getTimer().cancel());
		dialog.show();
	}

}
