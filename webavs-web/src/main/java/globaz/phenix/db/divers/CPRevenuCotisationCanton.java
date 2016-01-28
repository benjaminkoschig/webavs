package globaz.phenix.db.divers;

import globaz.globall.db.BConstants;
import globaz.phenix.translation.CodeSystem;

public class CPRevenuCotisationCanton extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String anneeDebut = "";
    private Boolean avecCotisation = Boolean.FALSE;
    private java.lang.String canton = "";

    private java.lang.String codeCanton = "";

    private java.lang.String dateActivite = "";
    private java.lang.String idRevCotiCanton = "";

    /**
     * Commentaire relatif au constructeur CPPeriodeFiscale
     */
    public CPRevenuCotisationCanton() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdRevCotiCanton(this._incCounter(transaction, idRevCotiCanton));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPRECAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRevCotiCanton = statement.dbReadNumeric("RCIREC");
        anneeDebut = statement.dbReadNumeric("RCNAND");
        codeCanton = statement.dbReadString("RCCCAN");
        canton = statement.dbReadNumeric("RCTCAN");
        dateActivite = statement.dbReadDateAMJ("RCDACT");
        avecCotisation = statement.dbReadBoolean("RCBCOT");
    }

    // code systeme

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Recerche code canton
        setCodeCanton(CodeSystem.getCode(getSession(), getCanton()));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("RCIREC", this._dbWriteNumeric(statement.getTransaction(), getIdRevCotiCanton(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("RCIREC",
                this._dbWriteNumeric(statement.getTransaction(), getIdRevCotiCanton(), "idRevCotiCanton"));
        statement.writeField("RCNAND", this._dbWriteNumeric(statement.getTransaction(), getAnneeDebut(), "anneeDebut"));
        statement.writeField("RCTCAN", this._dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("RCCCAN", this._dbWriteString(statement.getTransaction(), getCodeCanton(), "codeCanton"));
        statement.writeField("RCDACT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateActivite(), "dateActivite"));
        statement.writeField("RCBCOT", this._dbWriteBoolean(statement.getTransaction(), getAvecCotisation(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "avecCotisation"));
    }

    public java.lang.String getAnneeDebut() {
        return anneeDebut;
    }

    public Boolean getAvecCotisation() {
        return avecCotisation;
    }

    public java.lang.String getCanton() {
        return canton;
    }

    public java.lang.String getCodeCanton() {
        return codeCanton;
    }

    public java.lang.String getDateActivite() {
        return dateActivite;
    }

    /**
     * Getter
     */
    public java.lang.String getIdRevCotiCanton() {
        return idRevCotiCanton;
    }

    public void setAnneeDebut(java.lang.String newAnneeRevenuDebut) {
        anneeDebut = newAnneeRevenuDebut;
    }

    public void setAvecCotisation(Boolean avecCotisation) {
        this.avecCotisation = avecCotisation;
    }

    public void setCanton(java.lang.String newAnneeDecisionDebut) {
        canton = newAnneeDecisionDebut;
    }

    public void setCodeCanton(java.lang.String codeCanton) {
        this.codeCanton = codeCanton;
    }

    public void setDateActivite(java.lang.String dateActivite) {
        this.dateActivite = dateActivite;
    }

    /**
     * Setter
     */
    public void setIdRevCotiCanton(java.lang.String newIdIfd) {
        idRevCotiCanton = newIdIfd;
    }
}
