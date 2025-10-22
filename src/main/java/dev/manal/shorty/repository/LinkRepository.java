package dev.manal.shorty.repository;

import dev.manal.shorty.domain.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    Optional<Link> findByCode(String code);

    Page<Link> findByCodeContainingIgnoreCaseOrTargetUrlContainingIgnoreCase(
            String code, String targetUrl, Pageable pageable);
}
