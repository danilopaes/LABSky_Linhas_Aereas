package tech.devinhouse.linhasaereas365.utils;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErroResponse {
    private Instant timestamp = Instant.now();
    private int statusCode;
    private String erro;
    private String mensagem;
    private Map<String, String> detalhes;

    public ErroResponse(int statusCode, String erro, String mensagem) {
        this.statusCode = statusCode;
        this.erro = erro;
        this.mensagem = mensagem;
    }
}
