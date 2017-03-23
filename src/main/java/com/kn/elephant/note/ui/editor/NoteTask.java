package com.kn.elephant.note.ui.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kn.elephant.note.ui.control.CheckBoxCell;

import lombok.Data;

/**
 * Created by Kamil Nadłonek on 20-03-2017
 * email:kamilnadlonek@gmail.com
 */
@Data
@JsonPropertyOrder({ "content", "isDone" })
public final class NoteTask implements CheckBoxCell<NoteTask> {
    private String content;
    private boolean isDone;

    @Override
    @JsonIgnore
    public void setCheck(boolean isCheck) {
        isDone = isCheck;
    }

    @Override
    @JsonIgnore
    public boolean isCheck() {
        return isDone();
    }
}
