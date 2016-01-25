package ch.globaz.pegasus.businessimpl.checkers.monnaieetrangere;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class MonnaieEtrangereChecker extends PegasusAbstractChecker {
    /**
     * Validation lors de l'effacement d'un home
     * 
     * @param monnaieEtrangere
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TypeChambreException
     * @throws MonnaieEtrangereException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Validation d'un home lors d'une mise a jours
     * 
     * @param monnaieetrangere
     * @throws MonnaieEtrangereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>Il ne peut exister qu'un seul home pour un tiersHome et un nom de batiment donne</li>
     * 
     * @param monnaiesEtrangeres
     * @throws MonnaieEtrangereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * @param monnaiesEtrangeres
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws MonnaieEtrangereException
     */
    private static void checkMandatory(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

}
