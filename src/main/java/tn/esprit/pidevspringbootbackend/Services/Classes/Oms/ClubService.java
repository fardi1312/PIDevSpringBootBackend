package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;


import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.ClubRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.MemberShipApplicationRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.clubMembershipRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.eventRepository;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileNamingUtil;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileUploadUtil;


import javax.swing.text.html.Option;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository ;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberShipApplicationRepository memberShipApplicationRepository ;
    @Autowired
    private  clubMembershipRepository clubMembershipRepository ;
    @Autowired
    private eventRepository calendarEventRepository;
    @Autowired
    Environment environment ;
    @Autowired
    private FileUploadUtil utils ;
    @Autowired
    private FileNamingUtil fileNamingUtil ;


    public Club createClub(Club club, long presidentId) {

        User president = userRepository.findByIdUser(presidentId);
        if (president != null) {
            club.setPresident(president);
        } else {
            System.out.println("president");
        }

        Club club1 = clubRepository.save(club);
        for (ClubMembership membership : club.getClubMemberShip()) {
            membership.setClub(club);

            clubMembershipRepository.save(membership) ;
        }
        return club1 ;
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
            return clubRepository.save(clubToUpdate);
        } else {
            return null;
        }
    }
    public Optional<User> verifyEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Club addMemberShipApplication(long clubId,long idUser,String message)  {
        User user = userRepository.findByIdUser(idUser);
        MemberShipApplication memberShipApplication = new MemberShipApplication() ;
        Club club = clubRepository.findById(clubId).get();
        memberShipApplication.setClub(club);
        memberShipApplication.setUser(user);
        memberShipApplication.setDate(new Date());
        memberShipApplication.setStatus(Request.Pending) ;

        memberShipApplication.setMessage(message);
        memberShipApplicationRepository.save(memberShipApplication) ;
        List<MemberShipApplication> memberShipApplications = club.getMemberShipApplications() ;
        memberShipApplications.add(memberShipApplication);
        club.setMemberShipApplications(memberShipApplications);
        return clubRepository.save(club);

    }



    public MemberShipApplication assignInterviewer (long id, MemberShipApplication updatedMemberShipApplication) {
        Optional<MemberShipApplication> existingMemberShipApplicationOptional = memberShipApplicationRepository.findById(id);
        MemberShipApplication existingMemberShipApplication = null;
        if (existingMemberShipApplicationOptional.isPresent()) {
            existingMemberShipApplication = existingMemberShipApplicationOptional.get();
            existingMemberShipApplication.setInterviewer(updatedMemberShipApplication.getInterviewer());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Date start = calendar.getTime();
            calendar.setTime(start);
            System.out.println("test" + start);
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            Date end = calendar.getTime();

            boolean conflictFound = true;
            Optional<User> offerer = userRepository.findByEmail(updatedMemberShipApplication.getInterviewer());
            List<calendarEvent> userCalendarEvents = calendarEventRepository.findByUsers(offerer.get());
            if (offerer.isPresent()) {


                while (conflictFound) { // Continue loop until conflictFound is false
                    conflictFound = false; // Reset conflictFound for each iteration
                    for (calendarEvent event : userCalendarEvents) {
                        if ((event.getStart().before(end) && event.getEnd().after(start)) ||
                                (event.getStart().after(start) && event.getEnd().before(end)) ||
                                (event.getStart().before(start) && event.getEnd().after(end)) ||
                                (event.getStart().equals(start) && event.getEnd().equals(end))) {
                            // Found a conflict with an existing event
                            conflictFound = true;
                            // Fix the time
                            start = event.getEnd(); // Update the start time to the end time of the conflicting event
                            calendar.setTime(start);
                            calendar.add(Calendar.HOUR_OF_DAY, 1); // Add 1 hour to the start time
                            end = calendar.getTime(); // Update the end time
                            break; // Exit the loop after fixing the time for the current conflicting event
                        }
                    }
                }


                System.out.println("dtest" + start);
                calendar.setTime(start);

                calendar.add(Calendar.HOUR_OF_DAY, 1);
                end = calendar.getTime();

                calendarEvent externalEvent = new calendarEvent();
                externalEvent.setTitle("Interview");
                externalEvent.setStart(start);
                externalEvent.setEnd(end);
                externalEvent.setType(false);
                externalEvent.setFixedRequester(true);
                externalEvent.setAcceptRenting(true) ;
                User requester = existingMemberShipApplication.getUser();

                externalEvent.setRequester(requester.getFirstName() + ' ' + requester.getLastName());
                externalEvent.setOfferer(existingMemberShipApplication.getClub().getName());
                externalEvent.setIdRequester(requester.getIdUser());
                externalEvent.setIdOfferer(offerer.get().getIdUser());
                System.out.println(existingMemberShipApplication.getClub().getId());
                externalEvent.setIdClub(existingMemberShipApplication.getClub().getId());
                memberShipApplicationRepository.save(existingMemberShipApplication);
                externalEvent.setMemberShipApplicationId(existingMemberShipApplication.getId());

                Resizable resizable = new Resizable();
                externalEvent.setResizable(resizable);
                List<User> users = new ArrayList<>();
                users.add(offerer.get());
                users.add(requester);
                externalEvent.setUsers(users);
                System.out.println("ahaya : "  +  externalEvent.getFixedRequester().toString());


                calendarEventRepository.save(externalEvent);
            }

           return memberShipApplicationRepository.save(existingMemberShipApplication);

        }


    else{
            throw new RuntimeException("MemberShipApplication not found with id: " + id);
        }

    }
    public List<MemberShipApplication> getMembershipApplications() {
        return memberShipApplicationRepository.findAll() ;
     }
    public Club acceptMemberShip(calendarEvent calendarEvent){
        calendarEvent = calendarEventRepository.findById(calendarEvent.getId()).get() ;
        System.out.println("club : " + calendarEvent.getIdClub());

        Club club = clubRepository.findById(calendarEvent.getIdClub()).get() ;

        List<ClubMembership> clubMemberships = club.getClubMemberShip() ;
        ClubMembership clubMembership = new ClubMembership() ;
        clubMembership.setClub(club);
        User member = userRepository.findById(calendarEvent.getIdRequester()).get() ;
        clubMembership.setMember(member);
        clubMembership.setEmail(member.getEmail());
        clubMembership.setEmailVerified(true);
        clubMembershipRepository.save(clubMembership) ;
        clubMemberships.add(clubMembership) ;
        club.setClubMemberShip(clubMemberships);
        List<MemberShipApplication> memberShipApplications = club.getMemberShipApplications() ;
        MemberShipApplication memberShipApplication = memberShipApplicationRepository.findById(calendarEvent.getMemberShipApplicationId()).get() ;
        memberShipApplications.remove(memberShipApplication) ;
        clubRepository.save(club) ;

        memberShipApplicationRepository.delete(memberShipApplication) ;

        return clubRepository.save(club) ;

    }
    public List<ClubMembership> getAllClubMemberships() {
        return clubMembershipRepository.findAll() ;
     }
    public Club refuseMemberShip(calendarEvent calendarEvent) {
        calendarEvent = calendarEventRepository.findById(calendarEvent.getId()).get() ;
        System.out.println("d5alna fih");
        System.out.println("aaaaaaaaaaaaa "  + calendarEvent.getMemberShipApplicationId());

        MemberShipApplication memberShipApplication = memberShipApplicationRepository.findById(calendarEvent.getMemberShipApplicationId()).get() ;
        memberShipApplication.setStatus(Request.Canceled);
        memberShipApplicationRepository.save(memberShipApplication);
        return clubRepository.findById(calendarEvent.getIdClub()).get() ;
    }
    public String getImageUrlForCovByID(Long id) {
        Club coo = clubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CarpoolingOffer not found with id: " + id));
        String baseUrl = environment.getProperty("export.collocation.images");
        String ccImage = coo.getLogo();

        if (ccImage != null && !ccImage.isEmpty()) {

            System.err.println(baseUrl + ccImage);
            return baseUrl + ccImage;
        }

        return null;
    }






    public Club updateClubLogo(Long idO, MultipartFile clubLogo) {
        Club coo = clubRepository.findById(idO).orElseThrow(() -> new EntityNotFoundException("CarpoolingOffer not found with id: " + idO));

        try {
                String uploadDir = environment.getProperty("upload.collocation.images");
                String newPhotoName = fileNamingUtil.nameFile(clubLogo);
                System.out.println(newPhotoName.toString()) ;
                coo.setLogo(newPhotoName);
                System.out.println("club logo" + coo.getLogo().toString());

                utils.saveNewFile(uploadDir, newPhotoName.toString(), clubLogo);

            return clubRepository.save(coo);
        } catch (IOException eg) {
            throw new RuntimeException("Failed to update CC photo", eg);
        }
    }









    public MemberShipApplication updateMemberShipApplication(long id, MemberShipApplication updatedMemberShipApplication) {
        Optional<MemberShipApplication> existingMemberShipApplicationOptional = memberShipApplicationRepository.findById(id);

        if (existingMemberShipApplicationOptional.isPresent()) {
            MemberShipApplication existingMemberShipApplication = existingMemberShipApplicationOptional.get();

            existingMemberShipApplication.setStatus(updatedMemberShipApplication.getStatus());
            existingMemberShipApplication.setInterviewer(updatedMemberShipApplication.getInterviewer());
            existingMemberShipApplication.setDate(updatedMemberShipApplication.getDate());
            existingMemberShipApplication.setUser(updatedMemberShipApplication.getUser());
            existingMemberShipApplication.setClub(updatedMemberShipApplication.getClub());
            existingMemberShipApplication.setMessage(updatedMemberShipApplication.getMessage());

            // Save the updated MemberShipApplication
            return memberShipApplicationRepository.save(existingMemberShipApplication);
        } else {
            throw new RuntimeException("MemberShipApplication not found with id: " + id);
        }
    }

   public Club addMember(long clubId, long idUser) {
        // Find the user by ID
        User newMember = userRepository.findByIdUser(idUser);
        if (newMember == null) {
            throw new RuntimeException("User with ID " + idUser + " not found");
        }
        Club club = clubRepository.findById(clubId).get() ;
        if (club == null) {
            throw new IllegalArgumentException("Club object cannot be null");
        }
        List<ClubMembership> members  ;
        members = club.getClubMemberShip();
        ClubMembership clubMembership = new ClubMembership() ;
        clubMembership.setMember(userRepository.findById(idUser).get());
        clubMembership.setEmail(userRepository.findById(idUser).get().getEmail());
        clubMembership.setClub(clubRepository.findById(clubId).get());
        clubMembershipRepository.save(clubMembership) ;
        members.add(clubMembership) ;
        club.setClubMemberShip(members);
        return clubRepository.save(club);
    }

    // Delete operation
    public void deleteClub(long id) {
        clubRepository.deleteById(id);
    }



}
