package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.ChangeValue;
import com.kn.elephant.note.ui.View;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.action.ActionMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kn.elephant.note.ui.setting.MenuCell.ACTIVE_CSS_CLASS;

/**
 * Created by Kamil Nadłonek on 14-04-2016.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class LeftMenuPanel extends BasePanel implements ChangeValue<View> {
    private VBox menuBox;
    private List<MenuCell> menuCells = new ArrayList<>();

    public LeftMenuPanel() {
        ActionMap.register(this);
        getStyleClass().addAll("menu-left");
        setCenter(getListButtons());
    }

    private javafx.scene.Node getListButtons() {
        menuBox = new VBox();
        menuBox.getStyleClass().add("menu-panel");
        MenuCell mainButton = new MenuCell("Main", View.MAIN);
        MenuCell settingsButton = new MenuCell("Settings", View.SETTINGS);
        MenuCell tags = new MenuCell("Tags", View.TAG);
        MenuCell about = new MenuCell("About", View.ABOUT);
        menuBox.getChildren().addAll(mainButton, settingsButton, tags, about);
        Collections.addAll(menuCells, mainButton, settingsButton, tags, about);

        AnchorPane borderPane = new AnchorPane();
        borderPane.getChildren().add(menuBox);
        AnchorPane.setTopAnchor(menuBox, 0.0);
        AnchorPane.setBottomAnchor(menuBox, 0.0);
        return borderPane;
    }

    @Override
    public void changeValue(View oldValue, View newValue) {
        menuCells.parallelStream().forEach(menuCell -> {
            menuCell.getStyleClass().remove(ACTIVE_CSS_CLASS);
            if (newValue.equals(menuCell.getView())) {
                menuCell.getStyleClass().add(ACTIVE_CSS_CLASS);
            }
        });

    }
}
