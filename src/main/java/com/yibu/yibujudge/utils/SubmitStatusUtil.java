package com.yibu.yibujudge.utils;

public class SubmitStatusUtil {

    public static final int SUBMIT_STATUS_AC = 1; // ac状态
    public static final int SUBMIT_STATUS_NOT_AC = 2; // 尝试过，但没通过
    public static final int SUBMIT_STATUS_NONE = 0;  // 还没提交

    public static int getProblemStatus(Integer submitACCount, Integer submitCount) {
        submitCount = submitCount == null ? 0 : submitCount;
        submitACCount = submitACCount == null ? 0 : submitACCount;
        return submitACCount > 0 ? SUBMIT_STATUS_AC : submitCount > 0 ? SUBMIT_STATUS_NOT_AC : SUBMIT_STATUS_NONE;
    }
}
