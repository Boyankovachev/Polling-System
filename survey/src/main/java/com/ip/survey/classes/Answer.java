package com.ip.survey.classes;

import javax.persistence.*;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name="answer_id" )
    private int answerId;

    @Column( name="question_id" )
    private int questionId;

    @Column( name="answer_text" )
    private String answerText;

    @Column( name="times_selected")
    private int timesSelected;

    public Answer(){}

    public Answer(int questionId, String answerText, int timesSelected){
        this.questionId = questionId;
        this.answerText = answerText;
        this.timesSelected = timesSelected;
    }

    //Getters and setters


    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getTimesSelected() {
        return timesSelected;
    }

    public void setTimesSelected(int timesSelected) {
        this.timesSelected = timesSelected;
    }

    public void implementTimesSelected(){
        timesSelected = timesSelected + 1;
    }

    public void printAnswer(){
        System.out.println("    Answer: " + answerText);
        System.out.println("    Answer Id: " + answerId);
        System.out.println("    Question Id: " + questionId);
        System.out.println("    Times Chosen: " + timesSelected);
    }
}
