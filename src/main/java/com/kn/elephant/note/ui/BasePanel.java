package com.kn.elephant.note.ui;

import com.gluonhq.ignite.guice.GuiceContext;
import com.kn.elephant.note.service.ElephantModule;
import javafx.scene.layout.BorderPane;

import java.util.Collections;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class BasePanel extends BorderPane {

    public BasePanel() {
        super();
        GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));
        context.init();
    }
}
