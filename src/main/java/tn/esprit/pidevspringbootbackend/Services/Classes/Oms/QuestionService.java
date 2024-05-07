package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Question;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.QuestionRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    private final Map<Long, LocalDateTime> userQuizStartTimes = new HashMap<>();

    public List<Question> addQuestions(List<Question> questions) {
        return questionRepository.saveAll(questions);
    }
    public   List<Question> getall(){return questionRepository.findAll();}

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        questionRepository.delete(question);
    }




    public boolean startQuizForUser(long userId) {
        if (userQuizStartTimes.containsKey(userId)) {
            return false;
        }

        LocalTime currentTime = LocalTime.now();
        if (currentTime.isBefore(LocalTime.of(00, 0)) || currentTime.isAfter(LocalTime.of(23, 59))) {
            return false;
        }

        userQuizStartTimes.put(userId, LocalDateTime.now());
        return true;
    }


}

