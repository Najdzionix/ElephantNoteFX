package com.kn.elephant.note.ui.event;

import java.util.List;
import java.util.stream.Collectors;

import com.kn.elephant.note.dto.EventContentDto;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.ui.setting.TitlePanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class EventPanel extends TitlePanel {

    protected void loadEvent(EventDto eventDto) {
        setTitle(eventDto.getName());
        setContent(createContent(eventDto));
    }

    private Node createContent(EventDto eventDto) {
        HBox box = new HBox();
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
}
