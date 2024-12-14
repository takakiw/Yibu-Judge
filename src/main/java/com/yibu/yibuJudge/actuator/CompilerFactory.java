package com.yibu.yibuJudge.actuator;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CompilerFactory {

    private final Map<String, Compiler> compilers;

    public CompilerFactory(Map<String, Compiler> compilers) {
        this.compilers = compilers;
    }

    public Compiler getCompiler(String type) {
        Compiler compiler = compilers.get(type);
        if (compiler == null) {
            throw new IllegalArgumentException("Unsupported compiler type: " + type);
        }
        return compiler;
    }
}
