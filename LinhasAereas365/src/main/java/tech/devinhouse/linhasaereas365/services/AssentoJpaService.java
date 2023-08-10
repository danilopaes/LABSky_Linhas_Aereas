package tech.devinhouse.linhasaereas365.services;

import tech.devinhouse.linhasaereas365.models.AssentoJpa;
import tech.devinhouse.linhasaereas365.models.Confirmacao;

import tech.devinhouse.linhasaereas365.repositories.AssentoRepository;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextRefreshedEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@AllArgsConstructor
@Data
public class AssentoJpaService {
    private AssentoRepository assentoRepository;

    private ConfirmacaoRepository confirmacaoRepository;

    @EventListener
    public void initializeAssentos(ContextRefreshedEvent event) {
        assentoRepository.saveAll(createAssentos());
    }

    private List<AssentoJpa> createAssentos() {
        List<AssentoJpa> assentos = new ArrayList<>();
        for (char letra = 'A'; letra <= 'F'; letra++) {
            for (int fileira = 1; fileira <= 9; fileira++) {
                String codigo = String.format("%d%c", fileira, letra);

                Optional<Confirmacao> confirmacaoOpt = confirmacaoRepository.findByAssento(codigo);
                if (confirmacaoOpt.isPresent()) {
                    Confirmacao confirmacao = confirmacaoOpt.get();

                    AssentoJpa assento = new AssentoJpa(letra, fileira, true);

                    assento.setConfirmacao(confirmacao);

                    assento.setPassageiro(confirmacao.getPassageiro());

                    assentos.add(assento);
                } else {
                    AssentoJpa assento = new AssentoJpa(letra, fileira, false);
                    assentos.add(assento);
                }
            }
        }
        return assentos;
    }
}
