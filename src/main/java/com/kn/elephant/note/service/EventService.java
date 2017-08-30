package com.kn.elephant.note.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.j256.ormlite.dao.Dao;
import com.kn.elephant.note.dto.EventDto;
import com.kn.elephant.note.model.Event;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class EventService extends BaseService {
	private Dao<Event, Long> eventDao;

    protected EventService() {
        eventDao = dbConnection.getDao(Event.class);
    }

    public List<EventDto> getAllEvents() {
        try {
            List<Event> events = eventDao.queryBuilder().orderBy("startDate", true).orderBy("id", false).query();
            log.debug("Found %s events", events.size());
            return events.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error("Can not get events form data base -  error. ", e);
            return Collections.emptyList();
        }

    }

    public Optional<EventDto> saveEvent(EventDto eventDto) {
    	Event event;
		if(eventDto.getId() == null) {
			event = new Event();
		} else {
			try {
				event = eventDao.queryForId(eventDto.getId());
			} catch (SQLException e) {
				log.error(e);
				event = new Event();
			}
		}

		event.setName(eventDto.getName());
		event.setRepeat(eventDto.getRepeat() != null ? eventDto.getRepeat().toString() : null);
		event.setDeleted(eventDto.isDeleted());
		event.setStartDate(eventDto.getStartDate());
		event.setContent(eventDto.getJsonBody());

		try {
			eventDao.createOrUpdate(event);
			return Optional.of(convertToDto(event));
		} catch (SQLException e) {
			log.error(e);
			return Optional.empty();
		}
	}

	public boolean deletedEvent(Long eventId) {
		try {
			Event note = eventDao.queryForId(eventId);
			if (note != null) {
				note.setDeleted(true);
				return eventDao.update(note) == 1;
			} else {
				log.warn(String.format("No found note (id %s)", eventId));
//                todo throw custom exception ...
				return false;
			}
		} catch (SQLException e) {
			log.error("Error database: ", e);
			return false;
		}
	}

    private EventDto convertToDto(Event event) {
        return new EventDto()
			.setName(event.getName())
			.setId(event.getId())
			.setStartDate(event.getStartDate())
			.setContent(event.getContent())
			.setRepeat(event.getRepeat())
			.setDeleted(event.isDeleted());
    }
	
}
