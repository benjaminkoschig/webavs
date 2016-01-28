package globaz.hermes.utils;

import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import java.io.Serializable;
import java.util.Comparator;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HECompareAnnonce implements Comparator, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur()) == 0) {
            // les deux utilisateurs sont =, on trie sur la référence interne
            try {

                if (((HEAnnoncesViewBean) o1)
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE)
                        .toString()
                        .compareTo(
                                ((HEAnnoncesViewBean) o2).getField(
                                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE).toString()) == 0) {
                    // les deux références internes sont =, donc on trie sous le
                    // numéro AVS
                    return ((HEAnnoncesViewBean) o1).getNumeroAVS().compareTo(((HEAnnoncesViewBean) o2).getNumeroAVS());
                } else {
                    return ((HEAnnoncesViewBean) o1)
                            .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE)
                            .toString()
                            .compareTo(
                                    ((HEAnnoncesViewBean) o2).getField(
                                            IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur());
            }
        } else {
            return ((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur());
        }
    }
}
