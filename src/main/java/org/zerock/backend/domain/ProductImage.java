package org.zerock.backend.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String uuid;

    private String fileName;

    private int ord;

    public void setOrd(int ord){
        this.ord = ord;
    }

}
