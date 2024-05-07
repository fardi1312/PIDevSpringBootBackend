package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.email.EmailService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAuthService;
import java.time.LocalDateTime;
import java.util.Random;

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
    @Autowired
    private EmailService emailService;


    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User newUser = buildNewUser(signupDTO);
        Role roleUser = roleRepository.findByType(RoleType.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .type(RoleType.ROLE_USER)
                        .build()));
        newUser.setRole(roleUser);
        PointCount pointCount = new PointCount();
        pointCount.setNbPoint(0.0);
        newUser.setPointCount(pointCount);
        repoPointCount.save(pointCount);
        newUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        User createdUser = userRepository.save(newUser);
        emailService.send(createdUser.getEmail(), "Email Verification", emailService.getMsgEmail(createdUser));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getIdUser());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setFirstName(createdUser.getFirstName());
        userDTO.setLastName(createdUser.getLastName());
        return userDTO;
    }

    @Override
    public boolean verifyEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        return true;
    }






    @Override
    public UserDTO createUserFromOAuth2(OAuth2User oauth2User) {
        String name = oauth2User.getAttribute("name");
        String[] nameParts = name.split("\\s+");
        String firstName = "";
        String lastName = "";
        if (nameParts.length > 1) {
            lastName = nameParts[0];
            firstName = nameParts[1];
        } else if (nameParts.length == 1) {
            firstName = nameParts[0];
        }
        String email = null;
        Object emailObj = oauth2User.getAttribute("email");
        if (emailObj != null) {
            email = emailObj.toString();
        } else {
            email = lastName + "." + firstName + "@esprit.tn";
        }
        User existingUser = userRepository.getUserByEmail(email);
        if (existingUser != null) {
        return null;
        }
        String generatedPassword = generateRandomPassword();
        SignupDTO signupDTO = SignupDTO.builder()
                .email(email)
                .password(generatedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        String emailContent = "Bonjour " + firstName + ",\n\n" +
                "Votre compte a été créé avec succès. Voici votre mot de passe : " + generatedPassword ;
        emailService.send(email, "Création de compte réussie", emailContent);
        return createUser(signupDTO);
    }


    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private User buildNewUser(SignupDTO signupDTO) {
        User newUser = new User();
        newUser.setEmail(signupDTO.getEmail());
        newUser.setUsername(signupDTO.getFirstName() + "_" + signupDTO.getLastName());
        newUser.setFollowerCount(0);
        newUser.setFollowingCount(0);
        newUser.setEnabled(true);
        newUser.setAccountVerified(false);
        newUser.setEmailVerified(false);
        newUser.setFirstName(signupDTO.getFirstName());
        newUser.setLastName(signupDTO.getLastName());
        newUser.setJoinDate(LocalDateTime.now());
        newUser.setDateLastModified(LocalDateTime.now());
        newUser.setPhoneNumber(null);
        return newUser;
    }
    // Method to get the currently authenticated user
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
