package com.gogitek.toeictest.constant;

public enum Period {
    ONE_MONTH, THREE_MONTH, SIX_MONTH;

    public static Integer getPeriodInt(Period period) {
        if(period.equals(ONE_MONTH)) {
            return 1;
        }

        if(period.equals(THREE_MONTH)) {
            return 3;
        }

        return 6;
    }
}
