package com.berry.repository;

import com.berry.entity.BerryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BerryGroupRepository extends JpaRepository<BerryGroup, Long> {

  Optional<BerryGroup> findByName(String name);
}
