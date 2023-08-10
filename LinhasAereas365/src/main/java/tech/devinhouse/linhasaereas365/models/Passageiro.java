package tech.devinhouse.linhasaereas365.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "PASSAGEIROS")
@Table(name = "PASSAGEIROS")
public class Passageiro {

    @Id
    @NotNull(message = "CPF não pode ser nulo")
    @Column(name = "CPF", nullable = false)
    private Long cpf;

    @NotNull(message = "Nome não pode ser nulo")
    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    @PastOrPresent(message = "Data futura não é permitida")
    private LocalDate dataNascimento;

    @Column(name = "CLASSIFICACAO", nullable = false)
    @Enumerated(EnumType.STRING)
    private Classificacao classificacao = Classificacao.ASSOCIADO;

    @Column(name = "MILHAS", nullable = false)
    private Integer milhas = 0;
}
