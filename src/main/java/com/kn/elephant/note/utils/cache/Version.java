package com.kn.elephant.note.utils.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
	private Integer version;

	public Version(final long noteId) {
		this.noteId = noteId;
		version = 0;
	}

	public void increaseVersion(){
		this.version++;
	}

}