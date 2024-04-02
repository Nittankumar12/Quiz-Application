package com.Railworld.questionservice.service;


import com.Railworld.questionservice.dao.QuestionDao;
import com.Railworld.questionservice.model.Question;
import com.Railworld.questionservice.model.QuestionWrapper;
import com.Railworld.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions(){
        try{
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(questionDao.findAll(),HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<List<Question>>  getAllQuestionsByCategory(String category) {
        try{
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<Question> getQuestionById(int id) {
        Optional<Question> questionOptional = questionDao.findById(id);
        if(questionOptional.isPresent()) {
            return new ResponseEntity<>(questionOptional.get(),HttpStatus.OK);
        } else {
            System.out.println("Question not found for id: " + id);
            return new ResponseEntity<>(questionOptional.get(),HttpStatus.BAD_REQUEST);
    }
    }

    public ResponseEntity<String> addQuestion(Question question) {
            questionDao.save(question);
            return new ResponseEntity<>("success",HttpStatus.CREATED);
    }


    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(category,numQuestions);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for(Integer id: questionIds){
            questions.add(questionDao.findById(id).get());
        }

        for(Question question: questions){
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestionTitle(question.getQuestionTitle());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(questionWrapper.getOption4());
            questionWrappers.add(questionWrapper);
        }

        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int score = 0;
        for(Response response: responses){
            Question question = questionDao.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())){
                score++;
            }
        }
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
