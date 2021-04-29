CREATE DATABASE IF NOT EXISTS survey;

use survey;

CREATE TABLE IF NOT EXISTS user(
	user_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
	username VARCHAR(32) NOT NULL,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS survey(
    survey_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
	user_id INT NOT NULL,
	name VARCHAR(64),
    is_open BOOL,
    survey_url TEXT,
    survey_result_url TEXT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS question(
    question_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
	survey_id INT NOT NULL,
	question_text TEXT,
    multiple_answers BOOL,
    is_manditory BOOL,
    image_name VARCHAR(64),
    FOREIGN KEY (survey_id) REFERENCES survey(survey_id)
);

CREATE TABLE IF NOT EXISTS answer(
    answer_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
	question_id INT NOT NULL,
	answer_text TEXT,
    times_selected INT,
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);

