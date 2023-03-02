package com.mentionall.cpr2u.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseTemplate {
    public int status;
    public String message;
    public Object data;
}