package com.example.mongochat.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMappingUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private ObjectMappingUtils() {}
    
    public static <T> T getObject(final String message, Class<T> valueType){
        try {
            return objectMapper.readValue(message, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> String getString(final T message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
}