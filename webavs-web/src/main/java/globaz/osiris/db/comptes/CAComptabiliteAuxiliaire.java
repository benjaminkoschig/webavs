package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 08:03:00)
 * 
 * @author: Administrator
 */
public class CAComptabiliteAuxiliaire extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idComptabiliteAuxiliaire = new String();
    private java.lang.String idTraduction = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAComptabiliteAuxiliaire
     */
    public CAComptabiliteAuxiliaire() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CACAUXP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idComptabiliteAuxiliaire = statement.dbReadNumeric("IDCOMAUX");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCOMAUX",
                this._dbWriteNumeric(statement.getTransaction(), getIdComptabiliteAuxiliaire(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCOMAUX",
                this._dbWriteNumeric(statement.getTransaction(), getIdComptabiliteAuxiliaire(), "idComAux"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdComptabiliteAuxiliaire() {
        return idComptabiliteAuxiliaire;
    }

    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Setter
     */
    public void setIdComptabiliteAuxiliaire(java.lang.String newIdComptabiliteAuxiliaire) {
        idComptabiliteAuxiliaire = newIdComptabiliteAuxiliaire;
    }

    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }
}
