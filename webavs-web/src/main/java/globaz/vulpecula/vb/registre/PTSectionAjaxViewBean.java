package globaz.vulpecula.vb.registre;

import ch.globaz.vulpecula.external.models.pyxis.CSTiers;

/**
 * ViewBean ajax pour la page des sections (PPT1005)
 * 
 * @author sel
 * @since Web@BMS 0.01.01
 */
public class PTSectionAjaxViewBean extends PTAbstractAdministrationAjaxViewBean {

    public PTSectionAjaxViewBean() {
        super(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_SECTION);
    }
}
