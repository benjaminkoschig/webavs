package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 08:33:43)
 * 
 * @author: Administrator
 */
public class CAComptabiliteAuxiliaireTypeSection extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idComptabiliteAuxilaire = new String();
    private java.lang.String idTypeSection = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAComptabiliteAuxiliaireTypeSection
     */
    public CAComptabiliteAuxiliaireTypeSection() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CACXTSP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idComptabiliteAuxilaire = statement.dbReadNumeric("IDCOMPTABILITEAUXILAIRE");
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
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
        statement.writeKey("IDCOMPTABILITEAUXILAIRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdComptabiliteAuxilaire(), ""));
        statement.writeKey("IDTYPESECTION", this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCOMPTABILITEAUXILAIRE", this._dbWriteNumeric(statement.getTransaction(),
                getIdComptabiliteAuxilaire(), "idComptabiliteAuxilaire"));
        statement.writeField("IDTYPESECTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), "idTypeSection"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdComptabiliteAuxilaire() {
        return idComptabiliteAuxilaire;
    }

    public java.lang.String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * Setter
     */
    public void setIdComptabiliteAuxilaire(java.lang.String newIdComptabiliteAuxilaire) {
        idComptabiliteAuxilaire = newIdComptabiliteAuxilaire;
    }

    public void setIdTypeSection(java.lang.String newIdTypeSection) {
        idTypeSection = newIdTypeSection;
    }
}
