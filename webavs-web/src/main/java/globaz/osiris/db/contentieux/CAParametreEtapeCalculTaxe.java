package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 10:58:51)
 * 
 * @author: Administrator
 */
public class CAParametreEtapeCalculTaxe extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CACalculTaxe _calculTaxe = null;
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idParametreEtape = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAParametreEtapeCalculTaxe
     */
    public CAParametreEtapeCalculTaxe() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CACTPEP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idParametreEtape = statement.dbReadNumeric("IDPARAMETREETAPE");
        idCalculTaxe = statement.dbReadNumeric("IDCALCULTAXE");
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
        statement.writeKey("IDPARAMETREETAPE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), ""));
        statement.writeKey("IDCALCULTAXE", this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPARAMETREETAPE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), "idParametreEtape"));
        statement.writeField("IDCALCULTAXE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.07.2002 10:27:35)
     * 
     * @return globaz.osiris.db.contentieux.CACalculTaxe
     */
    public CACalculTaxe getCalculTaxe() {

        // Récupérer le calcul taxe si vide
        if (_calculTaxe == null) {
            _calculTaxe = new CACalculTaxe();
            _calculTaxe.setSession(getSession());
            _calculTaxe.setIdCalculTaxe(getIdCalculTaxe());
            try {
                _calculTaxe.retrieve();
                if (_calculTaxe.isNew() || _calculTaxe.hasErrors()) {
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
    public java.lang.String getIdParametreEtape() {
        return idParametreEtape;
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
        _calculTaxe = null;
    }

    /**
     * Setter
     */
    public void setIdParametreEtape(java.lang.String newIdParametreEtape) {
        idParametreEtape = newIdParametreEtape;
    }
}
