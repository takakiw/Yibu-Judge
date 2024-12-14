package com.yibu.yibuJudge.controller;

import cn.hutool.core.bean.BeanUtil;
import com.yibu.yibuJudge.model.dto.ContestDTO;
import com.yibu.yibuJudge.model.entity.Contest;
import com.yibu.yibuJudge.model.entity.ContestLeaderboard;
import com.yibu.yibuJudge.model.entity.User;
import com.yibu.yibuJudge.model.response.PageBean;
import com.yibu.yibuJudge.model.response.Result;
import com.yibu.yibuJudge.model.vo.ContestLeaderboardVO;
import com.yibu.yibuJudge.model.vo.ContestVO;
import com.yibu.yibuJudge.service.ContestService;
import com.yibu.yibuJudge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contest")
@Validated
public class ContestController {

    private final ContestService contestService;

    private final UserService userService;

    public ContestController(ContestService contestService, UserService userService) {
        this.contestService = contestService;
        this.userService = userService;
    }

    @PostMapping("/create") // 创建竞赛
    public Result<Void> createContest(@RequestBody @Valid ContestDTO contestDTO) {
        contestService.createContest(contestDTO);
        return Result.success();
    }

    @PutMapping("/update") // 更新竞赛
    public Result<Void> updateContest(@RequestBody ContestDTO contestDTO) {
        contestService.updateContest(
                contestDTO.getId(),
                contestDTO.getName(),
                contestDTO.getDescription(),
                contestDTO.getStartTime(),
                contestDTO.getEndTime(),
                contestDTO.getOrganizer());
        return Result.success();
    }

    @DeleteMapping("/del/{contestId}") // 删除竞赛
    public Result<Void> deleteContest(@PathVariable("contestId") Integer contestId,
                                      @RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
        contestService.deleteContest(contestId, force);
        return Result.success();
    }

    @GetMapping("/list") // 列出竞赛
    public Result<PageBean<Contest>> listContest(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
                                                 @RequestParam(value = "preparation" , required = false, defaultValue = "true") Boolean preparation) {
        PageBean<Contest> pageBean = contestService.listContest(page, size, preparation);
        return Result.success(pageBean);
    }

    @GetMapping("/{contestId}") // 获取竞赛详情, 包括竞赛题目列表
    public Result<ContestVO> getContest(@PathVariable("contestId") Integer contestId) {
        ContestVO contestVO = contestService.getContestInfo(contestId);
        return Result.success(contestVO);
    }

    @GetMapping("/rank/{contestId}") // 获取排行榜
    public Result<PageBean<ContestLeaderboardVO>> getRank(@PathVariable("contestId") Integer contestId,
                                                          @RequestParam("page") Integer page,
                                                          @RequestParam("size") Integer size) {

        List<ContestLeaderboard> rankList = contestService.getRankList(contestId, page, size);
        List<User> users = userService.getUsers(rankList.stream().map(ContestLeaderboard::getUserId).toList());
        List<ContestLeaderboardVO> voList = BeanUtil.copyToList(rankList, ContestLeaderboardVO.class);
        for (int i = 0; i < voList.size(); i++) {
            voList.get(i).setUsername(users.get(i).getUsername());
            voList.get(i).setNickName(users.get(i).getNickName());
            voList.get(i).setAvatar(users.get(i).getAvatar());
        }
        return Result.success(new PageBean<>((long) voList.size(), voList));
    }

    @GetMapping("/rank/user/{contestId}")
    public Result<ContestLeaderboardVO> getRankByUser(@PathVariable("contestId") Integer contestId) {
        ContestLeaderboard leaderboard = contestService.getRank(contestId);
        User user = userService.getUser(leaderboard.getUserId());
        ContestLeaderboardVO vo = BeanUtil.copyProperties(leaderboard, ContestLeaderboardVO.class);
        vo.setUsername(user.getUsername());
        vo.setNickName(user.getNickName());
        vo.setAvatar(user.getAvatar());
        return Result.success(vo);
    }

    // 参加竞赛
    @PostMapping("/{contestId}/join")
    public Result<Void> joinContest(@PathVariable("contestId") Integer contestId) {
        contestService.registerContest(contestId);
        return Result.success();
    }
    // 退出竞赛
    @DeleteMapping("/{contestId}/quit")
    public Result<Void> quitContest(@PathVariable("contestId") Integer contestId) {
        contestService.unregisterContest(contestId);
        return Result.success();
    }
}
