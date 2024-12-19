package com.yibu.yibujudge.service;

import com.yibu.yibujudge.constant.ProblemConstants;
import com.yibu.yibujudge.exceptions.BaseException;
import com.yibu.yibujudge.mapper.TagMapper;
import com.yibu.yibujudge.model.dto.TagDTO;
import com.yibu.yibujudge.model.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TagService {

    private final TagMapper tagMapper;

    public TagService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Transactional
    public Integer add(String name) {
        Tag tag = tagMapper.getTagByName(name);
        if (tag != null) {
            throw new BaseException(ProblemConstants.TAG_ALREADY_EXISTS);
        }
        Tag dbTag = new Tag(null, name);
        tagMapper.insert(dbTag);
        Integer id = dbTag.getId();
        if (id == null) {
            throw new BaseException(ProblemConstants.ADD_TAG_FAILED);
        }
        return id;
    }

    @Transactional
    public void delete(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        log.info("delete tag ids: {}", ids);
        tagMapper.delete(ids);
        tagMapper.deleteProblemTag(ids);
    }

    public void update(TagDTO tag) {
        if (tag == null || tag.getId() == null) {
            throw new BaseException(ProblemConstants.PARAM_ERROR);
        }
        Tag oldTag = tagMapper.getTagById(tag.getId());
        if (oldTag == null) {
            throw new BaseException(ProblemConstants.TAG_NOT_EXISTS);
        }
        if (oldTag.getName().equals(tag.getName())) {
            return;
        }
        oldTag.setName(tag.getName());
        int update = tagMapper.update(oldTag);
        if (update != 1) {
            throw new BaseException(ProblemConstants.UPDATE_TAG_FAILED);
        }
    }

    public List<Tag> list() {
        return tagMapper.list();
    }

    public List<Integer> getTagIdByIds(List<Integer> tagIds) {
        if (tagIds == null || tagIds.size() == 0) {
            return null;
        }
        return tagMapper.getTagIdByIds(tagIds);
    }
}
