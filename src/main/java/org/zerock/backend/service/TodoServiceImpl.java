package org.zerock.backend.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.backend.domain.Todo;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.TodoDTO;
import org.zerock.backend.repository.TodoRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@Transactional
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;


    @Override
    public Long register(TodoDTO todoDTO) {

        Todo todo = modelMapper.map(todoDTO, Todo.class);


        Todo result = todoRepository.save(todo);

        return result.getTno();
    }

    @Override
    public TodoDTO get(Long tno) {

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);

        return todoDTO;
    }


    @Override
    public void modify(TodoDTO todoDTO) {

        Optional<Todo> result = todoRepository.findById(todoDTO.getTno());

        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDTO.getTitle());
        todo.changeComplete(todoDTO.isComplete());
        todo.changeDueDate(todoDTO.getDueDate());

        todoRepository.save(todo);
    }


    @Override
    public void remove(Long tno) {

        todoRepository.deleteById(tno);

    }

    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending()
        );

        Page<Todo> result = todoRepository.findAll(pageable);

        List<TodoDTO> dtoList =
                result.getContent().stream()
                        .map(todo -> modelMapper.map(todo, TodoDTO.class))
                        .collect(Collectors.toList());


        PageResponseDTO<TodoDTO> responseDTO
                = PageResponseDTO.<TodoDTO>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();

        return responseDTO;
    }


}
