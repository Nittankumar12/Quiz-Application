package com.example.quizApp.service;

import com.example.quizApp.dao.QuestionDao;
import com.example.quizApp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions(){
        try{
            return new ResponseEntity<>(questionDao.findAll(),HttpStatus.OK);
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
}
