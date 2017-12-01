package com.kn.elephant.note.utils.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Kamil Nadłonek on 21-01-2017
 * email:kamilnadlonek@gmail.com
 */
@EqualsAndHashCode
@ToString
public class Version {

	@Getter
	private long noteId;
	@Getter
	private int version;

	@Getter @Setter
	private boolean isSave;

	public Version(final long noteId) {
		this.noteId = noteId;
		version = 1;
		isSave = true;
	}

    public Version(long noteId, int version) {
        this.noteId = noteId;
        this.version = version;
    }

    public Version increaseVersion() {
        return new Version(noteId, version + 1);
    }
}
