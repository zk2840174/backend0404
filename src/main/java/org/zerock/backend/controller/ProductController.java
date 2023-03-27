package org.zerock.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;
import org.zerock.backend.dto.ProductImageDTO;
import org.zerock.backend.dto.upload.UploadResultDTO;
import org.zerock.backend.service.ProductService;
import org.zerock.backend.util.UploadUtil;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/products")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final UploadUtil uploadUtil;


    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO){

        //return productService.list(pageRequestDTO);
        return productService.listSearch(pageRequestDTO);
    }

    @PostMapping(value ="" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, Long> register(ProductDTO productDTO){
        log.info("register.........................");

        log.info(productDTO);

        List<ProductImageDTO> imageDTOList = uploadUtil.upload(productDTO.getFiles());

        productDTO.setProductImageDTOList(imageDTOList);

        log.info(productDTO);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }


    @GetMapping("/{pno}")
    public ProductDTO get( @PathVariable(name = "pno") Long pno){

        log.info("get Product..............." + pno);


        return productService.get(pno);

    }

    @PutMapping("/{pno}")
    public Map<String, String>  update( @PathVariable(name="pno")Long pno,  @RequestBody ProductDTO productDTO ){

        productDTO.setPno(pno);

        log.info("modify............." + productDTO);

        productService.modify(productDTO);

        return Map.of("result", "updated");

    }

    @DeleteMapping("/{pno}")
    public Map<String, String> delete(@PathVariable(name="pno")Long pno) {

        log.info("delete..............." + pno);

        productService.remove(pno);

        return Map.of("result", "deleted");

    }




}

