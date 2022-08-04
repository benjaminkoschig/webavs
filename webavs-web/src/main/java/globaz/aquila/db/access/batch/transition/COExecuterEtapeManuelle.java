/*
 * Cr�� le 20 janv. 06
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
     * red�finie pour emp�cher la modification de l'�tat du contentieux.
     * 
     * @see COTransitionAction#effectuerTransition(COContentieux, COTransition)
     */
    @Override
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {
        // HACK: UNE IMPUTATION SE COMPORTE COMME SI AUCUNE TRANSITION N'AVAIT
        // ETE EFFECTUEE !!!
        contentieux.setDateDeclenchement(contentieux.getProchaineDateDeclenchement());
        contentieux.setDateExecution(getDateExecution());
        contentieux.setEBillPrintable(getEBillPrintable());
    }
}
