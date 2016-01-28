/*
 * Créé le 1 déc. 05
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;

/**
 * <H1>Description</H1>
 * 
 * @author ado
 */
public class CO000CreerContentieux extends CODefaultTransitionAction {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * special dans le cas de la creation d'un contentieux.
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#effectuerTransition(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.batch.COTransition)
     */
    @Override
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {
        contentieux.setIdEtape(getTransition().getIdEtapeSuivante());
        contentieux.setDateDeclenchement(transition.calculerDateProchainDeclenchement(contentieux));
        contentieux.setDateExecution(getDateExecution());
    }
}
