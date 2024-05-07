package tn.esprit.pidevspringbootbackend.Services.Classes.S;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.LikePost;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.RequestPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.PanierStatut;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.S.RepoLikePost;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.S.RepoPanier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.S.RepoPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.S.RepoRequestPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.S.IServiceEcommerce;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEcommerce implements IServiceEcommerce {
    @Autowired
    RepoPost9ach repoPost9ach;
    @Autowired
    RepoRequestPost9ach  repoRequestPost9ach;
    @Autowired
    UserRepository repoUser ;
    @Autowired
    RepoPanier9ach repoPanier9ach;
@Autowired
    RepoPointCount repoPointCount;
@Autowired
    RepoLikePost repoLikePost;
    @Override
    public Post9ach addpost9ach(long idu, Post9ach p){
        p.setStatutPost(CarpoolingStatus.Active);
        p.setNbslikes(0L);
        p.setOfferDate(new Date());
        User u = repoUser.findById(idu).orElse(null);
        List<Post9ach> l = u.getPost9achs();

        if(l==null){
            List<Post9ach> ln =new ArrayList<Post9ach>();
            ln.add(p);
            u.setPost9achs(ln);

        }
        else{
        l.add(p);
        u.setPost9achs(l);}
        p.setUser(u);
        return repoPost9ach.save(p);
    }



    @Override
    public List<Post9ach> getall() {
        List<Post9ach> posts = repoPost9ach.findAll();

        Iterator<Post9ach> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post9ach post = iterator.next();
            if (!post.getStatutPost().equals(CarpoolingStatus.Active)) {
                iterator.remove();
            }
        }

        return posts;
    }


    @Override
public  Post9ach fondPostById(Long idPost){
        return  repoPost9ach.findById(idPost).orElse(null);
}
    @Override
    public Panier9ach removeFromPanier(Long idu, Long postId) {

        User u = repoUser.findById(idu).orElse(null);
        Post9ach ps = repoPost9ach.findById(postId).orElse(null);



        if (u == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Panier9ach> paniers = u.getPanier9achs();
        Panier9ach panier = paniers.get(paniers.size() - 1);
        double newp = panier.getTotaleprice()-ps.getPrice();
        panier.setTotaleprice(newp);

        if (panier == null) {
            throw new IllegalArgumentException("Panier not found for user");
        }



        List<Post9ach> posts = panier.getPost9achs();

        if (posts != null) {


            Post9ach postToRemove = null;
            for (Post9ach post : posts) {
                if (post.getIdPost9ach().equals(postId)) {
                    postToRemove = post;
                    break;
                }
            }


            if (postToRemove != null) {

                posts.remove(postToRemove);
                panier.setPost9achs(posts);
                postToRemove.setPanier9ach(null);
            }
        }
        System.out.println("fasa5"+panier);
        return repoPanier9ach.save(panier) ;
    }
    @Override
    public Panier9ach clearPanier(Long idu) {
        User u = repoUser.findById(idu).orElse(null);
        if (u == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Panier9ach> paniers = u.getPanier9achs();
        if (paniers == null || paniers.isEmpty()) {
            throw new IllegalArgumentException("Panier list not found for user");
        }


        Panier9ach lastPanier = paniers.get(paniers.size() - 1);


      List<Post9ach> list =  lastPanier.getPost9achs();

      for( Post9ach l : list){

          l.setPanier9ach(null);
      }
lastPanier.setTotaleprice(0);
        return repoPanier9ach.save(lastPanier);
    }

    @Override
    public Panier9ach addToPanier(Long idu, Post9ach p){
        User u = repoUser.findById(idu).orElse(null);

        List<Panier9ach> paniers = u.getPanier9achs();

        Panier9ach panier = paniers.get(paniers.size() - 1);
        System.out.println("alloooo1111111"+panier);
        if(panier.getPost9achs()!=null) {
Post9ach post= repoPost9ach.findById(p.getIdPost9ach()).orElse(null);
panier.setTotaleprice(panier.getTotaleprice()+p.getPrice());
        List<Post9ach> l =    panier.getPost9achs();
            post.setPanier9ach(panier);
        l.add(post);

            panier.setPost9achs(l);

        }

        System.out.println("alloooooooooo"+panier);
        //paniers.add(panier);
//repoUser.save(u);
        return  repoPanier9ach.save(panier);
    }

    @Override
    public Panier9ach getPanier(Long idu) {

        User user = repoUser.findById(idu).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Panier9ach> paniers = user.getPanier9achs();
        Panier9ach panier = paniers.get(paniers.size() - 1);

        if (panier == null) {
            throw new IllegalArgumentException("Panier not found for user");
        }


        System.out.println("Retrieved Panier for user " + idu + ": " + panier);
        return panier;
    }
@Override
public RequestPost9ach buyPanier(Long idu,double newTotal){
    User user = repoUser.findById(idu).orElse(null);

    if (user == null) {
        throw new IllegalArgumentException("User not found");
    }
    List<Panier9ach> paniers = user.getPanier9achs();
    Panier9ach panier = paniers.get(paniers.size() - 1);
    panier.setTotaleprice(newTotal);
    if (panier == null) {
        throw new IllegalArgumentException("Panier not found for user");
    }

    for (Post9ach post9ach : panier.getPost9achs()) {
        if (post9ach.getUser().equals(user)) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas faire une demande sur sa propre offre de covoiturage.");
        }
    }

    double totalPrice = panier.getTotaleprice();
    System.out.println("prrcc"+totalPrice);

    PointCount pointCount = user.getPointCount();
    if (pointCount == null) {
        throw new IllegalArgumentException("Point count not found for user");
    }

    if (pointCount.getNbPoint() < totalPrice) {
        throw new IllegalArgumentException("User does not have enough points");
    }
    pointCount.setNbPoint(pointCount.getNbPoint() - totalPrice);
    user.setPointCount(pointCount);

    panier.setSatatuspanier(PanierStatut.confirmed);
    panier.setBuyDate(new Date());


    RequestPost9ach requestPost = new RequestPost9ach();
    requestPost.setPanier9ach(panier);
    requestPost.setUser(user);
    Panier9ach newpanier = new Panier9ach();
    newpanier.setSatatuspanier(PanierStatut.EnCour);
    newpanier.setUser(user);
    paniers.add(newpanier);
    user.setPanier9achs(paniers);


    user.getRequestPost9achs().add(requestPost);
//    repoUser.save(user);
    // Save the new RequestPost9ach to the database
 //   requestPost = repoRequestPost9ach.save(requestPost);

    // Update the user's cart with the new request
    panier.setRequestpost9ach(requestPost);



    // Return the new RequestPost9ach
    return  repoRequestPost9ach.save(requestPost);
}
@Override
    public void updatePostAndPoint(Long idP, Long qts){
Post9ach p = repoPost9ach.findById(idP).orElse(null);

p.setQuantitySold(qts);
    Long qtKdima = Long.parseLong(p.getQuantity());
Long jdida = qtKdima-qts;
    if (jdida < 0) {
        throw new IllegalArgumentException("Requested quantity exceeds available quantity.");
    }
p.setQuantity(String.valueOf(jdida));
    if (jdida == 0) {
        p.setStatutPost(CarpoolingStatus.Expired);
    }
repoPost9ach.save(p);


User u =p.getUser();
PointCount pcu= u.getPointCount();
pcu.setNbPoint( pcu.getNbPoint()+p.getPrice()*qts);
repoPointCount.save(pcu);
u.setPointCount(pcu);
repoUser.save(u);

    }

    @Override
    public Post9ach updateLike(Long userId, Long postId) {
        User user = repoUser.findById(userId).orElse(null);

        Post9ach post9ach = repoPost9ach.findById(postId).orElse(null);

        if (user != null && post9ach != null) {
            LikePost likePost = user.getLikePost();




            List<Post9ach> post9achList = likePost.getPost9aches();

            if (post9achList.contains(post9ach)) {

                    post9achList.remove(post9ach);
                    post9ach.setNbslikes(post9ach.getNbslikes() - 1);

            } else {
                post9achList.add(post9ach);
                post9ach.setNbslikes(post9ach.getNbslikes() + 1);
            }

            repoLikePost.save(likePost);

        }
    return            repoPost9ach.save(post9ach);


    }

    @Override
    public List<Post9ach> getLikedPosts(Long userId) {
        User user = repoUser.findById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        LikePost likePost = user.getLikePost();

        if (likePost == null) {
            throw new IllegalArgumentException("LikePost entity not found for the user");
        }

        List<Post9ach> likedPosts = likePost.getPost9aches();
        System.out.println("ff"+likedPosts);
        return likedPosts;
    }
    @Override
    public List<RequestPost9ach> getRequestsByUserId(Long userId) {

        List<RequestPost9ach> allRequests = repoRequestPost9ach.findAll();
User user = repoUser.findById(userId).orElse(null);
        List<RequestPost9ach> userRequests = new ArrayList<>();

        for (RequestPost9ach request : allRequests) {
            if (request.getUser() != null && request.getUser().getIdUser().equals(userId)) {
                userRequests.add(request);
            }
        }

        return userRequests;
    }


    @Override
    public List<Panier9ach> findPanierById(Long idu) {

        User user = repoUser.findById(idu).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Panier9ach> panier = user.getPanier9achs();

        if (panier == null) {
            throw new IllegalArgumentException("Panier not found for user");
        }



        return panier;
    }
    @Override
    public List<Post9ach> findPostByIdpanier(Long idp) {


      Panier9ach p =repoPanier9ach.findById(idp).orElse(null);





        return p.getPost9achs();
    }


    @Override
    public List<Post9ach> getPost9achByUser(Long userId) {
        User user = repoUser.findById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Post9ach> userPosts = user.getPost9achs();



        return userPosts;
    }
    @Override
    public void removePost(Long postId) {
        Post9ach post = repoPost9ach.findById(postId).orElse(null);
        post.setStatutPost(CarpoolingStatus.Canceled);
repoPost9ach.save(post);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

      //  repoPost9ach.delete(post);

    }
@Override
    public Post9ach findPostByIdPost(Long idPost) {
        return repoPost9ach.findById(idPost).orElse(null);
    }

    @Override
    public Post9ach updatePost(Long idPost, Post9ach updatedPost) {
        Post9ach existingPost = repoPost9ach.findById(idPost).orElse(null);

        if (existingPost == null) {
            throw new IllegalArgumentException("Post not found with ID: " + idPost);
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setLocation(updatedPost.getLocation());
        existingPost.setPrice(updatedPost.getPrice());
        existingPost.setQuantity(updatedPost.getQuantity());
        existingPost.setQuantitySold(updatedPost.getQuantitySold());
        existingPost.setStatutPost(updatedPost.getStatutPost());
        existingPost.setOfferDate(updatedPost.getOfferDate());


        Post9ach savedPost = repoPost9ach.save(existingPost);

        return savedPost;
    }
}
