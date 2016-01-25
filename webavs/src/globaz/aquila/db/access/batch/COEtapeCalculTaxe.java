package globaz.aquila.db.access.batch;

import globaz.globall.db.BConstants;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 10:58:51)
 * 
 * @author: Administrator
 */
public class COEtapeCalculTaxe extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FNAME_ID_CALCUL_TAXE = "OIICTX";
    public static final String FNAME_ID_ETAPE = "ODIETA";
    public static final String FNAME_IMPUTER_TAXE = "OHBIMP";

    public static final String TABLE_NAME = "COCTPEP";

    private COCalculTaxe _calculTaxe = null;
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idEtape = new String();
    private Boolean imputerTaxe;

    // code systeme

    /**
     * Commentaire relatif au constructeur CAParametreEtapeCalculTaxe
     */
    public COEtapeCalculTaxe() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return COEtapeCalculTaxe.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEtape = statement.dbReadNumeric(COEtapeCalculTaxe.FNAME_ID_ETAPE);
        idCalculTaxe = statement.dbReadNumeric(COEtapeCalculTaxe.FNAME_ID_CALCUL_TAXE);
        imputerTaxe = statement.dbReadBoolean(COEtapeCalculTaxe.FNAME_IMPUTER_TAXE);
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
        statement.writeKey(COEtapeCalculTaxe.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), ""));
        statement.writeKey(COEtapeCalculTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(COEtapeCalculTaxe.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), "idParametreEtape"));
        statement.writeField(COEtapeCalculTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField(COEtapeCalculTaxe.FNAME_IMPUTER_TAXE,
                this._dbWriteBoolean(statement.getTransaction(), imputerTaxe, BConstants.DB_TYPE_BOOLEAN_CHAR));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.07.2002 10:27:35)
     * 
     * @return globaz.osiris.db.contentieux.CACalculTaxe
     */
    public COCalculTaxe getCalculTaxe() {

        // Récupérer le calcul taxe si vide
        if (_calculTaxe == null) {
            _calculTaxe = new COCalculTaxe();
            _calculTaxe.setSession(getSession());
            _calculTaxe.setIdCalculTaxe(getIdCalculTaxe());
            try {
                _calculTaxe.retrieve();
                if (_calculTaxe.isNew()) {
                    _addError(null, getSession().getLabel("7217"));
                    _calculTaxe = null;
                }
            } catch (Exception e) {
                _calculTaxe = null;
            }
        }

        return _calculTaxe;
    }

    public java.lang.String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    /**
     * Getter
     */
    public java.lang.String getIdEtape() {
        return idEtape;
    }

    public Boolean getImputerTaxe() {
        return imputerTaxe;
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
        _calculTaxe = null;
    }

    /**
     * Setter
     */
    public void setIdEtape(java.lang.String newIdParametreEtape) {
        idEtape = newIdParametreEtape;
    }

    public void setImputerTaxe(Boolean boolean1) {
        imputerTaxe = boolean1;
    }

}
