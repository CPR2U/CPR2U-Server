package com.mentionall.cpr2u.call.domain;

import com.mentionall.cpr2u.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Dispatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcher_id")
    private User dispatcher;

    @Column
    @Enumerated(EnumType.STRING)
    private DispatchStatus statuc;

    @CreatedDate
    private LocalDateTime dispatchedAt;

    @Column
    private LocalDateTime arrivedAt;



}
