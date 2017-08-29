package com.kn.elephant.note.ui.event;

import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.ui.BasePanel;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class ListEvents extends BasePanel {

	private static final String ACTIVE_CLASS = "active";
	@Inject
	private EventService  eventService;
	private List<Button> allEventNodes;
	private VBox menuBox;
	private EventPanel eventPanel;
	private List<EventDto> allEvents;

	public ListEvents(EventPanel eventPanel){
		ActionMap.register(this);
		this.eventPanel = eventPanel;

		createContent();
		setActiveEvent(null);
		getStyleClass().addAll("menu-left", "events");
	}

	private void createContent() {
		menuBox = new VBox();
		menuBox.getStyleClass().add("menu-panel");
		allEvents = eventService.getAllEvents();
		allEventNodes = allEvents.stream().filter(e -> !e.getDeleted()).map(this::createEvent).collect(Collectors.toList());
		menuBox.getChildren().addAll(allEventNodes);

		AnchorPane borderPane = new AnchorPane();
		borderPane.getChildren().add(menuBox);
		AnchorPane.setTopAnchor(menuBox, 0.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);
		setCenter(borderPane);
    }

    private void setActiveEvent(EventDto eventDto) {
        if (eventDto != null) {
            eventPanel.loadEvent(eventDto);
            allEventNodes.forEach(node -> {
                if (eventDto.getId().toString().equals(node.getId())) {
                    node.getStyleClass().add(ACTIVE_CLASS);
                }
            });
        } else {
        	eventPanel.loadEvent(allEvents.get(0));
        	allEventNodes.get(0).getStyleClass().add(ACTIVE_CLASS);
		}
    }

    private Button createEvent(EventDto eventDto) {
        Button button = new Button(eventDto.getName());
        button.setId(eventDto.getId().toString());
        button.setAlignment(Pos.CENTER);
        button.getStyleClass().addAll("menu-cell", "event-button");
        button.setOnAction(event -> {
            eventPanel.loadEvent(eventDto);
            allEventNodes.forEach(node -> node.getStyleClass().removeAll(ACTIVE_CLASS));
            button.getStyleClass().add(ACTIVE_CLASS);
        });
        return button;
    }

    @ActionProxy(text = "")
    private void refreshEventList(ActionEvent event) {
        log.info("Refresh events list");
        createContent();
		if (event.getSource() instanceof  EventDto) {
			EventDto eventDto = (EventDto) event.getSource();
			setActiveEvent(eventDto);
		} else {
			setActiveEvent(null);
		}
	}
}
