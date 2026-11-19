package com.github.virdis.ms.project.client;

import com.github.virdis.ms.project.dto.SpaceResponseDTO;
import com.github.virdis.ms.project.dto.SpaceStatusRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-space")
public interface SpaceClient {

    @GetMapping("/api/v1/spaces/{id}")
    SpaceResponseDTO findSpaceById(
            @PathVariable("id") Long id
    );

    @PutMapping("/api/v1/spaces/{id}/status")
    SpaceResponseDTO updateSpaceStatus(
            @PathVariable("id") Long id,
            @RequestBody SpaceStatusRequestDTO request
    );
}