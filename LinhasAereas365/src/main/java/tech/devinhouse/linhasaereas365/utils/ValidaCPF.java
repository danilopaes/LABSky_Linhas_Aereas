package tech.devinhouse.linhasaereas365.utils;


import org.springframework.stereotype.Component;

@Component
public class ValidaCPF {
    private static final int[] pesoCpf = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public boolean isValidCPF(String cpf) {
        try {
            Long.parseUnsignedLong(cpf);
        } catch (Exception exception) {
            return false;
        }

        if (cpf.length() < 11) {
            int zeroes = 11 - cpf.length();

            for (int i = zeroes; i > 0; i--)
                cpf = '0' + cpf;
        }

        if (cpf == null || cpf.length() != 11 || cpf == "00000000000" || cpf == "11111111111" || cpf == "22222222222"
                || cpf == "33333333333" || cpf == "44444444444" || cpf == "55555555555" || cpf == "66666666666"
                || cpf == "77777777777" || cpf == "88888888888" || cpf == "99999999999")
            return false;

        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCpf);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCpf);

        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }
}
