package org.zerock.backend.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String status;


    public void changePrice(int price) {
        this.price = price;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void changeName(String name){
        this.pname = name;
    }

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void addImage(ProductImage image) {

        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void clearList() {
        this.imageList.clear();
    }


}
