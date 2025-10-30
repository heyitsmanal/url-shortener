package dev.manal.shorty.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "links")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Link {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 16)
    private String code;

    @Column(name = "target_url", nullable = false, columnDefinition = "TEXT")
    private String targetUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;
}
