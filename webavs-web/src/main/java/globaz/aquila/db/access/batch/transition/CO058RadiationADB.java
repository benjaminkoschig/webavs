package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;

public class CO058RadiationADB extends CODefaultTransitionAction {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * red�finie pour emp�cher la modification de l'�tat du contentieux.
     * 
     * @see COTransitionAction#effectuerTransition(COContentieux, COTransition)
     */
    @Override
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {

        contentieux.setDateDeclenchement(contentieux.getProchaineDateDeclenchement());
        contentieux.setDateExecution(getDateExecution());
        contentieux.setIdEtape(transition.getEtapeSuivante().getIdEtape());

    }
}
