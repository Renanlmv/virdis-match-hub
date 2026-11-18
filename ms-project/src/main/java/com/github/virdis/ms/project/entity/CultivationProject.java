package com.github.virdis.ms.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_cultivation_projects")
public class CultivationProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long spaceId;

    @Column(nullable = false)
    private String producerName;

    @Column(nullable = false)
    private String producerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CultivationType cultivationType;

    @Column(nullable = false, length = 1000)
    private String proposalDescription;

    @Column(nullable = false)
    private Integer estimatedCapacity;

    @Column(nullable = false)
    private BigDecimal offeredMonthlyRent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}