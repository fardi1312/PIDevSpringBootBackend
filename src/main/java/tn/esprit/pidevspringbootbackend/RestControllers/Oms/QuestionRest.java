package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Question;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.QuestionService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.util.List;

@RestController
@RequestMapping("/quiz1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class QuestionRest {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private IUserService userService;

    @PostMapping("/questions")
    public List<Question> addQuestions(@RequestBody List<Question> questions) {
        return questionService.addQuestions(questions);
    }

@GetMapping("/allQuestions")
    public   List<Question> getall(){return questionService.getall();}
@DeleteMapping("/deleteQuestion")
    public void deleteQuestion(@RequestParam  Long questionId) {
    questionService.deleteQuestion(questionId);
    }




    @GetMapping("/start/{userId}")
        public boolean startQuizForUser(@PathVariable long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        return questionService.startQuizForUser(user.getIdUser());
    }


}
