package tech.devinhouse.linhasaereas365.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.devinhouse.linhasaereas365.dtos.ConfirmacaoRequest;
import tech.devinhouse.linhasaereas365.dtos.ConfirmacaoResponse;
import tech.devinhouse.linhasaereas365.models.Confirmacao;
import tech.devinhouse.linhasaereas365.services.ConfirmacaoService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/api/passageiros/confirmacao")
@AllArgsConstructor
public class ConfirmacaoController {

    private ConfirmacaoService service;
    private ModelMapper mapper;

    @PostMapping
    public ResponseEntity<ConfirmacaoResponse> checkIn(@RequestBody @Valid ConfirmacaoRequest request) {

        Long cpf = request.getCpf();
        Confirmacao confirmacao = mapper.map(request, Confirmacao.class);
        confirmacao = service.checkin(cpf, confirmacao);
        ConfirmacaoResponse response = mapper.map(confirmacao, ConfirmacaoResponse.class);
        return ResponseEntity.created(URI.create(response.getEticket())).body(response);
    }
}
