package com.mentionall.cpr2u.call.domain;

import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.util.Timestamped;

import javax.persistence.*;

@Entity
public class Report extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpr_call_id")
    CPRCall cprCall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    User reporter;

    @Column(length = 100)
    private String content;

    public Report(CPRCall cprCall, User reporter, String content) {
        this.cprCall = cprCall;
        this.reporter = reporter;
        this.content = content;
    }
}
