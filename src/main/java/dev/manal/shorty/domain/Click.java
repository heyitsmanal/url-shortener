package dev.manal.shorty.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "clicks")
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Column(name = "ts", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime ts;

    @Column(name = "ip_hash", nullable = false, length = 64)
    private String ipHash;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "referrer", columnDefinition = "TEXT")
    private String referrer;

    @PrePersist
    void prePersist() {
        if (ts == null) ts = OffsetDateTime.now();
    }
}
