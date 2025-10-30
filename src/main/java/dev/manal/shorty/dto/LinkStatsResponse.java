package dev.manal.shorty.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LinkStatsResponse(
        String code,
        long totalClicks,
        List<DailyCount> daily,
        List<ReferrerCount> topReferrers
) {
    public static record DailyCount(String date, long count) {}
    public static record ReferrerCount(String referrer, long count) {}
}
