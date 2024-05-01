package tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     User findByIdUser(long idUser);
    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);

    User findFirstByEmail(String email);

    void deleteByEmail(String email);
    List<User> findUsersByFollowerUsers(User user, Pageable pageable);
    List<User> findUsersByFollowingUsers(User user, Pageable pageable);
    List<User> findUsersByLikedPosts(Post post, Pageable pageable);
    List<User> findUsersByLikedComments(Comment comment, Pageable pageable);

    @Query(value = "select * from users u " +
            "where concat(u.first_name, ' ', u.last_name) like %:name% " +
            "order by u.first_name asc, u.last_name asc",
            nativeQuery = true)
    List<User> findUsersByName(String name, Pageable pageable);


}
