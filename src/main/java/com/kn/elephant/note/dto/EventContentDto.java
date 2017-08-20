package com.kn.elephant.note.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */

@Data
@JsonPropertyOrder({ "date", "content" })
public class EventContentDto {
	private LocalDateTime date;
	private String content;

}
