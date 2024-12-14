package com.yibu.yibuJudge.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sandbox")
public class SandBoxProperties {
    private String defaultMaxMemory;
    private String defaultMaxOutputSize;
    private String exePath;
    private String logPath;
    private String defaultMaxCpuTime;
    private String defaultMaxRealTime;

    public String getDefaultMaxMemory() {
        return defaultMaxMemory;
    }

    public void setDefaultMaxMemory(String defaultMaxMemory) {
        this.defaultMaxMemory = defaultMaxMemory;
    }

    public String getDefaultMaxOutputSize() {
        return defaultMaxOutputSize;
    }

    public void setDefaultMaxOutputSize(String defaultMaxOutputSize) {
        this.defaultMaxOutputSize = defaultMaxOutputSize;
    }

    public String getExePath() {
        return exePath;
    }

    public void setExePath(String exePath) {
        this.exePath = exePath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getDefaultMaxCpuTime() {
        return defaultMaxCpuTime;
    }

    public void setDefaultMaxCpuTime(String defaultMaxCpuTime) {
        this.defaultMaxCpuTime = defaultMaxCpuTime;
    }

    public String getDefaultMaxRealTime() {
        return defaultMaxRealTime;
    }

    public void setDefaultMaxRealTime(String defaultMaxRealTime) {
        this.defaultMaxRealTime = defaultMaxRealTime;
    }
}
