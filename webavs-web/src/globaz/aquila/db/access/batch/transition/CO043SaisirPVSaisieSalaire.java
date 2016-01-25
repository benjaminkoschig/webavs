package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO043SaisirPVSaisieSalaire extends COTransitionAction {

    /** La date d'execution de la saisie */
    private String dateExecutionSaisie = "";

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        if (JadeStringUtil.isEmpty(getDateExecution())) {
            throw new COTransitionException("AQUILA_ERR_CO043_DATE_EXECUTION_SAISIE_SALAIRE_VIDE",
                    COActionUtils.getMessage(contentieux.getSession(),
                            "AQUILA_ERR_CO043_DATE_EXECUTION_SAISIE_SALAIRE_VIDE"));
        }
        // @TODO Autres pré conditions ?
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateExecutionSaisie() {
        return dateExecutionSaisie;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateExecutionSaisie(String string) {
        dateExecutionSaisie = string;
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_annuler(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.poursuite.COHistorique, globaz.globall.db.BTransaction)
     */
    // protected void _annuler(COContentieux contentieux, COHistorique
    // historique, BTransaction transaction) throws COTransitionException {
    // }

}
