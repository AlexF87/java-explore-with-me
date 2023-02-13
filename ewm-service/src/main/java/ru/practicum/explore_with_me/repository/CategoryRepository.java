package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);

    @Query("select c from Category as c where c.name like ?1")
    Category findByName(String name);
}
