package com.kn.elephant.note.ui.setting;

import com.google.inject.Inject;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.control.TagCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
    private final List<TagCell> tagCells;
    private GridPane contentGridPane;
    private int numberColumns = 0;
    private double cellWidth = 0;
    private final double maxHeight = 350;
    private final double maxWidth = 250;

    public TagPanel() {
        setTitle("List tags");

        tagCells = tagService.getAll().parallelStream().map(tagDto -> new TagCell(tagDto, maxWidth, maxHeight)).collect(Collectors.toList());
        tagCells.stream().forEach(tagCell -> tagCell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            tagCells.stream().filter(tagCell1 -> tagCell1.isShow() && !tagCell1.equals(tagCell)).forEach(tagCell2 -> {tagCell2.hideNotes(); GridPane.clearConstraints(tagCell2);});
            tagCell.showNotes();
            if (!tagCell.isShow()) {
//                tagCell.showNotes();
                GridPane.clearConstraints(tagCell);
            }
            calculateNumberOfColumns(tagCell.isShow());
            log.info("Test" + tagCell.getWidth());
            fillContent();
        }));

        initCellWidth();
        widthProperty().addListener((observable, oldValue, newValue) -> {
            if (getWidth() > 0 && getHeight() > 0) {
                calculateNumberOfColumns(false);
                fillContent();
            }

        });
        contentGridPane = new GridPane();
        contentGridPane.setMinWidth(500);
        contentGridPane.setAlignment(Pos.TOP_LEFT);
        contentGridPane.setHgap(10);
        contentGridPane.setVgap(10);
        contentGridPane.setPadding(new Insets(25, 25, 25, 25));

    }

    private void calculateNumberOfColumns(boolean withExpandCell) {
        double sum = cellWidth + contentGridPane.getHgap();
//        if(withExpandCell) {
//            double diference = maxWidth - cellWidth;
//            numberColumns = (int) Math.floor( (getWidth() - diference) / sum);
//        } else {
        numberColumns = (int) Math.floor(getWidth() / sum);
//        }
    }

    private void initCellWidth() {
        FlowPane tempPane = new FlowPane();
        tempPane.getChildren().addAll(tagCells);
        tempPane.applyCss();
        tempPane.layout();
        log.debug("Init TagCell width:" + tagCells.get(1).getWidth());
        cellWidth = tagCells.get(0).getWidth();
    }

    private void fillContent() {
        contentGridPane.getChildren().clear();
        BorderPane box = new BorderPane();
        box.getStyleClass().add("tag-grid");

        createListNodes();
        box.setCenter(contentGridPane);
        setCenter(box);
    }

    private void createListNodes() {
        int row = 0, col = 0;
        int blockedColumn = -1;
        int columnSpan = 3;
        log.info("Number columns:" + numberColumns + "\ttags" + tagCells.size());
//        for (TagCell tag : tagCells) {
         for(int i=0; i<tagCells.size();i++){
             TagCell tag = tagCells.get(i);
            //skip column with merge rows

            if (col >= numberColumns) {
                col = 0;
                row++;
            }
             if (blockedColumn == col) {
                 log.info("blocked column:" + blockedColumn);
                 col += columnSpan;
                 if (col >= numberColumns) {
                     col = 0;
                     row++;
                 }
             }
            if (tag.isShow()) {
                //TOOD how many row span ?
                GridPane.setRowSpan(tag, 5);
                GridPane.setColumnSpan(tag, columnSpan);
                if((col+columnSpan )> numberColumns) {
                    blockedColumn = 0;
                    contentGridPane.add(tag, 0, row+1);
                    log.info("blocked:" + blockedColumn);
                    continue;
                }   else {
                    blockedColumn = col;
                }
                log.info("blocked:" + blockedColumn);
            }
            log.info(tag + "\t\t\tcol" + col + "\trow" + row);
            contentGridPane.add(tag, col, row);
            if (blockedColumn == col) {
                col += columnSpan;
            } else {
                col++;
            }
        }
    }
}
