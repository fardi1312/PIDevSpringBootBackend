package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CollocationRequestService {

    @Autowired
    private  CollocationRequestRepository collocationRequestRepository;


    public List<CollocationRequest> getAllCollocationRequests() {
        return collocationRequestRepository.findAll();
    }

    public Optional<CollocationRequest> getCollocationRequestById(long id) {
        return collocationRequestRepository.findById(id);
    }

    public CollocationRequest saveCollocationRequest(CollocationRequest collocationRequest) {
        collocationRequest.setRequest(Request.Pending);
        return collocationRequestRepository.save(collocationRequest);
    }

    public void deleteCollocationRequest(long id) {
        collocationRequestRepository.deleteById(id);
    }



}
