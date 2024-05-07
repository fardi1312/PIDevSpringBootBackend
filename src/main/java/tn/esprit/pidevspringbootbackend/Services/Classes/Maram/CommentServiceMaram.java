package tn.esprit.pidevspringbootbackend.Services.Classes.Maram;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.CommentMaram;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.CommentRepositoryMaram;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.RepoEvents;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram.IServiceCommentMaram;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceMaram implements IServiceCommentMaram {
    @Autowired
    private CommentRepositoryMaram commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepoEvents repoEvents;
    @Override
    public CommentMaram createComment(CommentMaram comment, Long eventId, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Events> eventOptional = repoEvents.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent()) {
            comment.setUser(userOptional.get());
            comment.setEvent(eventOptional.get());
            comment.setCreatedAt(LocalDateTime.now());
            return commentRepository.save(comment);
        }

        return null;
    }

    @Override
    public List<CommentMaram> getAllComments() {
        return commentRepository.findAll();
    }
    @Override
    public Optional<CommentMaram> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }
    @Override
    // Update operation
    public CommentMaram updateComment(Long commentId, CommentMaram updatedComment) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(updatedComment.getContent());
                    comment.setUser(updatedComment.getUser());
                    comment.setEvent(updatedComment.getEvent());
                    return commentRepository.save(comment);
                })
                .orElse(null); // Comment not found
    }
    @Override

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


}


