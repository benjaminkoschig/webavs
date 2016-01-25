package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * Représente le model de la vue "_rcListe"
 * 
 * @author Arnaud Dostes, 20-apr-2005
 */
public class CAPlanRecouvrementListViewBean extends CAPlanRecouvrementManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAPlanRecouvrementViewBean();
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager#setOrderBy(java.lang.String)
     */
    @Override
    public void setOrderBy(String string) {
        if (CACompteAnnexe.FIELD_IDEXTERNEROLE.equals(string)) {
            super.setOrderBy(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + string); // Compte
            // Annexe
        } else {
            super.setOrderBy(_getCollection() + "CAPLARP." + string); // plan de
            // recouvrement
        }
    }

}
