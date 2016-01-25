package globaz.naos.db.planAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * La classe définissant l'entité PlanAssurance.
 * 
 * @author administrator
 */
public class AFPlanAssurance extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String assuranceId = new String();
    private java.lang.String planId = new String();

    /**
     * Constructeur d'AFPlanAssurance.
     */
    public AFPlanAssurance() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFPLASP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        planId = statement.dbReadNumeric("MRIPLA");
        assuranceId = statement.dbReadString("MBIASS");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MRIPLA", this._dbWriteNumeric(statement.getTransaction(), getPlanId(), ""));
        statement.writeKey("MBIASS", this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MRIPLA", this._dbWriteNumeric(statement.getTransaction(), getPlanId(), "PlanId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "AssuranceId"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    public java.lang.String getPlanId() {
        return planId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAssuranceId(java.lang.String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    public void setPlanId(java.lang.String newPlanId) {
        planId = newPlanId;
    }
}
