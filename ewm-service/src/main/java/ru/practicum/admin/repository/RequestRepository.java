package ru.practicum.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.admin.model.Event;
import ru.practicum.admin.model.Request;
import ru.practicum.admin.model.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.event.id = :eventId AND r.status = :status")
    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByEventId(Long eventId);

    Optional<List<Request>> findByEventIdAndIdIn(Long eventId, List<Long> id);
}
