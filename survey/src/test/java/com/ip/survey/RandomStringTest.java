package com.ip.survey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomStringTest {

    @Test
    void generateRandomString() {
        assertEquals(32, RandomString.generateRandomString().length());
    }
}