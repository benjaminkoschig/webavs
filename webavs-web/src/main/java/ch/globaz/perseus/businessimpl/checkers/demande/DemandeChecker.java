package ch.globaz.perseus.businessimpl.checkers.demande;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class DemandeChecker extends PerseusAbstractChecker {

    /**
     * @param demande
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */
    public static void checkForCreate(Demande demande) throws DemandeException, JadePersistenceException {

    }

    /**
     * @param demande
     * @throws DemandeException
     *             , JadePersistenceException
     */
    public static void checkForDelete(Demande demande) throws DemandeException, JadePersistenceException {

    }

    /**
     * @param demande
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */
    public static void checkForUpdate(Demande demande) throws DemandeException, JadePersistenceException {

        // Si la demande est validée, contrôler que uniquement la date de fin a été ajoutée
    }

}
