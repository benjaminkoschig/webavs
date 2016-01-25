package ch.globaz.vulpecula.documents.catalog;

import java.io.Serializable;
import java.util.List;
import ch.globaz.vulpecula.application.ApplicationConstants;

/**
 * <p>
 * Classe abstraite permettant la g�n�ration de document iText pour le module Vulpecula.
 * 
 * <p>
 * 
 * <p>
 * La classe g�n�rique utilis�e doit impl�ment�e �tre serializable.
 * <p>
 * La classe concr�te �tendant cette classe doit impl�menter deux constructeurs obligatoirement :
 * <ul>
 * <li>{@link #DocumentManager()} -> A ne pas utiliser mais n�cessaire pour le serveur de job. Celle-ci doit
 * OBLIGATOIREMENT faire appel au constructeur ci-dessous this(null) !!!
 * <li>{@link #DocumentManager(List, String, String)} -> A utiliser pour cr�er un processus
 * </ul>
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 4 juin 2014
 * 
 */
public abstract class VulpeculaDocumentManager<T extends Serializable> extends DocumentManager<T> {

    private static final long serialVersionUID = 1L;

    public VulpeculaDocumentManager() throws Exception {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA, ApplicationConstants.APPLICATION_VULPECULA_REP);
    }

    public VulpeculaDocumentManager(final T element, final String documentName, final String numeroInforom)
            throws Exception {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA, ApplicationConstants.APPLICATION_VULPECULA_REP,
                element, documentName, numeroInforom);
    }
}
