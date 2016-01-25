package globaz.phenix.process.acompte;

/*
 * ATTENTION: cette classe ne lit pas depuis la DB, c'est une classe temporaire utilisée pour une aggrégation de table
 * composée de jointures (JOIN). A utiliser uniquement depuis CPDecisionManager.setUseManagerForReprise(true).
 */

public class CPAcompteCreationAnnuelle extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String debutAffiliation = "";
    private java.lang.String finAffiliation = "";
    private java.lang.String idAffiliation = "";
    private java.lang.String idDecision = "";
    private java.lang.String idTiers = "";
    private java.lang.String noAffilie = "";

    // code systeme

    /**
     * Commentaire relatif au constructeur FARemarque
     */
    public CPAcompteCreationAnnuelle() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null; // unused
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        noAffilie = statement.dbReadString("MALNAF");
        debutAffiliation = statement.dbReadDateAMJ("MADDEB");
        finAffiliation = statement.dbReadDateAMJ("MADFIN");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
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
     * Returns the debutAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutAffiliation() {
        return debutAffiliation;
    }

    /**
     * Returns the finAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinAffiliation() {
        return finAffiliation;
    }

    /**
     * Returns the idAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the idDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 17:03:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Returns the noAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNoAffilie() {
        return noAffilie;
    }

    /**
     * Sets the debutAffiliation.
     * 
     * @param debutAffiliation
     *            The debutAffiliation to set
     */
    public void setDebutAffiliation(java.lang.String debutAffiliation) {
        this.debutAffiliation = debutAffiliation;
    }

    /**
     * Sets the finAffiliation.
     * 
     * @param finAffiliation
     *            The finAffiliation to set
     */
    public void setFinAffiliation(java.lang.String finAffiliation) {
        this.finAffiliation = finAffiliation;
    }

    /**
     * Sets the idAffiliation.
     * 
     * @param idAffiliation
     *            The idAffiliation to set
     */
    public void setIdAffiliation(java.lang.String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    /**
     * Sets the idDecision.
     * 
     * @param idDecision
     *            The idDecision to set
     */
    public void setIdDecision(java.lang.String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * /** Insert the method's description here. Creation date: (20.06.2003 17:03:30)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    /**
     * Sets the noAffilie.
     * 
     * @param noAffilie
     *            The noAffilie to set
     */
    public void setNoAffilie(java.lang.String noAffilie) {
        this.noAffilie = noAffilie;
    }

}
