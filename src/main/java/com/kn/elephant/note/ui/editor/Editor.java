package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.utils.cache.NoteCache;

/**
 * Created by Kamil Nadłonek on 19-03-2017
 * email:kamilnadlonek@gmail.com
 */
public interface Editor {
	void loadNote(NoteDto noteDto);

	String getContent();

	void setNoteCache(NoteCache cache);
}
