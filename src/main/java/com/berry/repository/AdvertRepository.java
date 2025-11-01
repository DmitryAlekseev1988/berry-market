package com.berry.repository;

import com.berry.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long>, JpaSpecificationExecutor<Advert> {

  List<Advert> findByUserId(Long userId);

  Page<Advert> findByUserIdNotAndStatus(Long userId, com.berry.entity.AdvertStatus status, Pageable pageable);
}
