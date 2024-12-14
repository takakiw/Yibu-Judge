package com.yibu.yibuJudge.enumerate;

public enum Error {
    SUCCESS(0, "success"),
    INVALID_CONFIG(-1, "invalid config"),
    FORK_FAILED(-2, "fork failed"),
    PTHREAD_FAILED(-3, "pthread failed"),
    WAIT_FAILED(-4, "wait failed"),
    ROOT_REQUIRED(-5, "root required"),
    LOAD_SECCOMP_FAILED(-6, "load seccomp failed"),
    SETRLIMIT_FAILED(-7, "setrlimit failed"),
    DUP2_FAILED(-8, "dup2 failed"),
    SETUID_FAILED(-9, "setuid failed"),
    EXECVE_FAILED(-10, "execve failed");

    private final int code;
    private final String errorMessage;

    // 构造函数
    Error(int code, String message) {
        this.code = code;
        this.errorMessage = message;
    }

    // 获取 code 值
    public int getCode() {
        return this.code;
    }
    // 获取 message 值
    public String getErrorMessage() {
        return this.errorMessage;
    }

    // 根据 code 值获取 Error
    public static Error fromCode(int code) {
        for (Error error : Error.values()) {
            if (error.getCode() == code) {
                return error;
            }
        }
        throw new IllegalArgumentException("Unknown error code: " + code);
    }
}