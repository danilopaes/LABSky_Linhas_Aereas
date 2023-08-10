package tech.devinhouse.linhasaereas365.models;

import lombok.Data;
import org.springframework.stereotype.Component;
import tech.devinhouse.linhasaereas365.dtos.AssentoDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class Assento {

    private List<AssentoDto> listaAssentos = new ArrayList<>();

    public Assento() {
        criarAssentos();
    }

    private void criarAssentos() {
        for (int fileira = 1; fileira <= 9; fileira++) {
            for (char poltrona = 'A'; poltrona <= 'F'; poltrona++) {
                String assento = fileira + String.valueOf(poltrona);
                listaAssentos.add(new AssentoDto(assento, false));
            }
        }
    }
}
