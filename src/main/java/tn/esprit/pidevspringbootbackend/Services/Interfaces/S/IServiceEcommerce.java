package tn.esprit.pidevspringbootbackend.Services.Interfaces.S;

import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.RequestPost9ach;

import java.util.List;

public interface IServiceEcommerce {
    Post9ach addpost9ach(long idu, Post9ach p);

    List<Post9ach> getall();

    Post9ach fondPostById(Long idPost);

    Panier9ach removeFromPanier(Long idu, Long postId);

    Panier9ach clearPanier(Long idu);

    Panier9ach addToPanier(Long idu, Post9ach p);

    Panier9ach getPanier(Long idu);

    RequestPost9ach buyPanier(Long idu,double newTotal);

    void updatePostAndPoint(Long idP, Long qts);

    Post9ach updateLike(Long userId, Long postId);

    List<Post9ach> getLikedPosts(Long userId);

    List<RequestPost9ach> getRequestsByUserId(Long userId);

    List<Panier9ach> findPanierById(Long idPanier);

    List<Post9ach> findPostByIdpanier(Long idp);

    List<Post9ach> getPost9achByUser(Long userId);

    void removePost(Long postId);

    Post9ach findPostByIdPost(Long idPost);

    Post9ach updatePost(Long idPost, Post9ach updatedPost);
}
