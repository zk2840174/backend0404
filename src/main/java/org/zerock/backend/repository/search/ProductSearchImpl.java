package org.zerock.backend.repository.search;


import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.zerock.backend.domain.Product;
import org.zerock.backend.domain.ProductImage;
import org.zerock.backend.domain.QProduct;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.backend.domain.QProductImage;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;
import org.zerock.backend.dto.ProductImageDTO;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport  implements ProductSearch{

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> search1(Pageable pageable) {

        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product);

        this.getQuerydsl().applyPagination(pageable, query);

        List<Product> productList = query.fetch();

        log.info("productList...............");

        log.info(productList);

        long total  = query.fetchCount();

        return new PageImpl<>(productList, pageable, total);
    }

    @Override
    public Page<Product> searchByTypes(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product);

        if(types != null && keyword  != null){
            for(String type: types){
                switch (type){
                    case "t":
                        query.where(product.pname.contains(keyword));
                        break;
                }
            }
        }

        this.getQuerydsl().applyPagination(pageable, query);

        List<Product> productList = query.fetch();

        log.info("productList...............");

        log.info(productList);

        long total  = query.fetchCount();

        return new PageImpl<>(productList, pageable, total);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchDTOByTypes(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.imageList, productImage);

        query.where(productImage.ord.eq(1));

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(product, productImage);


        this.getQuerydsl().applyPagination(pageable, tupleJPQLQuery);


        List<Tuple> fetchResult = tupleJPQLQuery.fetch();

        long totalCount = tupleJPQLQuery.fetchCount();

        List<ProductDTO> productDTOList = fetchResult.stream().map(tuple -> {

            Product product1 = (Product) tuple.get(product);

            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product1.getPno())
                    .pname(product1.getPname())
                    .price(product1.getPrice())
                    .status(product1.getStatus())
                    .build();

            ProductImage productImage1 = (ProductImage) tuple.get(productImage);

            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                    .uuid(productImage1.getUuid())
                    .fileName(productImage1.getFileName())
                    .ord(productImage1.getOrd())
                    .build();
            productDTO.setProductImageDTOList(List.of(productImageDTO));

            return productDTO;

        }).collect(Collectors.toList());


        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(productDTOList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
