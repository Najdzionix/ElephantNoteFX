package com.kn.elephant.note.ui.event;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.ui.BasePanel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class ListEvents extends BasePanel {

	private static final String ACTIVE_CLASS = "active";
	@Inject
	private EventService  eventService;
	private List<Node> allEventNodes;
	private VBox menuBox;
	private EventPanel eventPanel;

	public ListEvents(EventPanel eventPanel){
		this.eventPanel = eventPanel;
		createContent();
		getStyleClass().addAll("menu-left", "events");
	}

	private void createContent() {
		menuBox = new VBox();
		menuBox.getStyleClass().add("menu-panel");
		List<EventDto> allEvents = eventService.getAllEvents();
		allEventNodes = allEvents.stream().map(this::createEvent).collect(Collectors.toList());
		menuBox.getChildren().addAll(allEventNodes);

		AnchorPane borderPane = new AnchorPane();
		borderPane.getChildren().add(menuBox);
		AnchorPane.setTopAnchor(menuBox, 0.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);

		setCenter(borderPane);
        eventPanel.loadEvent(allEvents.get(0));
    }

    private Node createEvent(EventDto eventDto) {
        Button button = new Button(eventDto.getName());
        button.getStyleClass().add("menu-cell");
        button.setOnAction(event -> {
            eventPanel.loadEvent(eventDto);
            allEventNodes.forEach(node -> node.getStyleClass().removeAll(ACTIVE_CLASS));
            button.getStyleClass().add(ACTIVE_CLASS);
        });
        return button;
    }
}
