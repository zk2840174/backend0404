package org.zerock.backend.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;
import org.zerock.backend.dto.ProductImageDTO;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testRegister() {

        ProductDTO productDTO = ProductDTO.builder()
                .pname("test product1")
                .price(4000)
                .productImageDTOList(
                        List.of(ProductImageDTO.builder().uuid(UUID.randomUUID().toString()).ord(0).fileName("testF1").build(),
                                ProductImageDTO.builder().uuid(UUID.randomUUID().toString()).ord(1).fileName("testF2").build()
                                ))
                .build();

        productService.register(productDTO);

    }

    @Test
    public void testRead() {


        ProductDTO productDTO = productService.get(3L);

        log.info(productDTO);

    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).build();

        PageResponseDTO<ProductDTO> responseDTO = productService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testModify() {


        ProductDTO productDTO = productService.get(3L);

        productDTO.setPname("Modified Product 3");
        productDTO.setPrice(10000);

        List<ProductImageDTO>  imageList =       List.of(ProductImageDTO.builder().uuid(UUID.randomUUID().toString()).ord(0).fileName("testF1").build(),
                        ProductImageDTO.builder().uuid(UUID.randomUUID().toString()).ord(1).fileName("testF2").build());

        productDTO.setProductImageDTOList(imageList);

        productService.modify(productDTO);
    }

}






















