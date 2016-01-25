package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCTaxeJournaliereHomeHandler {

    public static String getLibelleAssurenceMaladie(PersonneEtendueComplexModel tierAsurenceMaladie, BSession objSession) {
        return PCCommonHandler.getStringDefault(tierAsurenceMaladie.getTiers().getDesignation1() + " "
                + tierAsurenceMaladie.getTiers().getDesignation2());
    }

    public static String getLibelleHomeAvecChambre(TypeChambre typeChambre, BSession objSession) {
        return PCTaxeJournaliereHomeHandler.getSimpleLibelleHome(typeChambre) + " "
                + typeChambre.getDesignationTypeChambre();
    }

    public static String getPrix(TaxeJournaliereHome taxeJournaliereHome, BSession objSession)
            throws JadeApplicationServiceNotAvailableException {

        String prix = null;
        try {
            prix = PegasusServiceLocator.getTaxeJournaliereHomeService().getPrixTypeChambreInMap(taxeJournaliereHome);

            if (!JadeStringUtil.isEmpty(prix)) {
                prix = PCCommonHandler.getCurrencyFormtedDefault(prix);
            } else {
                String texteToBubble = objSession.getLabel("JSP_PC_HABITAT_TAXE_JOURNALIERE_PRIX_NOT_FOUND");
                prix = "<img src ='/webavs/images/small_info.png' data-g-bubble='wantMarker:false,text:¦"
                        + texteToBubble + "¦'/> ";

            }
        } catch (Exception e) {
            return "error prix" + e.getMessage() + " c " + e.getCause() + " " + e.toString();
        }
        return prix;
    }

    public static String getSimpleLibelleHome(TypeChambre typeChambre) {
        return typeChambre.getHome().getAdresse().getTiers().getDesignation1() + " "
                + typeChambre.getHome().getAdresse().getTiers().getDesignation2();
    }

    public static void putPrix(Habitat habitat) throws PrixChambreException, HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PegasusServiceLocator.getTaxeJournaliereHomeService().putPrixTypeChambreInMap(habitat);
    }
}
