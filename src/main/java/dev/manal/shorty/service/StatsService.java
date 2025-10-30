package dev.manal.shorty.service;

import dev.manal.shorty.domain.Link;
import dev.manal.shorty.dto.LinkStatsResponse;
import dev.manal.shorty.repository.ClickRepository;
import dev.manal.shorty.repository.ClickStatsProjections;
import dev.manal.shorty.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final LinkRepository linkRepo;
    private final ClickRepository clickRepo;

    /** Controller-friendly: you already have the Link. */
    @Transactional(readOnly = true)
    public LinkStatsResponse statsFor(Link link) {
        // pick a sensible default for “top N” referrers
        return statsFor(link, 5);
    }

    /** Keeps your old logic but works off a Link */
    @Transactional(readOnly = true)
    public LinkStatsResponse statsFor(Link link, int topN) {
        long total = clickRepo.countByLinkId(link.getId());

        var daily = clickRepo.dailyCounts(link.getId()).stream()
                .map(d -> new LinkStatsResponse.DailyCount(
                        d.getDay().toString(),
                        d.getCnt()
                ))
                .collect(Collectors.toList());


        var refs = clickRepo.topReferrers(link.getId(), PageRequest.of(0, Math.max(1, topN))).stream()
                .map(r -> new LinkStatsResponse.ReferrerCount(r.getRef(), r.getCnt()))
                .collect(Collectors.toList());


        return new LinkStatsResponse(link.getCode(), total, daily, refs);
    }

    /** Optional: keep your old entry-point if you still use it elsewhere */
    @Transactional(readOnly = true)
    public LinkStatsResponse getByCode(String code, int topN) {
        Link link = linkRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
        return statsFor(link, topN);
    }
}
