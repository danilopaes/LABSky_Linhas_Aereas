package tech.devinhouse.linhasaereas365.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "CONFIRMACOES")
@Table(name = "CONFIRMACOES")
public class Confirmacao {
    @Id
    @Column(name = "ETICKET", updatable = false, nullable = true, unique = true)
    private String eticket = UUID.randomUUID().toString();

    @Column(name = "MALAS_DESPACHADAS", nullable = true)
    private boolean malasDespachadas;

    @CreationTimestamp
    @Column(name = "DATA_HORA_CONFIRMACAO", nullable = true, updatable = false)
    private LocalDateTime dataHoraConfirmacao;

    @Column(name = "ASSENTO", nullable = true, unique = true)
    private String assento;

    @OneToOne
    @JoinColumn(name = "CPF", referencedColumnName = "CPF", unique = true)
    private Passageiro passageiro;

}
