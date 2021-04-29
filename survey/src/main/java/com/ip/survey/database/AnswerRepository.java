package com.ip.survey.database;

import com.ip.survey.classes.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Integer> {
    Optional<Answer> findByAnswerId(int answerId);
    Optional<Answer> findByQuestionId(int questionId);
    Iterable<Answer> findAllByQuestionId(int questionId);
}
