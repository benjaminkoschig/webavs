package globaz.helios.db.comptes;

import globaz.jade.client.util.JadeStringUtil;

public class CGLibelleStandardViewBean extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idLibelleStandard = new String();
    private java.lang.String idMandat = new String();
    private java.lang.String libelleDE = new String();
    private java.lang.String libelleFR = new String();
    private java.lang.String libelleIT = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGOldLibelleStandard
     */
    public CGLibelleStandardViewBean() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGLISTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLibelleStandard = statement.dbReadString("IDLIBELLESTANDARD");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFR = statement.dbReadString("LIBELLEFR");
        libelleDE = statement.dbReadString("LIBELLEDE");
        libelleIT = statement.dbReadString("LIBELLEIT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdMandat())) {
            _addError(statement.getTransaction(), "Le mandat doit être renseigné.");
        }

        if (JadeStringUtil.isBlank(getIdLibelleStandard())) {
            _addError(statement.getTransaction(), "L'acronyme doit être renseigné.");
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDLIBELLESTANDARD", _dbWriteString(statement.getTransaction(), getIdLibelleStandard(), ""));
        statement.writeKey("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDLIBELLESTANDARD",
                _dbWriteString(statement.getTransaction(), getIdLibelleStandard(), "idLibelleStandard"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFR(), "libelleFR"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDE(), "libelleDE"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIT(), "libelleIT"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdLibelleStandard() {
        return idLibelleStandard;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the libelleDE.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDE() {
        return libelleDE;
    }

    /**
     * Returns the libelleFR.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFR() {
        return libelleFR;
    }

    /**
     * Returns the libelleIT.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleIT() {
        return libelleIT;
    }

    /**
     * Setter
     */
    public void setIdLibelleStandard(java.lang.String newIdLibelleStandard) {
        idLibelleStandard = newIdLibelleStandard;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Sets the libelleDE.
     * 
     * @param libelleDE
     *            The libelleDE to set
     */
    public void setLibelleDE(java.lang.String libelleDE) {
        this.libelleDE = libelleDE;
    }

    /**
     * Sets the libelleFR.
     * 
     * @param libelleFR
     *            The libelleFR to set
     */
    public void setLibelleFR(java.lang.String libelleFR) {
        this.libelleFR = libelleFR;
    }

    /**
     * Sets the libelleIT.
     * 
     * @param libelleIT
     *            The libelleIT to set
     */
    public void setLibelleIT(java.lang.String libelleIT) {
        this.libelleIT = libelleIT;
    }

}
