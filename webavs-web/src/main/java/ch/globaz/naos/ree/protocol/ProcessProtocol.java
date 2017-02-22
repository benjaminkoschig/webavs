package ch.globaz.naos.ree.protocol;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Protocole de process contenant les warnings et les erreurs
 * 
 * @author lga
 * 
 */
public class ProcessProtocol {

    private List<String> warnings;

    private List<String> errors;

    public ProcessProtocol() {
        warnings = new ArrayList<String>();
        errors = new ArrayList<String>();
    }

    /**
     * Ajoute un warning dans le protocole de fin de process
     * 
     * @param warning
     * @throws IllegalArgumentException Si l'erreur est null ou vide
     */
    public final void addWarning(String warning) {
        if (StringUtils.isEmpty(warning)) {
            throw new IllegalArgumentException("Interdiction d'ajouter un message warning vide ou null");
        }
        warnings.add(warning);
    }

    /**
     * Ajoute une erreur dans le protocole de fin de process
     * 
     * @param error
     * @throws IllegalArgumentException Si l'erreur est null ou vide
     */
    public final void addError(String error) {
        if (StringUtils.isEmpty(error)) {
            throw new IllegalArgumentException("Interdiction d'ajouter un message d'erreur vide ou null");
        }
        errors.add(error);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }
}
