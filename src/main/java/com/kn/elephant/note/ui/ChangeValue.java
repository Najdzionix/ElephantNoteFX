package com.kn.elephant.note.ui;

/**
 * Created by Kamil Nadłonek on 5-4-2016.
 * email:kamilnadlonek@gmail.com
 */
@FunctionalInterface
public interface ChangeValue<T> {

    void changeValue(T oldValue, T newValue);
}
