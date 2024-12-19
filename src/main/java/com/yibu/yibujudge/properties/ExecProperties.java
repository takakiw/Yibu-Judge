package com.yibu.yibujudge.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "exec")
public class ExecProperties {
    private String gcc;
    private String gpp;
    private String java;
    private String javac;
    private String python3;

    public String getGcc() {
        return gcc;
    }

    public void setGcc(String gcc) {
        this.gcc = gcc;
    }

    public String getGpp() {
        return gpp;
    }

    public void setGpp(String gpp) {
        this.gpp = gpp;
    }

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public String getJavac() {
        return javac;
    }

    public void setJavac(String javac) {
        this.javac = javac;
    }

    public String getPython3() {
        return python3;
    }

    public void setPython3(String python3) {
        this.python3 = python3;
    }
}
