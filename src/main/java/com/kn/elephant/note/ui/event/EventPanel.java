package com.kn.elephant.note.ui.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.kn.elephant.note.Main;
import com.kn.elephant.note.dto.EventContentDto;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.model.Interval;
import com.kn.elephant.note.service.EventService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.UIFactory;
import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
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

	protected void loadEvent(EventDto eventDto) {
		Button addEvent = new Button();
		Icons.addIcon(MaterialDesignIcon.PLUS_CIRCLE, addEvent, "2.5em");
		BorderPane box = new BorderPane();
        Label name = new Label(eventDto.getName());
        addEvent.setOnAction(event -> {
            Dialog<EventDto> dialog = addEventDialog();
            Optional<EventDto> newEventDto = dialog.showAndWait();
            if (newEventDto.isPresent()) {
                log.info("Save new event: {}", newEventDto);
                eventService.saveEvent(newEventDto.get());
            }
        });
		box.getStyleClass().addAll("custom-pane");
		box.setCenter(name);
		box.setRight(addEvent);
		setTop(box);
        setCenter(createContent(eventDto));
    }

    private Node createContent(EventDto eventDto) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.getStyleClass().addAll("custom-pane");
        List<Node> collect = eventDto.getContent().stream().map(this::create).collect(Collectors.toList());
        box.getChildren().addAll(collect);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    private Node create(EventContentDto dto) {
        HBox box = new HBox();
        Label date = new Label(dto.getDate().toString());
        Label text = new Label(dto.getContent());
        box.getChildren().addAll(date, text);

        return box;
    }


    private Dialog<EventDto> addEventDialog() {
		Dialog<EventDto> dialog = new Dialog<>();
		dialog.setTitle("Create event wizard");
		dialog.setHeaderText("Please give name and date start event and also if should be repeat....");
		dialog.setResizable(false);
		dialog.getDialogPane().getStyleClass().add("card");

		Label nameLabel = UIFactory.createLabel("Name: ");

		TextField nameTextField = new TextField();
		Platform.runLater(nameTextField::requestFocus);

		VBox box = new VBox();
		box.getChildren().addAll( nameLabel, nameTextField, createStartDate(), repeatEventUI());

		dialog.getDialogPane().setContent(box);

		ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);

		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
		btOk.getStyleClass().add("button-action");
//		btOk.addEventFilter(ActionEvent.ACTION, event -> {
//			if (!validatorHelper.isValid()) {
//				event.consume();
//			}
//		});

		dialog.setResultConverter(buttonType -> {
			if (buttonType == buttonTypeOk) {
				LocalDateTime time = datePicker.getValue().atTime(calendarTextField.getLocalTime());
				EventDto eventDto = new EventDto().setName(nameTextField.getText())
					.setStartDate(time);
				if(repeat.isSelected()) {
					eventDto.setRepeat(intervalUI.getValue());
				}

				return eventDto;
			}
			return null;
		});


		dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
		return dialog;
	}


    private Node repeatEventUI() {
        Label repeatLabel = UIFactory.createLabel("Should repeat?: ");
        repeat = new CheckBox();
        intervalUI = new ComboBox<>();
        intervalUI.setVisible(repeat.isSelected());
        intervalUI.getItems().addAll(Arrays.stream(Interval.values()).map(Enum::toString).collect(Collectors.toList()));
        intervalUI.getSelectionModel().select(0);
        repeat.selectedProperty().addListener((ov, old_val, new_val) -> {
            intervalUI.setVisible(new_val);
        });

        GridPane box = new GridPane();
        box.getChildren().addAll(repeatLabel, repeat, intervalUI);
        GridPane.setConstraints(repeatLabel, 0, 0);
        GridPane.setConstraints(repeat, 1, 0);
        GridPane.setConstraints(intervalUI, 0, 1);
        return box;
    }


	private Node createStartDate() {
		datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());
        Label dateLabel = UIFactory.createLabel("Date: ");
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker1) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(ChronoLocalDate.from(LocalDate.now()))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        Label hourLabel = UIFactory.createLabel("Time: ");
		calendarTextField = new LocalTimeTextField();
		calendarTextField.setLocalTime(LocalTime.now());
		calendarTextField.getStyleClass().add("textFieldTime");
		GridPane pane  = new GridPane();
		pane.setHgap(20);
		calendarTextField.setStyle("-fx-padding: 7 0 0 0;");
		GridPane.setConstraints(dateLabel,0,0);
		GridPane.setConstraints(hourLabel,1,0);
		GridPane.setConstraints(datePicker,0,1);
		GridPane.setConstraints(calendarTextField,1,1);

		pane.getChildren().addAll(dateLabel, datePicker, hourLabel, calendarTextField);

        return pane;
	}
}
