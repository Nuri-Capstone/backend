package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Integer> {
    @Query(value = "SELECT * FROM ranking WHERE id = :userId AND EXTRACT(YEAR FROM date) = :year AND EXTRACT(MONTH FROM date) = :month", nativeQuery = true)
    Optional<Ranking> findByUserIdAndYearAndMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT id, SUM(msg_total_cnt) as msg_total FROM ranking WHERE id IN (:userIds) GROUP BY id;", nativeQuery = true)
    List<Object[]> getMsgCnt(@Param("userIds") List<Long> userIds);

    @Query("SELECT r FROM Ranking r WHERE r.user.id = :userId")
    List<Ranking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(*) " +
            "FROM Ranking r " +
            "GROUP BY r.user.id")
    List<Object> findAllRankingsGroupedByUserId();
}