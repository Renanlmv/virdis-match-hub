package com.github.virdis.ms.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        description = "Dados do espaço retornados pelo ms-space"
)
public record SpaceResponseDTO(

        @Schema(
                description = "Identificador do espaço",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Identificador do usuário proprietário do anúncio",
                example = "user-001"
        )
        String ownerId,

        @Schema(
                description = "Nome do anúncio do espaço",
                example = "Cobertura disponível para horta urbana"
        )
        String title,

        @Schema(
                description = "Bairro onde o espaço está localizado",
                example = "Vila Mariana"
        )
        String neighborhood,

        @Schema(
                description = "Tipo do espaço",
                example = "ROOFTOP"
        )
        String type,

        @Schema(
                description = "Área disponível em metros quadrados",
                example = "240.50"
        )
        BigDecimal areaSquareMeters,

        @Schema(
                description = "Valor mensal pretendido pelo uso do espaço",
                example = "1800.00"
        )
        BigDecimal monthlyRent,

        @Schema(
                description = "Descrição das características do espaço"
        )
        String description,

        @Schema(
                description = "URL pública da imagem do espaço"
        )
        String imageUrl,

        @Schema(
                description = "Nome da pessoa autorizada para contato",
                example = "Marina Souza"
        )
        String contactName,

        @Schema(
                description = "E-mail autorizado para contato",
                example = "marina.souza@email.com"
        )
        String contactEmail,

        @Schema(
                description = "Quantidade de residências ou apartamentos beneficiados",
                example = "60"
        )
        Integer residenceCount,

        @Schema(
                description = "Cidade onde o espaço está localizado",
                example = "São Paulo"
        )
        String city,

        @Schema(
                description = "Sigla do estado",
                example = "SP"
        )
        String state,

        @Schema(
                description = "Situação atual do espaço",
                example = "AVAILABLE"
        )
        String status,

        @Schema(
                description = "Data e hora de criação do anúncio"
        )
        LocalDateTime createdAt

) {
}