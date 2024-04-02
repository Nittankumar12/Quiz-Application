package com.Railworld.quizservice.service;

import com.Railworld.quizservice.dto.QuizDto;
import com.Railworld.quizservice.dao.QuizDao;
import com.Railworld.quizservice.feign.QuizInterface;
import com.Railworld.quizservice.model.QuestionWrapper;
import com.Railworld.quizservice.model.Quiz;
import com.Railworld.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuizInterface quizInterface;
    public ResponseEntity<String> createQuiz(QuizDto quizDto) {
        System.out.println("Called createQuiz");

        List<Integer> questions = quizInterface.getQuestionsForQuiz(quizDto.getCategory(), quizDto.getNumQ()).getBody();
        System.out.println("questionIds generated");
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
//
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionsForUser = quizInterface.getQuestionsFromId(questionIds);
       return questionsForUser;
    }
//
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
       ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
