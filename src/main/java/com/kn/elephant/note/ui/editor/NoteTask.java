package com.kn.elephant.note.ui.editor;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by Kamil Nadłonek on 20-03-2017
 * email:kamilnadlonek@gmail.com
 */
@Data
@JsonPropertyOrder({ "task", "isDone" })
public final class NoteTask  {
	private String task;
	private boolean isDone;
}
