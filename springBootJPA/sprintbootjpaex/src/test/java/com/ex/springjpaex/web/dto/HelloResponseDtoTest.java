package com.ex.springjpaex.web.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelloResponseDtoTest {

    @Test
    public void 롬복() throws Exception{
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto helloResponseDto = new HelloResponseDto(name, amount);


        //then
        assertEquals(helloResponseDto.getName(), name);
        assertEquals(helloResponseDto.getAmount(), amount);
    }



}