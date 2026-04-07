package com.demo.user.utils;

import com.demo.user.enums.TaskStatus;

public class Util {

    public static boolean isValidStatus(String status) {
        return status.equals(TaskStatus.PENDING.toString()) || status.equals(TaskStatus.IN_PROGRESS.toString())
                || status.equals(TaskStatus.COMPLETED.toString());
    }
}
