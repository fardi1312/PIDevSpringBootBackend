package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;

import java.util.List;

public interface ITagService {
    Tag getTagById(Long id);
    Tag getTagByName(String name);
    Tag createNewTag(String name);
    Tag increaseTagUseCounter(String name);
    Tag decreaseTagUseCounter(String name);
    List<Tag> getTimelineTags();
}
