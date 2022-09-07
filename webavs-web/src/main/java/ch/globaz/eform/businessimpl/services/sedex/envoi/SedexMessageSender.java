package ch.globaz.eform.businessimpl.services.sedex.envoi;

import ch.globaz.common.exceptions.ValidationException;


import java.io.Closeable;
import java.io.File;

public interface SedexMessageSender<T> extends Closeable {

    /**
     * R�alise une validation JAXB sur un message unitaire
     *
     * @param Content L'object � valider
     */
    void validate(T content) throws ValidationException;

    public File generateMessageFile(T content, String nom, String prenom) throws ValidationException;

    void sendMessages();

    SedexInfo getSedexInfo();

}
