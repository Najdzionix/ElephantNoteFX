package com.kn.elephant.note.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kn.elephant.note.model.Interval;
import com.kn.elephant.note.utils.JsonParser;

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
	private Interval repeat;
	private List<EventContentDto> content;
	private Boolean done;

    public String getJsonBody() {
        return JsonParser.serializeToJsonString(content);
    }

    public EventDto setContent(String content) {
    	if(StringUtils.isNoneEmpty()) {
			this.content = JsonParser.unmarshallJSON(new TypeReference<List<EventContentDto>>() {
			}, content);
		} else {
    		this.content = Collections.emptyList();
		}
        return this;
    }

    public EventDto setRepeat(String interval) {
        if (interval != null) {
            this.repeat = Interval.valueOf(interval);
        }
        return this;
    }
}
