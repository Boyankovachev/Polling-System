package com.ip.survey.myservice;

import com.ip.survey.dto.UserRegisterDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

class MyServiceTest {

    MyService myService;

    MyServiceTest(){
        myService = new MyService();
    }

    @Test
    void registerUser() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setPassword("1234");
        userRegisterDto.setPassword2("2345");
        userRegisterDto.setUsername("some name");
        assertEquals("Passwords don't match!", myService.registerUser(userRegisterDto));
    }

}
