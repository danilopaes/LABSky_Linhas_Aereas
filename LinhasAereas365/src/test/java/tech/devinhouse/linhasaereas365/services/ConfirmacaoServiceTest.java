package tech.devinhouse.linhasaereas365.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.linhasaereas365.exceptions.MalasNaoDespachadasException;
import tech.devinhouse.linhasaereas365.models.Classificacao;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.models.Passageiro;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;
import tech.devinhouse.linhasaereas365.repositories.PassageiroRepository;
import tech.devinhouse.linhasaereas365.utils.ValidaCPF;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfirmacaoServiceTest {

    @Mock
    private PassageiroRepository passageiroRepository;

    @Mock
    private ConfirmacaoRepository confirmacaoRepository;

    @Mock
    private ValidaCPF validaCPF;

    @InjectMocks
    private ConfirmacaoService service;

    @Test
    void checkin() {
        String eticket = UUID.randomUUID().toString();
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        Confirmacao confirmacao = new Confirmacao(eticket, true, LocalDateTime.now(), "A1", passageiro);

        Mockito.when(validaCPF.isValidCPF(passageiro.getCpf().toString())).thenReturn(true);
        Mockito.when(passageiroRepository.findById(passageiro.getCpf())).thenReturn(Optional.of(passageiro));
        Mockito.when(confirmacaoRepository.save(confirmacao)).thenReturn(confirmacao);

        Confirmacao resultado = service.checkin(passageiro.getCpf(), confirmacao);

        assertInstanceOf(Confirmacao.class, resultado);
        assertEquals(passageiro.getCpf(), resultado.getPassageiro().getCpf());
        assertEquals(confirmacao.getAssento(), resultado.getAssento());
        assertDoesNotThrow(() -> service.checkin(passageiro.getCpf(), confirmacao));
    }

    @Test
    void checkin_FileiraEmergencia() {
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(),
                Classificacao.ASSOCIADO, 100);
        Confirmacao confirmacao = new Confirmacao();
        confirmacao.setAssento("4A");
        confirmacao.setMalasDespachadas(false);
        Mockito.when(validaCPF.isValidCPF(String.valueOf(passageiro.getCpf()))).thenReturn(true);

        assertThrows(MalasNaoDespachadasException.class, () -> service.checkin(11111111111L, confirmacao));
    }

    @Test
    void checkin_milhas() {
        String eticket = UUID.randomUUID().toString();
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(),
                Classificacao.VIP, 200);
        Confirmacao confirmacao = new Confirmacao(eticket, true, LocalDateTime.now(),
                "1A", passageiro);
        Mockito.when(passageiroRepository.findById(passageiro.getCpf())).thenReturn(Optional.of(passageiro));
        Mockito.when(validaCPF.isValidCPF(String.valueOf(passageiro.getCpf()))).thenReturn(true);
        Mockito.when(confirmacaoRepository.save(confirmacao)).thenReturn(confirmacao);

        service.checkin(passageiro.getCpf(), confirmacao);

        assertEquals(300, passageiro.getMilhas()); // Verifica se o saldo de milhas foi atualizado corretamente
    }
}