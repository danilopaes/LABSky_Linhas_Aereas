package tech.devinhouse.linhasaereas365.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssentoDto {

    private String assento;

    private boolean ocupado;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AssentoDto that = (AssentoDto) o;
        return ocupado == that.ocupado && Objects.equals(assento, that.assento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assento, ocupado);
    }
}
