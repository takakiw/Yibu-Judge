package com.yibu.yibuJudge.service;

import com.yibu.yibuJudge.constant.ProblemConstants;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.mapper.TagMapper;
import com.yibu.yibuJudge.model.dto.TagDTO;
import com.yibu.yibuJudge.model.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagMapper tagMapper;

    public TagService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public Integer add(String name) {
        Tag tag = tagMapper.getTagByName(name);
        if (tag!= null){
            throw new BaseException(ProblemConstants.TAG_ALREADY_EXISTS);
        }
        Integer id = tagMapper.insert(name);
        if (id == null){
            throw new BaseException(ProblemConstants.ADD_TAG_FAILED);
        }
        return id;
    }

    public List<Tag> delete(List<Integer> ids, Boolean force) {
        if (ids == null || ids.size() == 0){
            return null;
        }
        if (!force){
            List<Tag> tags = tagMapper.getUseingTag(ids);
            if (tags!= null && tags.size() > 0){
                return tags;
            }
            int delete = tagMapper.delete(ids);
            if (delete!= ids.size()){
                throw new BaseException(ProblemConstants.DELETE_TAG_FAILED);
            }
        }else{
            int delete = tagMapper.delete(ids);
            if (delete!= ids.size()){
                throw new BaseException(ProblemConstants.DELETE_TAG_FAILED);
            }
        }
        return null;
    }

    public void update(TagDTO tag) {
        if (tag == null || tag.getId() == null){
            throw new BaseException(ProblemConstants.PARAM_ERROR);
        }
        Tag oldTag = tagMapper.getTagById(tag.getId());
        if (oldTag == null){
            throw new BaseException(ProblemConstants.TAG_NOT_EXISTS);
        }
        if (oldTag.getName().equals(tag.getName())){
            return;
        }
        oldTag.setName(tag.getName());
        int update = tagMapper.update(oldTag);
        if (update!= 1){
            throw new BaseException(ProblemConstants.UPDATE_TAG_FAILED);
        }
    }

    public List<Tag> list() {
        return tagMapper.list();
    }

    public List<Integer> getTagIdByIds(List<Integer> tagIds) {
        if (tagIds == null || tagIds.size() == 0){
            return null;
        }
        return tagMapper.getTagIdByIds(tagIds);
    }
}
