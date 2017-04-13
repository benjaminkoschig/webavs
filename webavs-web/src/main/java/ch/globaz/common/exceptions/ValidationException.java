package ch.globaz.common.exceptions;

import java.util.Iterator;
import java.util.List;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private List<String> messageErreurDeValidation;

    public ValidationException(List<String> erreurDeValidation) {
        messageErreurDeValidation = erreurDeValidation;
    }

    public List<String> getMessageErreurDeValidation() {
        return messageErreurDeValidation;
    }

    /**
     * Retourne un message formaté avec les erreurs de validation JAXB
     * 
     * @return un message formaté avec les erreurs de validation JAXB
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        if (messageErreurDeValidation != null && messageErreurDeValidation.size() > 0) {
            for (Iterator<String> iterator = messageErreurDeValidation.iterator(); iterator.hasNext();) {
                sb.append(iterator.next());
                if (iterator.hasNext()) {
                    sb.append(" - ");
                }
            }
        } else {
            sb.append("Aucune information sur l'erreur...");
        }
        return sb.toString();
    }
}
