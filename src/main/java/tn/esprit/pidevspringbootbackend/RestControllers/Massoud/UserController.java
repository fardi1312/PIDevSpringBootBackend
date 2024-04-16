package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Response.PostResponse;
import tn.esprit.pidevspringbootbackend.DAO.Response.UserResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IPostService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/account")
public class UserController {
    private final IUserService userService;
    private final IPostService postService;

    @GetMapping("")
    public ResponseEntity<?> authenticatedUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(authentication.getName());
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }

    @GetMapping("/iduser")
    public ResponseEntity<?> getIdAuthenticatedUser(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return new ResponseEntity<>(user.getIdUser(), HttpStatus.OK);
    }








    @PostMapping("/update/info")
    public ResponseEntity<User> updateUserInfo(@Valid @RequestBody UpdateProfilDTO updateProfileDTO, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(authentication.getName());
            if (user != null) {
                user = userService.updateUserInfo(user, updateProfileDTO);
                return ResponseEntity.ok(user);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            userService.deleteUser(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/update/profilephoto")
    public ResponseEntity<User> updateProfilePhoto(
            Authentication authentication,
            @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        User user = userService.getUserByEmail(authentication.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.updateProfilePhoto(user, profilePhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @PostMapping("/update/coverphoto")
    public ResponseEntity<User> updateCoverPhoto(
            Authentication authentication,
            @RequestParam("coverPhoto") MultipartFile coverPhoto) {
        User user = userService.getUserByEmail(authentication.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.updateCoverPhoto(user, coverPhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(Authentication authentication,
                                                 @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            userService.updatePassword(user, updatePasswordDTO);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update password: " + e.getMessage());
        }
    }

    @GetMapping("/profilephoto")
    public String getUserProfilePhotoUrl(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return userService.getPhotoUrlForConnectedUser(user);
    }
    @GetMapping("/coverphoto")
    public String getUserCoverPhotoUrl(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return userService.getCoverPhotoUrlForConnectedUser(user);
    }



///////////////////////

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long userId) {
        postService.followUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long userId) {
        postService.unfollowUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<?> getUserFollowingUsers(@PathVariable("userId") Long userId,
                                                   @RequestParam("page") Integer page,
                                                   @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> followingList = postService.getFollowingUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/follower")
    public ResponseEntity<?> getUserFollowerUsers(@PathVariable("userId") Long userId,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> followingList = postService.getFollowerUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        User authUser = userService.getAuthenticatedUser();
        User targetUser = userService.getUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .user(targetUser)
                .followedByAuthUser(targetUser.getFollowerUsers().contains(authUser))
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable("userId") Long userId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        User targetUser = userService.getUserById(userId);
        List<PostResponse> userPosts = postService.getPostsByUserPaginate(targetUser, page, size);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUser(@RequestParam("key") String key,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> userSearchResult = userService.getUserSearchResult(key, page, size);
        return new ResponseEntity<>(userSearchResult, HttpStatus.OK);
    }

//http://localhost:8083/user/account/users/4/coverphoto
    @GetMapping("/users/{userId}/coverphoto")
    public String getUserCoverPhotoUrl(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        return userService.getCoverPhotoUrlForConnectedUser(user);
    }
//http://localhost:8083/user/account/users/4/profilephoto
    @GetMapping("/users/{userId}/profilephoto")
    public String getUserProfilePhotoUrl(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        return userService.getPhotoUrlForConnectedUser(user);
    }


}
