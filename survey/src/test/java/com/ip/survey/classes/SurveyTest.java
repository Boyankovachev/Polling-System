package com.ip.survey.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurveyTest {

    Survey survey;

    SurveyTest(){
        survey = new Survey();
    }

    @Test
    void getQuestionById() {
        List<Question> questionList = new ArrayList<>();
        Question question1 = new Question();
        Question question2 = new Question();
        Question question3 = new Question();
        question1.setQuestionText("question1 text");
        question2.setQuestionText("question2 text");
        question3.setQuestionText("question3 text");
        question1.setQuestionId(1);
        question2.setQuestionId(2);
        question3.setQuestionId(3);
        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        survey.setQuestions(questionList);

        assertEquals("question1 text", survey.getQuestionById(1).getQuestionText());
        assertEquals("question2 text", survey.getQuestionById(2).getQuestionText());
        assertEquals("question3 text", survey.getQuestionById(3).getQuestionText());
    }
}
