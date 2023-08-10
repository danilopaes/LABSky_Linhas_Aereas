package tech.devinhouse.linhasaereas365.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import tech.devinhouse.linhasaereas365.dtos.PassageiroConfirmacaoDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroPendenteDto;
import tech.devinhouse.linhasaereas365.exceptions.CpfInvalidoException;
import tech.devinhouse.linhasaereas365.exceptions.NotFoundException;
import tech.devinhouse.linhasaereas365.exceptions.PassageiroInexistenteException;
import tech.devinhouse.linhasaereas365.exceptions.PassageiroJaCadastradoException;
import tech.devinhouse.linhasaereas365.models.Classificacao;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.models.Passageiro;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;
import tech.devinhouse.linhasaereas365.repositories.PassageiroRepository;
import tech.devinhouse.linhasaereas365.utils.ValidaCPF;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {

    @Mock
    private PassageiroRepository passageiroRepository;

    @Mock
    private ConfirmacaoRepository confirmacaoRepository;

    @Mock
    private ValidaCPF validaCPF;

    @InjectMocks
    private PassageiroService passageiroService;

    @Test
    @DisplayName("Quando cadastra passageiro com CPF válido, deveria retornar um passageiroDto")
    void cadastrarPassageiro() {
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        PassageiroDto passageiroDto = new PassageiroDto();
        Mockito.when(validaCPF.isValidCPF(passageiro.getCpf().toString())).thenReturn(true);
        Mockito.when(passageiroRepository.existsById(passageiro.getCpf())).thenReturn(false);
        Mockito.when(passageiroRepository.save(passageiro)).thenReturn(passageiro);

        BeanUtils.copyProperties(passageiro, passageiroDto);

        PassageiroDto resultado = passageiroService.cadastrarPassageiro(passageiroDto);

        assertSame(resultado.getCpf(), passageiro.getCpf());
        assertInstanceOf(PassageiroDto.class, resultado);
        assertDoesNotThrow(() -> passageiroService.cadastrarPassageiro(passageiroDto));

    }

    @Test
    @DisplayName("Quando cadastra passageiro com CPF inválido, deveria lançar exceção CpfInvalidoException")
    void cadastrarPassageiroComCpfInvalido() {
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        PassageiroDto passageiroDto = new PassageiroDto();

        Mockito.when(validaCPF.isValidCPF(passageiro.getCpf().toString())).thenReturn(false);

        BeanUtils.copyProperties(passageiro, passageiroDto);

        assertThrows(CpfInvalidoException.class, () -> passageiroService.cadastrarPassageiro(passageiroDto));

    }

    @Test
    @DisplayName("Quando cadastra passageiro com CPF válido e já cadastrado, deveria lançar a exceção PassageiroJaCadastradoException")
    void cadastrarPassageiroJaCadastrado() {
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        PassageiroDto passageiroDto = new PassageiroDto();

        Mockito.when(validaCPF.isValidCPF(passageiro.getCpf().toString())).thenReturn(true);
        Mockito.when(passageiroRepository.existsById(passageiro.getCpf())).thenReturn(true);

        BeanUtils.copyProperties(passageiro, passageiroDto);

        assertThrows(PassageiroJaCadastradoException.class, () -> passageiroService.cadastrarPassageiro(passageiroDto));

    }

    @Test
    @DisplayName("Quando consulta todos passageiros, deve retornar lista preenchida")
    void consultar_todosPassageiros() {
        Passageiro passageiro1 = new Passageiro(
                11111111111L, "Pedro", LocalDate.of(1990, 5, 10),
                Classificacao.VIP, 100);
        Passageiro passageiro2 = new Passageiro(22222222222L, "Joana",
                LocalDate.of(1992, 9, 15), Classificacao.OURO, 100);
        List<Passageiro> passageiros = Arrays.asList(passageiro1, passageiro2);
        Confirmacao confirmacao = new Confirmacao();

        Mockito.when(passageiroRepository.findAll()).thenReturn(passageiros);
        Mockito.when(confirmacaoRepository.findByPassageiroCpf(11111111111L)).thenReturn(Optional.of(confirmacao));
        Mockito.when(confirmacaoRepository.findByPassageiroCpf(22222222222L)).thenReturn(Optional.empty());

        List<PassageiroConfirmacaoDto> resultado = passageiroService.consultarPassageiros();
        assertEquals(2, resultado.size());
        assertEquals(resultado.get(0).getCpf(), passageiro1.getCpf());
        assertEquals(resultado.get(1).getCpf(), passageiro2.getCpf());
    }

    @Test
    @DisplayName("Quando consulta um eticket existente, deveria retornar um passageiro com confirmação")
    void consultaConfirmacaoExistente() {
        String eticket = UUID.randomUUID().toString();
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        Confirmacao confirmacao = new Confirmacao(eticket, true, LocalDateTime.now(), "A1", passageiro);
        PassageiroConfirmacaoDto passageitoDto = new PassageiroConfirmacaoDto(passageiro, confirmacao);

        Mockito.when(confirmacaoRepository.findById(eticket)).thenReturn(Optional.of(confirmacao));
        Mockito.when(passageiroRepository.getReferenceById(passageiro.getCpf())).thenReturn(passageiro);
        PassageiroConfirmacaoDto resultado = passageiroService.consultarConfirmacaoPorEticket(eticket);

        assertNotNull(resultado);
        assertInstanceOf(PassageiroConfirmacaoDto.class, resultado);
        assertEquals(resultado.getCpf(), passageitoDto.getCpf());
        assertEquals(resultado.getEticket(), passageitoDto.getEticket());
        assertDoesNotThrow(() -> passageiroService.consultarConfirmacaoPorEticket(eticket));

    }

    @Test
    @DisplayName("Quando consulta passageiro com confirmacao pelo CPF válido, deveria retornar um passageiroConfirmacaoDto")
    void consultarPassageiroPeloCpf() {
        String eticket = UUID.randomUUID().toString();
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);
        Confirmacao confirmacao = new Confirmacao(eticket, true, LocalDateTime.now(), "A1", passageiro);

        Mockito.when(passageiroRepository.findById(passageiro.getCpf())).thenReturn(Optional.of(passageiro));
        Mockito.when(confirmacaoRepository.findConfirmacaoByCpf(passageiro.getCpf())).thenReturn(Optional.of(eticket));
        Mockito.when(confirmacaoRepository.getReferenceById(eticket)).thenReturn(confirmacao);

        PassageiroConfirmacaoDto resultado = passageiroService.consultarPassageiroPorCpf(passageiro.getCpf());

        assertEquals(resultado.getCpf(), passageiro.getCpf());
        assertSame(resultado.getEticket(), eticket);
        assertDoesNotThrow(() -> passageiroService.consultarPassageiroPorCpf(passageiro.getCpf()));

    }

    @Test
    @DisplayName("Quando consulta passageiro com confirmacao pelo CPF válido, deveria retornar um passageiroConfirmacaoDto")
    void consultarPassageiroInexistente() {

        Long cpf = 12312312311L;

        Mockito.when(passageiroRepository.findById(cpf)).thenReturn(Optional.empty());

        assertThrows(PassageiroInexistenteException.class, () -> passageiroService.consultarPassageiroPorCpf(cpf));

    }

    @Test
    @DisplayName("Quando consulta um eticket não cadastrado, deveria retornar uma exceção com código 404 NotFound")
    void consultaConfirmacaoInexistente() {
        String eticket = UUID.randomUUID().toString();

        Mockito.when(confirmacaoRepository.findById(eticket)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> passageiroService.consultarConfirmacaoPorEticket(eticket));

    }

    @Test
    @DisplayName("Quando consulta passageiros pendentes, deveria retornar uma lista de passageiroPendenteDto sem confirmação cadastrada")
    void consultaPassageirosPendentes() {

        Passageiro passageiro1 = new Passageiro(
                11111111111L, "Pedro", LocalDate.of(1990, 5, 10),
                Classificacao.VIP, 100);
        Passageiro passageiro2 = new Passageiro(22222222222L, "Joana",
                LocalDate.of(1992, 9, 15), Classificacao.OURO, 100);
        List<Passageiro> passageiros = Arrays.asList(passageiro1, passageiro2);

        Mockito.when(confirmacaoRepository.consultaPassageirosPendentes()).thenReturn(passageiros);

        List<PassageiroPendenteDto> resultado = passageiroService.consultaPassageiroPendentes();
        PassageiroPendenteDto dto1 = resultado.get(0);
        PassageiroPendenteDto dto2 = resultado.get(1);

        assertEquals(2, resultado.size());
        assertEquals(passageiro1.getCpf(), dto1.getCpf());
        assertEquals(passageiro2.getCpf(), dto2.getCpf());

    }

    @Test
    @DisplayName("Quando deleta passageiro pelo CPF válido e existente, deveria excluir do banco de dados e retornar true")
    void deletarPassageiroPeloCpf() {
        String eticket = UUID.randomUUID().toString();
        Passageiro passageiro = new Passageiro(11111111111L, "Joao", LocalDate.now(), Classificacao.ASSOCIADO, 100);

        new Confirmacao(eticket, true, LocalDateTime.now(), "A1", passageiro);

        Mockito.when(validaCPF.isValidCPF(passageiro.getCpf().toString())).thenReturn(true);
        Mockito.when(passageiroRepository.findById(passageiro.getCpf())).thenReturn(Optional.of(passageiro));
        Mockito.when(confirmacaoRepository.findConfirmacaoByCpf(passageiro.getCpf())).thenReturn(Optional.of(eticket));

        Boolean resultado = passageiroService.deletarPassageiro(passageiro.getCpf());

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Quando deleta passageiro com CPF inválido, deveria lançar exceção CpfInvalidoException")
    void deletarPassageiroComCpfInválido() {
        Long cpf = 12312312311L;

        Mockito.when(validaCPF.isValidCPF(cpf.toString())).thenReturn(false);

        assertThrows(CpfInvalidoException.class, () -> passageiroService.deletarPassageiro(cpf));

    }

    @Test
    @DisplayName("Quando deleta passageiro com CPF não cadastrado, deveria lançar exceção PassageiroInexistenteException")
    void deletarPassageiroInexistente() {
        Long cpf = 12312312311L;

        Mockito.when(validaCPF.isValidCPF(cpf.toString())).thenReturn(true);
        Mockito.when(passageiroRepository.findById(cpf)).thenReturn(Optional.empty());

        assertThrows(PassageiroInexistenteException.class, () -> passageiroService.deletarPassageiro(cpf));

    }

}