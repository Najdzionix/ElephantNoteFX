package com.kn.elephant.note.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 20-03-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class JsonParser {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> String serializeToJsonString(List<T> tasks) {
        try {
            return mapper.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }



    public static <T> T unmarshallJSON(final TypeReference<T> type, final String jsonPacket) {
        T data = null;
        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return data;
    }

}
