package com.kn.elephant.note.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;


/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class TestNode extends Label {

    public TestNode(String text) {
        super(text);
        setAlignment(Pos.CENTER);
        setPrefSize(200, 200);
    }
}
