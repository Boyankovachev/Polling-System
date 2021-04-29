package com.ip.survey.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name="question_id" )
    private int questionId;

    @Column( name="survey_id" )
    private int surveyId;

    @Column( name="question_text" )
    private String questionText;

    @Column( name="multiple_answers" )
    private boolean isMultipleAnswerAllowed;

    @Column( name="is_manditory" )
    private boolean isMandatory;

    @Column( name="image_name" )
    private String imageName;

    //@JsonInclude
    @Transient
    private List<Answer> answers = new ArrayList<>();

    @Transient
    private String imageFileName;

    public Question(){}



    //Getters and setters

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public boolean isMultipleAnswerAllowed() {
        return isMultipleAnswerAllowed;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void setMultipleAnswerAllowed(boolean multipleAnswerAllowed) {
        isMultipleAnswerAllowed = multipleAnswerAllowed;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getAnswerById(int id){
        for(Answer answer: answers){
            if(answer.getAnswerId() == id){
                return answer;
            }
        }
        return null;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public void printQuestion(){
        System.out.println("  Question Text: " + questionText);
        System.out.println("  Question Id: " + questionId);
        System.out.println("  Survey Id: " + surveyId);
        System.out.println("  Is Multiple Answers Allowed: " + isMultipleAnswerAllowed);
        System.out.println("  Is Mandatory: " + isMandatory);
        System.out.println("  Image Name: " + imageName);
        for(Answer answer: answers){
            answer.printAnswer();
        }
    }

}
