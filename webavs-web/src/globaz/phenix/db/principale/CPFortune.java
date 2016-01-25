package globaz.phenix.db.principale;

public class CPFortune extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // TypeTerrain
    public final static String CS_AGRICOLE = "603001";
    public final static String CS_NON_AGRICOLE = "603002";
    private java.lang.String autreFortune = "";
    private java.lang.String canton1 = "";
    private java.lang.String canton2 = "";
    private java.lang.String canton3 = "";
    private java.lang.String canton4 = "";
    private java.lang.String dette = "";
    private java.lang.String idDecision = "";
    private java.lang.String montantImmobilier1 = "";
    private java.lang.String montantImmobilier2 = "";
    private java.lang.String montantImmobilier3 = "";
    private java.lang.String montantImmobilier4 = "";
    private java.lang.String typeTerrain1 = "";
    private java.lang.String typeTerrain2 = "";
    private java.lang.String typeTerrain3 = "";
    private java.lang.String typeTerrain4 = "";

    // code systeme

    /**
     * Commentaire relatif au constructeur CPFortune
     */
    public CPFortune() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPFORTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        montantImmobilier1 = statement.dbReadNumeric("IQMFO1", 2);
        canton1 = statement.dbReadNumeric("IQTCA1");
        typeTerrain1 = statement.dbReadNumeric("IQTTT1");
        montantImmobilier2 = statement.dbReadNumeric("IQMFO2", 2);
        canton2 = statement.dbReadNumeric("IQTCA2");
        typeTerrain2 = statement.dbReadNumeric("IQTTT2");
        montantImmobilier3 = statement.dbReadNumeric("IQMFO3", 2);
        canton3 = statement.dbReadNumeric("IQTCA3");
        typeTerrain3 = statement.dbReadNumeric("IQTTT3");
        montantImmobilier4 = statement.dbReadNumeric("IQMFO4", 2);
        canton4 = statement.dbReadNumeric("IQTCA4");
        typeTerrain4 = statement.dbReadNumeric("IQTTT4");
        dette = statement.dbReadNumeric("IQDETT", 2);
        autreFortune = statement.dbReadNumeric("IQMAFO", 2);
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
        statement.writeKey("IAIDEC", _dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IAIDEC", _dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("IQMFO1",
                _dbWriteNumeric(statement.getTransaction(), getMontantImmobilier1(), "montantImmobilier1"));
        statement.writeField("IQTCA1", _dbWriteNumeric(statement.getTransaction(), getCanton1(), "canton1"));
        statement.writeField("IQTTT1", _dbWriteNumeric(statement.getTransaction(), getTypeTerrain1(), "typeTerrain1"));
        statement.writeField("IQMFO2",
                _dbWriteNumeric(statement.getTransaction(), getMontantImmobilier2(), "montantImmobilier2"));
        statement.writeField("IQTCA2", _dbWriteNumeric(statement.getTransaction(), getCanton2(), "canton2"));
        statement.writeField("IQTTT2", _dbWriteNumeric(statement.getTransaction(), getTypeTerrain2(), "typeTerrain2"));
        statement.writeField("IQMFO3",
                _dbWriteNumeric(statement.getTransaction(), getMontantImmobilier3(), "montantImmobilier3"));
        statement.writeField("IQTCA3", _dbWriteNumeric(statement.getTransaction(), getCanton3(), "canton3"));
        statement.writeField("IQTTT3", _dbWriteNumeric(statement.getTransaction(), getTypeTerrain3(), "typeTerrain3"));
        statement.writeField("IQMFO4",
                _dbWriteNumeric(statement.getTransaction(), getMontantImmobilier4(), "montantImmobilier4"));
        statement.writeField("IQTCA4", _dbWriteNumeric(statement.getTransaction(), getCanton4(), "canton4"));
        statement.writeField("IQTTT4", _dbWriteNumeric(statement.getTransaction(), getTypeTerrain4(), "typeTerrain4"));
        statement.writeField("IQDETT", _dbWriteNumeric(statement.getTransaction(), getDette(), "dette"));
        statement.writeField("IQMAFO", _dbWriteNumeric(statement.getTransaction(), getAutreFortune(), "autreFortune"));
    }

    public java.lang.String getAutreFortune() {
        return autreFortune;
    }

    public java.lang.String getCanton1() {
        return canton1;
    }

    public java.lang.String getCanton2() {
        return canton2;
    }

    public java.lang.String getCanton3() {
        return canton3;
    }

    public java.lang.String getCanton4() {
        return canton4;
    }

    public java.lang.String getDette() {
        return dette;
    }

    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public java.lang.String getMontantImmobilier1() {
        return montantImmobilier1;
    }

    public java.lang.String getMontantImmobilier2() {
        return montantImmobilier2;
    }

    public java.lang.String getMontantImmobilier3() {
        return montantImmobilier3;
    }

    public java.lang.String getMontantImmobilier4() {
        return montantImmobilier4;
    }

    public java.lang.String getTypeTerrain1() {
        return typeTerrain1;
    }

    public java.lang.String getTypeTerrain2() {
        return typeTerrain2;
    }

    public java.lang.String getTypeTerrain3() {
        return typeTerrain3;
    }

    public java.lang.String getTypeTerrain4() {
        return typeTerrain4;
    }

    public void setAutreFortune(java.lang.String newAutreFortune) {
        autreFortune = newAutreFortune;
    }

    public void setCanton1(java.lang.String newCanton1) {
        canton1 = newCanton1;
    }

    public void setCanton2(java.lang.String newCanton2) {
        canton2 = newCanton2;
    }

    public void setCanton3(java.lang.String newCanton3) {
        canton3 = newCanton3;
    }

    public void setCanton4(java.lang.String newCanton4) {
        canton4 = newCanton4;
    }

    public void setDette(java.lang.String newDette) {
        dette = newDette;
    }

    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setMontantImmobilier1(java.lang.String newMontantImmobilier1) {
        montantImmobilier1 = newMontantImmobilier1;
    }

    public void setMontantImmobilier2(java.lang.String newMontantImmobilier2) {
        montantImmobilier2 = newMontantImmobilier2;
    }

    public void setMontantImmobilier3(java.lang.String newMontantImmobilier3) {
        montantImmobilier3 = newMontantImmobilier3;
    }

    public void setMontantImmobilier4(java.lang.String newMontantImmobilier4) {
        montantImmobilier4 = newMontantImmobilier4;
    }

    public void setTypeTerrain1(java.lang.String newTypeTerrain1) {
        typeTerrain1 = newTypeTerrain1;
    }

    public void setTypeTerrain2(java.lang.String newTypeTerrain2) {
        typeTerrain2 = newTypeTerrain2;
    }

    public void setTypeTerrain3(java.lang.String newTypeTerrain3) {
        typeTerrain3 = newTypeTerrain3;
    }

    public void setTypeTerrain4(java.lang.String newTypeTerrain4) {
        typeTerrain4 = newTypeTerrain4;
    }
}
