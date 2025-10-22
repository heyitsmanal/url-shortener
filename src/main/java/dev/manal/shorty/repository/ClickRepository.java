package dev.manal.shorty.repository;

import dev.manal.shorty.domain.Click;
import dev.manal.shorty.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click, Long> {
    long countByLink(Link link);
}
