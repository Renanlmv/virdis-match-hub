package com.github.virdis.ms.space.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_spaces")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType type;

    @Column(nullable = false)
    private BigDecimal areaSquareMeters;

    @Column(nullable = false)
    private Integer residenceCount;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
