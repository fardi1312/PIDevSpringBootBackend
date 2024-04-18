package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Club;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.ClubRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository ;
    @Autowired
    private UserRepository userRepository;
    public Club createClub(Club club,long id ) {
        club.setPresident(userRepository.findByIdUser(id));
        return clubRepository.save(club);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Optional<Club> getClubById(long id) {
        return clubRepository.findById(id);
    }

    // Update operation
    public Club updateClub(long id, Club newClubData) {
        Optional<Club> optionalClub = clubRepository.findById(id);
        if (optionalClub.isPresent()) {
            Club clubToUpdate = optionalClub.get();
            clubToUpdate.setName(newClubData.getName());
            clubToUpdate.setDescription(newClubData.getDescription());
            clubToUpdate.setCategory(newClubData.getCategory());
            clubToUpdate.setPresident(newClubData.getPresident());
            clubToUpdate.setMembers(newClubData.getMembers());
            return clubRepository.save(clubToUpdate);
        } else {
            return null;
        }
    }

    // Delete operation
    public void deleteClub(long id) {
        clubRepository.deleteById(id);
    }



}
