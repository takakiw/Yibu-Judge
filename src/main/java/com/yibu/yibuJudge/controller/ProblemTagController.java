package com.yibu.yibuJudge.controller;

import com.yibu.yibuJudge.model.dto.TagDTO;
import com.yibu.yibuJudge.model.entity.Tag;
import com.yibu.yibuJudge.model.response.Result;
import com.yibu.yibuJudge.service.TagService;
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

    @PostMapping("/add")
    public Result<Integer> add(@RequestBody @Valid TagDTO tagDTO){
        Integer id = tagService.add(tagDTO.getName());
        return Result.success(id);
    }

    @DeleteMapping("/delete/{ids}")
    public Result<List<Tag>> delete(@PathVariable("ids") List<Integer> ids, @RequestParam(required = false) Boolean force){
        List<Tag> tags = tagService.delete(ids, force);
        return Result.success(tags);
    }


    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Valid TagDTO tag){
        tagService.update(tag);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Tag>> list(){
        List<Tag> tags = tagService.list();
        return Result.success(tags);
    }
}
