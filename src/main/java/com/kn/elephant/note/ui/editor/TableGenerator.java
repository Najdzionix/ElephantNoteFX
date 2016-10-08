package com.kn.elephant.note.ui.editor;

import static com.kn.elephant.note.ui.UIFactory.createLabel;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil Nadłonek on 08-10-2016 email:kamilnadlonek@gmail.com
 */
public class TableGenerator {
    private static final String TR_TAG = "<tr>";
    private static final String TD_TAG = "<td>#</td>";
    private static final String TR_END_TAG = "</tr>";

    private PopOver popOver;
    private TextField colTF;
    private TextField rowTF;
    private VBox content;
    private Button tableButton;

    public TableGenerator(Button button) {
        popOver = new PopOver();
        popOver.setDetachable(false);
        tableButton = button;
    }

    private void createDialog() {
        content = new VBox();
		content.getStyleClass().add("contentWhite");
        colTF = new TextField();
        rowTF = new TextField();
        content.getChildren().addAll(createLabel("Columns:"), colTF, createLabel("Rows:"), rowTF);

    }

    public void insertTable(Consumer<String> consumerOfTable) {
        createDialog();
        popOver.setOnHidden(event -> {
        	if(!validNumbers()) {
				event.consume();
				return;
			}
            StringBuilder table = new StringBuilder("<table class=\"pure-table pure-table-horizontal pure-table-striped\"> <thead>");
            for (int i = 0; i < Integer.parseInt(rowTF.getText()); i++) {
                table.append(TR_TAG);
                for (int j = 0; j < Integer.parseInt(colTF.getText()); j++) {
                    table.append(TD_TAG);
                }
                table.append(TR_END_TAG);
                if (i == 0) {
                    table.append("</thead><tbody>");
                }
            }
            table.append("</tbody></table>");
            consumerOfTable.accept(table.toString());
        });
        popOver.setContentNode(content);
        popOver.show(tableButton);
    }

    private boolean validNumbers() {
		return StringUtils.isNumeric(rowTF.getText()) && StringUtils.isNumeric(colTF.getText());
	}

}
