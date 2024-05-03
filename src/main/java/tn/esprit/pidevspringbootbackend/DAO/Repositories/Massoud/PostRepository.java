package tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByAuthor(User author, Pageable pageable);
    //List<Post> findPostsByAuthorIdIn(List<Long> authorIds, Pageable pageable); // Corrected method signature
    List<Post> findPostsBySharedPost(Post post, Pageable pageable);
    List<Post> findPostsByPostTags(Tag tag, Pageable pageable);

    List<Post> findPostsByAuthorIdUserIn(List<Long> userIds, Pageable pageable);

    List<Post> findByAuthor(User connectedUser);
}
