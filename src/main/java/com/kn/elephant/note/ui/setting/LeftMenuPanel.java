package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.View;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import static com.kn.elephant.note.ui.setting.MenuCell.ACTIVE_CSS_CLASS;

/**
 * Created by Kamil Nadłonek on 14-04-2016.
 * email:kamilnadlonek@gmail.com
 */
public class LeftMenuPanel extends BasePanel {
    private VBox menuBox;
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
        AnchorPane borderPane = new AnchorPane();
        borderPane.getChildren().add(menuBox);
        AnchorPane.setTopAnchor(menuBox, 0.0);
        AnchorPane.setBottomAnchor(menuBox, 0.0);
        return  borderPane;
    }
    @ActionProxy(text = "")
    private void clearMenuCell() {
        menuBox.getChildren().parallelStream().forEach(node -> node.getStyleClass().remove(ACTIVE_CSS_CLASS));
    }
}
