package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.dto.NoteDto;

/**
 * Created by Kamil Nadłonek on 19-03-2017
 * email:kamilnadlonek@gmail.com
 */
public interface Editor {
	void loadNote(NoteDto noteDto);

	String getContent();
}
