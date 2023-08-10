package tech.devinhouse.linhasaereas365.dtos;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PassageiroPendenteDto {

    @NotNull(message = "{campo.obrigatorio}")
    @Digits(integer = 11, fraction = 0, message = "{campo.invalido}")
    private Long cpf;

    @NotBlank(message = "{campo.obrigatorio}")
    private String nome;
}
