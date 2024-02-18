package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

}
