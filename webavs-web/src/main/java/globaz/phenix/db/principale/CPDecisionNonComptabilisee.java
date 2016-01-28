package globaz.phenix.db.principale;

public class CPDecisionNonComptabilisee extends CPDecisionAffiliationTiers {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String datefacturation = "";
    private java.lang.String libellePassage = "";

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPDecisionNonComptabilisee() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "FAPASSP";
        return from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IDPASSAGE="
                + _getCollection() + "CPDECIP.EBIPAS)";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        datefacturation = statement.dbReadDateAMJ("DATEFACTURATION");
        libellePassage = statement.dbReadNumeric("LIBELLEPASSAGE");
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    public java.lang.String getDatefacturation() {
        return datefacturation;
    }

    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    public void setDatefacturation(java.lang.String datefacturation) {
        this.datefacturation = datefacturation;
    }

    public void setLibellePassage(java.lang.String libelleJournal) {
        libellePassage = libelleJournal;
    }

}
