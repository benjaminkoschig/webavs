package ch.globaz.prestation.businessimpl.checkers.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;
import ch.globaz.prestation.businessimpl.checkers.PrestationCommonAbstractChecker;

public class DemandePrestationChecker extends PrestationCommonAbstractChecker {

    public static void checkForCreate(SimpleDemandePrestation demandePrestation) throws DemandePrestationException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        DemandePrestationChecker.checkMandatory(demandePrestation);
        DemandePrestationChecker.checkIntegrity(demandePrestation);
    }

    /**
     * @param demandePrestation
     */
    public static void checkForUpdate(SimpleDemandePrestation demandePrestation) {
    }

    private static void checkIntegrity(SimpleDemandePrestation demandePrestation) throws DemandePrestationException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * @param demandePrestation
     * @throws DemandePrestationException
     */
    private static void checkMandatory(SimpleDemandePrestation demandePrestation) throws DemandePrestationException {
        if (JadeStringUtil.isEmpty(demandePrestation.getIdTiers())) {
            throw new DemandePrestationException("A tiers is required");
        }
        if (JadeStringUtil.isEmpty(demandePrestation.getEtat())) {
            throw new DemandePrestationException("The field Etat can't be undefined");
        }
        if (JadeStringUtil.isEmpty(demandePrestation.getTypeDemande())) {
            throw new DemandePrestationException("The field TypeDemande can't be undefined");
        }

    }

}
