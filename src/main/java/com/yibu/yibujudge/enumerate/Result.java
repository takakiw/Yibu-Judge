package com.yibu.yibujudge.enumerate;

public enum Result {
    SUCCESS(0, "success"),
    CPU_TIME_LIMIT_EXCEEDED(1, "CPU time limit exceeded"),
    REAL_TIME_LIMIT_EXCEEDED(2, "real time limit exceeded"),
    MEMORY_LIMIT_EXCEEDED(3, "memory limit exceeded"),
    RUNTIME_ERROR(4, "runtime error"),
    SYSTEM_ERROR(5, "system error"),
    BUILD_ERROR(6, "build error"),
    WRONG_ANSWER(7, "wrong answer");

    private final int code;
    private final String message;

    // 构造函数
    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 获取 code 值
    public int getCode() {
        return this.code;
    }
    // 获取 message 值
    public String getMessage() {
        return this.message;
    }

    // 根据 code 值获取 Result
    public static Result fromCode(int code) {
        for (Result result : values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown result code: " + code);
    }
}