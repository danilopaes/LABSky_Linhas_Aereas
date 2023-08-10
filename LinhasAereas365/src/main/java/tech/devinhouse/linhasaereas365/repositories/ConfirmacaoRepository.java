package tech.devinhouse.linhasaereas365.repositories;

import org.springframework.data.jpa.repository.Query;
import tech.devinhouse.linhasaereas365.models.Confirmacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.devinhouse.linhasaereas365.models.Passageiro;

@Repository
public interface ConfirmacaoRepository extends JpaRepository<Confirmacao, String> {

    @Query("""
            SELECT c.eticket FROM CONFIRMACOES c WHERE c.passageiro.cpf= :cpf
            """)
    Optional<String> findConfirmacaoByCpf(Long cpf);

    Optional<Confirmacao> findByPassageiroCpf(Long cpf);

    @Query("""
                select p from PASSAGEIROS p
                where
                p.cpf not in (select c.passageiro.cpf from CONFIRMACOES c)
            """)
    List<Passageiro> consultaPassageirosPendentes();

    boolean existsByAssento(String assento);

    Optional<Confirmacao> findByAssento(String assento);

}
