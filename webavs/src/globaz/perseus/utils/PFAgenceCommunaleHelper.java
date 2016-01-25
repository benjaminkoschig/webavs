/**
 * 
 */
package globaz.perseus.utils;

import globaz.globall.api.GlobazSystem;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.web.application.PFApplication;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFAgenceCommunaleHelper {

    public static Vector getAgencesList() {
        return PFAgenceCommunaleHelper.getListAdministration(IPFConstantes.CS_AGENCE_COMMUNALE);
    }

    public static Vector getListAdministration(String genreAdministration) {
        Vector list = new Vector();

        // Ajout du premier vide
        list.add(new String[] { "", "" });

        AdministrationSearchComplexModel searchComplexModel = new AdministrationSearchComplexModel();
        searchComplexModel.setForCanton(IPFConstantes.CS_CANTON_VAUD);
        searchComplexModel.setForGenreAdministration(genreAdministration);
        try {
            searchComplexModel = TIBusinessServiceLocator.getAdministrationService().find(searchComplexModel);
            for (JadeAbstractModel model : searchComplexModel.getSearchResults()) {
                AdministrationComplexModel administration = (AdministrationComplexModel) model;
                String nomAgence = administration.getTiers().getDesignation2() + " - "
                        + administration.getTiers().getDesignation1();
                list.add(new String[] { administration.getId(), nomAgence });
            }

        } catch (Exception e) {
            JadeThread.logError(PFAgenceCommunaleHelper.class.getName(), e.toString());
        }

        return list;
    }

    public static Vector getRiList() throws Exception {
        String csAgenceRi = ((PFApplication) GlobazSystem.getApplication(PFApplication.DEFAULT_APPLICATION_PERSEUS))
                .getProperty(PFApplication.PROPERTY_PERSEUS_CODESYSTEM_GENREADM_AGENCERI);
        return PFAgenceCommunaleHelper.getListAdministration(csAgenceRi);
    }
}
