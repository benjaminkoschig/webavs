package globaz.phenix.db.divers;

import globaz.globall.util.JANumberFormatter;

public class CPTableRentier extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String anneeRentier = "";
    private java.lang.String cotisationAnnuelle = "";
    private java.lang.String idTableRentier = "";
    private java.lang.String revenuRentier = "";

    // code systeme

    /**
     * Commentaire relatif au constructeur CPTableRentier
     */
    public CPTableRentier() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdTableRentier(_incCounter(transaction, idTableRentier));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPTRENP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTableRentier = statement.dbReadNumeric("JCITRE");
        anneeRentier = statement.dbReadNumeric("JCANNE");
        revenuRentier = statement.dbReadNumeric("JCMREV", 2);
        cotisationAnnuelle = statement.dbReadNumeric("JCMCOT", 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("JCITRE", _dbWriteNumeric(statement.getTransaction(), getIdTableRentier(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("JCITRE",
                _dbWriteNumeric(statement.getTransaction(), getIdTableRentier(), "idTableRentier"));
        statement.writeField("JCANNE", _dbWriteNumeric(statement.getTransaction(), getAnneeRentier(), "anneeRentier"));
        statement
                .writeField("JCMREV", _dbWriteNumeric(statement.getTransaction(), getRevenuRentier(), "revenuRentier"));
        statement.writeField("JCMCOT",
                _dbWriteNumeric(statement.getTransaction(), getCotisationAnnuelle(), "cotisationAnnuelle"));
    }

    public java.lang.String getAnneeRentier() {
        return anneeRentier;
    }

    public java.lang.String getCotisationAnnuelle() {
        return cotisationAnnuelle;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTableRentier() {
        return idTableRentier;
    }

    public java.lang.String getRevenuRentier() {
        return JANumberFormatter.fmt(revenuRentier, true, false, false, 0);
    }

    public void setAnneeRentier(java.lang.String newAnneeRentier) {
        anneeRentier = newAnneeRentier;
    }

    public void setCotisationAnnuelle(java.lang.String newCotisationAnnuelle) {
        cotisationAnnuelle = newCotisationAnnuelle;
    }

    /**
     * Setter
     */
    public void setIdTableRentier(java.lang.String newIdTableRentier) {
        idTableRentier = newIdTableRentier;
    }

    public void setRevenuRentier(java.lang.String newRevenuRentier) {
        revenuRentier = JANumberFormatter.deQuote(newRevenuRentier);
    }
}
