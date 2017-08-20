package com.kn.elephant.note.ui.event;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.ui.BasePanel;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class ListEvents extends BasePanel {

	@Inject
	private EventService  eventService;
	private VBox menuBox;

	public ListEvents(){
		createContent();
		getStyleClass().addAll("menu-left");
	}

	private void createContent() {
		menuBox = new VBox();
		menuBox.getStyleClass().add("menu-panel");
		List<EventDto> allEvents = eventService.getAllEvents();
		List<Label> collect = allEvents.stream().map(eventDto -> new Label(eventDto.getName())).collect(Collectors.toList());
		menuBox.getChildren().addAll(collect);

		AnchorPane borderPane = new AnchorPane();
		borderPane.getChildren().add(menuBox);
		AnchorPane.setTopAnchor(menuBox, 0.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);

		setCenter(borderPane);
	}
}
