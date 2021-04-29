package com.ip.survey.image;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    ImageService imageService;

    ImageServiceTest(){
        imageService = new ImageService();
    }

    @Test
    void testGetFullImageName(){
        assertEquals("1.png", imageService.getFullImageName("1"));
        assertEquals("2.jpg", imageService.getFullImageName("2"));
        assertEquals("", imageService.getFullImageName("blablablablagwg"));
    }
}