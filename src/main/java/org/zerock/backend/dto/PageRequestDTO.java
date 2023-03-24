package org.zerock.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String typeStr;
    private String[] types;

    private String keyword;

    public String[] getTypes() {

        if(typeStr == null) {
            return null;
        }
        return typeStr.split("");
    }
}
