package com.kn.elephant.note.ui.event;

import java.util.Optional;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.EventContentDto;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.UIFactory;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.Utils;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.LocalTimeTextField;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class EventPanel extends BasePanel {

    @Inject
    private EventService eventService;

    private DatePicker datePicker;
    private LocalTimeTextField calendarTextField;
    private CheckBox repeat;
    private ComboBox<String> intervalUI;

    public EventPanel(){
        getStyleClass().addAll( "content-pane");
    }

    protected void loadEvent(EventDto eventDto) {
        VBox boxMargin = new VBox();
        boxMargin.getStyleClass().addAll("detailPanel");

        BorderPane box = new BorderPane();
        Label name = UIFactory.createLabel(eventDto.getName());
        name.getStyleClass().add("title");
        box.getStyleClass().addAll("custom-pane");
        box.setCenter(name);
        Node eventInfo = createPanelWithEventInfo(eventDto);
        box.setLeft(eventInfo);
        box.setRight(eventAdminPanel(eventDto));
        boxMargin.getChildren().add(box);
        setTop(boxMargin);
        setCenter(createContent(eventDto));
    }

    private Node createPanelWithEventInfo(EventDto eventDto) {
        Label startDate = UIFactory.createLabel("Start date: " + eventDto.getStartDate().format(Utils.DATE_TIME_FORMATTER));
        Label intervalLabel = UIFactory.createLabel("Interval: " + (eventDto.getRepeat() == null ? "none" : eventDto.getRepeat().toString()));
        VBox panel = new VBox();
        panel.getChildren().addAll(startDate, intervalLabel);
        return panel;
    }

    private Node eventAdminPanel(EventDto eventDto) {
        HBox panel = new HBox();
        panel.setSpacing(5);
        panel.getChildren().addAll(createEventButton(), editEventButton(eventDto), deletedEventButton(eventDto));
        return panel;
    }

    private Node editEventButton(EventDto eventDto) {
        EventDialog eventDialog = new EventDialog(eventDto);
        return createButton(eventDialog, MaterialDesignIcon.PENCIL);
    }

    private Node createEventButton() {
        EventDialog eventDialog = new EventDialog();
        return createButton(eventDialog, MaterialDesignIcon.PLUS_CIRCLE);
    }

    private Node deletedEventButton(EventDto eventDto) {
		Button deletedButton = new Button();
		Icons.addIcon(MaterialDesignIcon.CLOSE_CIRCLE, deletedButton, "1.5em");
		deletedButton.setOnAction( event -> {
		    log.info("Deleted: {}", eventDto);
			eventService.deletedEvent(eventDto.getId());
			ActionFactory.callAction("refreshEventList");
		});

		return deletedButton;
	}


    private Node createButton(EventDialog dialog, GlyphIcons icon) {
        Button button = new Button();
        Icons.addIcon(icon, button, "1.5em");
        button.setOnAction(event -> {
            Optional<EventDto> newEventDto = dialog.showAndWait();
            newEventDto.ifPresent(eventDto -> {
                Optional<EventDto> savedEvent = eventService.saveEvent(eventDto);
                savedEvent.ifPresent(dto -> ActionFactory.callAction("refreshEventList", dto));
            });
        });

        return button;
    }
    
    private Node createContent(EventDto eventDto) {
        TableView<EventContentDto> tableView = new TableView<>();

        TableColumn<EventContentDto, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().format(Utils.DATE_TIME_FORMATTER)));
        TableColumn<EventContentDto, String> descCol = new TableColumn<>("Description");
        descCol.setMinWidth(600);
        descCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getContent()));

        tableView.getColumns().addAll(dateCol, descCol);
        ObservableList<EventContentDto> dtos = FXCollections.observableArrayList(eventDto.getContent());
        tableView.setItems(dtos);
        return tableView;
    }
}
