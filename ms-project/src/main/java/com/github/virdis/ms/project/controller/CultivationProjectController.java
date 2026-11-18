package com.github.virdis.ms.project.controller;

import com.github.virdis.ms.project.dto.CultivationProjectRequestDTO;
import com.github.virdis.ms.project.dto.CultivationProjectResponseDTO;
import com.github.virdis.ms.project.dto.ProjectStatusRequestDTO;
import com.github.virdis.ms.project.service.CultivationProjectService;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(
        name = "Propostas de cultivo",
        description = """
                Operações de cadastro, consulta, atualização,
                exclusão e alteração de status das propostas
                de implantação de hortas urbanas.
                """
)
public class CultivationProjectController {

    private final CultivationProjectService projectService;

    @GetMapping
    @Operation(
            summary = "Listar propostas",
            description = "Retorna todas as propostas de cultivo cadastradas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Propostas retornadas com sucesso"
    )
    public ResponseEntity<List<CultivationProjectResponseDTO>>
    findAllProjects() {

        List<CultivationProjectResponseDTO> response =
                projectService.findAllProjects();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar proposta por ID",
            description = """
                    Retorna os dados da proposta correspondente
                    ao identificador informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Proposta encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Proposta não encontrada"
            )
    })
    public ResponseEntity<CultivationProjectResponseDTO>
    findProjectById(
            @PathVariable Long id
    ) {

        CultivationProjectResponseDTO response =
                projectService.findProjectById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar proposta",
            description = """
                    Cadastra uma proposta para um espaço disponível.
                    Antes do cadastro, o ms-project consulta o ms-space
                    por comunicação síncrona.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Proposta cadastrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Corpo da requisição inválido"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Espaço indisponível para propostas"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dados da proposta inválidos"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço de espaços indisponível"
            )
    })
    public ResponseEntity<CultivationProjectResponseDTO>
    createProject(
            @RequestBody
            @Valid
            CultivationProjectRequestDTO request
    ) {

        CultivationProjectResponseDTO response =
                projectService.saveProject(request);

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
            summary = "Atualizar proposta",
            description = """
                    Atualiza os dados da proposta sem alterar
                    o status ou a data de criação.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Proposta atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Proposta não encontrada"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Novo espaço indisponível"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dados de atualização inválidos"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço de espaços indisponível"
            )
    })
    public ResponseEntity<CultivationProjectResponseDTO>
    updateProject(
            @PathVariable Long id,
            @RequestBody
            @Valid
            CultivationProjectRequestDTO request
    ) {

        CultivationProjectResponseDTO response =
                projectService.updateProject(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Alterar status da proposta",
            description = """
                    Altera o status de uma proposta conforme as
                    regras de negócio. Dependendo da transição,
                    o ms-project também altera o status do espaço
                    por OpenFeign.
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
                    description = "Proposta não encontrada"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Transição de status inválida"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Status não informado"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço de espaços indisponível"
            )
    })
    public ResponseEntity<CultivationProjectResponseDTO>
    updateProjectStatus(
            @PathVariable Long id,
            @RequestBody
            @Valid
            ProjectStatusRequestDTO request
    ) {

        CultivationProjectResponseDTO response =
                projectService.updateProjectStatus(
                        id,
                        request.status()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir proposta",
            description = """
                    Exclui a proposta correspondente ao
                    identificador informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Proposta excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Proposta não encontrada"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Não foi possível excluir a proposta"
            )
    })
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id
    ) {

        projectService.deleteProjectById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}