package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import java.io.Serializable;

/**
 * 
 */
public class CACompteCourant extends BEntity implements Serializable, APICompteCourant {
    private static final long serialVersionUID = -4750122542454362735L;
    public static final String FIELD_ACCEPTERVEN = "ACCEPTERVEN";
    public static final String FIELD_ALIAS = "ALIAS";
    public static final String FIELD_IDCOMPTECOURANT = "IDCOMPTECOURANT";
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_JOURNALDESDEBIT = "JOURNALDEBIT";
    public static final String FIELD_PRIORITE = "PRIORITE";
    public static final String FIELD_SOLDE = "SOLDE";
    public static final String TABLE_CACPTCP = "CACPTCP";

    private CARubrique _rubrique = null;
    private Boolean accepterVentilation = new Boolean(true);
    private String alias = new String();
    private String idCompteCourant = new String();

    private String idExterne = new String();

    private String idRubrique = new String();
    private Boolean journalDesDebit = new Boolean(true);
    private String priorite = new String();
    private String solde = "0.00";

    /**
     * Commentaire relatif au constructeur CACompteCourant
     */
    public CACompteCourant() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdCompteCourant(this._incCounter(transaction, idCompteCourant));

        Integer secteur = Integer.parseInt(getIdExterne().substring(0, 1));
        if ((secteur < 5) || (secteur > 8)) {
            setJournalDesDebit(new Boolean(false));
        }
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Vérifier s'il y a des opérations
        if (hasOperations()) {
            _addError(transaction, getSession().getLabel("7030"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CACompteCourant.TABLE_CACPTCP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteCourant = statement.dbReadNumeric(CACompteCourant.FIELD_IDCOMPTECOURANT);
        idRubrique = statement.dbReadNumeric(CACompteCourant.FIELD_IDRUBRIQUE);
        solde = statement.dbReadNumeric(CACompteCourant.FIELD_SOLDE, 2);
        accepterVentilation = statement.dbReadBoolean(CACompteCourant.FIELD_ACCEPTERVEN);
        journalDesDebit = statement.dbReadBoolean(CACompteCourant.FIELD_JOURNALDESDEBIT);
        priorite = statement.dbReadNumeric(CACompteCourant.FIELD_PRIORITE);
        idExterne = statement.dbReadString(CACompteCourant.FIELD_IDEXTERNE);
        alias = statement.dbReadString(CACompteCourant.FIELD_ALIAS);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdRubrique(), getSession().getLabel("7045"));
        _propertyMandatory(statement.getTransaction(), getIdCompteCourant(), getSession().getLabel("7033"));
        _propertyMandatory(statement.getTransaction(), getIdExterne(), getSession().getLabel("7034"));

        // Vérifier la rubrique
        if (getRubrique() == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7032"));
        } else if (!getRubrique().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                && !getRubrique().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
            _addError(statement.getTransaction(), getSession().getLabel("7031"));
        }

        if (getJournalDesDebit()) {
            Integer secteur = Integer.parseInt(getIdExterne().substring(0, 1));
            if ((secteur < 5) || (secteur > 8)) {
                _addError(statement.getTransaction(), getSession().getLabel("JOURNALDEBIT_ERREUR_SECTEUR"));
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2002 10:33:12)
     * 
     * @param alternateKey
     *            int
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {

        // Clé alternée numéro 1 : idExterne
        switch (alternateKey) {
            case AK_IDEXTERNE:
                statement.writeKey(CACompteCourant.FIELD_IDEXTERNE,
                        this._dbWriteString(statement.getTransaction(), getIdExterne(), ""));
                break;
            case AK_IDRUBRIQUE:
                statement.writeKey(CACompteCourant.FIELD_IDRUBRIQUE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CACompteCourant.FIELD_IDCOMPTECOURANT,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CACompteCourant.FIELD_IDCOMPTECOURANT,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), "idCompteCourant"));
        statement.writeField(CACompteCourant.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CACompteCourant.FIELD_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));
        statement.writeField(CACompteCourant.FIELD_ACCEPTERVEN, this._dbWriteBoolean(statement.getTransaction(),
                getAccepterVentilation(), BConstants.DB_TYPE_BOOLEAN_CHAR, "accepterVen"));
        statement.writeField(CACompteCourant.FIELD_PRIORITE,
                this._dbWriteNumeric(statement.getTransaction(), getPriorite(), "priorite"));
        statement.writeField(CACompteCourant.FIELD_IDEXTERNE,
                this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField(CACompteCourant.FIELD_ALIAS,
                this._dbWriteString(statement.getTransaction(), getAlias(), "alias"));
        statement.writeField(CACompteCourant.FIELD_JOURNALDESDEBIT, this._dbWriteBoolean(statement.getTransaction(),
                getJournalDesDebit(), BConstants.DB_TYPE_BOOLEAN_CHAR, "journalDesDebit"));
    }

    /**
     * Insérer une opération dans le compte Date de création : (18.01.2002 13:15:52)
     * 
     * @param oper
     *            l'opération à insérer
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     */
    public FWMessage addOperation(CAOperation oper) {

        // Initialiser un nouveau message
        FWMessage msg = null;

        // S'il s'agit d'une écriture, on met à jour le solde du compte annexe
        if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            try {
                CAEcriture ecr = (CAEcriture) oper;
                FWCurrency _solde = new FWCurrency(getSolde());
                _solde.add(ecr.getMontant());
                setSolde(_solde.toString());
            } catch (Exception e) {
                msg = new FWMessage();
                msg.setMessageId("5131");
                msg.setComplement(e.getMessage());
                msg.setIdSource("CACompteCourant");
                msg.setTypeMessage(FWMessage.ERREUR);
            }
        }

        return msg;
    }

    @Override
    public Boolean getAccepterVentilation() {
        return accepterVentilation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @return String
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * Getter
     */
    @Override
    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @return String
     */
    @Override
    public String getIdExterne() {
        return idExterne;
    }

    @Override
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return the journalDesDebit
     */
    public Boolean getJournalDesDebit() {
        return journalDesDebit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @return String
     */
    @Override
    public String getPriorite() {
        return priorite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 10:36:57)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    @Override
    public APIRubrique getRubrique() {

        // Si rubrique pas déjà instanciée
        if (_rubrique == null) {
            // Instancier une nouelle rubrique
            _rubrique = new CARubrique();
            _rubrique.setSession(getSession());

            // Récupérer la rubrique en question
            _rubrique.setIdRubrique(getIdRubrique());
            try {
                _rubrique.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }

        // Retourner la rubrique
        return _rubrique;
    }

    @Override
    public String getSolde() {
        return JANumberFormatter.deQuote(solde);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2002 15:07:39)
     * 
     * @return String
     */
    @Override
    public String getSoldeFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(getSolde(), 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 15:55:50)
     * 
     * @return boolean
     */
    public boolean hasOperations() {

        // Vérifier s'il y a des opération
        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteCourant(getIdCompteCourant());
        try {
            if (mgr.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return true;
        }
    }

    /**
     * Supprimer une opération du compte Date de création : (18.01.2002 13:15:52)
     * 
     * @param oper
     *            l'opération à supprimer
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     */
    public FWMessage removeOperation(CAOperation oper) {

        // Initialiser un nouveau message
        FWMessage msg = null;

        // S'il s'agit d'une écriture, on met à jour le solde du compte
        if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            try {
                CAEcriture ecr = (CAEcriture) oper;
                FWCurrency _solde = new FWCurrency(getSolde());
                _solde.sub(ecr.getMontant());
                setSolde(_solde.toString());
            } catch (Exception e) {
                msg = new FWMessage();
                msg.setMessageId("5168");
                msg.setComplement(e.getMessage());
                msg.setIdSource("CACompteCourant");
                msg.setTypeMessage(FWMessage.ERREUR);
            }
        }

        return msg;
    }

    @Override
    public void setAccepterVentilation(Boolean newAccepterVentilation) {
        accepterVentilation = newAccepterVentilation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @param newAlias
     *            String
     */
    @Override
    public void setAlias(String newAlias) {
        alias = newAlias;
    }

    /**
     * Setter
     */
    @Override
    public void setIdCompteCourant(String newIdCompteCourant) {
        idCompteCourant = newIdCompteCourant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @param newIdExterne
     *            String
     */
    @Override
    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    @Override
    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
        _rubrique = null;
    }

    /**
     * @param journalDesDebit
     *            the journalDesDebit to set
     */
    public void setJournalDesDebit(Boolean journalDesDebit) {
        this.journalDesDebit = journalDesDebit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @param newPriorite
     *            String
     */
    @Override
    public void setPriorite(String newPriorite) {
        priorite = newPriorite;
    }

    @Override
    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * Synchroniser le compteur en fonction des opérations liées Date de création : (06.02.2002 13:37:51)
     */
    public void synchronizeFromOperations(BTransaction tr) {

        // Initialisation
        String _lastIdOperation = "0";

        // Vider les variables de cumul
        setSolde("0.00");

        // Instancer un manager pour récupérer les types d'opération
        CAEcritureManager mgr = new CAEcritureManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteCourant(getIdCompteCourant());

        // Récupérer les opérations
        while (true) {

            // Récupérer une série d'opérations
            try {
                mgr.clear();
                mgr.find(tr);
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return;
            }

            // Sortir s'il n'y a aucune opération trouvée
            if (mgr.size() == 0) {
                break;
            }

            // Récupérer les opérations
            for (int i = 0; i < mgr.size(); i++) {

                // Récupérer une opération et la convertir dans le type
                // d'opération
                CAOperation _operX = (CAOperation) mgr.getEntity(i);
                CAOperation _oper = _operX.getOperationFromType(tr);

                // Si l'opération n'a pas été convertie
                if (_oper == null) {
                    _addError(null, getSession().getLabel("5013") + " " + _operX.getIdOperation());
                    return;
                } else {

                    // Instancier un objet en fonction du type d'opération
                    _oper.setSession(getSession());

                    // Conserver le dernier ID
                    _lastIdOperation = _oper.getIdOperation();

                    // Si provisoire ou comptabilisée
                    if (_oper.getEstActive().booleanValue()) {

                        // Mettre à jour
                        FWMessage msg = addOperation(_oper);
                        if (msg != null) {
                            _addError(null, msg.getMessageText());
                            return;
                        }
                    }
                }
            }

            // Charger les opérations suivantes sauf si vide
            if (JadeStringUtil.isIntegerEmpty(_lastIdOperation)) {
                break;
            }
            mgr.setAfterIdOperation(_lastIdOperation);

        }
    }
}
