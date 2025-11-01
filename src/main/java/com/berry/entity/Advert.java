package com.berry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "adverts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "berry_id", nullable = false)
  private Berry berry;

  @Column(nullable = false)
  private Double quantity; // количество в кг

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price; // цена за кг

  @Column(length = 500)
  private String address; // адрес текстом

  @Column(length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AdvertType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AdvertStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
