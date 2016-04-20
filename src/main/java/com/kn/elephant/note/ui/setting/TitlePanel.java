package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.ui.BasePanel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Kamil Nadłonek on 20-04-2016
 * email:kamilnadlonek@gmail.com
 */
abstract class TitlePanel extends BasePanel {

    TitlePanel() {
        getStyleClass().addAll("content-panel");
        setPadding(new Insets(15)); //WTF ???

    }

    protected void setTitle(String text) {
        BorderPane content = new BorderPane();
        content.getStyleClass().addAll("custom-pane");
        Text title = new Text(text);
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("title");
        content.setCenter(title);
        setMargin(content, new Insets(0, 0, 15, 0));
        setTop(content);
    }

    protected void setContent(Node node) {
        node.getStyleClass().add("custom-pane");
        setCenter(node);
    }
}
