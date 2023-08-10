package tech.devinhouse.linhasaereas365.exceptions;

public class PassageiroInexistenteException extends RuntimeException {
    public PassageiroInexistenteException(String recurso, String identificador) {
        super("Recurso " + recurso + " com identificador " + identificador + " não encontrado!");
    }
}
