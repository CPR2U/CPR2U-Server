package com.mentionall.cpr2u.education.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LectureType {
    NONE("NONE"),
    THEORY("THEORY"),
    POSTURE("POSTURE");

    private final String type;
}
