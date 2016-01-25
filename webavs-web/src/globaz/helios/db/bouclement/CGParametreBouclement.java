package globaz.helios.db.bouclement;

public class CGParametreBouclement extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idBouclement = new String();
    private java.lang.String idCentreCharge = "";
    private java.lang.String idCompte = "";
    private java.lang.String idDomaine = new String();
    private java.lang.String idGenre = new String();
    private java.lang.String idMandat = new String();
    private java.lang.String idParametreBouclement = new String();
    private java.lang.String idSecteurAVS = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();

    private java.lang.String libelleIt = new String();
    private java.lang.String numeroOrdre = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGParametreBouclement
     */
    public CGParametreBouclement() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGBOPMP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idParametreBouclement = statement.dbReadNumeric("IDPARAMETREBOUCL");
        idBouclement = statement.dbReadNumeric("IDBOUCLEMENT");
        idSecteurAVS = statement.dbReadNumeric("IDSECTEURAVS");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        numeroOrdre = statement.dbReadNumeric("NUMEROORDRE");
        idDomaine = statement.dbReadNumeric("IDDOMAINE");
        idGenre = statement.dbReadNumeric("IDGENRE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
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
        statement.writeKey("IDPARAMETREBOUCL",
                _dbWriteNumeric(statement.getTransaction(), getIdParametreBouclement(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPARAMETREBOUCL",
                _dbWriteNumeric(statement.getTransaction(), getIdParametreBouclement(), "idParametreBouclement"));
        statement.writeField("IDBOUCLEMENT",
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), "idBouclement"));
        statement.writeField("IDSECTEURAVS",
                _dbWriteNumeric(statement.getTransaction(), getIdSecteurAVS(), "idSecteurAVS"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("NUMEROORDRE",
                _dbWriteNumeric(statement.getTransaction(), getNumeroOrdre(), "numeroOrdre"));
        statement.writeField("IDDOMAINE", _dbWriteNumeric(statement.getTransaction(), getIdDomaine(), "idDomaine"));
        statement.writeField("IDGENRE", _dbWriteNumeric(statement.getTransaction(), getIdGenre(), "idGenre"));
        statement.writeField("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("IDCENTRECHARGE",
                _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
    }

    public java.lang.String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:32:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:32:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompte() {
        return idCompte;
    }

    public java.lang.String getIdDomaine() {
        return idDomaine;
    }

    public java.lang.String getIdGenre() {
        return idGenre;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Getter
     */
    public java.lang.String getIdParametreBouclement() {
        return idParametreBouclement;
    }

    public java.lang.String getIdSecteurAVS() {
        return idSecteurAVS;
    }

    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public java.lang.String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setIdBouclement(java.lang.String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:32:48)
     * 
     * @param newIdCentreCharge
     *            java.lang.String
     */
    public void setIdCentreCharge(java.lang.String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:32:32)
     * 
     * @param newIcCompte
     *            java.lang.String
     */
    public void setIdCompte(java.lang.String newIdCompte) {
        idCompte = newIdCompte;
    }

    public void setIdDomaine(java.lang.String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    public void setIdGenre(java.lang.String newIdGenre) {
        idGenre = newIdGenre;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Setter
     */
    public void setIdParametreBouclement(java.lang.String newIdParametreBouclement) {
        idParametreBouclement = newIdParametreBouclement;
    }

    public void setIdSecteurAVS(java.lang.String newIdSecteurAVS) {
        idSecteurAVS = newIdSecteurAVS;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setNumeroOrdre(java.lang.String newNumeroOrdre) {
        numeroOrdre = newNumeroOrdre;
    }
}
