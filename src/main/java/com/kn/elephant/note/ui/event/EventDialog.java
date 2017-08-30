package com.kn.elephant.note.ui.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kn.elephant.note.Main;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.model.Interval;
import com.kn.elephant.note.ui.UIFactory;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import javafx.event.ActionEvent;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimeTextField;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 27-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class EventDialog {

    private DatePicker datePicker;
    private LocalTimeTextField timeTextField;
    private CheckBox repeat;
    private ComboBox<String> intervalUI;
    @Getter
    private Dialog<EventDto> dialog;
    private TextField nameTextField;

	private ValidatorHelper validatorHelper;
	private Button createEventButton;

	private EventDto currentEventDto;

	public EventDialog(EventDto eventDto) {
    	this();
    	currentEventDto = eventDto;
        editDialog();
    }

    public EventDialog() {
    	validatorHelper = new ValidatorHelper();
        createDialog();
    }

    public Optional<EventDto> showAndWait(){
		return dialog.showAndWait();
	}

    private void editDialog() {
		dialog.setTitle("Edit event wizard");
		dialog.setHeaderText("Please change values.");
        nameTextField.setText(currentEventDto.getName());
        datePicker.setValue(currentEventDto.getStartDate().toLocalDate());
        timeTextField.setLocalTime(currentEventDto.getStartDate().toLocalTime());
        if (currentEventDto.getRepeat() != null) {
            repeat.setSelected(true);
            intervalUI.setVisible(true);
            intervalUI.getSelectionModel().select(currentEventDto.getRepeat().toString());
        }

		createEventButton.setText("Save");
    }

    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.getDialogPane().getStyleClass().add("card");
        dialog.setTitle("Create event wizard");
        dialog.setHeaderText("Please define event");

        Label nameLabel = UIFactory.createLabel("Name: ");
        nameTextField = new TextField();
//        Platform.runLater(nameTextField::requestFocus);
		nameTextField.requestFocus();
        VBox box = new VBox();
        box.getChildren().addAll(nameLabel, nameTextField, createStartDate(), repeatEventUI());

		ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		createEventButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
		createEventButton.getStyleClass().add("button-action");

		dialog.setResultConverter(getCreatorEventDto(buttonTypeOk));
		dialog.getDialogPane().setContent(box);
		dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
		setUpValidation();
    }

	private Callback<ButtonType, EventDto> getCreatorEventDto(ButtonType buttonTypeOk) {
		return buttonType -> {
			if (buttonType == buttonTypeOk) {
				LocalDateTime time = datePicker.getValue().atTime(timeTextField.getLocalTime());
				log.info("Event time:" + time);
				EventDto eventDto;
				if(currentEventDto == null ) {
					eventDto = new EventDto();
				} else {
					eventDto = currentEventDto;
				}
				eventDto.setName(nameTextField.getText()).setStartDate(time).setDeleted(false);
				if (repeat.isSelected()) {
					eventDto.setRepeat(intervalUI.getValue());
				}
				return eventDto;
			}
			return null;
		};
	}

	private void setUpValidation() {
    	nameTextField.setId(UUID.randomUUID().toString());
		validatorHelper.registerEmptyValidator(nameTextField, "Event name can not empty.", false);
		createEventButton.addEventFilter(ActionEvent.ACTION, event -> {
			if (!validatorHelper.isValid()) {
				event.consume();
			}
		});
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

        datePicker.setDayCellFactory(dayFactoryBlockedPastDay());
        Label hourLabel = UIFactory.createLabel("Time: ");
        timeTextField = new LocalTimeTextField();
        timeTextField.setLocalTime(LocalTime.now());
        timeTextField.getStyleClass().add("textFieldTime");
		GridPane pane = new GridPane();
		pane.setHgap(20);
        GridPane.setConstraints(dateLabel, 0, 0);
        GridPane.setConstraints(hourLabel, 1, 0);
        GridPane.setConstraints(datePicker, 0, 1);
        GridPane.setConstraints(timeTextField, 1, 1);

        pane.getChildren().addAll(dateLabel, datePicker, hourLabel, timeTextField);

        return pane;
    }

    /**
     * Disable day in past. Only active day since today
     * 
     * @return DayCellFactory
     */
    private Callback<DatePicker, DateCell> dayFactoryBlockedPastDay() {
        return new Callback<DatePicker, DateCell>() {
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
    }
}
