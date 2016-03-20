package com.kn.elephant.note.utils.validator;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil Nadłonek on 19.03.16.
 * email:kamilnadlonek@gmail.com
 */
public class ValidatorHelper {
    private static final String ERROR_CSS = "error";
    private Map<Node, Boolean> fields = new HashMap<>();

    public void registerEmptyValidator(Node node, String message) {
        if (node instanceof TextField) {
            TextField tf = ((TextField) node);
            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                setValidationResult(tf, message, StringUtils.isNotEmpty(tf.getText()));
            });

            tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
//                    loses focus
                if (!newValue) {
                    setValidationResult(tf, message, StringUtils.isNotEmpty(tf.getText()));
                }
            });
        }

        fields.put(node, true);
    }

    /**
     * Register provide by user validator and it is fire after lose focus on node.
     *
     * @param node Node to validation
     * @param message Error message
     * @param validator Object which validate node
     */
    public void registerCustomValidator(Node node, String message, Validator validator) {
        node.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                boolean isValid = validator.validate(node);
                setValidationResult(node, message, isValid);
            }
        });
    }

    private void setValidationResult(Node node, String message, boolean isValid) {
        if (!isValid) {
            Tooltip tip = new Tooltip(message);
            tip.getStyleClass().add("tooltipError");
            Tooltip.install(node, tip);
            node.getStyleClass().add(ERROR_CSS);
            fields.put(node, false);
        } else {
            Tooltip.uninstall(node, null);
            node.getStyleClass().remove(ERROR_CSS);
            fields.put(node, true);
        }
    }

    public boolean isValid() {
        long count = fields.values().stream().filter(b -> !b).count();
        return count == 0;
    }

}
