package com.github.virdis.ms.space.controller;

import com.github.virdis.ms.space.dto.SpaceRequestDTO;
import com.github.virdis.ms.space.dto.SpaceResponseDTO;
import com.github.virdis.ms.space.dto.SpaceStatusRequestDTO;
import com.github.virdis.ms.space.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spaces")
@RequiredArgsConstructor
@Tag(
        name = "Espaços",
        description = """
                Operações de cadastro, consulta, atualização,
                exclusão e alteração de status dos espaços.
                """
)
public class SpaceController {

    private final SpaceService spaceService;

    @GetMapping
    @Operation(
            summary = "Listar espaços",
            description = "Retorna todos os espaços cadastrados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Espaços retornados com sucesso"
    )
    public ResponseEntity<List<SpaceResponseDTO>>
    findAllSpaces() {

        List<SpaceResponseDTO> response =
                spaceService.findAllSpaces();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar espaço por ID",
            description = """
                    Retorna os dados do espaço correspondente
                    ao identificador informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Espaço encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Espaço não encontrado"
            )
    })
    public ResponseEntity<SpaceResponseDTO>
    findSpaceById(
            @PathVariable Long id
    ) {

        SpaceResponseDTO response =
                spaceService.findSpaceById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar espaço",
            description = """
                    Cadastra um novo espaço com status inicial
                    AVAILABLE.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Espaço cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Corpo da requisição inválido"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dados de cadastro inválidos"
            )
    })
    public ResponseEntity<SpaceResponseDTO>
    createSpace(
            @RequestBody
            @Valid
            SpaceRequestDTO request
    ) {

        SpaceResponseDTO response =
                spaceService.saveSpace(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar espaço",
            description = """
                    Atualiza os dados cadastrais do espaço sem
                    alterar o status ou a data de criação.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Espaço atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Espaço não encontrado"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dados de atualização inválidos"
            )
    })
    public ResponseEntity<SpaceResponseDTO>
    updateSpace(
            @PathVariable Long id,
            @RequestBody
            @Valid
            SpaceRequestDTO request
    ) {

        SpaceResponseDTO response =
                spaceService.updateSpace(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Alterar status do espaço",
            description = """
                    Altera o status do espaço conforme as regras
                    de negócio. Esse endpoint será utilizado pelo
                    microsserviço de projetos por comunicação
                    síncrona.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Espaço não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Transição de status inválida"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Status não informado"
            )
    })
    public ResponseEntity<SpaceResponseDTO>
    updateSpaceStatus(
            @PathVariable Long id,
            @RequestBody
            @Valid
            SpaceStatusRequestDTO request
    ) {

        SpaceResponseDTO response =
                spaceService.updateSpaceStatus(
                        id,
                        request.status()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir espaço",
            description = """
                    Exclui o espaço correspondente ao
                    identificador informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Espaço excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Espaço não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Não foi possível excluir o espaço"
            )
    })
    public ResponseEntity<Void>
    deleteSpace(
            @PathVariable Long id
    ) {

        spaceService.deleteSpaceById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}