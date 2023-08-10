package tech.devinhouse.linhasaereas365.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;
import tech.devinhouse.linhasaereas365.dtos.PassageiroConfirmacaoDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroDto;
import tech.devinhouse.linhasaereas365.dtos.PassageiroPendenteDto;
import tech.devinhouse.linhasaereas365.services.PassageiroService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/passageiros")
public class PassageiroController {
    private final PassageiroService passageiroService;

    @GetMapping(value = { "", "/{cpf}" })
    public ResponseEntity<List<PassageiroConfirmacaoDto>> consultarPassageiros(
            @PathVariable(required = false) Long cpf) {
        List<PassageiroConfirmacaoDto> passageiros = new ArrayList<>();

        if (cpf == null)
            passageiros = passageiroService.consultarPassageiros();
        else {
            PassageiroConfirmacaoDto passageiroConfirmacaoDto = passageiroService.consultarPassageiroPorCpf(cpf);
            passageiros.add(passageiroConfirmacaoDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(passageiros);
    }

    @PostMapping
    public ResponseEntity<PassageiroDto> cadastrarPassageiro(@RequestBody @Valid PassageiroDto passageiroDto) {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080").path("api/passageiros").build().toUri();
        return ResponseEntity.created(uri).body(passageiroService.cadastrarPassageiro(passageiroDto));
    }

    @DeleteMapping("{cpf}")
    public ResponseEntity<String> deletarPassageiro(@PathVariable Long cpf) {
        String mensagem = Boolean.TRUE.equals(passageiroService.deletarPassageiro(cpf))
                ? "Passageiro com o CPF " + cpf + " foi excluído!"
                : "";
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }

    @GetMapping("/confirmacao/{eticket}")
    public ResponseEntity<PassageiroConfirmacaoDto> consultaConfirmacaoPorEticket(@PathVariable String eticket) {
        return ResponseEntity.ok(passageiroService.consultarConfirmacaoPorEticket(eticket));

    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<PassageiroPendenteDto>> consultarPassageirosPendentes() {
        return ResponseEntity.ok(passageiroService.consultaPassageiroPendentes());
    }
}