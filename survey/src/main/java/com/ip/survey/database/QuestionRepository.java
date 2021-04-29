package com.ip.survey.database;

import com.ip.survey.classes.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    Optional<Question> findBySurveyId(int surveyId);
    Iterable<Question> findAllBySurveyId(int surveyId);
}
