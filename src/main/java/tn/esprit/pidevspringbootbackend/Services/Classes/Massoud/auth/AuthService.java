package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Role;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.RoleType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.RoleRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAuthService;

import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RepoPointCount repoPointCount;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User newUser = new User();
        newUser.setEmail(signupDTO.getEmail());
        newUser.setUsername(signupDTO.getFirstName() + "_" + signupDTO.getLastName());
        newUser.setFollowerCount(0);
        newUser.setFollowingCount(0);
        newUser.setEnabled(false);
        newUser.setAccountVerified(false);
        newUser.setEmailVerified(false);
        newUser.setFirstName(signupDTO.getFirstName());
        newUser.setLastName(signupDTO.getLastName());
        newUser.setEmailVerified(false);
        newUser.setJoinDate(LocalDateTime.now());
        newUser.setDateLastModified(LocalDateTime.now());
        newUser.setPhoneNumber(null);

        Role roleUser = roleRepository.findByType(RoleType.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .type(RoleType.ROLE_USER)
                        .build()));
        newUser.setRole(roleUser);

        PointCount c = new PointCount();
        c.setNbPoint(0.0);
        newUser.setPointCount(c);
        repoPointCount.save(c);

        newUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        User createdUser = userRepository.save(newUser);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getIdUser());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setFirstName(createdUser.getFirstName());
        userDTO.setLastName(createdUser.getLastName());
        return userDTO;
    }

}
