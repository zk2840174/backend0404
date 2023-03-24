package org.zerock.backend.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.backend.domain.Product;
import org.zerock.backend.dto.PageRequestDTO;
import org.zerock.backend.dto.PageResponseDTO;
import org.zerock.backend.dto.ProductDTO;

public interface ProductSearch {

    Page<Product> search1(Pageable pageable);

    Page<Product> searchByTypes(PageRequestDTO pageRequestDTO);

    PageResponseDTO<ProductDTO> searchDTOByTypes(PageRequestDTO pageRequestDTO);
}
