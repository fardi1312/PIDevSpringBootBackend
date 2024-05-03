package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Role;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.RoleType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.RoleRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAdminService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IEmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService implements IAdminService {
    @Autowired
    private IEmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RepoPointCount repoPointCount;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repoPointCount.delete(user.getPointCount());
        userRepository.delete(user);
    }

    @Override
    public void disableUserAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
        sendEmailNotification(email, "User Account Disabled", "Your account has been disabled.");
    }

    @Override
    public void enableUserAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        sendEmailNotification(email, "User Account Enabled", "Your account has been enabled.");
    }

    private void sendEmailNotification(String email, String subject, String content) {
        emailService.send(email, subject, content);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int getTotalUserCount() {
        return userRepository.findAll().size();
    }


    private User buildNewUser(SignupDTO signupDTO) {
        User newUser = new User();
        newUser.setEmail(signupDTO.getEmail());
        newUser.setUsername(signupDTO.getFirstName() + "_" + signupDTO.getLastName());
        newUser.setFollowerCount(0);
        newUser.setFollowingCount(0);
        newUser.setEnabled(true);
        newUser.setAccountVerified(true);
        newUser.setEmailVerified(true);
        newUser.setFirstName(signupDTO.getFirstName());
        newUser.setLastName(signupDTO.getLastName());
        newUser.setJoinDate(LocalDateTime.now());
        newUser.setDateLastModified(LocalDateTime.now());
        newUser.setPhoneNumber(null);
        return newUser;
    }
    @Override
    public UserDTO createAdmin(SignupDTO signupDTO) {
        User newUser = buildNewUser(signupDTO);
        Role roleUser = roleRepository.findByType(RoleType.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .type(RoleType.ROLE_ADMIN)
                        .build()));
        newUser.setRole(roleUser);
        PointCount pointCount = new PointCount();
        pointCount.setNbPoint(0.0);
        newUser.setPointCount(pointCount);
        repoPointCount.save(pointCount);
        newUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        User createdUser = userRepository.save(newUser);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getIdUser());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setFirstName(createdUser.getFirstName());
        userDTO.setLastName(createdUser.getLastName());
        return userDTO;
    }

    @Override
    public User updateAdminInfo(User user, UpdateProfilDTO updateProfilDTO) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        user.setFirstName(updateProfilDTO.getFirstName());
        user.setLastName(updateProfilDTO.getLastName());
        user.setIntro(updateProfilDTO.getIntro());
        user.setGender(updateProfilDTO.getGender());
        user.setHometown(updateProfilDTO.getHometown());
        user.setCurrentCity(updateProfilDTO.getCurrentCity());
        user.setEduInstitution(updateProfilDTO.getEduInstitution());
        user.setWorkplace(updateProfilDTO.getWorkplace());
        user.setBirthDate(updateProfilDTO.getBirthDate());
        user.setPhoneNumber(updateProfilDTO.getPhoneNumber());
        return userRepository.save(user);
    }

    //update password by email





}
