package com.kn.elephant.note.utils;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kn.elephant.note.ui.editor.NoteTask;

/**
 * Created by Kamil Nadłonek on 20-03-2017
 * email:kamilnadlonek@gmail.com
 */
public class JsonParserTest {

	@Test
	public void shouldConvertJsonString() {
		// Given
		NoteTask task = new NoteTask();
		task.setDone(true);
		task.setContent("Change test!!");
		List<NoteTask> tasks = Collections.singletonList(task);
		// When
		String result = JsonParser.serializeToJsonString(tasks);
		// Then
		Assert.assertEquals("[{\"content\":\"Change test!!\",\"id\":\""+task.getId() +"\",\"done\":true}]", result);
	}
	
	@Test
	public void shouldUnmarshallingJsonStringToObject() {
		// Given
		String tasksString = "[{\"content\":\"Simple test!!\",\"done\":true, \"id\":\"testId\"}]";
		// When
		List<NoteTask> tasks = JsonParser.unmarshallJSON(new TypeReference<List<NoteTask>>() {}, tasksString);
		// Then
		Assert.assertEquals(1, tasks.size());
		Assert.assertEquals("Simple test!!", tasks.get(0).getContent());
		Assert.assertEquals("testId", tasks.get(0).getId());
		Assert.assertTrue(tasks.get(0).isDone());
	}

}