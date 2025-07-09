package ru.practicum.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.admin.model.User;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE (:ids IS NULL OR id IN :ids) " +
            "ORDER BY id LIMIT :size OFFSET :from",
            nativeQuery = true)
    List<User> getUsersNative(@Param("ids") List<Long> ids,
                              @Param("from") Long from,
                              @Param("size") Long size);

}
