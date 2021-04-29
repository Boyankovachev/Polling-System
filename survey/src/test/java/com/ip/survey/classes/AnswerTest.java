package com.ip.survey.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {
    Answer answer;

    AnswerTest(){
        answer = new Answer();
    }

    @Test
    void implementTimesSelected() {
        answer.setTimesSelected(1);
        answer.implementTimesSelected();
        assertEquals(answer.getTimesSelected(), 2);
    }
}