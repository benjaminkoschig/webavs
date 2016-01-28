package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 10:12:32)
 * 
 * @author: Administrator
 */
public class CAComptabiliteAuxiliaireCompteCourant extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idComptabiliteAuxiliaire = new String();
    private java.lang.String idCompteCourant = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAComptabiliteAuxiliaireCompteCourant
     */
    public CAComptabiliteAuxiliaireCompteCourant() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CACXCCP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteCourant = statement.dbReadNumeric("IDCOMPTECOURANT");
        idComptabiliteAuxiliaire = statement.dbReadNumeric("IDCOMAUX");
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
        statement.writeKey("IDCOMPTECOURANT",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), ""));
        statement.writeKey("IDCOMAUX",
                this._dbWriteNumeric(statement.getTransaction(), getIdComptabiliteAuxiliaire(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCOMPTECOURANT",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), "idCompteCourant"));
        statement.writeField("IDCOMAUX",
                this._dbWriteNumeric(statement.getTransaction(), getIdComptabiliteAuxiliaire(), "idComAux"));
    }

    public java.lang.String getIdComptabiliteAuxiliaire() {
        return idComptabiliteAuxiliaire;
    }

    /**
     * Getter
     */
    public java.lang.String getIdCompteCourant() {
        return idCompteCourant;
    }

    public void setIdComptabiliteAuxiliaire(java.lang.String newIdComptabiliteAuxiliaire) {
        idComptabiliteAuxiliaire = newIdComptabiliteAuxiliaire;
    }

    /**
     * Setter
     */
    public void setIdCompteCourant(java.lang.String newIdCompteCourant) {
        idCompteCourant = newIdCompteCourant;
    }
}
