package org.zerock.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    @Builder.Default
    private String status = "Y";

    @Builder.Default
    private List<ProductImageDTO> productImageDTOList = new ArrayList<>();

}
