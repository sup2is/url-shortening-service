package me.sup2is.repository;

import me.sup2is.domain.ConvertedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvertedUrlRepository extends JpaRepository<ConvertedUrl, Long> {
}
