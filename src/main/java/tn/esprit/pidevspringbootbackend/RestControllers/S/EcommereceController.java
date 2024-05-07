package tn.esprit.pidevspringbootbackend.RestControllers.S;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.RequestPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.S.IServiceEcommerce;

import java.util.List;

@RestController
@RequestMapping("/sui2")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class EcommereceController {
    @Autowired
    IServiceEcommerce iServiceEcommerce;

    @Autowired
    private IUserService userService;


    @PostMapping("/addP9")
    public Post9ach addpost9ach(@RequestBody Post9ach p ){
        System.out.println("ddddddddd");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.addpost9ach( idu,p);
    }
    @GetMapping("/getallpost")
    public List<Post9ach> gettAllPost9ach( ){

        return iServiceEcommerce.getall();
    }

    @PostMapping("/addtocart")
    public Panier9ach addToCart(@RequestBody Post9ach p ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.addToPanier( idu,p);
    }

    @DeleteMapping("/removefromcart/{postId}")
    public Panier9ach removeFromCart( @PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.removeFromPanier(idu, postId);
    }


    @DeleteMapping("/clearcart")
    public void clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        iServiceEcommerce.clearPanier(idu);
    }

    // Add the method to get the user's Panier9ach
    @GetMapping("/getpanier")
    public Panier9ach getPanier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.getPanier(idu);
    }

    @PostMapping("/buypanier/{newTotal}")
    public RequestPost9ach buyPanier(@PathVariable double newTotal ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.buyPanier(idu,newTotal);
    }

    @GetMapping("/getPostbyId/{idPost}")
    public Post9ach getPostById(@PathVariable Long idPost) {

            return iServiceEcommerce.fondPostById(idPost);
    }

    @PutMapping("/updatepostandpoint/{idP}/{qts}")
    public void updatePostAndPoint(  @PathVariable Long idP,   @PathVariable Long qts   ) {
        iServiceEcommerce.updatePostAndPoint(idP, qts);

    }

    @PutMapping("/updatelikepost/{idP}")
    public Post9ach updateLikePost(  @PathVariable Long idP  ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

       return iServiceEcommerce.updateLike(idu, idP);

    }


    @GetMapping("/liked-posts")
    public List<Post9ach> getLikedPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

        return iServiceEcommerce.getLikedPosts(idu);

    }

    @GetMapping("/requests")
    public List<RequestPost9ach> getRequestsByUserId() {
        return iServiceEcommerce.getRequestsByUserId(1L);
    }

    @GetMapping("findpanierbyid")
    public List<Panier9ach> findPanierById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceEcommerce.findPanierById(idu);
    }


    @GetMapping("findpostbuyidpanier/{idpanier}")
    public List<Post9ach> findpostbuyidpanier(@PathVariable Long idpanier ) {
        return iServiceEcommerce.findPostByIdpanier(idpanier);
    }

    @GetMapping("/userposts")
    public List<Post9ach> getPost9ach() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

        return iServiceEcommerce.getPost9achByUser(idu);
    }

    @DeleteMapping("/removePost/{postId}")
    public ResponseEntity<Void> removePost(@PathVariable Long postId) {


            iServiceEcommerce.removePost(postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // HTTP 204 No Content

    }


    @PutMapping("/updatepost/{idPost}")
    public ResponseEntity<Post9ach> updatePost(@PathVariable Long idPost, @RequestBody Post9ach updatedPost) {
        try {
            // Use the service to update the post.
            Post9ach savedPost = iServiceEcommerce.updatePost(idPost, updatedPost);
            // Return the updated Post9ach wrapped in a ResponseEntity.
            return ResponseEntity.ok(savedPost);
        } catch (IllegalArgumentException e) {
            // If there was an issue, return a BAD REQUEST status.
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/getpostbyidpost/{idPost}")
    public ResponseEntity<Post9ach> findPostByIdPost(@PathVariable Long idPost) {
        // Call the service method to find the Post9ach by ID.
        Post9ach post = iServiceEcommerce.findPostByIdPost(idPost);

        // If the post exists, return it wrapped in a ResponseEntity with an OK status.
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            // If the post doesn't exist, return a NOT FOUND status.
            return ResponseEntity.notFound().build();
        }
    }



}
