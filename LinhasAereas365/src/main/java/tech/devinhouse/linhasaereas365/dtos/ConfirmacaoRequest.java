package tech.devinhouse.linhasaereas365.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class ConfirmacaoRequest {
    @NotNull(message = "{campo.obrigatorio}")
    @Digits(integer = 11, fraction = 0, message = "{campo.invalido}")
    private Long cpf;

    @NotEmpty(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^\\d[a-fA-F]$", message = "{campo.invalido}")
    private String assento;

    @NotNull(message = "{campo.obrigatorio}")
    private Boolean malasDespachadas;
}
