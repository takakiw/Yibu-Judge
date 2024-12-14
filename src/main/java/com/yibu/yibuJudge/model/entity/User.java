package com.yibu.yibuJudge.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private String avatar;
    private String email;
    private Integer acCount;
    private Integer submitCount;
    private Integer role;
    private Integer score;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
