package ch.globaz.pegasus.businessimpl.checkers.habitat;

import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

public class SimpleTaxeJournaliereHomeChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleTaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        SimpleTaxeJournaliereHomeChecker.checkMandatory(taxeJournaliereHome);
        // SimpleTaxeJournaliereHomeChecker.checkIntegrity(taxeJournaliereHome);
    }

    /**
     * @param taxeJournaliereHome
     */
    public static void checkForDelete(SimpleTaxeJournaliereHome taxeJournaliereHome) {

    }

    /**
     * @param taxeJournaliereHome
     * @throws JadePersistenceException
     * @throws TaxeJournaliereHomeException
     */
    public static void checkForUpdate(SimpleTaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        SimpleTaxeJournaliereHomeChecker.checkMandatory(taxeJournaliereHome);
        // SimpleTaxeJournaliereHomeChecker.checkIntegrity(taxeJournaliereHome);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param taxeJournaliereHome
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimpleTaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                SimpleTaxeJournaliereHomeChecker.testIntegrityIdHome(taxeJournaliereHome);
                SimpleTaxeJournaliereHomeChecker.testIntegrityIdAssurenceMaladie(taxeJournaliereHome);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TaxeJournaliereHomeException("Unable to check taxeJournaliereHome", e);
        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * 
     * @param taxeJournaliereHome
     */

    private static void checkMandatory(SimpleTaxeJournaliereHome taxeJournaliereHome) {
        if (JadeStringUtil.isEmpty(taxeJournaliereHome.getIdHome())) {
            JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                    "pegasus.simpleTaxeJournalierHome.idHome.mandatory");
        }
        if (JadeStringUtil.isEmpty(taxeJournaliereHome.getIdTypeChambre())) {
            JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                    "pegasus.simpleTaxeJournalierHome.csTypeChambre.mandatory");
        }

        if (taxeJournaliereHome.getIsParticipationLCA()) {
            if (JadeStringUtil.isEmpty(taxeJournaliereHome.getPrimeAPayer())) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.primeAPayer.mandatory");
            }
            if (JadeNumericUtil.isEmptyOrZero(taxeJournaliereHome.getIdAssureurMaladie())) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.idAssurenceMaladie.mandatory");
            }
            if (JadeNumericUtil.isEmptyOrZero(taxeJournaliereHome.getMontantJournalierLCA())) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.montantJournalierLCA.mandatory");
            }
        }
        if(taxeJournaliereHome.getIsVersementDirect()){
            try {
                Home home = PegasusServiceLocator.getHomeService().read(taxeJournaliereHome.getIdHome());
                AdresseTiersDetail homeAdressePaiementFormatee = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE, TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), "");
                if (homeAdressePaiementFormatee.getAdresseFormate() == null){
                 JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.versementDirect.mandatory");
                }
            } catch (JadePersistenceException e) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),e.getMessage());
            } catch (JadeApplicationException e) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),e.getMessage());
            }

        }
        if(!JadeStringUtil.isEmpty(taxeJournaliereHome.getDateEntreeHome()) && !taxeJournaliereHome.getOldDonneeFinanciere()) {
            JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                    "pegasus.simpleTaxeJournalierHome.entreehome.mandatory");
        }

    }

    private static void testIntegrityIdAssurenceMaladie(SimpleTaxeJournaliereHome taxeJournaliereHome) {
        // TODO
    }

    private static void testIntegrityIdHome(SimpleTaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // vérifie que l'id du home existe
        SimpleHomeSearch homesearch = new SimpleHomeSearch();
        homesearch.setForIdHome(taxeJournaliereHome.getIdHome());
        try {
            if (PegasusImplServiceLocator.getSimpleHomeService().count(homesearch) < 1) {
                JadeThread.logError(taxeJournaliereHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.idHome.integrity");

            }
        } catch (HomeException e) {
            throw new TaxeJournaliereHomeException("Unable to check the id home", e);
        }
    }
}
