package net.catsnap.domain.review.repository;

import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Slice<Review> findAllByMemberId(Long memberId, Pageable pageable);

    Slice<Review> findAllByReservation_Photographer_Id(Long photographerId, Pageable pageable);

    @Query(value = """
        SELECT r.*
        FROM review r
        JOIN reservation res
        ON r.reservation_id = res.reservation_id
        WHERE res.location &&  ST_MakeEnvelope(:minX, :minY, :maxX, :maxY, 4326)
        """,
        nativeQuery = true)
    List<Review> findAllByLocation(
        @Param("minX") Double minX,
        @Param("minY") Double minY,
        @Param("maxX") Double maxX,
        @Param("maxY") Double maxY
    );

    @Query("""
        SELECT AVG(r.photographerScore)
        FROM Review r
        JOIN r.reservation res
        WHERE res.photographer.id = :photographerId
        """)
    Double findAvgPhotographerScoreByPhotographerId(@Param("photographerId") Long photographerId);

    @Query("""
            SELECT COUNT(r)
            FROM Review r
            JOIN r.reservation res
            WHERE r.createdAt >= :fromDate
            AND res.photographer.id = :photographerId
        """)
    Long countRecentReviewsByPhotographerId(
        @Param("fromDate") LocalDateTime fromDate,
        @Param("photographerId") Long photographerId
    );
}
