package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.View;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 14-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class MenuCell extends BasePanel {

    static final String ACTIVE_CSS_CLASS = "menu-cell-active";
    private Text textUI;

    MenuCell(String text, View view) {
        getStyleClass().add("menu-cell");

        setOnMouseClicked(event -> {
                    ActionFactory.callAction("clearMenuCell");
                    getStyleClass().add(ACTIVE_CSS_CLASS);
                    ActionFactory.callAction("changeMainView", view);
                }
        );

        textUI = new Text(text);
        textUI.setTextAlignment(TextAlignment.CENTER);
        setCenter(textUI);
    }
}
