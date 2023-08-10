package tech.devinhouse.linhasaereas365.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devinhouse.linhasaereas365.dtos.AssentoDto;
import tech.devinhouse.linhasaereas365.services.AssentoService;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/assentos")
public class AssentoController {

    @Autowired
    private AssentoService assentoService;

    @GetMapping
    public ResponseEntity<List<AssentoDto>> consultarAssentos() {

        return ResponseEntity.ok(assentoService.consultarAssentos());

    }
}