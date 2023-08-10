package tech.devinhouse.linhasaereas365.dtos;

import tech.devinhouse.linhasaereas365.models.Classificacao;

import java.time.LocalDate;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class PassageiroDto {

    @NotNull(message = "{campo.obrigatorio}")
    @Digits(integer = 11, fraction = 0, message = "{campo.invalido}")
    private Long cpf;

    @NotBlank(message = "{campo.obrigatorio}")
    private String nome;

    @NotNull(message = "{campo.obrigatorio}")
    @PastOrPresent(message = "{campo.invalido}")
    private LocalDate dataNascimento;

    @NotNull(message = "{campo.obrigatorio}")
    private Classificacao classificacao;

    @NotNull(message = "{campo.obrigatorio}")
    @PositiveOrZero(message = "{campo.invalido}")
    private Integer milhas;
}
