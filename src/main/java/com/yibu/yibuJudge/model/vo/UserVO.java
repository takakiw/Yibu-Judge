package com.yibu.yibuJudge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long id;
    private String username;
    private String nickName;
    private String avatar;
    private String email;
    private Integer acCount;
    private Integer submitCount;
    private Integer score;
}
