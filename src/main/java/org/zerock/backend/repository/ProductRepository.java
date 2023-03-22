package org.zerock.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.backend.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0")
    Page<Object[]> listPage(Pageable pageable);


    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product  p where p.pno = :pno")
    Product getOne(Long pno);
}
