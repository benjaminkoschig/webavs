package globaz.osiris.db.comptes;

/**
 * Classe : type_conteneur Description : Date de création: 3 août 04
 * 
 * @author scr
 */
public class CACumulOperation extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String totalMontant = null;
    private String typeOperation = null;

    /**
     * Constructor for CACumulOperation.
     */
    public CACumulOperation() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null;// unused
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        typeOperation = statement.dbReadString("IDTYPEOPERATION");
        totalMontant = statement.dbReadNumeric("TOTALMONTANT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * Returns the totalMontant.
     * 
     * @return String
     */
    public String getTotalMontant() {
        return totalMontant;
    }

    /**
     * Returns the typeOperation.
     * 
     * @return String
     */
    public String getTypeOperation() {
        return typeOperation;
    }

    /**
     * Sets the totalMontant.
     * 
     * @param totalMontant
     *            The totalMontant to set
     */
    public void setTotalMontant(String totalMontant) {
        this.totalMontant = totalMontant;
    }

    /**
     * Sets the typeOperation.
     * 
     * @param typeOperation
     *            The typeOperation to set
     */
    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

}
