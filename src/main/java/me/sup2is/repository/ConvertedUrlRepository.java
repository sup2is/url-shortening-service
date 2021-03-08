package me.sup2is.repository;

import me.sup2is.domain.ConvertedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConvertedUrlRepository extends JpaRepository<ConvertedUrl, Long> {

    Optional<ConvertedUrl> findByOrgUrl(String orgUrl);
    boolean existsByOrgUrl(String orgUrl);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ConvertedUrl c SET c.requestCnt = c.requestCnt + 1 where c.id = :targetId")
    void updateRequestCnt(Long targetId);

    Optional<ConvertedUrl> findByShorteningUrl(String shorteningUrl);
}
