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
import org.zerock.backend.domain.Product;
import org.zerock.backend.domain.ProductImage;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;
import org.zerock.backend.dto.ProductImageDTO;
import org.zerock.backend.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public Long register(ProductDTO productDTO) {


        Product product = modelMapper.map(productDTO, Product.class);

        log.info(product);
        log.info(product.getImageList());

        int ord = 0;
        for (ProductImage productImage : product.getImageList()) {

            productImage.setOrd(ord++);
        }

        Product result = productRepository.save(product);

        return result.getPno();
    }

    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    @Override
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        Page<Object[]> result = productRepository.listPage(pageable);

        List<ProductDTO> productDTOList = result.getContent().stream().map(arr -> {

            Product product = (Product)arr[0];

            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .price(product.getPrice())
                    .status(product.getStatus())
                    .build();

            ProductImageDTO productImageDTO = modelMapper.map(arr[1], ProductImageDTO.class);

            productDTO.getProductImageDTOList().add(productImageDTO);

            return productDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(productDTOList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();
    }

    @Override
    public void modify(ProductDTO productDTO) {

        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        product.changeName(productDTO.getPname());
        product.setStatus(productDTO.getStatus());
        product.changePrice(productDTO.getPrice());

        product.clearList();

        productDTO.getProductImageDTOList().forEach( productImageDTO -> {

            ProductImage productImage = modelMapper.map(productImageDTO, ProductImage.class);

            product.addImage(productImage);

        });

        productRepository.save(product);

    }

    @Override
    public void remove(Long pno) {


        productRepository.deleteById(pno);

    }


}
