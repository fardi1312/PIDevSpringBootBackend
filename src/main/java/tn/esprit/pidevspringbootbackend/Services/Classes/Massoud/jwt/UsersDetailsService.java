package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
    private final ThreadLocal<UserDetails> currentUser = new ThreadLocal<>();
    public void saveCurrentUser(UserDetails user) {
        currentUser.set(user);
    }
    public UserDetails getCurrentUser() {
        return currentUser.get();
    }

    public void deleteCurrentUser() {
        currentUser.remove();
    }
}
