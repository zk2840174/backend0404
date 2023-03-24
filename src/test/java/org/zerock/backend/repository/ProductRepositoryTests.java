package org.zerock.backend.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.backend.domain.Product;
import org.zerock.backend.domain.ProductImage;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() {

        for(int i = 0; i < 100; i++){

            Product product = Product.builder()
                    .pname("Product_" +i)
                    .price(1000 + i)
                    .status("Y")
                    .build();

            product.addImage(ProductImage.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName(i + "file1.jpg")
                    .build());

            product.addImage(ProductImage.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName(i + "_file2.jpg")
                    .build());

            productRepository.save(product);
        }//end for

    }

    @Test
    public void testList() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.listPage(pageable);

        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));

            log.info("----------------------");
        });
    }

    @Test
    public void testRead() {

        Product product = productRepository.getOne(99L);

        log.info(product);

        log.info(product.getImageList());

    }

    @Transactional
    @Commit
    @Test
    public void testModify() {


        Product product = productRepository.findById(3L).get();

        product.changeName("Product 3");

        product.clearList();

        product.addImage(ProductImage.builder().uuid(UUID.randomUUID().toString()).fileName("test1.jpg").build());
        product.addImage(ProductImage.builder().uuid(UUID.randomUUID().toString()).fileName("test2.jpg").build());
        product.addImage(ProductImage.builder().uuid(UUID.randomUUID().toString()).fileName("test3.jpg").build());

        productRepository.save(product);

    }



    @Test
    public void testSearch1() {

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> result = productRepository.search1(pageable);

        log.info(result);
    }

    @Test
    public void testSearch2() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .typeStr("t")
                .keyword("1")
                .build();

        Page<Product> result = productRepository.searchByTypes(pageRequestDTO);

        log.info(result);

        result.getContent().forEach(product -> {
            log.info(product);
        });
    }


    @Test
    public void testSearchDTO() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
//                .typeStr("t")
//                .keyword("1")
                .build();

        PageResponseDTO<ProductDTO> result = productRepository.searchDTOByTypes(pageRequestDTO);

        log.info(result);

        log.info("-------------------------");
        result.getDtoList().forEach(productDTO -> {
            log.info(productDTO);
        });

        log.info("================================");

    }

}
