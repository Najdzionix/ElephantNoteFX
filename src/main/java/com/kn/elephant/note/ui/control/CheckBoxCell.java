package com.kn.elephant.note.ui.control;

/**
 * Created by Kamil Nadłonek on 22-03-2017
 * email:kamilnadlonek@gmail.com
 */
public interface CheckBoxCell<T> {

    void setCheck(boolean isCheck);

    void setContent(String content);

    boolean isCheck();

    String getContent();
}
