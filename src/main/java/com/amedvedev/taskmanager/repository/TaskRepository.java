package com.amedvedev.taskmanager.repository;

import com.amedvedev.taskmanager.entitiy.Task;
import com.amedvedev.taskmanager.entitiy.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user, Sort sort);

    @Query("SELECT t FROM Task t JOIN t.categories c WHERE t.user = :user AND (c.name IN :categories OR t.priority IN :priorities)")
    List<Task> findAllByUserAndCategoriesOrPriorityIn(
            @Param("user") User user,
            @Param("categories") Collection<String> categories,
            @Param("priorities") Collection<String> priorities,
            Sort sort
    );

    List<Task> findAllByUserAndCategoriesIn(User user, Collection<String> categories, Sort sort);
    List<Task> findAllByUserAndPriorityIn(User user, Collection<String> priorities, Sort sort);

    @Query("""
            SELECT t FROM Task t
            WHERE t.user = :user AND
            (LOWER(t.name) LIKE LOWER(CONCAT('%', :term, '%')) OR
            LOWER(t.description) LIKE LOWER(CONCAT('%', :term, '%')))
            """)
    List<Task> findAllByUserAndNameOrDescriptionContainingIgnoreCase(User user, @Param("term") String term);
}
