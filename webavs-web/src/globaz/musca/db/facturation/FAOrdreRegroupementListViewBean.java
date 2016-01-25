package globaz.musca.db.facturation;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FAOrdreRegroupementListViewBean extends FAOrdreRegroupementManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getIdOrdreRegroupement(int pos) {
        return ((FAOrdreRegroupement) getEntity(pos)).getIdOrdreRegroupement();
    }

    public String getLibelleFR(int pos) {
        return ((FAOrdreRegroupement) getEntity(pos)).getLibelleFR();
    }

    public String getNature(int pos) {
        return ((FAOrdreRegroupement) getEntity(pos)).getNature();
    }

    public String getNumCaisse(int pos) {
        return ((FAOrdreRegroupement) getEntity(pos)).getNumCaisse();

    }

    public String getOrdreRegroupement(int pos) {
        return ((FAOrdreRegroupement) getEntity(pos)).getOrdreRegroupement();
    }

}
