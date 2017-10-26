package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.Closeable;
import java.io.File;
import ch.globaz.common.exceptions.ValidationException;

public interface SedexMessageSender<T> extends Closeable {

    /**
     * Réalise une validation JAXB sur un message unitaire
     * 
     * @param Content L'object à valider
     */
    void validate(T content) throws ValidationException;

    public File generateMessageFile(T content, String nom, String prenom) throws ValidationException;

    void sendMessages();

    SedexInfo getSedexInfo();

}
