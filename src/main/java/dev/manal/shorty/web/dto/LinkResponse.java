package dev.manal.shorty.web.dto;

import dev.manal.shorty.domain.Link;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record LinkResponse(
        UUID id,
        String code,
        String targetUrl,
        Instant createdAt,
        String createdBy,
        long totalClicks
) {
    // static factory helper
    public static LinkResponse from(Link link, long totalClicks) {
        return LinkResponse.builder()
                .id(link.getId())
                .code(link.getCode())
                .targetUrl(link.getTargetUrl())
                .createdAt(link.getCreatedAt())
                .createdBy(link.getCreatedBy())
                .totalClicks(totalClicks)
                .build();
    }
}
