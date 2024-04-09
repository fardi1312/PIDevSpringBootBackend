package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;
import tn.esprit.pidevspringbootbackend.DAO.Response.PostResponse;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IPostService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.ITagService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class TimelineController {
    private final IPostService postService;
    private final ITagService tagService;

    @GetMapping( "/")
    public ResponseEntity<?> getTimelinePosts(@RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<PostResponse> timelinePosts = postService.getTimelinePostsPaginate(page, size);
        return new ResponseEntity<>(timelinePosts, HttpStatus.OK);
    }

    @GetMapping( "/tags")
    public ResponseEntity<?> getTimelineTags() {
        List<Tag> timelineTags = tagService.getTimelineTags();
        return new ResponseEntity<>(timelineTags, HttpStatus.OK);
    }
}
