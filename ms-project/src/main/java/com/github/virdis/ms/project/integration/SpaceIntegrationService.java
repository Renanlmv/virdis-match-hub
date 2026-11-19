package com.github.virdis.ms.project.integration;

import com.github.virdis.ms.project.client.SpaceClient;
import com.github.virdis.ms.project.dto.SpaceResponseDTO;
import com.github.virdis.ms.project.dto.SpaceStatusRequestDTO;
import com.github.virdis.ms.project.exception.SpaceServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceIntegrationService {

    private final SpaceClient spaceClient;

    @CircuitBreaker(
            name = "spaceService",
            fallbackMethod = "findSpaceByIdFallback"
    )
    public SpaceResponseDTO findSpaceById(
            Long spaceId
    ) {

        return spaceClient.findSpaceById(spaceId);
    }

    private SpaceResponseDTO findSpaceByIdFallback(
            Long spaceId,
            Throwable throwable
    ) {

        throw new SpaceServiceUnavailableException(
                "Não foi possível consultar o espaço de ID "
                        + spaceId
                        + ". O serviço de espaços está "
                        + "temporariamente indisponível."
        );
    }

    @CircuitBreaker(
            name = "spaceService",
            fallbackMethod = "updateSpaceStatusFallback"
    )
    public SpaceResponseDTO updateSpaceStatus(
            Long spaceId,
            String newStatus
    ) {

        SpaceStatusRequestDTO request =
                new SpaceStatusRequestDTO(
                        newStatus
                );

        return spaceClient.updateSpaceStatus(
                spaceId,
                request
        );
    }

    private SpaceResponseDTO updateSpaceStatusFallback(
            Long spaceId,
            String newStatus,
            Throwable throwable
    ) {

        throw new SpaceServiceUnavailableException(
                "Não foi possível alterar o status do espaço de ID "
                        + spaceId
                        + " para "
                        + newStatus
                        + ". O serviço de espaços está "
                        + "temporariamente indisponível."
        );
    }
}