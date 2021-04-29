package com.ip.survey.database;

import com.ip.survey.classes.Survey;
import com.ip.survey.classes.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SurveyRepository extends CrudRepository<Survey, Integer>{
    Optional<Survey> findBySurveyUrl(String surveyUrl);
    Optional<Survey> findByResultsUrl(String resultsUrl);
    Iterable<Survey> findAllByOwnerId(int ownerId);
}


