package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.Comment;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.NotificationType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.CommentRepository;
import tn.esprit.pidevspringbootbackend.DAO.Response.CommentResponse;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.ICommentService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.INotificationService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.CommentNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.InvalidOperationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final IUserService userService;
    private final INotificationService notificationService;

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Comment createNewComment(String content, Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setAuthor(authUser);
        newComment.setPost(post);
        newComment.setLikeCount(0);
        newComment.setDateCreated(new Date());
        newComment.setDateLastModified(new Date());
        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(Long commentId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Comment targetComment = getCommentById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            targetComment.setContent(content);
            targetComment.setDateLastModified(new Date());
            return commentRepository.save(targetComment);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Comment targetComment = getCommentById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            commentRepository.deleteById(commentId);
            notificationService.deleteNotificationByOwningComment(targetComment);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Comment likeComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Comment targetComment = getCommentById(commentId);
        if (!targetComment.getLikeList().contains(authUser)) {
            targetComment.setLikeCount(targetComment.getLikeCount()+1);
            targetComment.getLikeList().add(authUser);
            targetComment.setDateLastModified(new Date());
            Comment updatedComment = commentRepository.save(targetComment);

            if (!targetComment.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetComment.getAuthor(),
                        authUser,
                        targetComment.getPost(),
                        targetComment,
                        NotificationType.COMMENT_LIKE.name()
                );
            }

            return updatedComment;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Comment unlikeComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Comment targetComment = getCommentById(commentId);
        if (targetComment.getLikeList().contains(authUser)) {
            targetComment.setLikeCount(targetComment.getLikeCount() - 1);
            targetComment.getLikeList().remove(authUser);
            targetComment.setDateLastModified(new Date());
            Comment updatedComment = commentRepository.save(targetComment);

            if (!targetComment.getAuthor().equals(authUser)) {
                notificationService.removeNotification(
                        targetComment.getPost().getAuthor(),
                        targetComment.getPost(),
                        NotificationType.COMMENT_LIKE.name()
                );
            }

            return updatedComment;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public List<CommentResponse> getPostCommentsPaginate(Post post, Integer page, Integer size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               List<Comment> foundCommentList = commentRepository.findByPost(
                post,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        );

        List<CommentResponse> commentResponseList = new ArrayList<>();
        foundCommentList.forEach(comment -> {
            CommentResponse newCommentResponse = CommentResponse.builder()
                    .comment(comment)
                    .likedByAuthUser(comment.getLikeList().contains(authUser))
                    .build();
            commentResponseList.add(newCommentResponse);
        });

        return commentResponseList;
    }
}
