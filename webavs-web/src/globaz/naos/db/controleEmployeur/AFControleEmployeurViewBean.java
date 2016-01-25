package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;

public class AFControleEmployeurViewBean extends AFControleEmployeur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur d'AFControleEmployeurViewBean
     */
    public AFControleEmployeurViewBean() {
        super();
    }

    public String getNumAffilie(String idAffiliation, BSession session) {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(session);
        aff.setId(idAffiliation);
        try {
            aff.retrieve();
        } catch (Exception e) {
            return "";
        }
        return aff.getAffilieNumero();
    }

    public boolean isInscrit() {
        try {
            return AFParticulariteAffiliation.existeParticulariteDateDonnee(getSession(), getAffiliationId(),
                    CodeSystem.PARTIC_AFFILIE_INSCRIT_RC, JACalendar.todayJJsMMsAAAA());
        } catch (Exception e) {
            return false;
        }

    }
}
