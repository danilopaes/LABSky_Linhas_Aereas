package tech.devinhouse.linhasaereas365.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.devinhouse.linhasaereas365.exceptions.CpfInvalidoException;
import tech.devinhouse.linhasaereas365.models.Classificacao;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.exceptions.MalasNaoDespachadasException;
import tech.devinhouse.linhasaereas365.models.Passageiro;
import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;
import tech.devinhouse.linhasaereas365.repositories.PassageiroRepository;
import tech.devinhouse.linhasaereas365.utils.ValidaCPF;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmacaoService {

    private final PassageiroRepository passageiroRepository;
    private final ConfirmacaoRepository confirmacaoRepository;
    private final AssentoService assentoService;
    private final ValidaCPF validaCPF;

    public Confirmacao checkin(Long cpf, Confirmacao confirmacao) {

        String assentos = confirmacao.getAssento();

        if (!validaCPF.isValidCPF(String.valueOf(cpf)))
            throw new CpfInvalidoException("CPF inválido!");

        if ((assentos.contains("4") || assentos.contains("5")) && !confirmacao.isMalasDespachadas()) {

            throw new MalasNaoDespachadasException();
        }

        Passageiro passageiro = passageiroRepository.findById(cpf).orElseThrow();
        Classificacao classificao = passageiro.getClassificacao();
        Integer milhas = passageiro.getMilhas();
        switch (classificao) {
            case VIP:
                milhas = milhas + 100;
                break;
            case OURO:
                milhas = milhas + 80;
                break;
            case PRATA:
                milhas = milhas + 50;
                break;
            case BRONZE:
                milhas = milhas + 30;
                break;
            case ASSOCIADO:
                milhas = milhas + 10;
                break;
        }
        passageiro.setMilhas(milhas);
        String eticket = generateEticket();
        confirmacao.setEticket(eticket);
        confirmacao.setDataHoraConfirmacao(LocalDateTime.now());
        confirmacao.setPassageiro(passageiro);
        confirmacao = confirmacaoRepository.save(confirmacao);
        passageiroRepository.save(passageiro);

        String assento = confirmacao.getAssento();
        assentoService.setAssentoOcupado(assento);

        log.info(String.format("Confirmação feita pelo passageiro de CPF %s com e-ticket %s", cpf, eticket));

        return confirmacao;
    }

    private String generateEticket() {
        return UUID.randomUUID().toString();
    }

}
