package com.kn.elephant.note;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import com.kn.elephant.note.model.Interval;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 30-08-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SchedulerEventsTest {


    @Test
    public void shouldReturnNextHourWhenDateFromPastLessThenOneHourForHourInterval() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventTime = now.minusSeconds(2);
        Interval interval = Interval.HOUR;
        LocalDateTime expectedTime = eventTime.plusHours(1);
        // When
        LocalDateTime startTime = SchedulerEvents.getStartTime(eventTime, interval);
        // Then
		validateTime(expectedTime, startTime);
    }

	@Test
	public void shouldReturnNextHourWhenDateFromPastMoreThenOneHourForHourInterval() {
		// Given                                        
		LocalDateTime eventTime = LocalDateTime.now().minusMinutes(65);
		Interval interval = Interval.HOUR;
		LocalDateTime expectedTime = LocalDateTime.now().plusHours(1).withMinute(eventTime.getMinute()).withSecond(eventTime.getSecond());
		// When
		LocalDateTime startTime = SchedulerEvents.getStartTime(eventTime, interval);
		// Then
		validateTime(expectedTime, startTime);
	}

	private void validateTime(LocalDateTime expectedTime, LocalDateTime startTime) {
		assertTrue(startTime.isAfter(LocalDateTime.now()));
		assertEquals(expectedTime.withNano(0), startTime.withNano(0));
	}

	@Test
    public void shouldReturnEventTimeWhenIsFromFeatureFor() {
        // Given
        LocalDateTime eventTime = LocalDateTime.now().plusSeconds(35);
        // When
        LocalDateTime startTime = SchedulerEvents.getStartTime(eventTime, Interval.HOUR);
		validateTime(eventTime, startTime);

		LocalDateTime startTimeDay = SchedulerEvents.getStartTime(eventTime, Interval.DAY);
		validateTime(eventTime, startTimeDay);
    }

    @Test
    public void shouldReturnNextDayWhenDateFromPast() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventTime = now.minusMinutes(5);
        Interval interval = Interval.DAY;
        LocalDateTime expectedTime = eventTime.plusDays(1);
        // When
        LocalDateTime startTime = SchedulerEvents.getStartTime(eventTime, interval);
		validateTime(expectedTime, startTime);

		LocalDateTime startTimeDays = SchedulerEvents.getStartTime(eventTime.minusDays(14), interval);
		validateTime(expectedTime, startTimeDays);
	}

    @Test
    public void shouldTest() {
        // Given
        assertTrue(LocalDateTime.now().isAfter(LocalDateTime.now().minusMinutes(10)));
        // When

        // Then
    }

}