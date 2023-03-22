package org.zerock.backend.service;

import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;

public interface ProductService {

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO);

    void modify(ProductDTO productDTO);

    void remove(Long pno);

}
