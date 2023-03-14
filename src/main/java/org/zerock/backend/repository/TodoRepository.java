package org.zerock.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.backend.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
