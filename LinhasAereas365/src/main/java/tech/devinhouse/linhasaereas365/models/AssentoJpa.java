package tech.devinhouse.linhasaereas365.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ASSENTOS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssentoJpa {
    @Id
    private String codigo;

    private Character letra;

    private Integer fileira;

    private Boolean ocupado;

    @OneToOne
    @JoinColumn(name = "CPF", referencedColumnName = "CPF", unique = true)
    private Passageiro passageiro;

    @OneToOne
    @JoinColumn(name = "ETICKET", referencedColumnName = "ETICKET", unique = true)
    private Confirmacao confirmacao;

    public AssentoJpa(Character letra, Integer fileira, Boolean ocupado) {
        this.codigo = "" + fileira + letra;
        this.letra = letra;
        this.fileira = fileira;
        this.ocupado = ocupado;
    }

    public AssentoJpa(String codigo, Boolean ocupado) {
        this.codigo = codigo;
        this.letra = codigo.charAt(0);
        this.fileira = Integer.parseInt(codigo.substring(1));
        this.ocupado = ocupado;
    }
}
