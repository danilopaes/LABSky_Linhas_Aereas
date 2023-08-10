package tech.devinhouse.linhasaereas365.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.devinhouse.linhasaereas365.dtos.AssentoDto;
import tech.devinhouse.linhasaereas365.models.Assento;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssentoService {

    private final ConfirmacaoRepository repository;
    private final Assento listaDeAssentos;

    public List<AssentoDto> consultarAssentos() {
        return listaDeAssentos.getListaAssentos();
    }

    public AssentoDto consultarAssento(String assento) {
        List<AssentoDto> assentos = consultarAssentos();
        return assentos.stream()
                .filter(dto -> dto.getAssento().equals(assento))
                .findFirst()
                .orElse(null);
    }

    public void setAssentoOcupado(String assento) {
        if (existeAssento(assento)) {
            AssentoDto assentoEncontrado = consultarAssento(assento);
            assentoEncontrado.setOcupado(true);
        }
    }

    public boolean existeAssento(String assento) {
        return repository.existsByAssento(assento);
    }
}