package tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.CommentMaram;

import java.util.List;
import java.util.Optional;

public interface IServiceCommentMaram {
     CommentMaram createComment(CommentMaram comment, Long eventId, Long userId) ;
     List<CommentMaram> getAllComments() ;
     Optional<CommentMaram> getCommentById(Long commentId) ;
     CommentMaram updateComment(Long commentId, CommentMaram updatedComment) ;
     void deleteComment(Long commentId) ;






}
