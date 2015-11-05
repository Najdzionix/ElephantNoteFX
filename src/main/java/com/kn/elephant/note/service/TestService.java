package com.kn.elephant.note.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class TestService implements Test {
    public static final Logger LOGGER = LogManager.getLogger(TestService.class);

    @Override
    public void hello() {
        LOGGER.info("Hello test service.:)");
    }
}
