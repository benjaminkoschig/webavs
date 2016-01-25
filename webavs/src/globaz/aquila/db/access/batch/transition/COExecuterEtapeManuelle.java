/*
 * Créé le 20 janv. 06
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;

/**
 * <H1>Description</H1>
 * 
 * @author sel
 */
public class COExecuterEtapeManuelle extends CODefaultTransitionAction {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redéfinie pour empêcher la modification de l'état du contentieux.
     * 
     * @see COTransitionAction#effectuerTransition(COContentieux, COTransition)
     */
    @Override
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {
        // HACK: UNE IMPUTATION SE COMPORTE COMME SI AUCUNE TRANSITION N'AVAIT
        // ETE EFFECTUEE !!!
        contentieux.setDateDeclenchement(contentieux.getProchaineDateDeclenchement());
        contentieux.setDateExecution(getDateExecution());
    }
}
