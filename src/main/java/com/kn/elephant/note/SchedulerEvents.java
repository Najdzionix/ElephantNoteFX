package com.kn.elephant.note;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.TimerTask;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventContentDto;
import com.kn.elephant.note.dto.EventDto;
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
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 25-08-2017
 * email:kamilnadlonek@gmail.com
 */

@Log4j2
public class SchedulerEvents {

    @Inject
    private EventService eventService;

    SchedulerEvents() {
        GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));
        context.init();

    }

    public void start() {
		eventService.getAllEvents().stream()
			 .filter(eventDto -> eventDto.getRepeat() != null)
			 .forEach(this::scheduleEvent);
	 }

    private void scheduleEvent(EventDto eventDto) {
    	log.info("Schedule event: {}",  eventDto);
//    	TODO for develop reasons set start time manually
        LocalDateTime time = LocalDateTime.now().plusSeconds(10);
        Reminder reminder1 = new Reminder(time);

        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
					createReminderDialog(eventDto, reminder1);
				});
            }
        };

        reminder1.setTask(timerTask);
        reminder1.schedule();
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
