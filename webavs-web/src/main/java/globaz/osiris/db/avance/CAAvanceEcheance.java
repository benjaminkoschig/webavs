package globaz.osiris.db.avance;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;

/**
 * Class extends de CAEcheancePlan. <br>
 * Repr�sente l'entit� de l'�ch�ance li�e � l'avance. <br>
 * Dans cette entit� sera sauvegard� la date de fin (si sp�cifi�) de l'avance, le montant total �x�cut� ainsi que la
 * derni�re date d'�x�cution.
 * 
 * @author dda
 */
public class CAAvanceEcheance extends CAEcheancePlan {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlan#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlan#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdEcheancePlan(this._incCounter(transaction, getIdEcheancePlan(), _getTableName()));
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlan#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlan#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // idPlanRecouvrement
        _propertyMandatory(statement.getTransaction(), getIdPlanRecouvrement(),
                getSession().getLabel(CAEcheancePlan.LABEL_PLAN_OBLIGATOIRE));
        getPlanRecouvrement();
    }
}
