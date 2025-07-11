package ru.practicum.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

    boolean existsByCategoryId(Long categoryId);

}
