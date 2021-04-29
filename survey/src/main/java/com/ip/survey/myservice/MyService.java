package com.ip.survey.myservice;

import com.ip.survey.RandomString;
import com.ip.survey.classes.Answer;
import com.ip.survey.classes.Question;
import com.ip.survey.classes.Survey;
import com.ip.survey.classes.User;
import com.ip.survey.database.AnswerRepository;
import com.ip.survey.database.QuestionRepository;
import com.ip.survey.database.SurveyRepository;
import com.ip.survey.database.UserRepository;
import com.ip.survey.dto.UserRegisterDto;
import com.ip.survey.image.ImageService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.*;

@Service
public class MyService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    public MyService(){}

    public String registerUser(UserRegisterDto userRegisterDto){
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getPassword2())){
            return "Passwords don't match!";
        }

        if(userRepository.findByUsername(userRegisterDto.getUsername()).isPresent()){
            return "Username taken!";
        }else {
            User user = new User(userRegisterDto.getUsername(), userRegisterDto.getPassword());
            userRepository.save(user);
        }
        return "success";
    }

    public User setupUser(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            Iterable<Survey> surveyIterable = surveyRepository.findAllByOwnerId(user.getUserId());
            List<Survey> surveyList = new ArrayList<>();
            surveyIterable.forEach(surveyList::add);
            user.setSurveys(surveyList);
            return user;
        }
        else {
            //Throw exception and redirect to login screen
            return null;
        }
    }

    public HashMap<String, String> createSurvey(JSONObject jsonObject, User user){
        //izlgeshda mnogo grozno tazi funkciq ama raboti
        Survey survey = new Survey();
        survey.setOwnerId(user.getUserId());
        survey.setOpen(true);
        HashMap<String, String> surveyUrls = generateSurveyUrls();
        survey.setSurveyUrl(surveyUrls.get("survey"));
        survey.setResultsUrl(surveyUrls.get("results"));
        survey.setName(jsonObject.getString("surveyName"));
        survey = surveyRepository.save(survey);

        List<Question> questions = new ArrayList<>();
        for(int i=0; i<jsonObject.getJSONArray("questions").length(); i++){

            JSONObject questionJson = jsonObject.getJSONArray("questions").getJSONObject(i);
            Question question = new Question();

            question.setSurveyId(survey.getSurveyId());
            question.setQuestionText(questionJson.getString("questionText"));
            question.setImageName(questionJson.getString("imageName"));
            if(questionJson.getString("isQuestionMandatory").equals("yes")) {
                question.setMandatory(true);
            }else {
                question.setMandatory(false);
            }
            if(questionJson.getString("areMultipleAnswersPossible").equals("yes")){
                question.setMultipleAnswerAllowed(true);
            }else {
                question.setMultipleAnswerAllowed(false);
            }

            question = questionRepository.save(question);

            List<Answer> answers = new ArrayList<>();
            Iterator<String> keys = questionJson.getJSONObject("answers").keys();
            while (keys.hasNext()){
                String key = keys.next();
                Answer answer = new Answer(question.getQuestionId(), questionJson.getJSONObject("answers").getString(key), 0);
                answer = answerRepository.save(answer);
                answers.add(answer);
            }
            question.setAnswers(answers);

            questions.add(question);
        }
        survey.setQuestions(questions);

        HashMap<String, String> response = new HashMap<>();
        response.put("surveyUrl", survey.getSurveyUrl());
        response.put("resultsUrl", survey.getResultsUrl());
        return response;
        //survey.printSurvey();
    }

    private HashMap<String, String> generateSurveyUrls(){

        String surveyUrl;
        do{
            surveyUrl = RandomString.generateRandomString();
        }while (surveyRepository.findBySurveyUrl(surveyUrl).isPresent());

        String resultsUrl;
        do{
            resultsUrl = RandomString.generateRandomString();
        }while (surveyRepository.findByResultsUrl(resultsUrl).isPresent());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("survey", surveyUrl);
        hashMap.put("results", resultsUrl);
        return hashMap;
    }

    public Survey getSurveyByUrl(String url){

        Optional<Survey> surveyOptional = surveyRepository.findBySurveyUrl(url);

        if(surveyOptional.isPresent()){
            Survey survey = surveyOptional.get();
            return setupSurveyObject(survey);
        }

        // if survey not found
        return null;
    }

    public Survey getSurveyByResultsUrl(String url) {
        Optional<Survey> surveyOptional = surveyRepository.findByResultsUrl(url);

        if(surveyOptional.isPresent()){
            Survey survey = surveyOptional.get();
            return setupSurveyObject(survey);
        }

        // if survey not found
        return null;
    }

    private Survey setupSurveyObject(Survey survey){
        Iterable<Question> questions = questionRepository.findAllBySurveyId(survey.getSurveyId());
        List<Question> questionList = new ArrayList<>();
        ImageService temp = new ImageService();
        questions.forEach(
                question -> {
                    Iterable<Answer> answersIterable = answerRepository.findAllByQuestionId(question.getQuestionId());
                    List<Answer> answerList = new ArrayList<>();
                    answersIterable.forEach(answerList::add);
                    question.setAnswers(answerList);
                    question.setImageFileName(temp.getFullImageName(question.getImageName()));

                    questionList.add(question);
                }
        );
        survey.setQuestions(questionList);

        //survey.printSurvey();
        return survey;
    }

    public void appendVotes(JSONObject jsonObject){

        Survey survey = getSurveyByUrl(jsonObject.getString("surveyUrl"));
        survey.printSurvey();

        for(int i=0; i<jsonObject.getJSONArray("questions").length(); i++) {

            JSONObject questionJson = jsonObject.getJSONArray("questions").getJSONObject(i);
            Question question = survey.getQuestionById(questionJson.getInt("questionId"));

            Iterator<String> answerIds = questionJson.getJSONObject("answers").keys();
            while (answerIds.hasNext()){
                String answerId = answerIds.next();
                if(questionJson.getJSONObject("answers").getInt(answerId) == 1) {
                    Answer answer = question.getAnswerById(Integer.parseInt(answerId));
                    answer.implementTimesSelected();
                    answerRepository.save(answer);
                }
            }
        }

        Survey survey2 = getSurveyByUrl(jsonObject.getString("surveyUrl"));
        System.out.println("\n\n\n\n");
        survey2.printSurvey();

    }

    public HttpStatus switchSurveyOpenById(int surveyId, boolean status){
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        if(surveyOptional.isPresent()){
            Survey survey = surveyOptional.get();
            survey.setOpen(status);
            surveyRepository.save(survey);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
