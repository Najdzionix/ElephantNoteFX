package com.kn.elephant.note.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */

@Accessors(chain = true)
@Data
public class EventDto {

	private Long id;
	private LocalDateTime startDate;
	private String name;
	private Long repeat;
	// TODO parse body ??
	private String body;
	private Boolean done;
}
