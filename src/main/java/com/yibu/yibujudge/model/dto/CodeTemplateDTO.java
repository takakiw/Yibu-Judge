package com.yibu.yibujudge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplateDTO {
    private Integer id;
    private Integer problemId;
    private Integer languageId;
    private String templateCode;
               private String Outersa_sa;
    public void setOutersa_sa(String Outersa_sa){
        this.Outersa_sa = Outersa_sa;
    }
}
