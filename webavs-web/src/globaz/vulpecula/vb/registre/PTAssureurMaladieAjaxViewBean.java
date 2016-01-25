package globaz.vulpecula.vb.registre;

import ch.globaz.vulpecula.external.models.pyxis.CSTiers;
import ch.globaz.vulpecula.web.application.PTActions;

/**
 * ViewBean ajax pour la page des assureurs maladie (PPT1003)
 * 
 * @author sel
 * @since Web@BMS 0.01.01
 */
public class PTAssureurMaladieAjaxViewBean extends PTAbstractAdministrationAjaxViewBean {

    /**
     * Constructeur par défaut
     */
    public PTAssureurMaladieAjaxViewBean() {
        super(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_ASSUREURMALADIE);
    }

    @Override
    public String getDestination(String idEntity) {
        return "/vulpecula?userAction=" + PTActions.ASSUREUR_MALADIE_AJAX + ".afficherAJAX&idEntity=" + idEntity;
    }
}
