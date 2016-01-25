package globaz.naos.db.assurance;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CARubriqueViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class AFAssuranceListViewBean extends AFAssuranceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAssuranceCanton(int index) {

        return ((AFAssurance) getEntity(index)).getAssuranceCanton();
    }

    public String getAssuranceGenre(int index) {

        return ((AFAssurance) getEntity(index)).getAssuranceGenre();
    }

    public String getAssuranceId(int index) {

        return ((AFAssurance) getEntity(index)).getAssuranceId();
    }

    public String getAssuranceLibelle(int index) {

        return ((AFAssurance) getEntity(index)).getAssuranceLibelle();
    }

    public String getAssuranceReferenceLibelle(int index) {

        return ((AFAssurance) getEntity(index)).getAssuranceReferenceLibelle();
    }

    public CARubriqueViewBean getRubriqueComptable(int index) {

        return ((AFAssurance) getEntity(index)).getRubriqueComptable();
    }

    public String getRubriqueId(int index) {

        return ((AFAssurance) getEntity(index)).getRubriqueId();
    }
}
