package com.yibu.yibuJudge.enumerate;

import com.yibu.yibuJudge.exceptions.BaseException;

public enum SubmitStatusCode {

    ACCEPTED(1, 0),
    TLE(2, 1),
    RTLE(2, 2),
    MLE(3, 3),
    RTE(4, 4),
    SE(5, 5),
    BE(6, 6),
    WA(7, 7);

    private final int SubmitCode;
    private final int JudgeCode;

    SubmitStatusCode(int SubmitCode, int JudgeCode) {
        this.SubmitCode = SubmitCode;
        this.JudgeCode = JudgeCode;
    }

    public int getSubmitCode() {
        return SubmitCode;
    }

    public int getJudgeCode() {
        return JudgeCode;
    }


    public static SubmitStatusCode getSubmitStatusCodeByJudgeStatusCode(int judgeCode) {
        for (SubmitStatusCode submitStatusCode : values()) {
            if (submitStatusCode.getJudgeCode() == judgeCode) {
                return submitStatusCode;
            }
        }
        throw new BaseException("系统错误：未知的评测状态码");
    }
}
