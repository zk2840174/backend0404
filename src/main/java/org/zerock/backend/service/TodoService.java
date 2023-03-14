package org.zerock.backend.service;


import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.TodoDTO;

public interface TodoService {

    Long register(TodoDTO todoDTO);

    TodoDTO get(Long tno);

    void modify(TodoDTO todoDTO);

    void remove(Long tno);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);
}
