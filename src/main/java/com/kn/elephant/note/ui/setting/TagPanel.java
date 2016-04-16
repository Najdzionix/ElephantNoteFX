package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.ui.BasePanel;
import javafx.scene.text.Text;

/**
 * Created by Kamil Nadłonek on 16-04-2016
 * email:kamilnadlonek@gmail.com
 */
public class TagPanel extends BasePanel {

    public TagPanel() {
        getStyleClass().add("custom-panel");
        setCenter(new Text("TODO: Tags panel"));
    }
}
