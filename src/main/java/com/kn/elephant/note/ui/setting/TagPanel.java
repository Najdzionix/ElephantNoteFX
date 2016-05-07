package com.kn.elephant.note.ui.setting;

import com.google.inject.Inject;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.control.TagCell;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 16-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TagPanel extends TitlePanel {

    @Inject
    private TagService tagService;
    private List<TagCell> tagCells;

    public TagPanel() {
        setTitle("List tags");
        setContent(createTagPanel());
    }

    private Node createTagPanel() {
        FlowPane content = new FlowPane();
        content.setRowValignment(VPos.CENTER);
        content.setHgap(10);
        content.setVgap(10);

        tagCells = tagService.getAll().parallelStream().map(TagCell::new).collect(Collectors.toList());
        tagCells.parallelStream().forEach(tagCell -> tagCell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    clearCssTagCell();
                    tagCell.getStyleClass().add("active");
                })
        );
        content.getChildren().addAll(tagCells);

        return content;
    }

    private void clearCssTagCell() {
        tagCells.parallelStream().forEach(tagCell -> tagCell.getStyleClass().remove("active"));
    }
}
