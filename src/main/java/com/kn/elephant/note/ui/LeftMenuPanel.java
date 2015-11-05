package com.kn.elephant.note.ui;

import javafx.scene.layout.BorderPane;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class LeftMenuPanel extends BorderPane {

    public LeftMenuPanel(){

        this.setCenter(new TestNode("Left menu ..."));
        setStyle("-fx-background-color: rgba(255,0,0,.25);");

    }

}
