package com.kn.elephant.note.ui.setting;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Created by Kamil Nadłonek on 20-04-2016
 * email:kamilnadlonek@gmail.com
 */
public class AboutPanel extends TitlePanel {

    public AboutPanel() {
        setTitle("About");
        setContent(createContent());
    }

    private Node createContent() {
        //TODO
        HBox box = new HBox();
        Text text = new Text("Me name is Kamil Nadłonek from Poland etc... \nTODO");
        box.getChildren().addAll(text);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }
}
