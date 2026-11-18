package com.github.virdis.ms.project.service;

import com.github.virdis.ms.project.dto.CultivationProjectRequestDTO;
import com.github.virdis.ms.project.dto.CultivationProjectResponseDTO;
import com.github.virdis.ms.project.dto.SpaceResponseDTO;
import com.github.virdis.ms.project.entity.CultivationProject;
import com.github.virdis.ms.project.entity.ProjectStatus;
import com.github.virdis.ms.project.exception.DatabaseException;
import com.github.virdis.ms.project.exception.InvalidStatusTransitionException;
import com.github.virdis.ms.project.exception.ResourceNotFoundException;
import com.github.virdis.ms.project.integration.SpaceIntegrationService;
import com.github.virdis.ms.project.repository.CultivationProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CultivationProjectService {

    private final CultivationProjectRepository projectRepository;
    private final SpaceIntegrationService spaceIntegrationService;

    @Transactional(readOnly = true)
    public List<CultivationProjectResponseDTO> findAllProjects() {

        List<CultivationProject> projects =
                projectRepository.findAll();

        return projects
                .stream()
                .map(CultivationProjectResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CultivationProjectResponseDTO findProjectById(
            Long id
    ) {

        CultivationProject project =
                findEntityById(id);

        return new CultivationProjectResponseDTO(
                project
        );
    }

    @Transactional
    public CultivationProjectResponseDTO saveProject(
            CultivationProjectRequestDTO request
    ) {

        SpaceResponseDTO space =
                spaceIntegrationService.findSpaceById(
                        request.spaceId()
                );

        validateAvailableSpace(space);

        CultivationProject project =
                new CultivationProject();

        copyDtoToEntity(
                request,
                project
        );

        project.setStatus(
                ProjectStatus.CREATED
        );

        project.setCreatedAt(
                LocalDateTime.now()
        );

        project = projectRepository.save(
                project
        );

        return new CultivationProjectResponseDTO(
                project
        );
    }

    @Transactional
    public CultivationProjectResponseDTO updateProject(
            Long id,
            CultivationProjectRequestDTO request
    ) {

        CultivationProject project =
                findEntityById(id);

        if (!project.getSpaceId().equals(request.spaceId())) {

            SpaceResponseDTO space =
                    spaceIntegrationService.findSpaceById(
                            request.spaceId()
                    );

            validateAvailableSpace(space);
        }

        copyDtoToEntity(
                request,
                project
        );

        project = projectRepository.save(
                project
        );

        return new CultivationProjectResponseDTO(
                project
        );
    }

    @Transactional
    public void deleteProjectById(
            Long id
    ) {

        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Proposta não encontrada. ID: " + id
            );
        }

        try {
            projectRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(
                    "Não foi possível excluir a proposta. "
                            + "O registro possui dados relacionados."
            );
        }
    }

    @Transactional
    public CultivationProjectResponseDTO updateProjectStatus(
            Long id,
            ProjectStatus newStatus
    ) {

        CultivationProject project =
                findEntityById(id);

        validateStatusTransition(
                project.getStatus(),
                newStatus
        );

        updateRelatedSpaceStatus(
                project,
                newStatus
        );

        project.setStatus(newStatus);

        project = projectRepository.save(
                project
        );

        return new CultivationProjectResponseDTO(
                project
        );
    }

    private CultivationProject findEntityById(
            Long id
    ) {

        return projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Proposta não encontrada. ID: "
                                        + id
                        )
                );
    }

    private void validateAvailableSpace(
            SpaceResponseDTO space
    ) {

        if (!"AVAILABLE".equals(space.status())) {
            throw new InvalidStatusTransitionException(
                    "O espaço de ID "
                            + space.id()
                            + " não está disponível para "
                            + "receber propostas."
            );
        }
    }

    private void validateStatusTransition(
            ProjectStatus currentStatus,
            ProjectStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new InvalidStatusTransitionException(
                    "A proposta já possui o status informado."
            );
        }

        switch (currentStatus) {

            case CREATED -> {

                if (
                        newStatus != ProjectStatus.ANALYZING
                                && newStatus != ProjectStatus.CANCELED
                ) {
                    throwInvalidStatusTransition(
                            currentStatus,
                            newStatus
                    );
                }
            }

            case ANALYZING -> {

                if (
                        newStatus != ProjectStatus.APPROVED
                                && newStatus != ProjectStatus.REJECTED
                                && newStatus != ProjectStatus.CANCELED
                ) {
                    throwInvalidStatusTransition(
                            currentStatus,
                            newStatus
                    );
                }
            }

            case APPROVED, REJECTED, CANCELED ->
                    throwInvalidStatusTransition(
                            currentStatus,
                            newStatus
                    );
        }
    }

    private void updateRelatedSpaceStatus(
            CultivationProject project,
            ProjectStatus newStatus
    ) {

        switch (newStatus) {

            case ANALYZING ->
                    spaceIntegrationService.updateSpaceStatus(
                            project.getSpaceId(),
                            "UNDER_EVALUATION"
                    );

            case APPROVED ->
                    spaceIntegrationService.updateSpaceStatus(
                            project.getSpaceId(),
                            "MATCHED"
                    );

            case REJECTED ->
                    spaceIntegrationService.updateSpaceStatus(
                            project.getSpaceId(),
                            "AVAILABLE"
                    );

            case CANCELED -> {

                if (
                        project.getStatus()
                                == ProjectStatus.ANALYZING
                ) {
                    spaceIntegrationService.updateSpaceStatus(
                            project.getSpaceId(),
                            "AVAILABLE"
                    );
                }
            }

            case CREATED -> {
            }
        }
    }

    private void throwInvalidStatusTransition(
            ProjectStatus currentStatus,
            ProjectStatus newStatus
    ) {

        throw new InvalidStatusTransitionException(
                "Não é possível alterar o status da proposta de "
                        + currentStatus
                        + " para "
                        + newStatus
                        + "."
        );
    }

    private void copyDtoToEntity(
            CultivationProjectRequestDTO request,
            CultivationProject project
    ) {

        project.setSpaceId(
                request.spaceId()
        );

        project.setProducerName(
                request.producerName()
        );

        project.setProducerEmail(
                request.producerEmail()
        );

        project.setCultivationType(
                request.cultivationType()
        );

        project.setProposalDescription(
                request.proposalDescription()
        );

        project.setEstimatedCapacity(
                request.estimatedCapacity()
        );

        project.setOfferedMonthlyRent(
                request.offeredMonthlyRent()
        );
    }
}