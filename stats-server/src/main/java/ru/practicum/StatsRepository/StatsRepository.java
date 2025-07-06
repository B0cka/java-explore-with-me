package ru.practicum.StatsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.Model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h.app, h.uri, COUNT(h.ip) " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findStats(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             @Param("uris") List<String> uris);


    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) " +
            "FROM Hit h " +
            "WHERE h.timeStamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findUniqueStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);
}
