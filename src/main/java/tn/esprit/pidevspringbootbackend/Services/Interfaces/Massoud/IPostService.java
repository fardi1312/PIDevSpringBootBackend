package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Response.PostResponse;
import tn.esprit.pidevspringbootbackend.DAO.Response.UserResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.TagDTO;

import java.io.IOException;
import java.util.List;

public interface IPostService {
    Post getPostById(Long postId);
    PostResponse getPostResponseById(Long postId);
    List<PostResponse> getPostsByUserPaginate(User author, Integer page, Integer size);
    List<PostResponse> getTimelinePostsPaginate(Integer page, Integer size);
    List<PostResponse> getPostSharesPaginate(Post sharedPost, Integer page, Integer size);
    List<PostResponse> getPostByTagPaginate(Tag tag, Integer page, Integer size);
    Post createNewPost(String content, MultipartFile postPhoto, List<TagDTO> postTags);
    Post updatePost(Long postId, String content, MultipartFile postPhoto, List<TagDTO> postTags);
    void deletePost(Long postId);
    void deletePostPhoto(Long postId) throws IOException;
    void likePost(Long postId);
    void unlikePost(Long postId);
    Comment createPostComment(Long postId, String content);
    Comment updatePostComment(Long commentId, Long postId, String content);
    void deletePostComment(Long commentId, Long postId);
    Post createPostShare(String content, Long postShareId);
    Post updatePostShare(String content, Long postShareId);
    void deletePostShare(Long postShareId);

    User getUserById(Long userId);

    List<UserResponse> getFollowerUsersPaginate(Long userId, Integer page, Integer size);

    void followUser(Long userId);

    void unfollowUser(Long userId);

    List<UserResponse> getFollowingUsersPaginate(Long userId, Integer page, Integer size);

    String getPhotoUrlPostbyIdPost(Long postId);

    //getAllPosts
    List<Post> getAllPosts();
}