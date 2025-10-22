package dev.manal.shorty.web.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LinkResponse {
    private UUID id;
    private String code;
    private String targetUrl;
    private OffsetDateTime createdAt;
    private String createdBy;

    // stats (filled later)
    private Long totalClicks;
}
