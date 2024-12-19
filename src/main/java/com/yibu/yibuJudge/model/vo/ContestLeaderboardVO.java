package com.yibu.yibuJudge.model.vo;
import com.yibu.yibuJudge.model.entity.ProblemDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestLeaderboardVO implements Serializable {

    private Long id; // 主键

    private Integer contestId; // 竞赛ID

    private Long userId; // 用户ID

    private String username; // 用户名

    private String nickName; // 昵称

    private String avatar; // 头像地址

    private Integer score; // 得分

    private Long finishTime; // 完成时间

    private Map<String, ProblemDetail> details; // 详情

    private LocalDateTime updateTime; // 更新时间

    private LocalDateTime createTime; // 创建时间

    private Integer rank; // 排名
}
