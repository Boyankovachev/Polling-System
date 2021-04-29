package com.ip.survey.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name="survey_id" )
    private int surveyId;

    @Column( name="user_id" )
    private int ownerId;

    @Column( name="name" )
    private String name;

    @Column( name="is_open" )
    private boolean isOpen;

    @Column( name="survey_url" )
    private String surveyUrl;

    @Column( name="survey_result_url" )
    private String resultsUrl;

    //@JsonInclude
    @Transient
    private List<Question> questions = new ArrayList<>();

    public Survey(){}



    //Getters and setters

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public String getSurveyUrl() {
        return surveyUrl;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public void setSurveyUrl(String surveyUrl) {
        this.surveyUrl = surveyUrl;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question getQuestionById(int id){
        for(Question question: questions){
            if(question.getQuestionId() == id){
                return question;
            }
        }
        return null;
    }

    public void printSurvey(){
        System.out.println("-----------Survey------------");
        System.out.println("Name: " + name);
        System.out.println("Owner Id: " + ownerId);
        System.out.println("Survey Id: " + surveyId);
        System.out.println("Is Open: " + isOpen);
        System.out.println("Survey URL: " + surveyUrl);
        System.out.println("Results URL: " + resultsUrl);
        System.out.println("-----------Questions---------");
        for(Question question: questions){
            question.printQuestion();
            System.out.println("---------------------------");
        }
    }
}
