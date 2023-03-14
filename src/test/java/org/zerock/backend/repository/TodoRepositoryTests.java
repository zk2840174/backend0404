package org.zerock.backend.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.backend.domain.Todo;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    TodoRepository todoRepository;


    @Test
    public void testCreate() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Todo todo = Todo.builder()
                    .title("Title..."+i)
                    .writer("user.." + (i % 10))
                    .dueDate(LocalDate.of(2025,12,31))
                    .build();
            todoRepository.save(todo);
        });
    }

    @Test
    public void testRead() {

        Long tno = 100L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info(todo);
    }


    @Test
    public void testUpdate() {

        Long tno = 100L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.of(2023,1,1));

        todoRepository.save(todo);
    }

    @Test
    public void testDelete() {

        Long tno = 1L;

        todoRepository.deleteById(tno);

    }

    @Test
    public void testPaging() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());


        Page<Todo> result = todoRepository.findAll(pageable);

        //long
        log.info("TOTAL COUNT: " + result.getTotalElements());

        //int
        log.info("TOTAL PAGE" + result.getTotalPages());

        List<Todo> todoList = result.getContent();

        todoList.forEach(todo -> log.info(todo));

    }



}
