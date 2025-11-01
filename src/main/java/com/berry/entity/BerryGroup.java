package com.berry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "berry_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BerryGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(length = 500)
  private String description;

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Berry> berries = new ArrayList<>();
}
