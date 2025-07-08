package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h.app, h.uri, COUNT(h.ip) AS cnt " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY cnt DESC")
    List<Object[]> findStats(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             @Param("uris") List<String> uris);


    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) AS cnt " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY cnt DESC")
    List<Object[]> findUniqueStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT h.app, h.uri, COUNT(h.ip) AS cnt " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY cnt DESC")
    List<Object[]> findStatsWithoutUris(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) AS cnt " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY cnt DESC")
    List<Object[]> findUniqueStatsWithoutUris(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);
}
