package tech.devinhouse.linhasaereas365.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.linhasaereas365.dtos.AssentoDto;
import tech.devinhouse.linhasaereas365.models.Assento;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssentoServiceTest {

    @Mock
    private ConfirmacaoRepository confirmacaoRepository;

    @Mock
    private Assento assentoModel;

    @InjectMocks
    private AssentoService service;

    @Test
    @DisplayName("Quando consulta por assentos, deveria retornar uma lista com os assentosDto")
    void consultarAssentos() {
        List<AssentoDto> assentos = Arrays.asList(new AssentoDto("1A", false),
                new AssentoDto("2A", false));

        Mockito.when(assentoModel.getListaAssentos()).thenReturn(assentos);

        List<AssentoDto> resultado = service.consultarAssentos();

        assertEquals(2, resultado.size());
        assertEquals(resultado.get(0).getAssento(), assentos.get(0).getAssento());

    }

    @Test
    @DisplayName("Quando consulta um assento que existe, deveria retornar o assentoDto")
    void consultarAssento() {
        String assento = "1A";
        List<AssentoDto> assentos = Arrays.asList(new AssentoDto("1A", false),
                new AssentoDto("2A", false));

        Mockito.when(assentoModel.getListaAssentos()).thenReturn(assentos);

        AssentoDto resultado = service.consultarAssento(assento);

        assertEquals(assento, resultado.getAssento());

    }

    @Test
    @DisplayName("Quando consulta um assento existente, deveria retornar true")
    void setAssentoOcupado() {
        String assento = "1A";

        Mockito.when(confirmacaoRepository.existsByAssento(assento)).thenReturn(true);

        boolean resultado = service.existeAssento(assento);

        assertTrue(resultado);
    }

}