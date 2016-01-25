/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.adhesion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/**
 * Le listViewBean de l'entité Adhésion.
 * 
 * @author sau
 */
public class AFAdhesionListViewBean extends AFAdhesionManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAdhesionId(int index) {
        return ((AFAdhesion) getEntity(index)).getAdhesionId();
    }

    public TIAdministrationViewBean getAdministrationCaisse(int index) {
        return ((AFAdhesion) getEntity(index)).getAdministrationCaisse();
    }

    public TIAdministrationViewBean getAdminstrationAssocia(int index) {
        return ((AFAdhesion) getEntity(index)).getAdminstrationAssocia();
    }

    public String getAffiliationId(int index) {
        return ((AFAdhesion) getEntity(index)).getAffiliationId();
    }

    public String getDateDebut(int index) {
        return ((AFAdhesion) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFAdhesion) getEntity(index)).getDateFin();
    }

    public String getLibelle(int index) {
        AFAdhesion adhesion = (AFAdhesion) getEntity(index);
        if (adhesion != null) {
            if (CodeSystem.TYPE_ADHESION_ASSOCIATION.equals(adhesion.getTypeAdhesion())
                    || CodeSystem.TYPE_ADHESION_AGENCE.equals(adhesion.getTypeAdhesion())) {
                return adhesion.getAdminstrationAssocia().getNom();
            } else if (CodeSystem.TYPE_ADHESION_CAISSE.equals(adhesion.getTypeAdhesion())
                    || CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE.equals(adhesion.getTypeAdhesion())
                    || CodeSystem.TYPE_ADHESION_AFFILIATION.equals(adhesion.getTypeAdhesion())) {
                String institution = "";
                if (!JadeStringUtil.isEmpty(adhesion.getAdministrationCaisse().getCodeInstitution())) {
                    // ajouter le code instituation si présent
                    institution = " " + adhesion.getAdministrationCaisse().getCodeInstitution();
                }
                return adhesion.getAdministrationCaisse().getCodeAdministration() + institution + " - "
                        + adhesion.getPlanCaisse().getLibelle();
            } /*
               * else { String libelle; if (!JadeStringUtil.isEmpty(adhesion.getAdminstrationAssocia
               * ().getCodeInstitution())) { //ajouter le code instituation si présent libelle =
               * adhesion.getAdminstrationAssocia().getCodeInstitution(); } else { // sinon, nom du tiers libelle =
               * adhesion.getAdminstrationAssocia().getNom(); } return
               * adhesion.getAdminstrationAssocia().getCodeAdministration() + " " + libelle; }
               */
        }
        return "";
    }

    public AFPlanCaisse getPlanCaisse(int index) {
        return ((AFAdhesion) getEntity(index)).getPlanCaisse();
    }

    public String getPlanCaisseId(int index) {
        return ((AFAdhesion) getEntity(index)).getPlanCaisseId();
    }

    public String getTypeAdhesion(int index) {
        return ((AFAdhesion) getEntity(index)).getTypeAdhesion();
    }
}
