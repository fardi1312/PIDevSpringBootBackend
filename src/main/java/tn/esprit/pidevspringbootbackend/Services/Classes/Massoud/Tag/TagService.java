package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.Tag;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Tag;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.TagRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.ITagService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.TagExistsException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.TagNotFoundException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name).orElseThrow(TagNotFoundException::new);
    }

    @Override
    public Tag createNewTag(String name) {
        try {
            Tag tag = getTagByName(name);
            if (tag != null) {
                throw new TagExistsException();
            }
        } catch (TagNotFoundException e) {
            Tag newTag = new Tag();
            newTag.setName(name);
            newTag.setTagUseCounter(1);
            newTag.setDateCreated(new Date());
            newTag.setDateLastModified(new Date());
            return tagRepository.save(newTag);
        }
        return null;
    }

    @Override
    public Tag increaseTagUseCounter(String name) {
        Tag targetTag = getTagByName(name);
        targetTag.setTagUseCounter(targetTag.getTagUseCounter()+1);
        targetTag.setDateLastModified(new Date());
        return tagRepository.save(targetTag);
    }

    @Override
    public Tag decreaseTagUseCounter(String name) {
        Tag targetTag = getTagByName(name);
        targetTag.setTagUseCounter(targetTag.getTagUseCounter()-1);
        targetTag.setDateLastModified(new Date());
        return tagRepository.save(targetTag);
    }

    @Override
    public List<Tag> getTimelineTags() {
        return tagRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "tagUseCounter"))
        ).getContent();
    }
}
