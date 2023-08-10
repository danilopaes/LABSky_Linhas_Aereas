package tech.devinhouse.linhasaereas365.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String recurso, String identificador) {
        super("Recurso " + recurso + " com identificador " + identificador + " não encontrado!");
    }
}
