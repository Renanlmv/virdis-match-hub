package com.github.virdis.ms.project.repository;

import com.github.virdis.ms.project.entity.CultivationProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CultivationProjectRepository
        extends JpaRepository<CultivationProject, Long> {
}