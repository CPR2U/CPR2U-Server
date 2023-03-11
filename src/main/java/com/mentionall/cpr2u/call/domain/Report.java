package com.mentionall.cpr2u.call.domain;

import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    public Report(Long id, CPRCall cprCall, User reporter, String content) {
        this.id = id;
        this.cprCall = cprCall;
        this.reporter = reporter;
        this.content = content;
    }
}
