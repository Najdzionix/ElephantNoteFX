package com.kn.elephant.note.ui.editor;

import static com.kn.elephant.note.ui.UIFactory.createLabel;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;

import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.validator.Validator;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 08-10-2016 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TableGenerator {
    private static final String TR_TAG = "<tr>";
    private static final String TD_TAG = "<td>#</td>";
    private static final String TR_END_TAG = "</tr>";
    private static final String VALIDATION_MESSAGE = "Given string is not number or number is to big. Max value is: "
            + NoteConstants.MAX_NUMBER_OF_COLUMNS_AND_ROWS;

    private PopOver popOver;
    private TextField colTF;
    private TextField rowTF;
    private VBox content;
    private Button tableButton;

    private ValidatorHelper validatorHelper;

    public TableGenerator(Button button) {
        popOver = new PopOver();
        popOver.setDetachable(false);
        tableButton = button;
        validatorHelper = new ValidatorHelper();
    }

    private void createDialog() {
        content = new VBox();
        content.getStyleClass().add("contentWhite");
        colTF = new TextField();
        rowTF = new TextField();
        content.getChildren().addAll(createLabel("Columns:"), colTF, createLabel("Rows:"), rowTF);

        Validator numberValidator = node -> {
            String text = ((TextField) node).getText();
            return StringUtils.isNotEmpty(text) && StringUtils.isNumeric(text) && Integer.parseInt(text) > -1
                    && Integer.parseInt(text) < NoteConstants.MAX_NUMBER_OF_COLUMNS_AND_ROWS;
        };
        validatorHelper.registerCustomValidatorChangeText(colTF, VALIDATION_MESSAGE, numberValidator);
        validatorHelper.registerCustomValidatorChangeText(rowTF, VALIDATION_MESSAGE, numberValidator);
        colTF.setText("");
        rowTF.setText("");
    }

    public void insertTable(Consumer<String> consumerOfTable) {
        createDialog();
        popOver.setOnHidden(event -> {
            log.info("KAMILLLLL" + validatorHelper.isValid());
            if (!validatorHelper.isValid()) {
                event.consume();
                ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Insert table failed." + VALIDATION_MESSAGE));
                validatorHelper.removeAllNodes();
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
            validatorHelper.removeAllNodes();
            consumerOfTable.accept(table.toString());
        });
        popOver.setContentNode(content);
        popOver.show(tableButton);
    }
}
