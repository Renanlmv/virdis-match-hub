package com.github.virdis.ms.space.dto;

import com.github.virdis.ms.space.entity.SpaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(
        description = "Dados para cadastro ou atualização de um espaço"
)
public record SpaceRequestDTO(

        @Schema(
                description = "Identificador do usuário proprietário do anúncio",
                example = "user-001"
        )
        @NotBlank(message = "O identificador do proprietário é obrigatório")
        String ownerId,

        @Schema(
                description = "Nome do anúncio do espaço",
                example = "Cobertura disponível para horta urbana"
        )
        @NotBlank(message = "O nome do espaço é obrigatório")
        String title,

        @Schema(
                description = "Bairro onde o espaço está localizado",
                example = "Vila Mariana"
        )
        @NotBlank(message = "O bairro é obrigatório")
        String neighborhood,

        @Schema(
                description = "Tipo do espaço disponibilizado",
                example = "ROOFTOP"
        )
        @NotNull(message = "O tipo do espaço é obrigatório")
        SpaceType type,

        @Schema(
                description = "Área disponível em metros quadrados",
                example = "240.50"
        )
        @NotNull(message = "A área disponível é obrigatória")
        @DecimalMin(
                value = "1.0",
                message = "A área disponível deve ser maior ou igual a 1 m²"
        )
        BigDecimal areaSquareMeters,

        @Schema(
                description = "Valor mensal pretendido pelo uso do espaço",
                example = "1800.00"
        )
        @NotNull(message = "O valor mensal é obrigatório")
        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "O valor mensal não pode ser negativo"
        )
        BigDecimal monthlyRent,

        @Schema(
                description = "Descrição das características do espaço",
                example = "Cobertura com acesso à água e boa incidência solar."
        )
        @NotBlank(message = "A descrição é obrigatória")
        @Size(
                max = 1000,
                message = "A descrição deve possuir no máximo 1000 caracteres"
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
        @NotBlank(message = "O nome do contato é obrigatório")
        String contactName,

        @Schema(
                description = "E-mail autorizado para contato",
                example = "marina.souza@email.com"
        )
        @NotBlank(message = "O e-mail de contato é obrigatório")
        @Email(message = "O e-mail de contato deve ser válido")
        String contactEmail,

        @Schema(
                description = "Quantidade de residências ou apartamentos beneficiados",
                example = "60"
        )
        @NotNull(message = "A quantidade de residências é obrigatória")
        @PositiveOrZero(
                message = "A quantidade de residências não pode ser negativa"
        )
        Integer residenceCount,

        @Schema(
                description = "Cidade onde o espaço está localizado",
                example = "São Paulo"
        )
        @NotBlank(message = "A cidade é obrigatória")
        String city,

        @Schema(
                description = "Sigla do estado onde o espaço está localizado",
                example = "SP"
        )
        @NotBlank(message = "O estado é obrigatório")
        @Pattern(
                regexp = "^[A-Za-z]{2}$",
                message = "O estado deve possuir exatamente duas letras"
        )
        String state

) {
}