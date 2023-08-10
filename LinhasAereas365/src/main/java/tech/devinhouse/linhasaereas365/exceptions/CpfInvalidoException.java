package tech.devinhouse.linhasaereas365.exceptions;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException(String mensagem) {
        super(mensagem);
    }
}
