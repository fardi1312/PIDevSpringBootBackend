package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.io.ByteArrayOutputStream;

@Service
@Transactional
@RequiredArgsConstructor

public class QrCodeServices {

    public String generateQRContent(User u ) {
        return "Phone: " + u.getPhoneNumber() + "\nEmail: " + u.getEmail();
    }



}
