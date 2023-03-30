package org.zerock.backend.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.backend.dto.ProductImageDTO;
import org.zerock.backend.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Log4j2
public class UploadUtil {

    @Value("${org.zerock.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;


    public void deleteFiles(List<String> fileNames) {

        fileNames.forEach(fileName -> {
            try{

                Files.delete(Paths.get(uploadPath, fileName));

            }catch(Exception e){

            }

        });

    }

    public List<ProductImageDTO>  upload(List<MultipartFile> files) {

        List<ProductImageDTO> uploadResultDTOList = new ArrayList<>();

        if(files == null || files.size() == 0){
            return uploadResultDTOList;
        }

        for (int i = 0; i < files.size() ; i++) {

            MultipartFile multipartFile = files.get(i);

            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();

            Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
            try {
                multipartFile.transferTo(savePath);

                //make thumbnail
                if(multipartFile.getContentType().startsWith("image")){

                    Path thumbnailPath = Paths.get(uploadPath, "s_"+uuid + "_" + originalName);

                    Thumbnails.of(savePath.toFile())
                            .size(100,100)
                            .toFile(thumbnailPath.toFile());

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadResultDTOList.add(ProductImageDTO.builder()
                    .uuid(uuid)
                    .fileName(originalName)
                    .ord(i)
                    .build()
            );

        };

        return uploadResultDTOList;

    }
}
