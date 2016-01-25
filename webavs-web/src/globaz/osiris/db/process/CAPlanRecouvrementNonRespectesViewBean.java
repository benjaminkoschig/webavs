/*
 * Cr�� le May 24, 2005
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.process.CAProcessPlanRecouvrementNonRespectes;
import java.util.List;

/**
 * @author dostes Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CAPlanRecouvrementNonRespectesViewBean extends CAProcessPlanRecouvrementNonRespectes {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPlanRecouvrement = "";
    private List roles = null;

    public CAPlanRecouvrementNonRespectesViewBean() throws Exception {
        super();
    }

    /**
     * Pour l'affichage � l'�cran des roles
     * 
     * @return the roles
     */
    public List _getRoles() {
        if (roles == null) {
            // -- les roles
            CARoleManager manRole = new CARoleManager();

            manRole.setSession(getSession());
            try {
                manRole.find();
            } catch (Exception e) {
                super._addError(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
                super.setMsgType(FWViewBeanInterface.WARNING);
                super.setMessage(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            }
            _setRoles(manRole.getContainer());
        }
        return roles;
    }

    /**
     * @param roles
     *            the roles to set
     */
    public void _setRoles(List roles) {
        this.roles = roles;
    }

    /**
     * @return the idPlanRecouvrement
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return true si la caisse g�re les rappels sur les plans
     */
    public boolean isRappelSurPlan() {
        return CAApplication.getApplicationOsiris().getCAParametres().isRappelSurPlan();
    }

    /**
     * @param idPlanRecouvrement
     *            the idPlanRecouvrement to set
     */
    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }
}
