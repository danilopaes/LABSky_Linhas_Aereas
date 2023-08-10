package tech.devinhouse.linhasaereas365.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import tech.devinhouse.linhasaereas365.dtos.PassageiroConfirmacaoDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroPendenteDto;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.models.Passageiro;

import tech.devinhouse.linhasaereas365.repositories.ConfirmacaoRepository;
import tech.devinhouse.linhasaereas365.repositories.PassageiroRepository;

import tech.devinhouse.linhasaereas365.exceptions.CpfInvalidoException;
import tech.devinhouse.linhasaereas365.exceptions.PassageiroInexistenteException;
import tech.devinhouse.linhasaereas365.exceptions.PassageiroJaCadastradoException;
import tech.devinhouse.linhasaereas365.exceptions.NotFoundException;

import tech.devinhouse.linhasaereas365.utils.ValidaCPF;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassageiroService {

    private final PassageiroRepository passageiroRepository;

    private final ConfirmacaoRepository confirmacaoRepository;

    private final ValidaCPF validaCPF;

    @Transactional
    public PassageiroConfirmacaoDto consultarPassageiroPorCpf(Long cpf) {
        Optional<Passageiro> passageiroOpt = passageiroRepository.findById(cpf);
        PassageiroConfirmacaoDto passageiroConfirmacaoDto;

        if (passageiroOpt.isEmpty())
            throw new PassageiroInexistenteException("Passageiro", cpf.toString());

        Passageiro passageiro = passageiroOpt.get();
        Optional<String> eticketOpt = confirmacaoRepository.findConfirmacaoByCpf(passageiro.getCpf());

        if (eticketOpt.isPresent()) {
            Confirmacao confirmacao = confirmacaoRepository.getReferenceById(eticketOpt.get());
            passageiroConfirmacaoDto = new PassageiroConfirmacaoDto(passageiro, confirmacao);
        } else {
            passageiroConfirmacaoDto = new PassageiroConfirmacaoDto(passageiro);
        }

        return passageiroConfirmacaoDto;
    }

    public List<PassageiroConfirmacaoDto> consultarPassageiros() {
        List<Passageiro> passageiros = passageiroRepository.findAll();

        List<PassageiroConfirmacaoDto> passageiroConfirmacaoDtos = new ArrayList<>();

        for (Passageiro passageiro : passageiros) {
            Optional<Confirmacao> confirmacaoOpt = confirmacaoRepository.findByPassageiroCpf(passageiro.getCpf());

            PassageiroConfirmacaoDto passageiroConfirmacaoDto;
            if (confirmacaoOpt.isEmpty())
                passageiroConfirmacaoDto = new PassageiroConfirmacaoDto(passageiro);
            else
                passageiroConfirmacaoDto = new PassageiroConfirmacaoDto(passageiro, confirmacaoOpt.get());

            passageiroConfirmacaoDtos.add(passageiroConfirmacaoDto);
        }

        return passageiroConfirmacaoDtos;
    }

    @Transactional
    public PassageiroDto cadastrarPassageiro(PassageiroDto passageiroDto) {
        Passageiro passageiro = new Passageiro();

        BeanUtils.copyProperties(passageiroDto, passageiro);

        if (!validaCPF.isValidCPF(passageiro.getCpf().toString()))
            throw new CpfInvalidoException("CPF inválido!");

        if (passageiroRepository.existsById(passageiro.getCpf()))
            throw new PassageiroJaCadastradoException("Passageiro já cadastrado!");

        passageiro = passageiroRepository.save(passageiro);

        BeanUtils.copyProperties(passageiro, passageiroDto);

        return passageiroDto;
    }

    @Transactional
    public Boolean deletarPassageiro(Long cpf) {
        if (!validaCPF.isValidCPF(cpf.toString()))
            throw new CpfInvalidoException("CPF inválido!");

        Optional<Passageiro> passageiroOpt = passageiroRepository.findById(cpf);

        if (passageiroOpt.isEmpty())
            throw new PassageiroInexistenteException("Passageiro", cpf.toString());
        else {
            Passageiro passageiro = passageiroOpt.get();
            Optional<String> eticket = confirmacaoRepository.findConfirmacaoByCpf(passageiro.getCpf());

            if (eticket.isPresent())
                confirmacaoRepository.deleteById(eticket.get());

            passageiroRepository.delete(passageiro);
        }
        return true;
    }

    @Transactional
    public PassageiroConfirmacaoDto consultarConfirmacaoPorEticket(String eticket) {
        Optional<Confirmacao> confirmacaoOpt = confirmacaoRepository.findById(eticket);

        if (confirmacaoOpt.isPresent()) {
            Confirmacao confirmacao = confirmacaoOpt.get();
            Passageiro passageiro = passageiroRepository.getReferenceById(confirmacao.getPassageiro().getCpf());
            return new PassageiroConfirmacaoDto(passageiro, confirmacao);
        }
        throw new NotFoundException("Eticket", eticket);
    }

    public List<PassageiroPendenteDto> consultaPassageiroPendentes() {
        List<Passageiro> listaPassageirosPendentes = confirmacaoRepository.consultaPassageirosPendentes();

        List<PassageiroPendenteDto> listaDto = new ArrayList<>();

        for (Passageiro passageiro : listaPassageirosPendentes) {
            PassageiroPendenteDto passageiroDto = new PassageiroPendenteDto();
            BeanUtils.copyProperties(passageiro, passageiroDto);
            listaDto.add(passageiroDto);
        }
        return listaDto;
    }
}
