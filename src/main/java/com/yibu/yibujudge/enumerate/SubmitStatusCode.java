package com.yibu.yibujudge.enumerate;

import com.yibu.yibujudge.exceptions.BaseException;

public enum SubmitStatusCode {

    ACCEPTED(1, 0),
    TLE(2, 1),
    RTLE(2, 2),
    MLE(3, 3),
    RTE(4, 4),
    SE(5, 5),
    BE(6, 6),
    WA(7, 7);

    private  final int submitCode;
    private final int judgeCode;

    SubmitStatusCode(int submitCode, int judgeCode) {
        this.submitCode = submitCode;
        this.judgeCode = judgeCode;
    }

    public int getSubmitCode() {
        return submitCode;
    }

    public int getJudgeCode() {
        return judgeCode;
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
