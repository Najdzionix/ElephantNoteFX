package com.kn.elephant.note.service;

import com.google.inject.AbstractModule;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class ElephantModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Test.class).to(TestService.class);
    }
}
