package com.yibu.yibujudge.controller;

import com.yibu.yibujudge.annotation.CheckAuth;
import com.yibu.yibujudge.model.dto.TagDTO;
import com.yibu.yibujudge.model.entity.Tag;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.service.TagService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem/tag")
@Validated
public class ProblemTagController {

    private final TagService tagService;

    public ProblemTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @CheckAuth
    @PostMapping("/add")
    public Result<Integer> add(@RequestBody @Valid TagDTO tagDTO) {
        Integer id = tagService.add(tagDTO.getName());
        return Result.success(id);
    }

    @CheckAuth
    @DeleteMapping("/delete/{ids}")
    public Result<Void> delete(@PathVariable("ids") List<Integer> ids) {
        tagService.delete(ids);
        return Result.success();
    }


    @CheckAuth
    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Valid TagDTO tag) {
        tagService.update(tag);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Tag>> list() {
        List<Tag> tags = tagService.list();
        return Result.success(tags);
    }
}
