package org.zerock.backend.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zerock.backend.dto.upload.UploadFileDTO;
import org.zerock.backend.dto.upload.UploadResultDTO;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
@RequestMapping("/upload")
public class UploadController {

    @Value("${org.zerock.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;


    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {

        log.info(uploadFileDTO);

        final List<UploadResultDTO> list = new ArrayList<>();

        if (uploadFileDTO.getFiles() != null) {

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                try {
                    multipartFile.transferTo(savePath);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .build()
                );

            });//end each

        }
        return list;
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+ File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){

        log.info("remove file name : " + fileName);

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }


}
