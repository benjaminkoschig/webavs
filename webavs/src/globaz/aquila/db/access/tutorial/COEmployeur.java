package globaz.aquila.db.access.tutorial;

import globaz.aquila.common.COBEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Représente une entité de type Employeur
 * 
 * @author Arnaud Dostes, 04-oct-2004
 */
public class COEmployeur extends COBEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (idemployeur) */
    private String idEmployeur = "";
    /** (nomemployeur) */
    private String nomEmployeur = "";
    /** (typeentreprise) */
    private String typeEntreprise = "";

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        System.out.println("_afterAdd");
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        System.out.println("_afterDelete");
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        System.out.println("_afterRetrieve");
    }

    /**
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        System.out.println("_afterUpdate");
    }

    /**
     * @see globaz.globall.db.BEntity#_alwaysAfterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws Exception {
        System.out.println("_alwaysAfterAdd");
    }

    /**
     * @see globaz.globall.db.BEntity#_alwaysAfterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _alwaysAfterDelete(BTransaction transaction) throws Exception {
        System.out.println("_alwaysAfterDelete");
    }

    /**
     * @see globaz.globall.db.BEntity#_alwaysAfterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _alwaysAfterRetrieve(BTransaction transaction) throws Exception {
        System.out.println("_alwaysAfterRetrieve");
    }

    /**
     * @see globaz.globall.db.BEntity#_alwaysAfterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _alwaysAfterUpdate(BTransaction transaction) throws Exception {
        System.out.println("_alwaysAfterUpdate");
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idEmployeur = this._incCounter(transaction, "0");
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        System.out.println("_beforeDelete");
        COPersonneManager manager = new COPersonneManager();
        manager.setSession(getSession());
        manager.setForIdEmployeur(idEmployeur);
        try {
            manager.find(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }
        if (manager.size() > 0) {
            _addError(transaction, "Impossible de supprimer un employeur lorsque des personnes liées existent!");
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        System.out.println("_beforeRetrieve");
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        System.out.println("_beforeUpdate");
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "EMPLOYEUR";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEmployeur = statement.dbReadNumeric("idemployeur");
        nomEmployeur = statement.dbReadString("nomemployeur");
        typeEntreprise = statement.dbReadString("typeentreprise");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        _propertyMandatory(statement.getTransaction(), nomEmployeur, "Le nom est obligatoire");
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("idemployeur", this._dbWriteNumeric(statement.getTransaction(), idEmployeur, ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("idemployeur",
                this._dbWriteNumeric(statement.getTransaction(), idEmployeur, "idEmployeur"));
        statement.writeField("nomemployeur",
                this._dbWriteString(statement.getTransaction(), nomEmployeur, "nomEmployeur"));
        statement.writeField("typeentreprise",
                this._dbWriteString(statement.getTransaction(), typeEntreprise, "typeEntreprise"));
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getNomEmployeur() {
        return nomEmployeur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getTypeEntreprise() {
        return typeEntreprise;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEmployeur(String string) {
        idEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setNomEmployeur(String string) {
        nomEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setTypeEntreprise(String string) {
        typeEntreprise = string;
    }

}
