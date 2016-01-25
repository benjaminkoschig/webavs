package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPSortieMontantListViewBean extends CPSortieMontantManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CPSortieMontantListViewBean() {
        super();
    }

    /**
     * @return
     */
    public String getAssurance(int pos) {
        try {
            // Si l'assurance est négative => c'est id rubrique qui est stocké
            float varTravail = Float.parseFloat(((CPSortieMontant) getEntity(pos)).getAssurance());
            if (varTravail < 0) {
                CARubrique rubrique = new CARubrique();
                rubrique.setIdRubrique(Float.toString(Math.abs(varTravail)));
                rubrique.setSession(getSession());
                rubrique.retrieve();
                return rubrique.getDescription();
            } else {
                AFCotisation coti = new AFCotisation();
                coti.setSession(getSession());
                coti.setCotisationId(((CPSortieMontant) getEntity(pos)).getAssurance());
                coti.retrieve();
                return coti.getAssurance().getAssuranceLibelle();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getIdSortieMontant(int pos) {
        return ((CPSortieMontant) getEntity(pos)).getIdSortieMontant();
    }

    /**
     * @return
     */
    public String getMontant(int pos) {
        return ((CPSortieMontant) getEntity(pos)).getMontant();
    }

}
