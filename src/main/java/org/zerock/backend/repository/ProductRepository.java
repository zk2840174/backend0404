package org.zerock.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.backend.domain.Product;
import org.zerock.backend.repository.search.ProductSearch;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {


    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 or pi is null")
    Page<Object[]> listPage(Pageable pageable);


    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product  p where p.pno = :pno")
    Product getOne(Long pno);
}
