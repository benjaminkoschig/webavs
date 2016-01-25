package globaz.pavo.db.splitting;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;

/**
 * Informations du domicile concernant le splitting. Date de création : (15.10.2002 10:47:23)
 * 
 * @author: dgi
 */
public class CIDomicileSplitting extends BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String idDomicileSplitting = new String();
    private java.lang.String idDossierSplitting = new String();
    private java.lang.String idTiersPartenaire = new String();
    private java.lang.String libelle = new String();

    /**
     * Constructeur.
     */
    public CIDomicileSplitting() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // test état du dossier
        if (isOperationAllowed(transaction)) {
            // incrémente de +1 le numéro
            setIdDomicileSplitting(_incCounter(transaction, "0"));
        } else {
            // ajout impossible
            _addError(transaction, getSession().getLabel("MSG_DOMICILE_ADD_ETAT"));
        }
    }

    /**
     * Teste si la supression est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (!isOperationAllowed(transaction)) {
            // suppression impossible
            _addError(transaction, getSession().getLabel("MSG_DOMICILE_DEL_ETAT"));
        }
    }

    /**
     * Teste si la supression est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (!isOperationAllowed(transaction)) {
            // modification impossible
            _addError(transaction, getSession().getLabel("MSG_DOMICILE_MOD_ETAT"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISPDOP";
    }

    /**
     * Charge les attributs
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDomicileSplitting = statement.dbReadNumeric("KGID");
        idDossierSplitting = statement.dbReadNumeric("KDID");
        idTiersPartenaire = statement.dbReadNumeric("KGITIE");
        libelle = statement.dbReadString("KGLIB");
        dateDebut = statement.dbReadDateAMJ("KGDDEB");
        dateFin = statement.dbReadDateAMJ("KGDFIN");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Dates
        if (JAUtil.isDateEmpty(dateDebut)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOMICILE_VAL_DEBUT"));
        }
        if (JAUtil.isDateEmpty(dateFin)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOMICILE_VAL_FIN"));
        }
        if (!hasErrors()) {
            try {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateFin)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_DOMICILE_VAL_DATE"));
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
        }
        // libellé
        if (JAUtil.isStringEmpty(libelle)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOMICILE_VAL_LIBELLE"));
        }
    }

    /**
     * Enregistre la clé primaire
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("KGID", _dbWriteNumeric(statement.getTransaction(), getIdDomicileSplitting(), ""));
    }

    /**
     * Enregistre les attributs
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("KGID",
                _dbWriteNumeric(statement.getTransaction(), getIdDomicileSplitting(), "idDomicileSplitting"));
        statement.writeField("KDID",
                _dbWriteNumeric(statement.getTransaction(), getIdDossierSplitting(), "idDossierSplitting"));
        statement.writeField("KGITIE",
                _dbWriteNumeric(statement.getTransaction(), getIdTiersPartenaire(), "idTiersPartenaire"));
        statement.writeField("KGLIB", _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("KGDDEB", _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("KGDFIN", _dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getIdDomicileSplitting() {
        return idDomicileSplitting;
    }

    public java.lang.String getIdDossierSplitting() {
        return idDossierSplitting;
    }

    public java.lang.String getIdTiersPartenaire() {
        return idTiersPartenaire;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Teste si l'état actuel du dossier de splitting autorise une opération sur les domiciles à l'étranger. Date de
     * création : (28.10.2002 16:39:58)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @return true si une modification est autorisée.
     */
    public boolean isOperationAllowed(BTransaction transaction) throws Exception {
        // requête du dossier
        CIDossierSplitting _dossier = new CIDossierSplitting();
        _dossier.setIdDossierSplitting(getIdDossierSplitting());
        _dossier.setSession(getSession());
        _dossier.retrieve(transaction);
        return _dossier.isModificationAllowedFromDossier();
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    /**
     * Setter
     */
    public void setIdDomicileSplitting(java.lang.String newIdDomicileSplitting) {
        idDomicileSplitting = newIdDomicileSplitting;
    }

    public void setIdDossierSplitting(java.lang.String newIdDossierSplitting) {
        idDossierSplitting = newIdDossierSplitting;
    }

    public void setIdTiersPartenaire(java.lang.String newIdTiersPartenaire) {
        idTiersPartenaire = newIdTiersPartenaire;
    }

    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
    }
}
