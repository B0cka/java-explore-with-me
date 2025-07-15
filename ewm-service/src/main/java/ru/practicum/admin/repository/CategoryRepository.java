package ru.practicum.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.admin.model.Category;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findById(Long id);
    boolean existsByNameAndIdNot(String name, Long id);

    Page<Category> findAll(Pageable pageable);
}
