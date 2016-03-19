package com.kn.elephant.note.utils.validator;

import javafx.scene.Node;

/**
 * Created by Kamil Nadłonek on 19.03.16.
 * email:kamilnadlonek@gamil.com
 */

@FunctionalInterface
public interface Validator {

    boolean validate(Node node);
}
