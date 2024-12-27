package com.yibu.yibujudge.enumerate;

public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isValid(int value) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getValue() == value) {
                return true;
            }
        }
        return false;
    }
}
