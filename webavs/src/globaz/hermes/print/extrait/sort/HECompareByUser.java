/*
 * Cr�� le 28 oct. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.extrait.sort;

import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import java.util.Comparator;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class HECompareByUser implements Comparator {
    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur()) == 0) {
            // les deux utilisateurs sont =, on trie sur la r�f�rence interne
            try {

                if (((HEAnnoncesViewBean) o1)
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE)
                        .toString()
                        .compareTo(
                                ((HEAnnoncesViewBean) o2).getField(
                                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE).toString()) == 0) {
                    // les deux r�f�rences internes sont =, donc on trie sous le
                    // num�ro AVS
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
