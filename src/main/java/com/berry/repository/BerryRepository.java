package com.berry.repository;

import com.berry.entity.Berry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BerryRepository extends JpaRepository<Berry, Long> {

  List<Berry> findByGroupId(Long groupId);

  boolean existsByName(String name);
}
