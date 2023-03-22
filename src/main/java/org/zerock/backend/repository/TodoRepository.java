package org.zerock.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.backend.domain.Product;
import org.zerock.backend.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {





}
