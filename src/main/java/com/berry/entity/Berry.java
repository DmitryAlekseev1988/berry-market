package com.berry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "berries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Berry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id", nullable = false)
  private BerryGroup group;

  @OneToMany(mappedBy = "berry", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Advert> adverts = new ArrayList<>();
}
