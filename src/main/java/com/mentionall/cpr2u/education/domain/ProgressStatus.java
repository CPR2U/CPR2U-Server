package com.mentionall.cpr2u.education.domain;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ProgressStatus {
    NotCompleted("Not Completed"),
    InProgress("In Progress"),
    Completed("Completed");

    private final String status;
}
