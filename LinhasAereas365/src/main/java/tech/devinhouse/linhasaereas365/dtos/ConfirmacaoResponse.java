package tech.devinhouse.linhasaereas365.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConfirmacaoResponse {

    private String eticket;
    private LocalDateTime dataHoraConfirmacao;

}
