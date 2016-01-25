package globaz.phenix.db.divers;

import globaz.globall.util.JANumberFormatter;

public class CPTableFortune extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String anneeFortune = "";
    private java.lang.String canton = "";
    private java.lang.String idTableFortune = "";
    private java.lang.String tauxAgricole = "";
    private java.lang.String tauxNonAgricole = "";

    // code systeme

    /**
     * Commentaire relatif au constructeur CPTableFortune
     */
    public CPTableFortune() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdTableFortune(_incCounter(transaction, idTableFortune));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPTFORP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTableFortune = statement.dbReadNumeric("JDITFO");
        anneeFortune = statement.dbReadNumeric("JDANNE");
        canton = statement.dbReadNumeric("JDTCAN");
        tauxAgricole = statement.dbReadNumeric("JDTAGR", 5);
        tauxNonAgricole = statement.dbReadNumeric("JDTNAG", 5);
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
        statement.writeKey("JDITFO", _dbWriteNumeric(statement.getTransaction(), getIdTableFortune(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("JDITFO",
                _dbWriteNumeric(statement.getTransaction(), getIdTableFortune(), "idTableFortune"));
        statement.writeField("JDANNE", _dbWriteNumeric(statement.getTransaction(), getAnneeFortune(), "anneeFortune"));
        statement.writeField("JDTCAN", _dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("JDTAGR", _dbWriteNumeric(statement.getTransaction(), getTauxAgricole(), "tauxAgricole"));
        statement.writeField("JDTNAG",
                _dbWriteNumeric(statement.getTransaction(), getTauxNonAgricole(), "tauxNonAgricole"));
    }

    public java.lang.String getAnneeFortune() {
        return anneeFortune;
    }

    public java.lang.String getCanton() {
        return canton;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTableFortune() {
        return idTableFortune;
    }

    public java.lang.String getTauxAgricole() {
        return JANumberFormatter.fmt(tauxAgricole, true, false, false, 0);
    }

    public java.lang.String getTauxNonAgricole() {
        return JANumberFormatter.fmt(tauxNonAgricole, true, false, false, 0);
    }

    public void setAnneeFortune(java.lang.String newAnneeFortune) {
        anneeFortune = newAnneeFortune;
    }

    public void setCanton(java.lang.String newCanton) {
        canton = newCanton;
    }

    /**
     * Setter
     */
    public void setIdTableFortune(java.lang.String newIdTableFortune) {
        idTableFortune = newIdTableFortune;
    }

    public void setTauxAgricole(java.lang.String newTauxAgricole) {
        tauxAgricole = JANumberFormatter.deQuote(newTauxAgricole);
    }

    public void setTauxNonAgricole(java.lang.String newTauxNonAgricole) {
        tauxNonAgricole = JANumberFormatter.deQuote(newTauxNonAgricole);
    }
}
