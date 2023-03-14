package org.zerock.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.TodoDTO;
import org.zerock.backend.service.TodoService;


import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/api1")
@RequiredArgsConstructor
public class SampleTodoController {

    private final TodoService todoService;


    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno){

        log.info("tno: " + tno);

        return todoService.get(tno);

    }

    @PostMapping("/")
    public Map<String, Long> add( @RequestBody TodoDTO todoDTO){

        log.info("todoDTO: " + todoDTO);

        return Map.of("result", todoService.register(todoDTO));

    }

    @GetMapping("/getArr")
    public String[] getArr() {

        return new String[] {"AAA","BBB","CCC"};

    }

    @DeleteMapping("/{tno}")
    public Map<String, Long> remove(@PathVariable("tno") Long tno){

        log.info("tno: " + tno);

        todoService.remove(tno);

        return Map.of("result", tno);

    }

    @PutMapping("/{tno}")
    public Map<String, Long> modify(@PathVariable("tno") Long tno,
                                    @RequestBody TodoDTO todoDTO) {


        //tno 값은 일치 시키도록
        todoDTO.setTno(tno);

        todoService.modify(todoDTO);

        return Map.of("result", tno);

    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){

        return todoService.getList(pageRequestDTO);
    }


}
