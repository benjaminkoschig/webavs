package ch.globaz.pegasus.businessimpl.checkers.habitat;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class TaxeJournaliereHomeChecker extends PegasusAbstractChecker {

    public static void checkForCreate(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
        if (!PegasusAbstractChecker.threadOnError()) {
            TaxeJournaliereHomeChecker.checkIntegrity(taxeJournaliereHome);

            // try {
            // // Si la date de fin est rempli il faut définir un motifi de sortie
            // if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
            //
            // if (!taxeJournaliereHome.getSimpleDonneeFinanciereHeader().getIsCopieFromPreviousVersion()
            // && !JadeStringUtil.isEmpty(taxeJournaliereHome.getSimpleDonneeFinanciereHeader()
            // .getDateFin())
            // && JadeStringUtil.isEmpty(taxeJournaliereHome.getSimpleTaxeJournaliereHome()
            // .getCsDestinationSortie())) {
            //
            // }
            // }
            // } catch (PropertiesException e) {
            // throw new TaxeJournaliereHomeException("Unable to obtain the propertie "
            // + EPCProperties.GESTION_ANNONCES_LAPRAMS.getProperty(), e);
            // }
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleLoyer
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
    }

}
