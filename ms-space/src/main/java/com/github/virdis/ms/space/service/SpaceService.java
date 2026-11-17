package com.github.virdis.ms.space.service;

import com.github.virdis.ms.space.dto.SpaceRequestDTO;
import com.github.virdis.ms.space.dto.SpaceResponseDTO;
import com.github.virdis.ms.space.entity.Space;
import com.github.virdis.ms.space.entity.SpaceStatus;
import com.github.virdis.ms.space.exception.DatabaseException;
import com.github.virdis.ms.space.exception.InvalidStatusTransitionException;
import com.github.virdis.ms.space.exception.ResourceNotFoundException;
import com.github.virdis.ms.space.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findAllSpaces() {

        List<Space> list = spaceRepository.findAll();

        return list
                .stream()
                .map(SpaceResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public SpaceResponseDTO findSpaceById(Long id) {

        Space space = findEntityById(id);

        return new SpaceResponseDTO(space);
    }

    @Transactional
    public SpaceResponseDTO saveSpace(
            SpaceRequestDTO request
    ) {

        Space space = new Space();

        copyDtoToEntity(request, space);

        space.setStatus(SpaceStatus.AVAILABLE);
        space.setCreatedAt(LocalDateTime.now());

        space = spaceRepository.save(space);

        return new SpaceResponseDTO(space);
    }

    @Transactional
    public SpaceResponseDTO updateSpace(
            Long id,
            SpaceRequestDTO request
    ) {

        Space space = findEntityById(id);

        copyDtoToEntity(request, space);

        space = spaceRepository.save(space);

        return new SpaceResponseDTO(space);
    }

    @Transactional
    public void deleteSpaceById(Long id) {

        if (!spaceRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Espaço não encontrado. ID: " + id
            );
        }

        try {
            spaceRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(
                    "Não foi possível excluir o espaço. "
                            + "O registro possui dados relacionados."
            );
        }
    }

    @Transactional
    public SpaceResponseDTO updateSpaceStatus(
            Long id,
            SpaceStatus newStatus
    ) {

        Space space = findEntityById(id);

        validateStatusTransition(
                space.getStatus(),
                newStatus
        );

        space.setStatus(newStatus);

        space = spaceRepository.save(space);

        return new SpaceResponseDTO(space);
    }

    private Space findEntityById(Long id) {

        return spaceRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Espaço não encontrado. ID: " + id
                        )
                );
    }

    private void validateStatusTransition(
            SpaceStatus currentStatus,
            SpaceStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new InvalidStatusTransitionException(
                    "O espaço já possui o status informado."
            );
        }

        if (
                newStatus == SpaceStatus.MATCHED
                        && currentStatus
                        != SpaceStatus.AVAILABLE
        ) {
            throw new InvalidStatusTransitionException(
                    "Somente espaços disponíveis podem "
                            + "ser vinculados a um projeto."
            );
        }
    }

    private void copyDtoToEntity(
            SpaceRequestDTO request,
            Space space
    ) {

        space.setOwnerName(request.ownerName());
        space.setTitle(request.title());
        space.setType(request.type());
        space.setAreaSquareMeters(
                request.areaSquareMeters()
        );
        space.setResidenceCount(
                request.residenceCount()
        );
        space.setCity(request.city());
        space.setState(
                request.state().toUpperCase()
        );
    }
}
