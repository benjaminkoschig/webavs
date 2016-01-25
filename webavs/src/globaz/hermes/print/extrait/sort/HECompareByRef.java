/*
 * Créé le 28 oct. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.extrait.sort;

import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.jade.log.JadeLogger;
import java.util.Comparator;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HECompareByRef implements Comparator {
    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        try {
            if (((HEAnnoncesViewBean) o1)
                    .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE)
                    .toString()
                    .compareTo(
                            ((HEAnnoncesViewBean) o2)
                                    .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE).toString()) == 0) {
                if (((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur()) == 0) {
                    return ((HEAnnoncesViewBean) o1).getNumeroAVS().compareTo(((HEAnnoncesViewBean) o2).getNumeroAVS());
                } else {
                    return ((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(
                            ((HEAnnoncesViewBean) o2).getUtilisateur());
                }
            } else {
                return ((HEAnnoncesViewBean) o1)
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE)
                        .toString()
                        .compareTo(
                                ((HEAnnoncesViewBean) o2).getField(
                                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE).toString());
            }
        } catch (Exception e) {
            JadeLogger.info("globaz.hermes.print.extrait.sort.HECompareByRef", e.getMessage());
            return ((HEAnnoncesViewBean) o1).getUtilisateur().compareTo(((HEAnnoncesViewBean) o2).getUtilisateur());
        }
    }
}
