package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.Post;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.NotificationType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.PostRepository;
import tn.esprit.pidevspringbootbackend.DAO.Response.PostResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.TagDTO;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.Comment.CommentService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.Tag.TagService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.UserService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.INotificationService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IPostService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.EmptyCommentException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.InvalidOperationException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.PostNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.TagNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileNamingUtil;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final TagService tagService;
    private final INotificationService notificationService;
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public PostResponse getPostResponseById(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post foundPost = getPostById(postId);
        return PostResponse.builder()
                .post(foundPost)
                .likedByAuthUser(foundPost.getLikeList().contains(authUser))
                .build();
    }

    @Override
    public List<PostResponse> getTimelinePostsPaginate(Integer page, Integer size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               List<User> followingList = authUser.getFollowingUsers();
        followingList.add(authUser);
        List<Long> followingListIds = followingList.stream().map(User::getIdUser).toList();
        return postRepository.findPostsByAuthorIdIn(
                        followingListIds,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostSharesPaginate(Post sharedPost, Integer page, Integer size) {
        return postRepository.findPostsBySharedPost(
                        sharedPost,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostByTagPaginate(Tag tag, Integer page, Integer size) {
        return postRepository.findPostsByPostTags(
                        tag,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsByUserPaginate(User author, Integer page, Integer size) {
        return postRepository.findPostsByAuthor(
                        author,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public Post createNewPost(String content, MultipartFile postPhoto, List<TagDTO> postTags) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post newPost = new Post();
        newPost.setContent(content);
        newPost.setAuthor(authUser);
        newPost.setLikeCount(0);
        newPost.setShareCount(0);
        newPost.setCommentCount(0);
        newPost.setIsTypeShare(false);
        newPost.setSharedPost(null);
        newPost.setDateCreated(new Date());
        newPost.setDateLastModified(new Date());

        if (postPhoto != null && postPhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            newPost.setPostPhoto(newPhotoUrl);
            try {
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, postPhoto);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        if (postTags != null && postTags.size() > 0) {
            postTags.forEach(tagDto -> {
                Tag tagToAdd = null;
                try {
                    Tag existingTag = tagService.getTagByName(tagDto.getTagName());
                    if (existingTag != null) {
                        tagToAdd = tagService.increaseTagUseCounter(tagDto.getTagName());
                    }
                } catch (TagNotFoundException e) {
                    tagToAdd = tagService.createNewTag(tagDto.getTagName());
                }
                newPost.getPostTags().add(tagToAdd);
            });
        }

        return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(Long postId, String content, MultipartFile postPhoto, List<TagDTO> postTags) {
        Post targetPost = getPostById(postId);
        if (StringUtils.isNotEmpty(content)) {
            targetPost.setContent(content);
        }

        if (postPhoto != null && postPhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String oldPhotoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            targetPost.setPostPhoto(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, postPhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, postPhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        if (postTags != null && postTags.size() > 0) {
            postTags.forEach(tagDto -> {
                Boolean isNewTag = false;
                Tag targetTag;
                try {
                    targetTag = tagService.getTagByName(tagDto.getTagName());
                } catch (TagNotFoundException e) {
                    targetTag = tagService.createNewTag(tagDto.getTagName());
                    isNewTag = true;
                }

                if (tagDto.getAction().equalsIgnoreCase("remove")) {
                    targetPost.getPostTags().remove(targetTag);
                    tagService.decreaseTagUseCounter(tagDto.getTagName());
                } else if (tagDto.getAction().equalsIgnoreCase("add")) {
                    targetPost.getPostTags().add(targetTag);
                    if (!isNewTag) {
                        tagService.increaseTagUseCounter(tagDto.getTagName());
                    }
                }
            });
        }

        targetPost.setDateLastModified(new Date());
        return postRepository.save(targetPost);
    }

    @Override
    public void deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);

        if (targetPost.getAuthor().equals(authUser)) {
            targetPost.getShareList().forEach(sharingPost -> {
                sharingPost.setSharedPost(null);
                postRepository.save(sharingPost);
            });

            notificationService.deleteNotificationByOwningPost(targetPost);

            postRepository.deleteById(postId);

            if (targetPost.getPostPhoto() != null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);
                } catch (IOException ignored) {}
            }
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deletePostPhoto(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);

        if (targetPost.getAuthor().equals(authUser)) {
            if (targetPost.getPostPhoto() != null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);
                } catch (IOException ignored) {}
            }

            targetPost.setPostPhoto(null);
            postRepository.save(targetPost);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void likePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);
        if (!targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikeCount(targetPost.getLikeCount() + 1);
            targetPost.getLikeList().add(authUser);
            postRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetPost.getAuthor(),
                        authUser,
                        targetPost,
                        null,
                        NotificationType.POST_LIKE.name()
                );
            }
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void unlikePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);
        if (targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikeCount(targetPost.getLikeCount()-1);
            targetPost.getLikeList().remove(authUser);
            postRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.removeNotification(
                        targetPost.getAuthor(),
                        targetPost,
                        NotificationType.POST_LIKE.name()
                );
            }
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Comment createPostComment(Long postId, String content) {
        if (StringUtils.isEmpty(content)) throw new EmptyCommentException();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);
        Comment savedComment = commentService.createNewComment(content, targetPost);
        targetPost.setCommentCount(targetPost.getCommentCount()+1);
        postRepository.save(targetPost);

        if (!targetPost.getAuthor().equals(authUser)) {
            notificationService.sendNotification(
                    targetPost.getAuthor(),
                    authUser,
                    targetPost,
                    savedComment,
                    NotificationType.POST_COMMENT.name()
            );
        }

        return savedComment;
    }

    @Override
    public Comment updatePostComment(Long commentId, Long postId, String content) {
        if (StringUtils.isEmpty(content)) throw new EmptyCommentException();

        return commentService.updateComment(commentId, content);
    }

    @Override
    public void deletePostComment(Long commentId, Long postId) {
        Post targetPost = getPostById(postId);
        commentService.deleteComment(commentId);
        targetPost.setCommentCount(targetPost.getCommentCount()-1);
        targetPost.setDateLastModified(new Date());
        postRepository.save(targetPost);
    }

    @Override
    public Post createPostShare(String content, Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPost = getPostById(postId);
        if (!targetPost.getIsTypeShare()) {
            Post newPostShare = new Post();
            newPostShare.setContent(content);
            newPostShare.setAuthor(authUser);
            newPostShare.setLikeCount(0);
            newPostShare.setShareCount(null);
            newPostShare.setCommentCount(0);
            newPostShare.setPostPhoto(null);
            newPostShare.setIsTypeShare(true);
            newPostShare.setSharedPost(targetPost);
            newPostShare.setDateCreated(new Date());
            newPostShare.setDateLastModified(new Date());
            Post savedPostShare = postRepository.save(newPostShare);
            targetPost.getShareList().add(savedPostShare);
            targetPost.setShareCount(targetPost.getShareCount()+1);
            postRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetPost.getAuthor(),
                        authUser,
                        targetPost,
                        null,
                        NotificationType.POST_SHARE.name()
                );
            }

            return savedPostShare;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Post updatePostShare(String content, Long postShareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPostShare = getPostById(postShareId);
        if (targetPostShare.getAuthor().equals(authUser)) {
            targetPostShare.setContent(content);
            targetPostShare.setDateLastModified(new Date());
            return postRepository.save(targetPostShare);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deletePostShare(Long postShareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               Post targetPostShare = getPostById(postShareId);
        if (targetPostShare.getAuthor().equals(authUser)) {
            Post sharedPost = targetPostShare.getSharedPost();
            sharedPost.getShareList().remove(targetPostShare);
            sharedPost.setShareCount(sharedPost.getShareCount()-1);
            postRepository.save(sharedPost);
            postRepository.deleteById(postShareId);

            notificationService.deleteNotificationByOwningPost(targetPostShare);
        } else {
            throw new InvalidOperationException();
        }
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }

    private PostResponse postToPostResponse(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByEmail(authentication.getName());
               return PostResponse.builder()
                .post(post)
                .likedByAuthUser(post.getLikeList().contains(authUser))
                .build();
    }
}
