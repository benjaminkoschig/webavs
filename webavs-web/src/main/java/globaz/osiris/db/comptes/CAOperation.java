package globaz.osiris.db.comptes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntJournalCG;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;

/**
 * Date de création : (18.01.2002 08:44:22)
 */
public class CAOperation extends BEntity implements Serializable, APIOperation {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_ANNEECOTISATION = "ANNEECOTISATION";
    public static final String FIELD_ASSURANCE_CANTON = "MBTCAN";
    public static final String FIELD_CODEDEBITCREDIT = "CODEDEBITCREDIT";

    public static final String FIELD_CODEMASTER = "CODEMASTER";

    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_ETAT = "ETAT";
    public static final String FIELD_ID_ASSURANCE = "MBIASS";
    public static final String FIELD_IDCAISSEPROFESSIONNELLE = "FANCAI";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDCOMPTECOURANT = "IDCOMPTECOURANT";
    public static final String FIELD_IDCONTREPARTIE = "IDCONTREPARTIE";
    public static final String FIELD_IDECHEANCEPLAN = "IDECHEANCEPLAN";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDLOG = "IDLOG";
    public static final String FIELD_IDOPERATION = "IDOPERATION";
    public static final String FIELD_IDORDRE = "IDORDRE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDSECTION_COMPENSATION = "IDSECTIONCMP";
    public static final String FIELD_IDSECTIONAUX = "IDSECTIONAUX";
    public static final String FIELD_IDTYPEOPERATION = "IDTYPEOPERATION";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MASSE = "MASSE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_NOECRCOL = "NOECRCOL";
    public static final String FIELD_PIECE = "PIECE";
    public static final String FIELD_PROVENANCEPMT = "PROVENANCEPMT";
    public static final String FIELD_SECTION_COMPENSATION_DE_SUR = "SECTIONCMPDS";
    public static final String FIELD_TAUX = "TAUX";
    public static final String FIELD_TAXEESTREPORTE = "TAXEESTREPORTE";

    public final static String TABLE_CAOPERP = "CAOPERP";

    public static final String convertHashMapForJQuery(HashMap<String, String> theHashMap) {
        StringBuffer hashMapJQuery = new StringBuffer();
        hashMapJQuery.append("{");

        if (theHashMap != null) {
            Iterator<String> itKeys = theHashMap.keySet().iterator();

            while (itKeys.hasNext()) {
                String theKey = itKeys.next();
                hashMapJQuery.append("\"" + theKey + "\" : \"" + theHashMap.get(theKey).replace("\"", "\\\"") + "\"");

                if (itKeys.hasNext()) {
                    hashMapJQuery.append(",");
                }
            }
        }

        hashMapJQuery.append("}");

        return hashMapJQuery.toString();
    }

    protected String anneeCotisation = new String();
    protected String assuranceCanton = "";
    protected String codeDebitCredit = new String();
    private String codeMaster = new String();
    private CACompteAnnexe compteAnnexe = null;
    private CACompteCourant compteCourant = null;
    // code systeme
    private FWParametersSystemCode csEtat = null;
    private FWParametersSystemCodeManager csEtats = null;

    private FWParametersSystemCodeManager csProvenancePaiement = null;
    protected String date = new String();
    private String etat = new String();
    private CAGroupementOperationManager groupementOperations;
    protected String idAssurance = "";
    protected String idCaisseProfessionnelle = new String();
    protected String idCompte = new String();
    private String idCompteAnnexe = new String();
    private String idCompteCourant = new String();
    protected String idContrepartie = new String();
    protected String idEcheancePlan = new String();
    private String idExterneCompteCourantEcran = new String();
    private String idExterneRoleEcran = new String();
    private String idExterneSectionAuxEcran = new String();
    private String idExterneSectionEcran = new String();
    private String idJournal = new String();
    private String idLog = new String();

    private String idOperation = new String();
    protected String idOrdre = new String();

    private String idRoleEcran = new String();
    private String idSection = new String();

    protected String idSectionAux = new String();

    protected String idSectionCompensation = "";
    private String idTypeOperation = new String();
    private String idTypeSectionEcran = new String();
    private CAJournal journal = null;
    protected String libelle = new String();
    private boolean loadIdExterneCompteCourantEcran = false;
    private boolean loadIdExterneRoleEcran = false;
    private boolean loadIdExterneSectionEcran = false;
    private boolean loadIdRoleEcran = false;
    private boolean loadIdTypeSectionEcran = false;
    private FWLog log = null;
    private HashMap<String, String> mapValeurUtilisateur = new HashMap<String, String>();
    protected String masse = "0";
    private FWMemoryLog memLog = null;
    protected String montant = "0";
    private boolean newCompteAnnexe = false;
    private boolean newSection = false;
    protected String noEcritureCollective = new String();
    private String nomEcran = new String();
    protected String piece = new String();
    protected String provenancePmt = new String();
    private Boolean quittanceLogEcran = new Boolean(false);
    private Boolean rechercheCompteAnnexeEcran = new Boolean(false);
    private Boolean rechercheCompteCourantEcran = new Boolean(false);
    private Boolean rechercheSectionEcran = new Boolean(false);
    private Boolean saisieEcran = new Boolean(false);
    private boolean saveLog = true;
    private CASection section;
    protected String sectionCompensationDeSur = "";
    protected String taux = "0";

    protected Boolean taxeEstReporte = new Boolean(false);
    private CATypeOperation typeOperation = null;
    private FWParametersUserCode ucEtat;

    private boolean useOptimisation = false;

    protected Vector<String> valeurUtilisateur = null;
    // D0009
    private String modeTraitementBulletinNeutreParDefaut = new String();

    /**
     * Commentaire relatif au constructeur CAOperation
     */
    public CAOperation() {
        super();
        // Valeurs par défaut
        setCodeMaster(APIOperation.SINGLE);
        setEtat(APIOperation.ETAT_OUVERT);
    }

    /**
     * @param operation
     */
    public CAOperation(CAOperation operation) {
        super();

        _setSpy(operation.getSpy());
        setSession(operation.getSession());
        setId(operation.getId());

        setIdOperation(operation.getIdOperation());
        setIdCompteAnnexe(operation.getIdCompteAnnexe());
        setIdCompteCourant(operation.getIdCompteCourant());
        setIdTypeOperation(operation.getIdTypeOperation());
        setIdJournal(operation.getIdJournal());
        setIdSection(operation.getIdSection());
        setIdLog(operation.getIdLog());
        setDate(operation.getDate());
        setEtat(operation.getEtat());
        setCodeMaster(operation.getCodeMaster());
        setIdCompte(operation.getIdCompte());
        setIdContrepartie(operation.getIdContrepartie());
        setMontant(operation.getMontant());
        setCodeDebitCredit(operation.getCodeDebitCredit());
        setMasse(operation.getMasse());
        setTaux(operation.getTaux());
        setPiece(operation.getPiece());
        setLibelle(operation.getLibelle());
        setAnneeCotisation(operation.getAnneeCotisation());
        setNoEcritureCollective(operation.getNoEcritureCollective());
        setIdOrdre(operation.getIdOrdre());
        setIdEcheancePlan(operation.getIdEcheancePlan());
        setIdSectionAux(operation.getIdSectionAux());
        setIdCaisseProfessionnelle(operation.getIdCaisseProfessionnelle());
        setTaxeEstReporte(operation.getTaxeEstReporte());
        setProvenancePmt(operation.getProvenancePmt());
        setIdSectionCompensation(operation.getIdSectionCompensation());
        setSectionCompensationDeSur(operation.getSectionCompensationDeSur());
        setAssuranceCanton(operation.getAssuranceCanton());
        setIdAssurance(operation.getIdAssurance());
    }

    /**
     * Opérations effectuées après l'activations Date de création : (24.01.2002 14:33:54)
     */
    protected void _afterActiver(BTransaction transaction) {
    }

    /**
     * Effectue des traitements après un ajout dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après l'ajout de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        _synchroValUtili();
    }

    /**
     * Date de création : (26.03.2002 18:39:22)
     */
    @Override
    protected void _afterDelete(globaz.globall.db.BTransaction transaction) throws Exception {
        // Supprimer la section et le compte annexe si créé dans ce journal
        CACompteAnnexe _cpt = getCompteAnnexe();
        CASection _sec = getSection();
        CAJournal _jou = getJournal();

        // Si le journal existe
        if (_jou != null) {
            // Suppression de la section
            if (_sec != null) {
                // Si le journal correspond
                if (_sec.getIdJournal().equals(getIdJournal())) {
                    // S'il n'y a pas d'opérations en cours
                    if (!_sec.hasOperations()) {
                        _sec.delete(transaction);
                    }
                }
            }

            // Suppression du compte annexe
            if (_cpt != null) {
                // Si le journal correspond
                if (_cpt.getIdJournal().equals(getIdJournal())) {
                    // S'il n'y a pas d'opérations en cours
                    if (!_cpt.hasOperations() && !_cpt.hasSections()) {
                        _cpt.delete(transaction);
                    }
                }
            }
        }
    }

    /**
     * Date de création : (25.02.2002 09:41:25)
     */
    protected void _afterDesactiver(BTransaction transaction) {
    }

    /**
     * Date de création : (26.03.2002 18:39:22)
     */
    @Override
    protected void _afterRetrieveWithResultSet(globaz.globall.db.BStatement statement) throws Exception {
        // Laisser la supercalsse traiter l'événement
        super._afterRetrieveWithResultSet(statement);

        // Charger les zones pour l'écran si nécessaire
        loadIdExterneCompteCourantEcran = true;
        loadIdExterneRoleEcran = true;
        loadIdExterneSectionEcran = true;
        loadIdRoleEcran = true;
        loadIdTypeSectionEcran = true;
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        _synchroValUtili();
    }

    /**
     * Indique si l'implémentation des héritages est gérée automatiquement ( <code>true</code> par défaut)
     * <p>
     * Cette méthode doit être surchargée pour renvoyer <code>false</code> si les entités ont comme cible la même table
     * (dans ce cas, l'entité parente est certainement une classe abstraite...)
     *
     * @return <code>true</code> si l'implémentation des héritages est gérée automatiquement, <code>false</code> sinon
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Opération à exécuter avant activation <br />
     * Date de création : (24.01.2002 14:29:03)
     */
    protected void _beforeActiver(BTransaction transaction) {
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        if (isUseOptimisation()) {
            setIdOperation(this._incCounter(transaction, idOperation));
        } else {
            setIdOperation(this._incCounter(transaction, "0"));
        }
        // Etat du journal par défaut
        if ((getEtat() == null) || JadeStringUtil.isIntegerEmpty(getEtat())) {
            setEtat(APIOperation.ETAT_OUVERT);
        }
        // Code master par défaut
        if ((getCodeMaster() == null) || JadeStringUtil.isIntegerEmpty(getCodeMaster())) {
            setCodeMaster(APIOperation.SINGLE);
        }
        // Vider le log si saisie écran
        if (getSaisieEcran().booleanValue()) {
            setMemoryLog(null);
            _deleteLog(transaction);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Suppression autorisée uniquement si l'opération est ouverte
        if (!isUpdatable()) {
            _addError(transaction, getSession().getLabel("7100"));
        } else {
            // Suppression du log
            _deleteLog(transaction);
            // Supprimer les éventuelles liaisons
            deleteLiaisons(transaction);
            // Supprimer les groupements
            deleteGroupements(transaction);
        }
    }

    /**
     * Date de création : (25.02.2002 09:41:12)
     */
    protected void _beforeDesactiver(BTransaction transaction) {
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Si un log existe et que l'on est en saisie écran, on le supprime s'il
        // a été quittancé
        if (getSaisieEcran().booleanValue()) {
            if (getLog() != null) {
                if (getQuittanceLogEcran().booleanValue()) {
                    _deleteLog(transaction);
                }
            }
            if (getEtat().equals(APIOperation.ETAT_ERREUR)) {
                setEtat(APIOperation.ETAT_OUVERT);
            }
        }

        // S'il s'agit d'une écriture MASTER, on supprimer le groupement si
        // l'écriture est en état ouvert
        if (getCodeMaster().equals(APIOperation.MASTER)) {
            if (getEtat().equals(APIOperation.ETAT_OUVERT)) {
                if (findAllGroupementOperations(transaction) != null) {
                    for (int i = 0; i < findAllGroupementOperations(transaction).size(); i++) {
                        CAGroupementOperation grpOper = (CAGroupementOperation) findAllGroupementOperations(transaction)
                                .getEntity(i);
                        if (grpOper.getGroupement().getTypeGroupement().equals(CAGroupement.MASTER)) {
                            grpOper.delete(transaction);
                            if (transaction.hasErrors()) {
                                _addError(transaction, getSession().getLabel("7102"));
                            } else {
                                // Tenter de supprimer le groupement
                                grpOper.getGroupement().delete(transaction);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * L'extourne sur une opération n'est pas possible. On renvoit une exception.
     *
     * @param transaction
     * @param text
     * @return
     * @throws Exception
     */
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        throw new Exception(getSession().getLabel("NO_EXTOURNE") + " [idOperation :" + getIdOperation()
                + ", idTypeOperation : " + getIdTypeOperation() + "]");
    }

    /**
     * Création d'une nouvelle section <br />
     * Date de création : (18.04.2002 10:08:37)
     */
    private void _createSection() throws Exception {

        // Chargement des attributs
        CASection _sec = new CASection();
        _sec.setSession(getSession());
        _sec.setIdCompteAnnexe(getIdCompteAnnexe());
        _sec.setIdTypeSection(getIdTypeSectionEcran());
        _sec.setIdExterne(getIdExterneSectionEcran());
        _sec.setIdJournal(getIdJournal());
        if (getJournal() != null) {
            _sec.setDateSection(getJournal().getDateValeurCG());
        }

        // Sauvegarder la section et indiquer qu'il s'agit d'une nouvelle
        // section
        section = _sec;
        setNewSection(true);
    }

    /**
     * Suppression du log <br />
     * Date de création : (22.11.2002 16:24:48)
     *
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     */
    private void _deleteLog(BTransaction transaction) {
        if (getLog() != null) {
            try {
                getLog().delete(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7103"));
                } else {
                    setIdLog("0");
                }
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOperation = statement.dbReadNumeric(CAOperation.FIELD_IDOPERATION);
        idCompteAnnexe = statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTEANNEXE);
        idCompteCourant = statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTECOURANT);
        idTypeOperation = statement.dbReadString(CAOperation.FIELD_IDTYPEOPERATION);
        idJournal = statement.dbReadNumeric(CAOperation.FIELD_IDJOURNAL);
        idSection = statement.dbReadNumeric(CAOperation.FIELD_IDSECTION);
        idLog = statement.dbReadNumeric(CAOperation.FIELD_IDLOG);
        date = statement.dbReadDateAMJ(CAOperation.FIELD_DATE);
        etat = statement.dbReadNumeric(CAOperation.FIELD_ETAT);
        codeMaster = statement.dbReadNumeric(CAOperation.FIELD_CODEMASTER);
        idCompte = statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTE);
        idContrepartie = statement.dbReadNumeric(CAOperation.FIELD_IDCONTREPARTIE);
        montant = statement.dbReadNumeric(CAOperation.FIELD_MONTANT);
        codeDebitCredit = statement.dbReadString(CAOperation.FIELD_CODEDEBITCREDIT);
        masse = statement.dbReadNumeric(CAOperation.FIELD_MASSE);
        taux = statement.dbReadNumeric(CAOperation.FIELD_TAUX);
        piece = statement.dbReadString(CAOperation.FIELD_PIECE);
        libelle = statement.dbReadString(CAOperation.FIELD_LIBELLE);
        anneeCotisation = statement.dbReadNumeric(CAOperation.FIELD_ANNEECOTISATION);
        noEcritureCollective = statement.dbReadNumeric(CAOperation.FIELD_NOECRCOL);
        idOrdre = statement.dbReadNumeric(CAOperation.FIELD_IDORDRE);
        idEcheancePlan = statement.dbReadNumeric(CAOperation.FIELD_IDECHEANCEPLAN);
        idSectionAux = statement.dbReadNumeric(CAOperation.FIELD_IDSECTIONAUX);
        idCaisseProfessionnelle = statement.dbReadNumeric(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE);
        taxeEstReporte = statement.dbReadBoolean(CAOperation.FIELD_TAXEESTREPORTE);
        provenancePmt = statement.dbReadNumeric(CAOperation.FIELD_PROVENANCEPMT);

        idSectionCompensation = statement.dbReadNumeric(CAOperation.FIELD_IDSECTION_COMPENSATION);
        sectionCompensationDeSur = statement.dbReadString(CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR);

        setIdAssurance(statement.dbReadNumeric(CAOperation.FIELD_ID_ASSURANCE));
        setAssuranceCanton(statement.dbReadNumeric(CAOperation.FIELD_ASSURANCE_CANTON));
    }

    /**
     * Date de création : (24.04.2002 10:56:30)
     */
    private void _saveCompteAnnexe(BTransaction transaction) {
        // Initialiser
        CACompteAnnexe cpt;

        // Ouverture du compte annexe
        try {
            cpt = openCompteAnnexe(transaction);
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            return;
        }

        // Mise à jour des valeurs
        setIdCompteAnnexe(cpt.getIdCompteAnnexe());
        compteAnnexe = cpt;
    }

    /**
     * Date de création : (04.04.2002 11:12:39)
     */
    private void _saveLog(BTransaction transaction) {

        if (saveLog) {
            _deleteLog(transaction);
            if (memLog.hasMessages()) {
                // Demander la sauvegarde
                memLog.setSession(getSession());
                log = memLog.saveToFWLog(transaction);

                // En cas d'erreur, on signale que la sauvegarde du log a échoué
                if (hasErrors()) {
                    _addError(null, getSession().getLabel("5011"));
                    log = null;
                }

                // Si la sauvegarde a réussi
                if (log != null) {
                    setIdLog(log.getIdLog());
                }
            }
        }
    }

    /**
     * Date de création : (18.04.2002 10:37:45)
     */
    private void _saveSection(BTransaction transaction) {
        try {
            section.setIdCompteAnnexe(getIdCompteAnnexe());
            section.add(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("5176"));
            } else {
                setIdSection(section.getIdSection());
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            _addError(transaction, getSession().getLabel("5176"));
        }
    }

    /**
     * Chargement des valeurs par défaut par utilisateur
     */
    public void _synchroChgValUtili() {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new Vector(6);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAOperation", getNomEcran());
                // chargement des propriétés internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(idExterneRoleEcran) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        setIdExterneRoleEcran(valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        setIdExterneSectionEcran(valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        setIdExterneCompteCourantEcran(valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        setDate(valeurUtilisateur.elementAt(5));
                    }
                }
            }
        }
    }

    /**
     * Date de création : (20.03.2002 13:19:42)
     */
    private void _synchroCompteAnnexeFromEcran(globaz.globall.db.BTransaction transaction) {
        // Initialiser
        setNewCompteAnnexe(false);

        // Vérifier les attributs vides
        if (JadeStringUtil.isIntegerEmpty(getIdRoleEcran()) || JadeStringUtil.isBlank(getIdExterneRoleEcran())) {
            setIdCompteAnnexe("");
        } else if ((getCompteAnnexe() == null) || !getCompteAnnexe().getIdExterneRole().equals(getIdExterneRoleEcran())
                || !getCompteAnnexe().getIdRole().equals(getIdRoleEcran())) {
            // Instancier un nouveau compte annexe
            CACompteAnnexe _cpt = new CACompteAnnexe();
            _cpt.setSession(getSession());
            _cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            _cpt.setIdRole(getIdRoleEcran());
            _cpt.setIdExterneRole(getIdExterneRoleEcran());

            // Lecture
            try {
                _cpt.retrieve(transaction);

                // En cas d'erreur, on remet à zéro le compte annexe
                if (_cpt.isNew()) {
                    setIdCompteAnnexe("");

                    // Erreur si pas de quittance
                    if (!getQuittanceLogEcran().booleanValue()) {
                        _addError(transaction, getSession().getLabel("5110"));
                    } else {
                        setNewCompteAnnexe(true);
                    }
                } else {
                    setIdCompteAnnexe(_cpt.getIdCompteAnnexe());
                    compteAnnexe = _cpt;
                }
            } catch (Exception e) {
                setIdCompteAnnexe("");
                _addError(transaction, e.getMessage());
                return;
            }
        }
    }

    /**
     * Date de création : (20.03.2002 13:20:13)
     */
    private void _synchroCompteCourantFromEcran(globaz.globall.db.BTransaction transaction) {
        // Vérifier les attributs vides
        if (JadeStringUtil.isBlank(getIdExterneCompteCourantEcran())) {
            setIdCompteCourant("");
        } else if ((getCompteCourant() == null)
                || !getCompteCourant().getIdExterne().equals(getIdExterneCompteCourantEcran())) {
            // Instancier un nouveau compte courant
            CACompteCourant _cpt = new CACompteCourant();
            _cpt.setSession(getSession());
            _cpt.setAlternateKey(APICompteCourant.AK_IDEXTERNE);
            _cpt.setIdExterne(getIdExterneCompteCourantEcran());

            // Lecture
            try {
                _cpt.retrieve(transaction);

                // En cas d'erreur, on remet à zéro le compte courant
                if (_cpt.isNew()) {
                    setIdCompteCourant("");
                    _addError(transaction, getSession().getLabel("5128"));
                } else {
                    setIdCompteCourant(_cpt.getIdCompteCourant());
                    compteCourant = _cpt;
                }
            } catch (Exception e) {
                setIdCompteCourant("");
                _addError(transaction, e.getMessage());
                return;
            }
        }
    }

    /**
     * Date de création : (20.03.2002 13:20:01)
     */
    private void _synchroSectionFromEcran(globaz.globall.db.BTransaction transaction) {
        // Initialiser
        setNewSection(false);

        // Vérifier les attributs vides
        if (JadeStringUtil.isBlank(getIdExterneSectionEcran())
                || JadeStringUtil.isIntegerEmpty(getIdTypeSectionEcran())) {
            setIdSection("");
        } else if ((getSection() == null) || !getSection().getIdExterne().equals(getIdExterneSectionEcran())
                || !getSection().getIdTypeSection().equals(getIdTypeSectionEcran())
                || !getSection().getIdCompteAnnexe().equals(getIdCompteAnnexe()) || isNewCompteAnnexe()) {
            // Instancier un nouveau compte annexe
            CASection sec = new CASection();
            sec.setSession(getSession());
            sec.setAlternateKey(CASection.AK_IDEXTERNE);
            sec.setIdCompteAnnexe(getIdCompteAnnexe());
            sec.setIdTypeSection(getIdTypeSectionEcran());
            sec.setIdExterne(getIdExterneSectionEcran());

            // Lecture
            try {
                sec.retrieve(transaction);

                // Si la section n'existe pas
                if (sec.isNew()) {
                    setIdSection("");
                    // Si quittance donnée pour création, pas de message
                    // d'erreur
                    if (!getQuittanceLogEcran().booleanValue()) {
                        _addError(transaction, getSession().getLabel("5126"));
                        // Si quittance donnée, on prépare une nouvelle section
                    } else {
                        _createSection();
                        // La section existe
                    }
                } else {
                    setIdSection(sec.getIdSection());
                    section = sec;
                }

            } catch (Exception e) {
                setIdSection("");
                _addError(transaction, e.getMessage());
                return;
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    protected void _synchroValUtili() {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new Vector(5);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getIdExterneCompteCourantEcran());
            valeurUtilisateur.add(5, getDate());
            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAOperation", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdOperation(), getSession().getLabel("7104"));
        _propertyMandatory(statement.getTransaction(), getEtat(), getSession().getLabel("7105"));
        _propertyMandatory(statement.getTransaction(), getCodeMaster(), getSession().getLabel("7106"));
        _propertyMandatory(statement.getTransaction(), getIdJournal(), getSession().getLabel("7013"));
        _propertyMandatory(statement.getTransaction(), getIdTypeOperation(), getSession().getLabel("7107"));

        // Vérifier le code master
        String _code = getCodeMaster();
        if (!_code.equals(APIOperation.SLAVE) && !_code.equals(APIOperation.MASTER)
                && !_code.equals(APIOperation.SINGLE)) {
            _addError(statement.getTransaction(), getSession().getLabel("7108") + _code);
        }

        // Vérifier le code système état
        if (getCsEtats().getCodeSysteme(getEtat()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7109") + getEtat());
        }

        // Contrôle du journal
        if (getJournal() == null) {
            _addError(statement.getTransaction(), getSession().getLabel("5157") + " " + getIdJournal());
        } else if (isNew() && getJournal().getEtat().equals(CAJournal.COMPTABILISE)) {
            _addError(statement.getTransaction(), getSession().getLabel("5158") + " " + getIdJournal());
        }

        // Validation complète si saisie à l'écran ou si accepter erreur = false
        checkSaisieEcran(statement);

        // Sauvegarder le compte annexe si nécessaire
        if (isNewCompteAnnexe() && !hasErrors()) {
            _saveCompteAnnexe(statement.getTransaction());
        }

        // Sauvegarder la section si nécessaire
        if (isNewSection() && !hasErrors()) {
            _saveSection(statement.getTransaction());
        }

        // Sauvegarder le log mémoire s'il existe et s'il y a des messages
        if ((memLog != null) && !hasErrors()) {
            _saveLog(statement.getTransaction());
        }
    }

    /**
     * Validation logique de l'entité <br/>
     * Date de création : (21.01.2002 10:55:50)
     */
    protected void _valider(globaz.globall.db.BTransaction transaction) {
        // Récupérer le compte annexe, la section et le compte courant en
        // provenance de l'écran
        if (getSaisieEcran().booleanValue() && isUpdatable()) {
            _synchroCompteAnnexeFromEcran(transaction);
            _synchroSectionFromEcran(transaction);
            _synchroCompteCourantFromEcran(transaction);

            if ((getCompteAnnexe() != null) && getCompteAnnexe().isASurveiller().booleanValue()
                    && !getQuittanceLogEcran().booleanValue()) {
                getMemoryLog().logMessage(getSession().getLabel("SOUS_SURVEILLANCE"), FWMessage.ERREUR,
                        this.getClass().getName());
            }

            if ((getCompteAnnexe() != null) && getCompteAnnexe().isVerrouille()
                    && !getQuittanceLogEcran().booleanValue()) {
                getMemoryLog().logMessage(getSession().getLabel("VERROUILLE"), FWMessage.ERREUR,
                        this.getClass().getName());
            }

            if ((getSection() != null) && getSection().getAttenteLSVDD().booleanValue()
                    && !getQuittanceLogEcran().booleanValue()) {
                getMemoryLog().logMessage(getSession().getLabel("SECTION_ATTENTELSVDD"), FWMessage.ERREUR,
                        this.getClass().getName());
            }
        }

        // Si le compte annexe n'est pas nouveau
        if (!isNewCompteAnnexe()) {
            // Vérifier le compte annexe
            if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
                getMemoryLog().logMessage("5106", null, FWMessage.ERREUR, this.getClass().getName());
            } else {
                // Vérifier l'existence du compte annexe
                if (getCompteAnnexe() == null) {
                    getMemoryLog().logMessage("5110", getIdCompteAnnexe(), FWMessage.ERREUR, this.getClass().getName());
                }
            }
        }
        if ((getSection() != null) && APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection().getIdTypeSection())
                && APISection.STATUTBN_ANNULE.equals(getSection().getIdModeCompensation())) {
            getMemoryLog().logMessage("7405", null, FWMessage.ERREUR, this.getClass().getName());
        }
        // Vérifier la date
        if (JadeStringUtil.isBlank(getDate())) {
            getMemoryLog().logMessage("5107", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            try {
                globaz.globall.db.BSessionUtil.checkDateGregorian(
                        ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                        getDate());
            } catch (Exception e) {
                getMemoryLog().logMessage("5108", getDate(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // Si section saisie, vérifier que le compte annexe correspond
        if ((getSection() != null) && !newCompteAnnexe) {
            if (!getSection().getIdCompteAnnexe().equals(getIdCompteAnnexe())) {
                getMemoryLog().logMessage("5174", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // Controler l'état du log et demander une quittance si >= avertissement
        if (getLog() != null) {
            if (getLog().getErrorLevel().compareTo(FWMessage.AVERTISSEMENT) >= 0) {
                if (!getSaisieEcran().booleanValue() || !getQuittanceLogEcran().booleanValue()) {
                    getMemoryLog().logMessage("5162", null, FWMessage.ERREUR, this.getClass().getName());

                    // En mode automatique, on concerver les anciens messages
                    if (!getSaisieEcran().booleanValue()) {
                        saveLog = false;
                    }
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CAOperation.FIELD_IDOPERATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CAOperation.FIELD_IDOPERATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
        statement.writeField(CAOperation.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CAOperation.FIELD_IDCOMPTECOURANT,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), "idCompteCourant"));
        statement.writeField(CAOperation.FIELD_IDTYPEOPERATION,
                this._dbWriteString(statement.getTransaction(), getIdTypeOperation(), "idTypeOperation"));
        statement.writeField(CAOperation.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CAOperation.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), idSection, "idSection"));
        statement.writeField(CAOperation.FIELD_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField(CAOperation.FIELD_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(CAOperation.FIELD_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));
        statement.writeField(CAOperation.FIELD_CODEMASTER,
                this._dbWriteNumeric(statement.getTransaction(), getCodeMaster(), "codeMaster"));

        statement.writeField(CAOperation.FIELD_IDSECTION_COMPENSATION,
                this._dbWriteNumeric(statement.getTransaction(), idSectionCompensation, "idSectionCompensation"));
        statement.writeField(CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR,
                this._dbWriteString(statement.getTransaction(), sectionCompensationDeSur, "sectionCompensationDeSur"));

        statement.writeField(CAOperation.FIELD_ASSURANCE_CANTON,
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceCanton(), "assuranceCanton"));
        statement.writeField(CAOperation.FIELD_ID_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAssurance(), "idAssurance"));
    }

    /**
     * Cette méthode déclenche l'insertion de l'opération des objets suivants :
     * <p>
     * <ul>
     * le compte annexe
     * <ul>
     * la section
     * <ul>
     * le compte courant
     * <ul>
     * le compte annexe / compte courant
     * <ul>
     * le compte courant / section
     * <p>
     * Cette opération doit être lancée sous contrôle de validation (transaction) <br />
     * Date de création : (18.01.2002 11:10:22)
     */
    public final void activer(BTransaction transaction) {
        // Création d'un nouveau log en mémoire
        memLog = new FWMemoryLog();

        // Vérifier l'état
        if (!getEtat().equals(APIOperation.ETAT_OUVERT) && !getEtat().equals(APIOperation.ETAT_ERREUR)) {
            _addError(transaction, getSession().getLabel("5101"));
            return;
        }
        // Vérifier qu'une transaction ouverte existe
        if (!transaction.isOpened()) {
            _addError(transaction, getSession().getLabel("5102"));
            return;
        }
        // Vérifier les propriétés
        _valider(transaction);
        if (transaction.hasErrors()) {
            return;
        }

        // S'il y a des erreurs, on sort
        if (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR);
            return;
        }

        // Récupérer le compte annexe
        CACompteAnnexe compteAnnexe = initCompteAnnexe();
        if (compteAnnexe == null) {
            _addError(transaction, getSession().getLabel("5104"));
            return;
        }

        // Exécuter les opération avant activation
        _beforeActiver(transaction);
        if (transaction.hasErrors()) {
            return;
        }

        // On ne met pas à jour les écritures MASTER
        if (!getCodeMaster().equals(APIOperation.MASTER)) {
            // S'il y a des erreurs, on sort
            if (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
                setEtat(APIOperation.ETAT_ERREUR);
                return;
            }

            // Récupérer le compte courrant (null autorisé)
            initCompteCourant();

            // Insérer l'opération dans le compte annexe
            memLog.logMessage(compteAnnexe.addOperation(this));

            // Récupérer la section (null autorisé)
            CASection sec = initSection();
            // Insérer l'opération dans la section
            if (sec != null) {
                memLog.logMessage(sec.addOperation(this));
            }

            // Effectuer les autres mises à jour s'il n'y a pas d'erreur
            if (!transaction.hasErrors() && (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
                // Commencer la mise à jour
                try {
                    // Mise à jour du compte annexe
                    updateCompteAnnexe(transaction, compteAnnexe);
                    // Mise à jour de la section
                    updateSection(transaction, sec);
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    return;
                }
            }
        }

        // Effectuer les autres mises à jour s'il n'y a pas d'erreur
        if (!transaction.hasErrors() && (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
            _afterActiver(transaction);
        }

        // S'il y a des erreurs, on le signale
        if (transaction.hasErrors() || (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0)) {
            setEtat(APIOperation.ETAT_ERREUR);
        } else if (getCodeMaster().equals(APIOperation.MASTER)) {
            setEtat(APIOperation.ETAT_INACTIF);
        } else {
            setEtat(APIOperation.ETAT_PROVISOIRE);
        }
    }

    public final void activerLite(BTransaction transaction) {
        // Création d'un nouveau log en mémoire
        memLog = new FWMemoryLog();

        // Vérifier l'état
        if (!getEtat().equals(APIOperation.ETAT_OUVERT) && !getEtat().equals(APIOperation.ETAT_ERREUR)) {
            _addError(transaction, getSession().getLabel("5101"));
            return;
        }
        // Vérifier qu'une transaction ouverte existe
        if (!transaction.isOpened()) {
            _addError(transaction, getSession().getLabel("5102"));
            return;
        }
        // Vérifier les propriétés
        _valider(transaction);
        if (transaction.hasErrors()) {
            return;
        }

        // S'il y a des erreurs, on sort
        if (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR);
            return;
        }

        // Exécuter les opération avant activation
        _beforeActiver(transaction);
        if (transaction.hasErrors()) {
            return;
        }

        // On ne met pas à jour les écritures MASTER
        if (!getCodeMaster().equals(APIOperation.MASTER)) {
            // S'il y a des erreurs, on sort
            if (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
                setEtat(APIOperation.ETAT_ERREUR);
                return;
            }

            // TODO : mettre à jour le solde du compte annexe et de la section dans le traitement du lot et non par
            // operation.
            // Insérer l'opération dans le compte annexe
            // this.memLog.logMessage(this.compteAnnexe.addOperation(this));
            //
            // // Effectuer les autres mises à jour s'il n'y a pas d'erreur
            // if (!transaction.hasErrors() && (this.memLog.getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
            // // Commencer la mise à jour
            // try {
            // // Mise à jour du compte annexe
            // this.updateCompteAnnexe(transaction, this.compteAnnexe);
            // // Mise à jour de la section
            // this.updateSection(transaction, this.section);
            // } catch (Exception e) {
            // this._addError(transaction, e.getMessage());
            // return;
            // }
            // }

        }

        // Effectuer les autres mises à jour s'il n'y a pas d'erreur
        if (!transaction.hasErrors() && (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
            _afterActiver(transaction);
        }

        // S'il y a des erreurs, on le signale
        if (transaction.hasErrors() || (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0)) {
            setEtat(APIOperation.ETAT_ERREUR);
        } else if (getCodeMaster().equals(APIOperation.MASTER)) {
            setEtat(APIOperation.ETAT_INACTIF);
        } else {
            setEtat(APIOperation.ETAT_PROVISOIRE);
        }
    }

    /**
     * Date de création : (26.03.2002 18:39:22)
     */
    protected void afterRetrieve() {
    }

    /**
     * @return
     */
    public String assuranceCantonCourt() {
        return getSession().getCode(getAssuranceCanton());
    }

    public void beforeAdd(BTransaction transaction) throws Exception {
        _beforeAdd(transaction);
    }

    /**
     * Validation complète si saisie à l'écran ou si accepter erreur = false
     *
     * @param statement
     */
    private void checkSaisieEcran(globaz.globall.db.BStatement statement) {
        if (getSaisieEcran().booleanValue()) {
            _valider(statement.getTransaction());

            // S'il y a des erreurs
            if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {

                // Parcourir les messages et logger les erreurs
                java.util.Enumeration enumerate = getMemoryLog().enumMessages();
                while (enumerate.hasMoreElements()) {
                    FWMessage msg = (FWMessage) enumerate.nextElement();
                    if (msg.getTypeMessage().compareTo(FWMessage.ERREUR) >= 0) {
                        _addError(statement.getTransaction(), msg.getMessageText());
                    }
                }
                setMemoryLog(null);
            }
        }
    }

    /**
     * Date de création : (24.01.2002 14:39:11)
     */
    public final void comptabiliser(BTransaction transaction, IntJournalCG journalCg) {
        // Création d'un nouveau log en mémoire ou récupération d'un ancien
        if (memLog == null) {
            memLog = new FWMemoryLog();
        }
        // Ignorer les écritures inactives
        if (getEtat().equals(APIOperation.ETAT_INACTIF)) {
            return;
        }
        // On ne comptabilise que les écritures déjà activees (en provisoire)
        if (!getEtat().equals(APIOperation.ETAT_PROVISOIRE)) {
            _addError(null, getSession().getLabel("5112"));
            return;
        }
        // S'il y a des erreurs, on sort
        if (transaction.hasErrors() || (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) >= 0)) {
            return;
        }
        // S'il n'y a pas d'erreurs, l'écriture est définitive
        if (!transaction.hasErrors() && (memLog.getErrorLevel().compareTo(FWMessage.AVERTISSEMENT) <= 0)) {
            setEtat(APIOperation.ETAT_COMPTABILISE);
        }
    }

    /**
     * Supprimer les groupements
     *
     * @param transaction
     * @throws Exception
     */
    private void deleteGroupements(globaz.globall.db.BTransaction transaction) throws Exception {
        CAGroupementManager mgr = findAllGroupements(transaction);
        if (!mgr.isEmpty()) {
            for (int i = 0; i < mgr.size(); i++) {
                CAGroupement grp = (CAGroupement) mgr.getEntity(i);
                if (!grp.hasOperations(transaction)) {
                    grp.delete(transaction);
                    if (transaction.hasErrors()) {
                        _addError(transaction, getSession().getLabel("7101"));
                    }
                }
            }
        }
    }

    /**
     * Supprimer les éventuelles liaisons
     *
     * @param transaction
     * @throws Exception
     */
    private void deleteLiaisons(globaz.globall.db.BTransaction transaction) throws Exception {
        if (findAllGroupementOperations(transaction) != null) {
            for (int i = 0; i < findAllGroupementOperations(transaction).size(); i++) {
                CAGroupementOperation grpOper = (CAGroupementOperation) findAllGroupementOperations(transaction)
                        .getEntity(i);
                grpOper.delete(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7101"));
                } else {
                    // Supprimer le groupement si vide
                    CAGroupement grp = new CAGroupement();
                    grp.setSession(getSession());
                    grp.setIdGroupement(grpOper.getIdGroupement());
                    grp.retrieve(transaction);
                    if (!grp.isNew() && !grp.hasOperations(transaction)) {
                        grp.delete(transaction);
                        if (transaction.hasErrors()) {
                            _addError(transaction, getSession().getLabel("7101"));
                        }
                    }
                }
            }
        }
    }

    /**
     * Cette méthode déclenche la suppression de l'opération dans les objets suivants :
     * <p>
     * <ul>
     * le compte annexe
     * <ul>
     * la section
     * <ul>
     * le compte courant
     * <ul>
     * le compte annexe / compte courant
     * <ul>
     * le compte courant / section
     * <p>
     * Cette opération doit être lancée sous contrôle de validation (transaction) Date de création : (18.01.2002
     * 11:10:22)
     */
    public final void desactiver(BTransaction transaction) {
        // Création d'un nouveau log en mémoire
        memLog = new FWMemoryLog();

        // Vérifier l'état
        if (getEtat().equals(APIOperation.ETAT_OUVERT) || getEtat().equals(APIOperation.ETAT_ERREUR)
                || JadeStringUtil.isIntegerEmpty(getEtat())) {
            _addError(transaction, getSession().getLabel("5169"));
            return;
        }
        // Vérifier qu'une transaction ouverte existe
        if (!transaction.isOpened()) {
            _addError(transaction, getSession().getLabel("5102"));
            return;
        }
        _beforeDesactiver(transaction);
        if (transaction.hasErrors() || getMemoryLog().hasErrors()) {
            return;
        }
        // Ecriture Master -> repasser en SINGLE et sortir
        if (getCodeMaster().equals(APIOperation.MASTER)) {
            setCodeMaster(APIOperation.SINGLE);
            setEtat(APIOperation.ETAT_OUVERT);
            return;
        }
        // Récupérer le compte annexe
        CACompteAnnexe _cptAnn = getCompteAnnexe();
        if (_cptAnn == null) {
            _addError(transaction, getSession().getLabel("5104"));
            return;
        }
        // On ne passe pas dedans pour les écritures master
        if (!getCodeMaster().equals(APIOperation.MASTER)) {
            // Récupérer la section (null autorisé)
            CASection _sec = getSection();
            // Insérer l'opération dans le compte annexe
            memLog.logMessage(_cptAnn.removeOperation(this));
            // Insérer l'opération dans la section
            if (_sec != null) {
                memLog.logMessage(_sec.removeOperation(this));
            }
            // Effectuer les autres mises à jour s'il n'y a pas d'erreur
            if (!transaction.hasErrors() && (memLog.getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
                // Commencer la mise à jour
                try {
                    updateCompteAnnexe(transaction, _cptAnn);
                    updateSection(transaction, _sec);
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    return;
                }
            }
        }

        // Effectuer les autres mises à jour s'il n'y a pas d'erreur
        if (!transaction.hasErrors() && !memLog.hasErrors()) {
            _afterDesactiver(transaction);
        }
        // S'il y a des erreurs, on le signale
        if (memLog.hasErrors()) {
            _addError(transaction, getSession().getLabel("5172"));
            // S'il n'y a pas d'erreurs, l'écriture est réouverte
        } else {
            setEtat(APIOperation.ETAT_OUVERT);
        }
    }

    /**
     * Dupliquer les valeurs de l'opération passée en paramètres <br />
     * Date de création : (13.02.2002 10:37:01)
     *
     * @param oper
     *            globaz.osiris.db.comptes.CAOperation
     */
    public void dupliquer(CAOperation oper) {
        if (oper != null) {
            setIdTypeOperation(oper.getIdTypeOperation());
            setDate(oper.getDate());
            setIdCompteAnnexe(oper.getIdCompteAnnexe());
            setIdCompteCourant(oper.getIdCompteCourant());
            setIdSection(oper.getIdSection());
            setIdAssurance(oper.getIdAssurance());
            setAssuranceCanton(oper.getAssuranceCanton());
            setIdCaisseProfessionnelle(oper.getIdCaisseProfessionnelle());
            setEtat(APIOperation.ETAT_OUVERT);
        }
    }

    /**
     * @param transaction
     * @param journal
     * @param text
     * @return
     */
    public final CAOperation extourner(BTransaction transaction, CAJournal journal, String text) {
        boolean useOwnTransaction = false;
        CAOperation opExtourne = null;

        try {
            // L'écriture doit être active
            if (!getEstComptabilise().booleanValue() || isNew()) {
                throw new Exception(getSession().getLabel("EXTOURNE_NOT_ACTIVE"));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }
            // Récupérer l'extourne
            if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
                opExtourne = _createExtourne(transaction, text.substring(0, 40));
            } else {
                opExtourne = _createExtourne(transaction, text);
            }
            opExtourne.setSession(getSession());

            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }
            // Insérer l'extourne dans le journal
            if ((opExtourne != null) && !transaction.hasErrors()) {
                opExtourne.setIdJournal(journal.getIdJournal());

                // Activer si nécessaire
                if (journal.getEstVisibleImmediatement().booleanValue()) {
                    opExtourne.activer(transaction);
                }
                // Insérer
                if (!transaction.hasErrors()) {
                    opExtourne.add(transaction);
                }
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.error(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
        // retourner l'extourne
        return opExtourne;
    }

    /**
     * Date de création : (04.03.2002 14:50:42)
     *
     * @return globaz.osiris.db.comptes.CAGroupementOperationManager
     */
    public CAGroupementOperationManager findAllGroupementOperations(BTransaction transaction) {
        // Si pas déjà chargé
        if (groupementOperations == null) {
            groupementOperations = new CAGroupementOperationManager();
            groupementOperations.setSession(getSession());
            groupementOperations.setForIdOperation(getIdOperation());
            try {
                groupementOperations.find(transaction);
                if (transaction.hasErrors()) {
                    groupementOperations = null;
                }
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
                groupementOperations = null;
            }
        }
        return groupementOperations;
    }

    /**
     * Récupères tous les groupements dans lesquels l'opération est mère
     *
     * @param transaction
     *            la transaction
     * @return CAGroupementManager le manager sur les groupements
     * @throws Exception
     *             si la méthode a échoué
     */
    public CAGroupementManager findAllGroupements(BTransaction transaction) throws Exception {
        CAGroupementManager mgr = new CAGroupementManager();
        mgr.setSession(getSession());
        mgr.setForIdOperationMaster(getIdOperation());
        mgr.find(transaction);
        return mgr;
    }

    /**
     * Récupère le groupement associé à l'opération. Retourne null s'il n'y a pas de groupement.
     *
     * @param transaction
     *            la transaction
     * @param typeGroupement
     *            le type de groupement
     * @return CAGroupement le groupement ou null
     * @throws Exception
     *             si l'opération échoue
     */
    public CAGroupement findGroupement(BTransaction transaction, String typeGroupement) throws Exception {
        CAGroupement grp = null;
        // Récupération du manager
        CAGroupementManager mgr = new CAGroupementManager();
        mgr.setSession(getSession());
        mgr.setForIdOperationMaster(getIdOperation());
        mgr.setForTypeGroupement(typeGroupement);
        mgr.find(transaction);
        if (!mgr.isEmpty()) {
            grp = (CAGroupement) mgr.getEntity(0);
        }
        // Retrourner le groupement
        return grp;
    }

    /**
     * Méthode redéfinie dans CAOperationOrdreVersement et CAOperationOrdreRecouvrement
     *
     * @return null
     */
    public IntAdressePaiement getAdressePaiement() throws Exception {
        return null;
    }

    /**
     * @return
     */
    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    /**
     * @return le code système de l'assurance
     */
    @Override
    public String getAssuranceCanton() {
        return assuranceCanton;
    }

    /**
     * @return
     */
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getCodeMaster()
     */
    @Override
    public String getCodeMaster() {
        return codeMaster;
    }

    /**
     * Date de création : (30.01.2002 07:44:40)
     *
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public CARubrique getCompte() {
        CARubrique rubrique;
        rubrique = new CARubrique();
        rubrique.setISession(getSession());
        rubrique.setIdRubrique(getIdCompte());
        try {
            rubrique.retrieve();
            if (rubrique.isNew()) {
                rubrique = null;
            }
        } catch (Exception e) {
            rubrique = null;
        }
        return rubrique;
    }

    /**
     * Date de création : (18.01.2002 10:59:42)
     *
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public CACompteAnnexe getCompteAnnexe() {
        if (compteAnnexe == null) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setISession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            try {
                compteAnnexe.retrieve();
                if (compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }
        return compteAnnexe;
    }

    /**
     * Date de création : (18.01.2002 11:02:00)
     *
     * @return globaz.osiris.db.comptes.CACompteCourant
     */
    public CACompteCourant getCompteCourant() {
        if (JadeStringUtil.isIntegerEmpty(getIdCompteCourant())) {
            return null;
        }
        if (compteCourant == null) {
            compteCourant = new CACompteCourant();
            compteCourant.setISession(getSession());
            compteCourant.setIdCompteCourant(getIdCompteCourant());
            try {
                compteCourant.retrieve();
                if (compteCourant.isNew()) {
                    compteCourant = null;
                }
            } catch (Exception e) {
                compteCourant = null;
            }
        }
        return compteCourant;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getCsEtat()
     */
    @Override
    public FWParametersSystemCode getCsEtat() {
        if (csEtat == null) {
            // liste pas encore chargee, on la charge
            csEtat = new FWParametersSystemCode();
            csEtat.getCode(getEtat());
        }
        return csEtat;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getCsEtats()
     */
    @Override
    public FWParametersSystemCodeManager getCsEtats() {
        // liste déjà chargée ?
        if (csEtats == null) {
            // liste pas encore chargée, on la charge
            csEtats = new FWParametersSystemCodeManager();
            csEtats.setSession(getSession());
            csEtats.getListeCodesSup("OSIETAOPE", getSession().getIdLangue());
        }
        return csEtats;
    }

    /**
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsProvenancePmt() {
        // liste déjà chargée ?
        if (csProvenancePaiement == null) {
            // liste pas encore chargée, on la charge
            csProvenancePaiement = new FWParametersSystemCodeManager();
            csProvenancePaiement.setSession(getSession());
            csProvenancePaiement.getListeCodesSup("OSIPROVPMT", getSession().getIdLangue());
        }
        return csProvenancePaiement;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getDate()
     */
    @Override
    public String getDate() {

        return date;
    }

    /**
     * Date de création : (11.03.2002 12:57:07)
     *
     * @return String
     */
    public String getDescription() {
        return this.getDescription(getSession().getIdLangueISO());
    }

    /**
     * Date de création : (11.03.2002 12:57:36)
     *
     * @return String
     * @param codeISOLangue
     *            String
     */
    public String getDescription(String codeISOLangue) {
        return "";
    }

    /**
     * Retourne vrai si l'opération a été comptabilisée <br/>
     * Date de création : (25.02.2002 13:28:26)
     *
     * @return Boolean
     */
    public Boolean getEstActive() {
        if (getEtat().equals(APIOperation.ETAT_PROVISOIRE) || getEtat().equals(APIOperation.ETAT_COMPTABILISE)
                || getEtat().equals(APIOperation.ETAT_VERSE) || getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Date de création : (25.02.2002 13:35:20)
     *
     * @return Boolean
     */
    public Boolean getEstComptabilise() {
        if (getEtat().equals(APIOperation.ETAT_COMPTABILISE) || getEtat().equals(APIOperation.ETAT_VERSE)
                || getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getEtat()
     */
    @Override
    public String getEtat() {
        return etat;
    }

    /**
     * @return the idAssurance
     */
    @Override
    public String getIdAssurance() {
        return idAssurance;
    }

    /**
     * @return
     */
    public String getIdCaisseProfessionnelle() {
        return idCaisseProfessionnelle;
    }

    /**
     * @return
     */
    public String getIdCompte() {
        return idCompte;
    }

    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Retourne le Nom du compte annexe auxiliaire lié.
     *
     * @return
     */
    public String getIdCompteAnnexeAuxDesc() {
        CASection sectionAux = new CASection();
        sectionAux.setSession(getSession());
        sectionAux.setIdSection(getIdSectionAux());
        try {
            sectionAux.retrieve();
        } catch (Exception e) {
            JadeLogger.warn(this, e);
            return new String();
        }

        if (sectionAux.isNew()) {
            return new String();
        }

        CACompteAnnexe compteAnnexeAux = new CACompteAnnexe();
        compteAnnexeAux.setSession(getSession());
        compteAnnexeAux.setIdCompteAnnexe(sectionAux.getIdCompteAnnexe());
        try {
            compteAnnexeAux.retrieve();
        } catch (Exception e1) {
            JadeLogger.warn(this, e1);
            return new String();
        }
        if (compteAnnexeAux.isNew()) {
            return new String();
        }

        return compteAnnexeAux.getIdExterneRole() + " - " + compteAnnexeAux.getTiers().getNom();
    }

    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    /**
     * @return
     */
    public String getIdContrepartie() {
        return idContrepartie;
    }

    /**
     * @return
     */
    public String getIdEcheancePlan() {
        return idEcheancePlan;
    }

    /**
     * Date de création : (20.03.2002 13:17:47)
     *
     * @return String
     */
    public String getIdExterneCompteCourantEcran() {
        if (loadIdExterneCompteCourantEcran) {
            loadIdExterneCompteCourantEcran = false;
            if (getCompteCourant() != null) {
                idExterneCompteCourantEcran = getCompteCourant().getIdExterne();
            }
        } else if (rechercheCompteCourantEcran.booleanValue() && (getCompteCourant() != null)) {
            idExterneCompteCourantEcran = getCompteCourant().getIdExterne();
        }

        return idExterneCompteCourantEcran;
    }

    /**
     * Date de création : (20.03.2002 13:07:42)
     *
     * @return String
     */
    public String getIdExterneRoleEcran() {
        if (loadIdExterneRoleEcran) {
            loadIdExterneRoleEcran = false;
            if (getCompteAnnexe() != null) {
                idExterneRoleEcran = getCompteAnnexe().getIdExterneRole();
            }
        } else if (rechercheCompteAnnexeEcran.booleanValue() && (getCompteAnnexe() != null)) {
            idExterneRoleEcran = getCompteAnnexe().getIdExterneRole();
        }

        return idExterneRoleEcran;
    }

    /**
     * @return
     */
    public String getIdExterneSectionAuxEcran() {
        if (JadeStringUtil.isBlank(idExterneSectionAuxEcran)) {
            CASection sectionAux = new CASection();
            sectionAux.setSession(getSession());
            sectionAux.setIdSection(getIdSectionAux());
            try {
                sectionAux.retrieve();
            } catch (Exception e) {
                JadeLogger.warn(this, e);
                return new String();
            }

            if (sectionAux.isNew()) {
                return new String();
            }

            idExterneSectionAuxEcran = sectionAux.getIdExterne();
        }

        return idExterneSectionAuxEcran;
    }

    /**
     * Date de création : (20.03.2002 13:12:54)
     *
     * @return String
     */
    public String getIdExterneSectionEcran() {
        if (loadIdExterneSectionEcran) {
            loadIdExterneSectionEcran = false;
            if (getSection() != null) {
                idExterneSectionEcran = getSection().getIdExterne();
            }
        } else if (rechercheSectionEcran.booleanValue() && (getSection() != null)) {
            idExterneSectionEcran = getSection().getIdExterne();
        }

        return idExterneSectionEcran;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getIdJournal()
     */
    @Override
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return
     */
    public String getIdLog() {
        return idLog;
    }

    /**
     * Getter
     */
    @Override
    public String getIdOperation() {
        return idOperation;
    }

    /**
     * @return
     */
    public String getIdOrdre() {
        return idOrdre;
    }

    /**
     * Méthode redéfinie dans CAOperationOrdreVersement et CAOperationOrdreRecouvrement
     *
     * @return null
     */
    public String getIdOrdreGroupe() {
        return null;
    }

    /**
     * Date de création : (20.03.2002 13:11:54)
     *
     * @return String
     */
    public String getIdRoleEcran() {
        if (loadIdRoleEcran) {
            loadIdRoleEcran = false;
            if (getCompteAnnexe() != null) {
                idRoleEcran = getCompteAnnexe().getIdRole();
            }
        } else if (rechercheCompteAnnexeEcran.booleanValue() && (getCompteAnnexe() != null)) {
            idRoleEcran = getCompteAnnexe().getIdRole();
        }

        return idRoleEcran;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getIdSection()
     */
    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return
     */
    public String getIdSectionAux() {
        return idSectionAux;
    }

    @Override
    public String getIdSectionCompensation() {
        return idSectionCompensation;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getIdTypeOperation()
     */
    @Override
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * Date de création : (20.03.2002 13:13:46)
     *
     * @return String
     */
    public String getIdTypeSectionEcran() {
        if (loadIdTypeSectionEcran) {
            loadIdTypeSectionEcran = false;
            if (getSection() != null) {
                idTypeSectionEcran = getSection().getIdTypeSection();
            }
        } else if (rechercheSectionEcran.booleanValue() && (getSection() != null)) {
            idTypeSectionEcran = getSection().getIdTypeSection();
        }

        return idTypeSectionEcran;
    }

    /**
     * Date de création : (18.01.2002 10:57:14)
     *
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal getJournal() {
        if (journal == null) {
            journal = new CAJournal();
            journal.setISession(getSession());
            journal.setIdJournal(getIdJournal());
            try {
                journal.retrieve();
                if (journal.isNew()) {
                    journal = null;
                }

            } catch (Exception e) {
                journal = null;
            }
        }

        return journal;
    }

    /**
     * @return
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Date de création : (10.01.2002 16:53:13)
     *
     * @return globaz.osiris.db.utils.FWLog
     */
    public FWLog getLog() {
        // Si le log n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdLog())) {
            return null;
        }

        // Si log pas déjà chargé
        if (log == null) {
            // Instancier un nouveau LOG
            log = new FWLog();
            log.setSession(getSession());

            // Récupérer le log en question
            log.setIdLog(getIdLog());
            try {
                log.retrieve();
                if (log.isNew()) {
                    log = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                log = null;
            }
        }

        return log;
    }

    public HashMap<String, String> getMapValeurUtilisateur() {
        return mapValeurUtilisateur;
    }

    /**
     * @return
     */
    public String getMasse() {
        return masse;
    }

    /**
     * Date de création : (21.01.2002 11:33:16)
     *
     * @return globaz.osiris.db.utils.FWMemoryLog
     */
    public FWMemoryLog getMemoryLog() {
        if (memLog == null) {
            memLog = new FWMemoryLog();
            memLog.setSession(getSession());
        }
        return memLog;
    }

    /**
     * Date de création : (03.06.2002 10:02:46)
     *
     * @return String
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public String getNoEcritureCollective() {
        return noEcritureCollective;
    }

    /**
     * Date de création : (28.03.2002 10:03:45)
     *
     * @return String
     */
    public String getNomEcran() {
        return nomEcran;
    }

    /**
     * Méthode redéfinie dans CAOperationOrdreVersement et CAOperationOrdreRecouvrement
     *
     * @return null
     */
    public String getNumTransaction() {
        return null;
    }

    /**
     * Instancie un nouvel objet en fonction du type d'opération <br/>
     * Date de création : (31.01.2002 13:23:00)
     *
     * @return globaz.osiris.db.comptes.CAOperation
     * @throws Exception
     */
    public CAOperation getOperationFromType(BTransaction tr) {
        // Déterminer l'objet à retourner en fonction du type d'opération
        if ((getIdTypeOperation() == null) || JadeStringUtil.isBlank(getIdTypeOperation())) {
            return this;
        } else if (getIdTypeOperation().equals(APIOperation.CAECRITURE)) {
            return new CAEcriture(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAECRITURECOMPENSATION)) {
            return new CAEcritureCompensation(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAPAIEMENT)) {
            return new CAPaiement(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAPAIEMENTETRANGER)) {
            return new CAPaiementEtranger(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAOPERATIONORDREVERSEMENT)) {
            CAOperationOrdreVersement ca = new CAOperationOrdreVersement(this);
            ca.initOrdreVersement(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE)) {
            CAOperationOrdreVersementAvance ca = new CAOperationOrdreVersementAvance(this);
            ca.initOrdreVersement(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAVERSEMENT)) {
            CAVersement ca = new CAVersement(this);
            ca.setSession(getSession());
            ca.getOrdreVersement();
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAVERSEMENTAVANCE)) {
            return new CAVersementAvance(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAOPERATIONORDRERECOUVREMENT)) {
            CAOperationOrdreRecouvrement ca = new CAOperationOrdreRecouvrement(this);
            ca.initOrderRecouvrement(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CARECOUVREMENT)) {
            CARecouvrement ca = new CARecouvrement(this);
            ca.initTransactionRecouvremenet(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAPAIEMENTBVR)) {
            CAPaiementBVR ca = new CAPaiementBVR(this);
            ca.initTransactionBVR(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAOPERATIONCONTENTIEUX)) {
            CAOperationContentieux ca = new CAOperationContentieux(this);
            ca.initEvenementContentieux(tr);
            return ca;
        } else if (getIdTypeOperation().equals(APIOperation.CAOPERATIONCONTENTIEUXAQUILA)) {
            return new CAOperationContentieuxAquila(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAECRITURELISSAGE)) {
            return new CAEcritureLissage(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAAUXILIAIRE)) {
            return new CAAuxiliaire(this);
        } else if (getIdTypeOperation().equals(APIOperation.CAAUXILIAIRE_PAIEMENT)) {
            return new CAAuxiliairePaiement(this);
        } else {
            return this;
        }
    }

    /**
     * @return
     */
    public CAOrdreGroupe getOrdreGroupe() {
        return null;
    }

    /**
     * Méthode redéfinie dans CAOperationOrdreVersement et CAOperationOrdreRecouvrement
     *
     * @param idOrdreGroupe
     * @return null
     */
    public CAOrdreGroupe getOrdreGroupe(String idOrdreGroupe) {
        return null;
    }

    /**
     * @return
     */
    public String getPiece() {
        return piece;
    }

    /**
     * @return the provenancePmt
     */
    public String getProvenancePmt() {
        return provenancePmt;
    }

    public String getProvenancePmtLibelle() {
        return getSession().getCodeLibelle(provenancePmt);
    }

    /**
     * Date de création : (20.02.2002 15:37:53)
     *
     * @return Boolean
     */
    public Boolean getQuittanceLogEcran() {
        return quittanceLogEcran;
    }

    /**
     * Date de création : (25.03.2002 13:44:35)
     *
     * @return String
     */
    public Boolean getRechercheCompteAnnexeEcran() {
        return rechercheCompteAnnexeEcran;
    }

    /**
     * Date de création : (26.03.2002 16:08:28)
     *
     * @return Boolean
     */
    public Boolean getRechercheCompteCourantEcran() {
        return rechercheCompteCourantEcran;
    }

    /**
     * Date de création : (25.03.2002 13:44:35)
     *
     * @return String
     */
    public Boolean getRechercheSectionEcran() {
        return rechercheSectionEcran;
    }

    /**
     * Date de création : (07.02.2002 10:39:00)
     *
     * @return Boolean
     */
    public Boolean getSaisieEcran() {
        return saisieEcran;
    }

    /**
     * Date de création : (18.01.2002 11:15:02)
     *
     * @return globaz.osiris.db.comptes.CASection
     */
    public CASection getSection() {
        if (section == null) {
            section = new CASection();
            section.setISession(getSession());
            section.setIdSection(getIdSection());
            try {
                section.retrieve();
                if (section.isNew()) {
                    section = null;
                }
            } catch (Exception e) {
                section = null;
            }
        }

        return section;
    }

    @Override
    public String getSectionCompensationDeSur() {
        return sectionCompensationDeSur;
    }

    /**
     * @return
     */
    public String getTaux() {
        return taux;
    }

    /**
     * Pour savoir si cette opération à généré un report sur la section.
     *
     * @return the taxeEstReporte
     */
    public Boolean getTaxeEstReporte() {
        return taxeEstReporte;
    }

    /**
     * @return
     */
    public String[] getTitulaireEntete() {
        String[] str = new String[1];
        CACompteAnnexe ca = getCompteAnnexe();
        if (ca != null) {
            str[0] = ca.getTiers().getTitulaireNomLieu();
        } else {
            str[0] = "";
        }
        return str;
    }

    /**
     * Date de création : (18.01.2002 11:04:54)
     *
     * @return globaz.osiris.db.comptes.CATypeOperation
     */
    public CATypeOperation getTypeOperation() {
        if (typeOperation == null) {
            typeOperation = new CATypeOperation();
            typeOperation.setISession(getSession());
            typeOperation.setIdTypeOperation(getIdTypeOperation());
            try {
                typeOperation.retrieve();
                if (typeOperation.isNew()) {
                    typeOperation = null;
                }
            } catch (Exception e) {
                typeOperation = null;
            }
        }

        return typeOperation;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#getUcEtat()
     */
    @Override
    public FWParametersUserCode getUcEtat() {
        if (ucEtat == null) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());

            if (!JadeStringUtil.isIntegerEmpty(getEtat())) {
                // Récupérer le code système dans la langue de l'utilisateur
                ucEtat.setIdCodeSysteme(getEtat());
                ucEtat.setIdLangue(getSession().getIdLangue());
                try {
                    ucEtat.retrieve();
                    if (ucEtat.isNew()) {
                        _addError(null, getSession().getLabel("7324"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("7324"));
                }
            }
        }
        return ucEtat;
    }

    /**
     * Date de création : (28.03.2002 10:06:39)
     *
     * @return Vector
     */
    public Vector getValeurUtilisateur() {
        return valeurUtilisateur;
    }

    /**
     * Indique s'il existe un groupement du type indiqué associé à l'opération
     *
     * @param transaction
     *            la transaction
     * @param typeGroupement
     *            le type de groupement
     * @return boolean vrai s'il existe un groupement qui correspond aux paramètres
     * @throws Exception
     *             si l'opération échoue
     */
    public boolean hasGroupement(BTransaction transaction, String typeGroupement) throws Exception {
        CAGroupementManager mgr = new CAGroupementManager();
        mgr.setSession(getSession());
        mgr.setForIdOperationMaster(getIdOperation());
        mgr.setForTypeGroupement(typeGroupement);
        if (mgr.getCount(transaction) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indique si l'opération fait partie d'un groupement (opération fille)
     *
     * @param transaction
     *            la transaction
     * @param typeGroupement
     *            le type de groupement ou null si non désiré
     * @return boolean vrai si l'opération fait partie au moins d'un groupement
     * @throws Exception
     *             si la méthode échoue
     */
    public boolean hasGroupementOperation(BTransaction transaction, String typeGroupement) throws Exception {
        CAGroupementOperationManager mgr = new CAGroupementOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdOperation(getIdOperation());
        if (!JadeStringUtil.isBlank(typeGroupement)) {
            mgr.setForTypeGroupement(typeGroupement);
        }
        if (mgr.getCount(transaction) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Récupérer le compte annexe
     *
     * @return
     */
    private CACompteAnnexe initCompteAnnexe() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setISession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
        try {
            compteAnnexe.retrieve();
            if (compteAnnexe.isNew()) {
                compteAnnexe = null;
            }
        } catch (Exception e) {
            compteAnnexe = null;
        }
        return compteAnnexe;
    }

    /**
     * Récupérer le compte courrant (null autorisé)
     */
    private void initCompteCourant() {
        // Récupérer le compte courrant (null autorisé)
        CACompteCourant compteCourant = new CACompteCourant();
        compteCourant.setISession(getSession());
        compteCourant.setIdCompteCourant(getIdCompteCourant());
        try {
            compteCourant.retrieve();
            if (compteCourant.isNew()) {
                compteCourant = null;
            }
        } catch (Exception e) {
            compteCourant = null;
        }
    }

    /**
     * Récupérer la section (null autorisé)
     *
     * @return
     */
    private CASection initSection() {
        CASection sec = new CASection();
        sec.setISession(getSession());
        sec.setIdSection(getIdSection());
        try {
            sec.retrieve();
            if (sec.isNew()) {
                sec = null;
            }
        } catch (Exception e) {
            sec = null;
        }
        return sec;
    }

    /**
     * Retourne vrai si la classe est une instance ou une sous classe du type operation fourni <br/>
     * Date de création : (30.01.2002 17:11:14)
     *
     * @return boolean
     * @param typeOperation
     *            String
     */
    public boolean isInstanceOrSubClassOf(String typeOperation) {
        // Faux si le type est nul ou vide
        if (JadeStringUtil.isBlank(typeOperation)) {
            return false;
        }
        // Vrai si le type concorde
        if (getIdTypeOperation().equals(typeOperation)) {
            return true;
        }
        // Vrai si c'est une sous-classe
        return getIdTypeOperation().startsWith(typeOperation);
    }

    /**
     * Date de création : (24.04.2002 10:51:55)
     *
     * @return boolean
     */
    public boolean isNewCompteAnnexe() {
        return newCompteAnnexe;
    }

    /**
     * Date de création : (18.04.2002 10:49:56)
     *
     * @return boolean
     */
    public boolean isNewSection() {
        return newSection;
    }

    /**
     * @return
     */
    public boolean isOperationExtournable() {
        return false;
    }

    /**
     * @return the taxeEstReporte
     */
    public boolean isTaxeEstReporte() {
        return taxeEstReporte.booleanValue();
    }

    /**
     * Retourne vrai si l'opération peut être mise à jour <br/>
     * Date de création : (01.02.2002 14:30:40)
     *
     * @return boolean
     */
    public boolean isUpdatable() {
        return JadeStringUtil.isBlank(getEtat()) || getEtat().equals(APIOperation.ETAT_OUVERT)
                || getEtat().equals(APIOperation.ETAT_ERREUR);
    }

    public boolean isUseOptimisation() {
        return useOptimisation;
    }

    /**
     * Ouverture d'un compte annexe.
     *
     * @param transaction
     * @return
     * @throws Exception
     */
    private CACompteAnnexe openCompteAnnexe(BTransaction transaction) throws Exception {
        // Vérifier les paramètres
        if (transaction == null) {
            throw new Exception(getSession().getLabel("5044"));
        }
        if (transaction.getSession() == null) {
            throw new Exception(transaction.toString());
        }

        if ((getIdRoleEcran() == null) || JadeStringUtil.isIntegerEmpty(getIdRoleEcran())) {
            throw new Exception(transaction.getSession().getLabel("5046"));
        }

        if (JadeStringUtil.isBlank(getIdExterneRoleEcran())) {
            throw new Exception(transaction.getSession().getLabel("5047"));
        }

        CAApplication currentApplication = CAApplication.getApplicationOsiris();
        IntRole role = (IntRole) globaz.globall.db.GlobazServer.getCurrentSystem()
                .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                .getImplementationFor(transaction.getSession(), IntRole.class);
        role.retrieve(getIdRoleEcran(), getIdExterneRoleEcran());

        if (role.isNew()) {
            throw new Exception(transaction.getSession().getLabel("5049"));
        }

        // Création d'un nouveau compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(transaction.getSession());
        cpt.setIdTiers(role.getIdTiers());
        cpt.setIdRole(getIdRoleEcran());
        cpt.setIdExterneRole(getIdExterneRoleEcran());
        if (getIdJournal() != null) {
            cpt.setIdJournal(getIdJournal());
        }

        if (getIdTypeOperation().startsWith(APIOperation.CAAUXILIAIRE)) {
            cpt.setIdGenreCompte(CACodeSystem.COMPTE_AUXILIAIRE);
        }

        // Ouverture
        cpt.add(transaction);

        // Vérifier les erreurs de création
        if (transaction.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("5048"));
        }

        return cpt;
    }

    /**
     * Supprimer les retours à la ligne dans un string. Nécessaire pour les motifs de versements et recouvrements.
     *
     * @param line
     * @return
     */
    protected String removeBlankLines(String line) {
        if (line != null) {
            String tmp = JadeStringUtil.change(line, "\r\n", " ");
            return JadeStringUtil.change(tmp, "\n", " ");
        } else {
            return line;
        }
    }

    /**
     * @param string
     */
    public void setAnneeCotisation(String string) {
        anneeCotisation = string;
    }

    /**
     * @param assuranceCanton
     *            Code système de l'assurance
     */
    @Override
    public void setAssuranceCanton(String assuranceCanton) {
        this.assuranceCanton = assuranceCanton;
    }

    /**
     * @param string
     */
    public void setCodeDebitCredit(String string) {
        codeDebitCredit = string;
    }

    /**
     * @param newCodeMaster
     */
    public void setCodeMaster(String newCodeMaster) {
        codeMaster = newCodeMaster;
    }

    public void setCompteAnnexe(CACompteAnnexe compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#setDate(java.lang.String)
     */
    @Override
    public void setDate(String newDate) {
        date = newDate;
    }

    /**
     * @param newEtat
     */
    public void setEtat(String newEtat) {
        etat = newEtat;
        csEtat = null;
        ucEtat = null;
    }

    /**
     * @param idAssurance
     *            the idAssurance to set
     */
    @Override
    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    /**
     * @param idCaisseProfessionnelle
     */
    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle) {
        this.idCaisseProfessionnelle = idCaisseProfessionnelle;
    }

    /**
     * @param string
     */
    public void setIdCompte(String string) {
        idCompte = string;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#setIdCompteAnnexe(java.lang.String)
     */
    @Override
    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        // if (isUpdatable()) {
        idCompteAnnexe = newIdCompteAnnexe;
        compteAnnexe = null;
        // } else {
        // // S'il s'agit d'un ordre de versement et que l'id du compte annexe
        // n'a pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if (!getIdTypeOperation().equals(CAOPERATIONORDREVERSEMENT) ||
        // !idCompteAnnexe.equals(newIdCompteAnnexe)) {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * @param newIdCompteCourant
     */
    public void setIdCompteCourant(String newIdCompteCourant) {
        // if (isUpdatable()) {
        idCompteCourant = newIdCompteCourant;
        compteCourant = null;
        // } else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * @param string
     */
    public void setIdContrepartie(String string) {
        idContrepartie = string;
    }

    /**
     * @param string
     */
    public void setIdEcheancePlan(String string) {
        idEcheancePlan = string;
    }

    /**
     * Date de création : (20.03.2002 13:17:47)
     *
     * @param newIdExterneCompteCourant
     *            String
     */
    public void setIdExterneCompteCourantEcran(String newIdExterneCompteCourantEcran) {
        idExterneCompteCourantEcran = newIdExterneCompteCourantEcran;
    }

    /**
     * Date de création : (20.03.2002 13:07:42)
     *
     * @param newIdExterneRoleEcran
     *            String
     */
    public void setIdExterneRoleEcran(String newIdExterneRoleEcran) {
        idExterneRoleEcran = newIdExterneRoleEcran;
    }

    /**
     * @param string
     */
    public void setIdExterneSectionAuxEcran(String s) {
        idExterneSectionAuxEcran = s;
    }

    /**
     * Date de création : (20.03.2002 13:12:54)
     *
     * @param newIdExterneSectionEcran
     *            String
     */
    public void setIdExterneSectionEcran(String newIdExterneSectionEcran) {
        idExterneSectionEcran = newIdExterneSectionEcran;
    }

    /**
     * @param newIdJournal
     */
    public void setIdJournal(String newIdJournal) {
        // if (isUpdatable()) {
        idJournal = newIdJournal;
        journal = null;
        // } else {
        // // S'il s'agit d'un ordre de versement et que l'id du journal n'a pas
        // changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if (!getIdTypeOperation().equals(CAOPERATIONORDREVERSEMENT) ||
        // !idJournal.equals(newIdJournal)) {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * @param newIdLog
     */
    public void setIdLog(String newIdLog) {
        log = null;
        idLog = newIdLog;
    }

    /**
     * Setter
     */
    public void setIdOperation(String newIdOperation) {
        idOperation = newIdOperation;
    }

    /**
     * @param string
     */
    public void setIdOrdre(String string) {
        idOrdre = string;
    }

    /**
     * Date de création : (20.03.2002 13:11:54)
     *
     * @param newIdRoleEcran
     *            String
     */
    public void setIdRoleEcran(String newIdRoleEcran) {
        idRoleEcran = newIdRoleEcran;
    }

    /**
     * Date de création : (18.01.2002 11:14:01)
     *
     * @param newIdSection
     *            String
     */
    @Override
    public void setIdSection(String newIdSection) {
        // if (isUpdatable()) {
        idSection = newIdSection;
        section = null;
        // } else {
        // // S'il s'agit d'un ordre de versement et que l'id de la section n'a
        // pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if (!getIdTypeOperation().equals(CAOPERATIONORDREVERSEMENT) ||
        // !idSection.equals(newIdSection)) {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * @param string
     */
    public void setIdSectionAux(String string) {
        idSectionAux = string;
    }

    @Override
    public void setIdSectionCompensation(String idSectionCompensation) {
        this.idSectionCompensation = idSectionCompensation;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.osiris.api.APIOperation#setIdTypeOperation(java.lang.String)
     */
    @Override
    public void setIdTypeOperation(String newIdTypeOperation) {
        idTypeOperation = newIdTypeOperation;
        typeOperation = null;
    }

    /**
     * Date de création : (20.03.2002 13:13:46)
     *
     * @param newIdTypeSectionEcran
     *            String
     */
    public void setIdTypeSectionEcran(String newIdTypeSectionEcran) {
        idTypeSectionEcran = newIdTypeSectionEcran;
    }

    public void setJournal(CAJournal journal) {
        this.journal = journal;
    }

    /**
     * @param s
     */
    public void setLibelle(String s) {
        if ((s != null) && (s.length() > 40)) {
            libelle = s.substring(0, 40);
        } else {
            libelle = s;
        }
    }

    public void setMapValeurUtilisateur(HashMap<String, String> newMapValeurUtilisateur) {
        mapValeurUtilisateur = newMapValeurUtilisateur;
    }

    /**
     * @param string
     */
    public void setMasse(String string) {
        masse = string;
    }

    /**
     * Date de création : (18.02.2002 13:52:56)
     *
     * @param newMemoryLog
     *            globaz.framework.util.FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memLog = newMemoryLog;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * Date de création : (24.04.2002 10:51:55)
     *
     * @param newNewCompteAnnexe
     *            boolean
     */
    public void setNewCompteAnnexe(boolean newNewCompteAnnexe) {
        newCompteAnnexe = newNewCompteAnnexe;
    }

    /**
     * Date de création : (18.04.2002 10:49:56)
     *
     * @param newNewSection
     *            boolean
     */
    public void setNewSection(boolean newNewSection) {
        newSection = newNewSection;
    }

    /**
     * @param string
     */
    public void setNoEcritureCollective(String string) {
        noEcritureCollective = string;
    }

    /**
     * Date de création : (28.03.2002 10:03:45)
     *
     * @param newNomEcran
     *            String
     */
    public void setNomEcran(String newNomEcran) {
        nomEcran = newNomEcran;
    }

    /**
     * @param string
     */
    public void setPiece(String string) {
        piece = string;
    }

    /**
     * @param provenancePmt
     *            the provenancePmt to set
     */
    public void setProvenancePmt(String provenancePmt) {
        this.provenancePmt = provenancePmt;
    }

    /**
     * Date de création : (20.02.2002 15:37:53)
     *
     * @param newQuittanceLogEcran
     *            Boolean
     */
    public void setQuittanceLogEcran(Boolean newQuittanceLogEcran) {
        quittanceLogEcran = newQuittanceLogEcran;
    }

    /**
     * Date de création : (25.03.2002 10:52:35)
     *
     * @param newRechercheCompteAnnexeEcran
     *            String
     */
    public void setRechercheCompteAnnexeEcran(String newRechercheCompteAnnexeEcran) {
        try {
            rechercheCompteAnnexeEcran = Boolean.valueOf(newRechercheCompteAnnexeEcran);
        } catch (Exception ex) {
            rechercheCompteAnnexeEcran = new Boolean(false);
        }
    }

    /**
     * Date de création : (25.03.2002 10:52:35)
     *
     * @param newRechercheSectionEcran
     *            String
     */
    public void setRechercheCompteCourantEcran(String newRechercheCompteCourantEcran) {
        try {
            rechercheCompteCourantEcran = Boolean.valueOf(newRechercheCompteCourantEcran);
        } catch (Exception ex) {
            rechercheCompteCourantEcran = new Boolean(false);
        }
    }

    /**
     * Date de création : (25.03.2002 10:52:35)
     *
     * @param newRechercheSectionEcran
     *            String
     */
    public void setRechercheSectionEcran(String newRechercheSectionEcran) {
        try {
            rechercheSectionEcran = Boolean.valueOf(newRechercheSectionEcran);
        } catch (Exception ex) {
            rechercheSectionEcran = new Boolean(false);
        }
    }

    /**
     * Date de création : (19.02.2002 10:06:50)
     *
     * @param newSaisieEcran
     *            String
     */
    public void setSaisieEcran(String newSaisieEcran) {
        try {
            saisieEcran = Boolean.valueOf(newSaisieEcran);
        } catch (Exception ex) {
            saisieEcran = new Boolean(false);
        }
    }

    public void setSection(CASection section) {
        this.section = section;
    }

    @Override
    public void setSectionCompensationDeSur(String sectionCompensationDeSur) {
        this.sectionCompensationDeSur = sectionCompensationDeSur;
    }

    /**
     * @param string
     */
    public void setTaux(String string) {
        taux = string;
    }

    /**
     * Pour savoir si cette opération à généré un report sur la section.
     *
     * @param taxeEstReporte
     *            the taxeEstReporte to set
     */
    public void setTaxeEstReporte(Boolean taxeEstReporte) {
        this.taxeEstReporte = taxeEstReporte;
    }

    /**
     * Date de création : (28.03.2002 10:06:39)
     *
     * @param newValeurUtilisateur
     *            Vector
     */
    public void setValeurUtilisateur(Vector newValeurUtilisateur) {
        valeurUtilisateur = newValeurUtilisateur;
    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            CACompteAnnexe ca = getCompteAnnexe();
            String s = "[" + getIdOperation() + ", " + getIdTypeOperation() + "] " + this.getDescription();
            if (ca != null) {
                s = s + "; " + ca.toString();
            }
            return s;
        } catch (Exception e) {
            return super.toString();
        }
    }

    /**
     * Mise à jour du compte annexe
     *
     * @param transaction
     * @param compteAnnexe
     * @throws Exception
     */
    private void updateCompteAnnexe(BTransaction transaction, CACompteAnnexe compteAnnexe) throws Exception {
        if (!transaction.hasErrors()) {
            compteAnnexe.setSession(getSession());
            compteAnnexe.update(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("5017"));
                throw new Exception(getSession().getLabel("5017"));
            }
        }
    }

    /**
     * Mise à jour de la section
     *
     * @param transaction
     * @param sec
     * @throws Exception
     */
    private void updateSection(BTransaction transaction, CASection sec) throws Exception {
        if (!transaction.hasErrors()) {
            if (sec != null) {
                sec.setSession(getSession());
                sec.update(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("5018"));
                    throw new Exception(getSession().getLabel("5018"));
                }
            }
        }
    }

    public void useOptimisation(boolean useOptimisation) {
        this.useOptimisation = useOptimisation;
    }

    /**
     * @param transaction
     */
    public void valider(BTransaction transaction) {
        _valider(transaction);
    }

    public String getModeTraitementBulletinNeutreParDefaut() {
        return modeTraitementBulletinNeutreParDefaut;
    }

    public void setModeTraitementBulletinNeutreParDefaut(String modeTraitementBulletinNeutreParDefaut) {
        this.modeTraitementBulletinNeutreParDefaut = modeTraitementBulletinNeutreParDefaut;
    }
}
