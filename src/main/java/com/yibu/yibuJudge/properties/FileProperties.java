package com.yibu.yibuJudge.properties;

import cn.hutool.core.io.FileUtil;
import jakarta.annotation.PostConstruct;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@ToString
public class FileProperties {
    private String caseInPath;
    private String caseOutPath;
    private String buildPath;
    private String codePath;

    private String tempCasePath;

    private String tempOutPath;

    private String tempErrPath;

    public String getTempOutPath() {
        return tempOutPath;
    }

    public void setTempOutPath(String tempOutPath) {
        this.tempOutPath = tempOutPath;
    }

    public String getTempCasePath() {
        return tempCasePath;
    }

    public void setTempCasePath(String tempCasePath) {
        this.tempCasePath = tempCasePath;
    }

    public String getCaseInPath() {
        return caseInPath;
    }

    public void setCaseInPath(String caseInPath) {
        this.caseInPath = caseInPath;
    }

    public String getCaseOutPath() {
        return caseOutPath;
    }

    public void setCaseOutPath(String caseOutPath) {
        this.caseOutPath = caseOutPath;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getTempErrPath() {
        return tempErrPath;
    }
    public void setTempErrPath(String tempErrPath) {
        this.tempErrPath = tempErrPath;
    }

    @PostConstruct
    public void initDirectories() {
        FileUtil.mkdir(caseInPath);
        FileUtil.mkdir(caseOutPath);
        FileUtil.mkdir(buildPath);
        FileUtil.mkdir(codePath);
        FileUtil.mkdir(tempCasePath);
        FileUtil.mkdir(tempOutPath);
        FileUtil.mkdir(tempErrPath);
    }
}
