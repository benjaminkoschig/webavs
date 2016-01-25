package globaz.pavo.db.splitting;

import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.translation.CodeSystem;
import org.nfunk.jep.JEP;

/**
 * Classe représentant le revenu à prendre en compte pour le splitting. Date de création : (14.10.2002 15:43:18)
 * 
 * @author: dgi
 */
public class CIRevenuSplitting extends BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String annee = new String();
    private java.lang.String cotisation = new String();
    private java.lang.String idMandatSplitting = new String();
    private java.lang.String idRevenuSplitting = new String();
    private java.lang.String revenu = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CIRevenuSplitting
     */
    public CIRevenuSplitting() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // test état du mandat
        if (isOperationAllowed(transaction)) {
            // incrémente de +1 le numéro
            setIdRevenuSplitting(_incCounter(transaction, "0"));
        } else {
            // ajout impossible
            _addError(transaction, getSession().getLabel("MSG_REVENU_ADD_ETAT"));
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
            _addError(transaction, getSession().getLabel("MSG_REVENU_DEL_ETAT"));
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
            _addError(transaction, getSession().getLabel("MSG_REVENU_MOD_ETAT"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISPREP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRevenuSplitting = statement.dbReadNumeric("KFID");
        idMandatSplitting = statement.dbReadNumeric("KEID");
        annee = statement.dbReadNumeric("KFANNE");
        revenu = statement.dbReadNumeric("KFMREV", 2);
        cotisation = statement.dbReadNumeric("KFMCOT", 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        try {
            if (JAUtil.isStringEmpty(getAnnee())) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_ANNEE"));
                return;
            }
            if (JadeStringUtil.isBlankOrZero(getRevenu())) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_SPL_VIDE"));
                return;
            }
            // test anneeDebut<annee<anneeFin
            int anneeRevenu;
            try {
                anneeRevenu = Integer.parseInt(getAnnee());
            } catch (Exception e) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_ANNEE"));
                return;
            }
            CIMandatSplitting _mandat = loadMandat(statement.getTransaction());
            int anneeDebut = Integer.parseInt(_mandat.getAnneeDebut());
            int anneeFin = Integer.parseInt(_mandat.getAnneeFin());
            if (anneeDebut > anneeRevenu || anneeFin < anneeRevenu) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_DATE") + " (" + anneeDebut
                        + "-" + anneeFin + ")");
            }
            // test si annee existe déjà pour ce mandat
            if (isNew()) {
                CIRevenuSplittingManager _manager = new CIRevenuSplittingManager();
                _manager.setForIdMandatSplitting(getIdMandatSplitting());
                _manager.setFromAnnee(getAnnee());
                _manager.setSession(getSession());
                _manager.find(statement.getTransaction());
                if (_manager.size() != 0) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_EXIST"));
                }
            }
            // cotisation ou revenu
            // parseur d'opération arithmétique
            if (JadeStringUtil.isBlankOrZero(revenu)) {
                // soit revenu, soit cotisation
                _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_XOR"));
            } else {
                if (!JadeStringUtil.isBlankOrZero(getCotisation())) {
                    // cotisations <= 1969
                    if (Integer.parseInt(getAnnee()) <= 1969) {
                        JEP parser = new JEP();
                        parser.parseExpression(JANumberFormatter.deQuote(getCotisation()));
                        setCotisation(JANumberFormatter.formatNoQuote(parser.getValue()));
                    } else {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_REVENU_VAL_COTI"));
                    }
                } else {
                    // pour les revenus, tester état du mandat
                    if (CIMandatSplitting.CS_PARTAGE_CI_CLOTURES.equals(_mandat.getIdGenreSplitting())
                            || CIMandatSplitting.CS_LICHTENSTEIN.equals(_mandat.getIdGenreSplitting())) {
                        JEP parser = new JEP();
                        parser.parseExpression(JANumberFormatter.deQuote(getRevenu()));
                        setRevenu(JANumberFormatter.formatNoQuote(parser.getValue()));
                    } else {
                        _addError(statement.getTransaction(), java.text.MessageFormat.format(
                                getSession().getLabel("MSG_REVENU_VAL_REVENU"),
                                new Object[] {
                                        CodeSystem.getLibelle(
                                                globaz.pavo.db.splitting.CIMandatSplitting.CS_LICHTENSTEIN,
                                                getSession()),
                                        CodeSystem.getLibelle(
                                                globaz.pavo.db.splitting.CIMandatSplitting.CS_PARTAGE_CI_CLOTURES,
                                                getSession()) }));
                    }
                }
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("KFID", _dbWriteNumeric(statement.getTransaction(), getIdRevenuSplitting(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("KFID",
                _dbWriteNumeric(statement.getTransaction(), getIdRevenuSplitting(), "idRevenuSplitting"));
        statement.writeField("KEID",
                _dbWriteNumeric(statement.getTransaction(), getIdMandatSplitting(), "idMandatSplitting"));
        statement.writeField("KFANNE", _dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("KFMREV", _dbWriteNumeric(statement.getTransaction(), getRevenu(), "revenu"));
        statement.writeField("KFMCOT", _dbWriteNumeric(statement.getTransaction(), getCotisation(), "cotisation"));
    }

    public java.lang.String getAnnee() {
        return annee;
    }

    public java.lang.String getCotisation() {
        return cotisation;
    }

    public java.lang.String getIdMandatSplitting() {
        return idMandatSplitting;
    }

    /**
     * Getter
     */
    public java.lang.String getIdRevenuSplitting() {
        return idRevenuSplitting;
    }

    public java.lang.String getRevenu() {
        return revenu;
    }

    /**
     * Teste si l'état actuel du mandat de splitting autorise une opération sur les revenus. Date de création :
     * (28.10.2002 16:39:58)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return true si une modification est autorisée.
     */
    public boolean isOperationAllowed(BTransaction transaction) throws Exception {
        return loadMandat(transaction).isModificationAllowedFromMandat();
    }

    /**
     * Charge le mandat lié au revenu. Date de création : (31.10.2002 07:45:01)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return le dossier du mandat de splitting.
     * @exception Exception
     *                si le mandat ne peut pas être chrgé.
     */
    private CIMandatSplitting loadMandat(BTransaction transaction) throws Exception {
        // requête du mandat
        CIMandatSplitting _mandat = new CIMandatSplitting();
        _mandat.setIdMandatSplitting(getIdMandatSplitting());
        _mandat.setSession(getSession());
        _mandat.retrieve(transaction);
        return _mandat;
    }

    public void setAnnee(java.lang.String newAnnee) {
        annee = newAnnee;
    }

    public void setCotisation(java.lang.String newCotisation) {
        cotisation = newCotisation;
    }

    public void setIdMandatSplitting(java.lang.String newIdMandatSplitting) {
        idMandatSplitting = newIdMandatSplitting;
    }

    /**
     * Setter
     */
    public void setIdRevenuSplitting(java.lang.String newIdRevenuSplitting) {
        idRevenuSplitting = newIdRevenuSplitting;
    }

    public void setRevenu(java.lang.String newRevenu) {
        revenu = newRevenu;
    }
}
