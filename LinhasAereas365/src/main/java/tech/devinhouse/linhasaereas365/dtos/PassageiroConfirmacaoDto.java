package tech.devinhouse.linhasaereas365.dtos;

import lombok.NoArgsConstructor;
import tech.devinhouse.linhasaereas365.models.Classificacao;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.models.Passageiro;

import java.time.LocalDate;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassageiroConfirmacaoDto {

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

    @NotBlank(message = "{campo.obrigatorio}")
    @Size(max = 36)
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String eticket;

    @NotBlank(message = "{campo.obrigatorio}")
    private String assento;

    public PassageiroConfirmacaoDto(Passageiro passageiro, Confirmacao confirmacao) {
        this.cpf = passageiro.getCpf();
        this.nome = passageiro.getNome();
        this.dataNascimento = passageiro.getDataNascimento();
        this.classificacao = passageiro.getClassificacao();
        this.milhas = passageiro.getMilhas();
        this.eticket = confirmacao.getEticket();
        this.assento = confirmacao.getAssento();
    }

    public PassageiroConfirmacaoDto(Passageiro passageiro) {
        this.cpf = passageiro.getCpf();
        this.nome = passageiro.getNome();
        this.dataNascimento = passageiro.getDataNascimento();
        this.classificacao = passageiro.getClassificacao();
        this.milhas = passageiro.getMilhas();
    }
}
