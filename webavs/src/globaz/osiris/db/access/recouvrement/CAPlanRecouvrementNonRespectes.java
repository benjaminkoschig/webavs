package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * Représente une entité de type PlanRecouvrementNonRespectes.
 * 
 * @author Arnaud Dostes, 24-may-2005
 */
public class CAPlanRecouvrementNonRespectes extends CAEcheancePlan {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CACouvertureSectionManager couverture = new CACouvertureSectionManager();

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        couverture.setSession(getSession());
        couverture.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        couverture.find(transaction);
    }

    /**
     * @return Le compte annexe lié
     */
    public CACompteAnnexe getCompteAnnexe() {
        return getPlanRecouvrement().getCompteAnnexe();
    }

    /**
     * @return La couverture liée
     */
    public CACouvertureSectionManager getCouverture() {
        return couverture;
    }

    /**
     * @return
     */
    public String getCouvertureListe() {
        String returnSt = "";
        CACouvertureSectionManager couverture = getCouverture();
        for (int j = 0; j < couverture.size(); j++) {
            returnSt = ((globaz.osiris.db.access.recouvrement.CACouvertureSection) couverture.getEntity(j))
                    .getSection().getDescription() + "<br>";
        }
        return returnSt;
    }
}
