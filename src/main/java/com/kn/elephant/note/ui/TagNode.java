package com.kn.elephant.note.ui;

import com.kn.elephant.note.dto.TagDto;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionUtils;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class TagNode extends GridCell<TagDto> {

    private Text text;
    private Button removeButton;
    private BorderPane box;

    public TagNode(String actionName) {
        Action action = ActionMap.action(actionName);
        action.setGraphic(Icons.REMOVE_TAG);
        removeButton = ActionUtils.createButton(action);
        removeButton.getStyleClass().add("removeButton");
        setGraphic(createContent(actionName));

    }

    @Override
    protected void updateItem(TagDto item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            text.setText(item.getName());
            setGraphic(createContent(item.getName()));
        }

    }

    private Node createContent(String tagValue) {
        box = new BorderPane();
        text = new Text(tagValue);
        text.setWrappingWidth(80);
        text.setTextOrigin(VPos.CENTER);
        box.setCenter(text);
        box.setRight(removeButton);
        return box;
    }


}
