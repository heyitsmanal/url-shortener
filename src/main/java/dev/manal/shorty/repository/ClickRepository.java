package dev.manal.shorty.repository;

import dev.manal.shorty.domain.Click;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ClickRepository extends JpaRepository<Click, UUID> {

    long countByLinkId(UUID linkId);

    /**
     * Daily counts per link.
     * Use CAST(ts AS date) instead of DATE(ts) so it works on both H2 and Postgres.
     */
    @Query("""
           select cast(c.ts as date) as day, count(c) as cnt
           from Click c
           where c.link.id = :linkId
           group by cast(c.ts as date)
           order by day asc
           """)
    List<ClickStatsProjections.DayCount> dailyCounts(UUID linkId);

    /**
     * Top referrers (null → empty string) ordered by count desc.
     */
    @Query("""
           select coalesce(c.referrer, '') as ref, count(c) as cnt
           from Click c
           where c.link.id = :linkId
           group by c.referrer
           order by cnt desc
           """)
    List<ClickStatsProjections.RefCount> topReferrers(UUID linkId, Pageable pageable);
}
