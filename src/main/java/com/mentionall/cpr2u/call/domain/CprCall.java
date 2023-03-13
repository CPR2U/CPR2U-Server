package com.mentionall.cpr2u.call.domain;

import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cpr_call")
public class CprCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caller_id")
    private User caller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    Address address;

    @Column
    String fullAddress;

    @CreatedDate
    LocalDateTime calledAt;

    @Column
    Double latitude;

    @Column
    Double  longitude;

    @Column
    @Enumerated(EnumType.STRING)
    CprCallStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cprCall")
    List<Dispatch> dispatchList = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cprCall")
    List<Report> reportList = new ArrayList();

    // TODO: /dispatch 테스트용 생성자(발견 시 삭제 요망)
    public CprCall(Long id, String fullAddress, double latitude, double longitude) {
        this.id = id;
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
