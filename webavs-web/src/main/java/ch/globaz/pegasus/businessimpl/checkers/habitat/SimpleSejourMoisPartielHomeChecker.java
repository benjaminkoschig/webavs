package ch.globaz.pegasus.businessimpl.checkers.habitat;

import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

public class SimpleSejourMoisPartielHomeChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        SimpleSejourMoisPartielHomeChecker.checkMandatory(sejourMoisPartielHome);
    }

    /**
     * @param sejourMoisPartielHome
     */
    public static void checkForDelete(SimpleSejourMoisPartielHome sejourMoisPartielHome) {

    }

    /**
     * @param sejourMoisPartielHome
     * @throws JadePersistenceException
     * @throws SejourMoisPartielHomeException
     */
    public static void checkForUpdate(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        SimpleSejourMoisPartielHomeChecker.checkMandatory(sejourMoisPartielHome);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param sejourMoisPartielHome
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleSejourMoisPartielHomeChecker.testIntegrityIdHome(sejourMoisPartielHome);
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * 
     * @param sejourMoisPartielHome
     */

    private static void checkMandatory(SimpleSejourMoisPartielHome sejourMoisPartielHome) {
        if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getPrixJournalier())) {
            JadeThread.logError(sejourMoisPartielHome.getClass().getName(),
                    "pegasus.sejourMoisPartielHome.prixJournalier.mandatory");
        }
        if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getNbJours())) {
            JadeThread.logError(sejourMoisPartielHome.getClass().getName(),
                    "pegasus.sejourMoisPartielHome.nbJours.mandatory");
        }

        if(sejourMoisPartielHome.getIsVersementDirect()){
            try {
                Home home = PegasusServiceLocator.getHomeService().read(sejourMoisPartielHome.getIdHome());
                AdresseTiersDetail homeAdressePaiementFormatee = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE, TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), "");
                if (homeAdressePaiementFormatee.getAdresseFormate() == null){
                    JadeThread.logError(sejourMoisPartielHome.getClass().getName(),
                            "pegasus.simpleTaxeJournalierHome.versementDirect.mandatory");
                } else {
                    String idAdressePaiement = homeAdressePaiementFormatee.getFields().get(AdresseTiersDetail.ADRESSEP_ID_ADRESSE);
                    sejourMoisPartielHome.setIdAdressePaiement(idAdressePaiement);
                }
            } catch (JadePersistenceException e) {
                JadeThread.logError(sejourMoisPartielHome.getClass().getName(),e.getMessage());
            } catch (JadeApplicationException e) {
                JadeThread.logError(sejourMoisPartielHome.getClass().getName(),e.getMessage());
            }
        }
    }

    private static void testIntegrityIdHome(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // vérifie que l'id du home existe
        SimpleHomeSearch homesearch = new SimpleHomeSearch();
        homesearch.setForIdHome(simpleSejourMoisPartielHome.getIdHome());
        try {
            if (PegasusImplServiceLocator.getSimpleHomeService().count(homesearch) < 1) {
                JadeThread.logError(simpleSejourMoisPartielHome.getClass().getName(),
                        "pegasus.simpleTaxeJournalierHome.idHome.integrity");

            }
        } catch (HomeException e) {
            throw new SejourMoisPartielHomeException("Unable to check the id home", e);
        }
    }

}
