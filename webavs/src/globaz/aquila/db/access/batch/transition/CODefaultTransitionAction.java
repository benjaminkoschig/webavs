/**
 *
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BTransaction;
import java.util.List;

/**
 * Classe pour les étapes n'ayant aucun traitement ou document. Cette action ne fait rien d'autre que de permettre de
 * passer à l'étape suivante, pas de doc, pas de critères.
 * 
 * @author SEL
 */
public class CODefaultTransitionAction extends COTransitionAction {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_annuler(
     * globaz.aquila.db.access.poursuite.COContentieux, globaz.aquila.db.access.poursuite.COHistorique,
     * globaz.globall.db.BTransaction)
     */
    // protected void _annuler(COContentieux contentieux, COHistorique
    // historique, BTransaction transaction) throws COTransitionException {
    // // Rien
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(
     * globaz.aquila.db.access.poursuite.COContentieux, java.util.List, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Rien
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate
     * (globaz.aquila.db.access.poursuite.COContentieux, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        // Rien
    }

}
