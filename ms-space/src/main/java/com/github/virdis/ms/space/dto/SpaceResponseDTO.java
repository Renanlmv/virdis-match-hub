package com.github.virdis.ms.space.dto;

import com.github.virdis.ms.space.entity.Space;
import com.github.virdis.ms.space.entity.SpaceStatus;
import com.github.virdis.ms.space.entity.SpaceType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        description = "Dados retornados pela API para um espaço"
)
public record SpaceResponseDTO(

        @Schema(
                description = "Identificador único do espaço",
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
                description = "Tipo do espaço disponibilizado",
                example = "ROOFTOP"
        )
        SpaceType type,

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
                description = "Descrição das características do espaço",
                example = "Cobertura com acesso à água e boa incidência solar."
        )
        String description,

        @Schema(
                description = "URL pública da imagem do espaço",
                example = "https://exemplo.com/imagens/cobertura.png"
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
                description = "Sigla do estado onde o espaço está localizado",
                example = "SP"
        )
        String state,

        @Schema(
                description = "Situação atual do espaço",
                example = "AVAILABLE"
        )
        SpaceStatus status,

        @Schema(
                description = "Data e hora de criação do anúncio",
                example = "2026-11-05T14:30:00"
        )
        LocalDateTime createdAt

) {

        public SpaceResponseDTO(Space space) {
                this(
                        space.getId(),
                        space.getOwnerId(),
                        space.getTitle(),
                        space.getNeighborhood(),
                        space.getType(),
                        space.getAreaSquareMeters(),
                        space.getMonthlyRent(),
                        space.getDescription(),
                        space.getImageUrl(),
                        space.getContactName(),
                        space.getContactEmail(),
                        space.getResidenceCount(),
                        space.getCity(),
                        space.getState(),
                        space.getStatus(),
                        space.getCreatedAt()
                );
        }
}