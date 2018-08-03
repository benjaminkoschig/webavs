package globaz.naos.db.suiviCaisseAffiliation;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.api.helper.IAFSuiviCaisseAffiliationHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.suivi.AFSuiviAttestIP;
import globaz.naos.suivi.AFSuiviLAA;
import globaz.naos.suivi.AFSuiviLPP;
import globaz.naos.suivi.AFSuiviLPPAnnuel;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class AFSuiviCaisseAffiliation extends BEntity {

    private static final long serialVersionUID = 1L;

    private TIAdministrationViewBean _administration = null;
    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;
    private Boolean accessoire = Boolean.FALSE;
    // Primary and Foreign Key
    private java.lang.String affiliationId = new String();
    private java.lang.Boolean attestationIp = new Boolean(false);
    private java.lang.String categorieSalarie = new String();
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    // Fields
    private java.lang.String genreCaisse = new String();

    private java.lang.String idTiersCaisse = new String();
    private java.lang.String motif = new String();
    private java.lang.String motifFin = new String();
    private java.lang.String numeroAffileCaisse = new String();;

    // DB
    // Primary Key
    private java.lang.String suiviCaisseId = new String();

    /**
     * Constructeur d'AFSuiviCaisseAffiliation.
     */
    public AFSuiviCaisseAffiliation() {
        super();
        setMethodsToLoad(IAFSuiviCaisseAffiliationHelper.METHODS_TO_LOAD);
    }

    public static List<AFSuiviCaisseAffiliation> listAllCaisse(String idAffiliation, BSession session) {
        List<AFSuiviCaisseAffiliation> suiviCaisseList = new ArrayList<AFSuiviCaisseAffiliation>();
        try {
            if (!JadeStringUtil.isIntegerEmpty(idAffiliation)) {
                AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
                manager.setSession(session);
                manager.setWantAllCaisse(true);
                manager.setForAffiliationId(idAffiliation);
                manager.find();
                String lastGenre = null;

                for (int i = 0; i < manager.size(); i++) {
                    AFSuiviCaisseAffiliation suiviCaisse = (AFSuiviCaisseAffiliation) manager.get(i);

                    if ((lastGenre == null) || !lastGenre.equals(suiviCaisse.getGenreCaisse())
                            || JadeStringUtil.isIntegerEmpty(suiviCaisse.getDateFin())) {
                        suiviCaisseList.add(suiviCaisse);
                        lastGenre = suiviCaisse.getGenreCaisse();
                    }
                }
            }
        } catch (Exception e) {
            return suiviCaisseList;
        }
        return suiviCaisseList;
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Incrémente de +1 le numéro
        setSuiviCaisseId(this._incCounter(transaction, "0"));

        BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
        BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());

        if (getGenreCaisse().equals(CodeSystem.GENRE_CAISSE_LAA)) {

            AFSuiviLAA suiviLaa = new AFSuiviLAA();
            LUJournalViewBean journal = suiviLaa.isAlreadySent(getAffiliation());
            if (journal != null) {
                LEJournalHandler handler = new LEJournalHandler();
                handler.genererJournalisationReception(journal.getIdJournalisation(), JACalendar.todayJJsMMsAAAA(),
                        sessionLeo, transaction);
            }

        } else if (getGenreCaisse().equals(CodeSystem.GENRE_CAISSE_LPP)) {

            AFSuiviLPP suiviLpp = new AFSuiviLPP();
            LUJournalViewBean journal = suiviLpp.isAlreadySent(getAffiliation());
            if ((journal != null) && JadeStringUtil.isBlank(journal.getDateReception())) {
                LEJournalHandler handler = new LEJournalHandler();
                handler.genererJournalisationReception(journal.getIdJournalisation(), JACalendar.todayJJsMMsAAAA(),
                        sessionLeo, transaction);
            }
            
            // Traitement pour Suivi Annuel LPP
            AFSuiviLPPAnnuel suiviLppAnnuel = new AFSuiviLPPAnnuel();
            LUJournalViewBean journalAnnuel = suiviLppAnnuel.isAlreadySent(getAffiliation());
            if ((journalAnnuel != null) && JadeStringUtil.isBlank(journalAnnuel.getDateReception())) {
                LEJournalHandler handler = new LEJournalHandler();
                handler.genererJournalisationReception(journalAnnuel.getIdJournalisation(), JACalendar.todayJJsMMsAAAA(),
                        sessionLeo, transaction);
            }

            if ((!getAttestationIp().booleanValue()) && JadeStringUtil.isEmpty(getMotif())) {

                AFSuiviAttestIP attestation = new AFSuiviAttestIP();
                if (attestation.isAffiliationConcerne(getAffiliation())) {
                    attestation.setIdDestinataire(getIdTiersCaisse());
                    attestation.genererControle(getAffiliation());
                }
            }
        }
    }

    /**
     * Effectue des traitements avant une suppression dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
        BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());

        LEJournalHandler handler = new LEJournalHandler();
        if (getGenreCaisse().equals(CodeSystem.GENRE_CAISSE_LAA)) {

            AFSuiviLAA suiviLaa = new AFSuiviLAA();
            LUJournalViewBean journal = suiviLaa.isAlreadySent(getAffiliation());
            if (journal != null) {
                handler.annulerReceptionFormule(journal.getIdJournalisation(), sessionLeo);
            }

        } else if (getGenreCaisse().equals(CodeSystem.GENRE_CAISSE_LPP)) {

            AFSuiviAttestIP suiviIp = new AFSuiviAttestIP();
            LUJournalViewBean journal = suiviIp.isAlreadySent(getAffiliation());
            if (journal != null) {
                handler.annulerTousJournaux(journal.getIdJournalisation(), sessionLeo, transaction);
            }

            AFSuiviLPP suiviLpp = new AFSuiviLPP();
            journal = suiviLpp.isAlreadySent(getAffiliation());
            if (journal != null) {
                handler.annulerReceptionFormule(journal.getIdJournalisation(), sessionLeo);
            }
            
            // Traitement pour Suivi Annuel LPP
            AFSuiviLPPAnnuel suiviLppAnnuel = new AFSuiviLPPAnnuel();
            journal = suiviLppAnnuel.isAlreadySent(getAffiliation());
            if (journal != null) {
                handler.annulerReceptionFormule(journal.getIdJournalisation(), sessionLeo);
            }
        }
    }

    /**
     * Effectue des traitements avant une modification dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        if (getGenreCaisse().equals(CodeSystem.GENRE_CAISSE_LPP)) {

            BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
            BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());

            AFSuiviAttestIP suiviIp = new AFSuiviAttestIP();
            LUJournalViewBean journal = suiviIp.isAlreadySent(getAffiliation());

            // Pas recu d'attestation IP
            if (!getAttestationIp().booleanValue()) {

                if (journal == null && !isNonSoumis()) {
                    AFSuiviAttestIP attestation = new AFSuiviAttestIP();
                    if (attestation.isAffiliationConcerne(getAffiliation())) {
                        attestation.setIdDestinataire(getIdTiersCaisse());
                        attestation.genererControle(getAffiliation());
                    }
                }

                // Reçu l'attestation IP
            } else {
                if ((journal != null) && JadeStringUtil.isBlank(journal.getDateReception())) {
                    LEJournalHandler handler = new LEJournalHandler();
                    handler.genererJournalisationReception(journal.getIdJournalisation(), JACalendar.todayJJsMMsAAAA(),
                            sessionLeo, transaction);
                }
            }
        }
    }

    private boolean isNonSoumis() {
        return !JadeStringUtil.isEmpty(getMotif());
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFSUAFP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        suiviCaisseId = statement.dbReadNumeric("MYISUA");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        idTiersCaisse = statement.dbReadNumeric("HTITIE");
        genreCaisse = statement.dbReadNumeric("MYTGEN");
        dateDebut = statement.dbReadDateAMJ("MYDDEB");
        dateFin = statement.dbReadDateAMJ("MYDFIN");
        motif = statement.dbReadNumeric("MYTMOT");
        attestationIp = statement.dbReadBoolean("MYBAIP");
        categorieSalarie = statement.dbReadNumeric("MYTCAT");
        numeroAffileCaisse = statement.dbReadString("MYTNAF");
        motifFin = statement.dbReadNumeric("MYTMFI");
        // inforom 459
        accessoire = statement.dbReadBoolean("MYBACC");

    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        boolean validationOK = true;
        if (JadeStringUtil.isIntegerEmpty(getMotif())) {
            validationOK &= _propertyMandatory(statement.getTransaction(), getIdTiersCaisse(),
                    getSession().getLabel("1870"));
        } else {
            if (!JadeStringUtil.isBlankOrZero(getIdTiersCaisse())) {
                _addError(statement.getTransaction(), getSession().getLabel("1890"));
            }
        }
        // *********************************************************
        // Test: - Date de Début
        // - Date de Fin
        // *********************************************************
        validationOK = checkDateDebutEtFin(statement, validationOK);

        if (validationOK
                && ((CodeSystem.GENRE_CAISSE_AF.equals(getGenreCaisse()) && !getAccessoire()) || CodeSystem.GENRE_CAISSE_AVS
                        .equals(getGenreCaisse()))) {
            // Test 1.14.0 RC2 -> ré activer le test si caisse principale
            // vérifier qu'il n'y ait pas de cotisations AVS ou AF actives pour cette période
            AFCotisationManager cotisMgr = new AFCotisationManager();
            if (JadeStringUtil.isIntegerEmpty(getDateFin())) {
                // pas de date de fin, recherche si existance d'assurances dès
                // la date de début
                cotisMgr.setForDate(getDateDebut());
            }
            if (JadeStringUtil.isIntegerEmpty(getDateDebut())) {
                // pas de date de début, recherche si existance d'assurances à
                // la date de fin
                cotisMgr.setForDate(getDateFin());
            }
            if (!JadeStringUtil.isEmpty(getDateDebut()) && !JadeStringUtil.isEmpty(getDateFin())) {
                cotisMgr.setForPeriode(getDateDebut(), getDateFin());
            }
            cotisMgr.setForAffiliationId(getAffiliationId());
            cotisMgr.setSession(getSession());

            if (CodeSystem.GENRE_CAISSE_AF.equals(getGenreCaisse())) {
                cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);

                if (cotisMgr.getCount() > 0) {
                    _addError(statement.getTransaction(), getSession().getLabel("NAOS_SUIVI_INTERDIT_COT_AF"));
                }
            }

            if (CodeSystem.GENRE_CAISSE_AVS.equals(getGenreCaisse())) {
                cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);

                if (cotisMgr.getCount() > 0) {
                    _addError(statement.getTransaction(), getSession().getLabel("NAOS_SUIVI_INTERDIT_COT_AVS"));
                }
            }
        }
        // Inforom 459
        if (validationOK) {
            checkValiditeSuivi(statement.getTransaction());
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MYISUA", this._dbWriteNumeric(statement.getTransaction(), getSuiviCaisseId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MYISUA",
                this._dbWriteNumeric(statement.getTransaction(), getSuiviCaisseId(), "suiviCaisseId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("HTITIE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersCaisse(), "idTiersCaisse"));
        statement.writeField("MYTGEN",
                this._dbWriteNumeric(statement.getTransaction(), getGenreCaisse(), "genreCaisse"));
        statement.writeField("MYDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MYDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("MYTMOT", this._dbWriteNumeric(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("MYBAIP",
                this._dbWriteBoolean(statement.getTransaction(), getAttestationIp(), "attestationIp"));
        statement.writeField("MYTCAT",
                this._dbWriteNumeric(statement.getTransaction(), getCategorieSalarie(), "categorieSalarie"));
        statement.writeField("MYTNAF",
                this._dbWriteString(statement.getTransaction(), getNumeroAffileCaisse(), "numeroAffileCaisse"));
        statement.writeField("MYTMFI", this._dbWriteNumeric(statement.getTransaction(), getMotifFin(), "motifFin"));
        statement.writeField("MYBACC", this._dbWriteBoolean(statement.getTransaction(), getAccessoire(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "accessoire"));
    }

    /**
     * Test de la validatié de la date de début et de fin selon les périodes déjà existante
     * 
     * @param transaction
     * @param result
     * @param suiviCaisse
     * @return
     * @throws Exception
     */

    public boolean checkChevauchementDate(BTransaction transaction, boolean result, AFSuiviCaisseAffiliation suiviCaisse)
            throws Exception {
        String suiviExistantDateFin = suiviCaisse.getDateFin();
        if (JadeStringUtil.isIntegerEmpty(suiviCaisse.getDateFin())) {
            suiviExistantDateFin = "31.12.9999";
        }
        // Contrôle si chevauchement de la date de début
        if (JadeStringUtil.isIntegerEmpty(getDateDebut())) {
            if (JadeStringUtil.isIntegerEmpty(suiviCaisse.getDateDebut())) {
                _addError(transaction, getSession().getLabel("1701"));
                result = false;
            }
        } else if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(), suiviCaisse.getDateDebut(),
                suiviExistantDateFin, getDateDebut())) {
            if (suiviExistantDateFin.equalsIgnoreCase("31.12.9999")) {
                _addError(transaction, getSession().getLabel("1701"));
            } else {
                _addError(
                        transaction,
                        FWMessageFormat.format(getSession().getLabel("1730"), getDateDebut(), getDateFin(),
                                suiviCaisse.getDateDebut(), suiviCaisse.getDateFin()));
            }
            result = false;
        }
        // Contrôle si chevauchement de la date de fin
        if (JadeStringUtil.isIntegerEmpty(getDateFin())) {
            // Test si chevauchement
            if (JadeStringUtil.isIntegerEmpty(suiviCaisse.getDateFin())) {
                _addError(transaction, getSession().getLabel("1700"));
                result = false;
            }
        } else if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(), suiviCaisse.getDateDebut(),
                suiviExistantDateFin, getDateFin())) {
            if (suiviExistantDateFin.equalsIgnoreCase("31.12.9999")) {
                _addError(transaction, getSession().getLabel("1700"));
            } else {
                _addError(
                        transaction,
                        FWMessageFormat.format(getSession().getLabel("1730"), getDateDebut(), getDateFin(),
                                suiviCaisse.getDateDebut(), suiviCaisse.getDateFin()));
            }
            result = false;
        }
        return result;
    }

    public boolean checkDateDebutEtFin(BStatement statement, boolean validationOK) throws Exception {
        if (validationOK) {
            // Contrôle de la date de début
            if (!JadeStringUtil.isEmpty(getDateDebut())) {
                // Validité Date
                validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getAffiliation().getDateDebut())) {

                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1850"),
                            getDateDebut(), getAffiliation().getDateDebut()));
                    validationOK = false;
                }
            }
            // Contrôle de la date de fin
            if (!JadeStringUtil.isEmpty(getDateFin())) {
                validationOK &= _checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));
                if (validationOK
                        && BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), getAffiliation()
                                .getDateFin()) && (!JadeStringUtil.isBlankOrZero(getAffiliation().getDateFin()))) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1860"),
                            getDateFin(), getAffiliation().getDateFin()));
                    validationOK = false;
                }

            }
            // Erreru si date Fin > Date Debut
            if (!JadeStringUtil.isEmpty(getDateFin()) && !JadeStringUtil.isEmpty(getDateDebut())) {
                // Date Fin > Date Debut
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("90"));
                    validationOK = false;
                }
            }

        }
        return validationOK;
    }

    private boolean checkValiditeSuivi(BTransaction transaction) throws Exception {

        boolean result = true;
        // Manager sur les caisses principales pour le genre saisi
        AFSuiviCaisseAffiliationManager manager = managerSuiviPourAffiliationEtGenreAssurance(transaction);

        if (getAccessoire().equals(Boolean.TRUE)) {
            // Il ne peut y avoir de caisse accessoire pour le genre AVS
            if (CodeSystem.GENRE_CAISSE_AVS.equalsIgnoreCase(getGenreCaisse())) {
                _addError(transaction, getSession().getLabel("1715"));
            } else if (manager.size() == 0) {
                // Caisse principale obligatoire (sauf cas particulier AF)
                if (CodeSystem.GENRE_CAISSE_AF.equals(getGenreCaisse())) {
                    // Pour les AF: vérifier qu'il existe une cotisation active pour la période
                    AFCotisationManager cotisMgr = new AFCotisationManager();
                    if (JadeStringUtil.isIntegerEmpty(getDateFin())) {
                        cotisMgr.setForDate(getDateDebut());
                    }
                    if (JadeStringUtil.isIntegerEmpty(getDateDebut())) {
                        cotisMgr.setForDate(getDateFin());
                    }
                    if (!JadeStringUtil.isEmpty(getDateDebut()) && !JadeStringUtil.isEmpty(getDateFin())) {
                        cotisMgr.setForPeriode(getDateDebut(), getDateFin());
                    }
                    cotisMgr.setForDateDifferente(Boolean.TRUE);
                    cotisMgr.setForAffiliationId(getAffiliationId());
                    cotisMgr.setSession(getSession());
                    cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
                    if (cotisMgr.getCount() == 0) {
                        _addError(transaction, getSession().getLabel("1705"));
                    }
                } else {
                    _addError(transaction, getSession().getLabel("1705"));
                }
            }
        } else {
            // *********************************************************
            // Test Date de Début, Date de Fin
            // *********************************************************
            for (int i = 0; i < manager.size(); i++) {
                AFSuiviCaisseAffiliation suiviCaisse = (AFSuiviCaisseAffiliation) manager.get(i);
                result = checkChevauchementDate(transaction, result, suiviCaisse);
            }
        }
        return result;
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    public Boolean getAccessoire() {
        return accessoire;
    }

    /**
     * Rechercher l'Administration (Tiers) du plan de caisse en fonction de son ID.
     * 
     * @return l'Administration (Tiers)
     */
    public TIAdministrationViewBean getAdministration() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdTiersCaisse())) {
            return null;
        }

        if ((_administration == null) || !_administration.getIdTiers().equals(getIdTiersCaisse())) {

            _administration = new TIAdministrationViewBean();
            _administration.setSession(getSession());
            _administration.setIdTiersAdministration(getIdTiersCaisse());
            try {
                _administration.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _administration = null;
            }
        }
        return _administration;
    }

    /**
     * Rechercher l'Affiliation de l'adhésion en fonction de son ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if ((_affiliation == null) || !_affiliation.getAffiliationId().equals(getAffiliationId())) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.Boolean getAttestationIp() {
        return attestationIp;
    }

    public java.lang.String getCategorieSalarie() {
        return categorieSalarie;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getGenreCaisse() {
        return genreCaisse;
    }

    public java.lang.String getIdTiersCaisse() {
        return idTiersCaisse;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFSuiviCaisseAffiliationManager();
    }

    public java.lang.String getMotif() {
        return motif;
    }

    public java.lang.String getMotifFin() {
        return motifFin;
    }

    public java.lang.String getNumeroAffileCaisse() {
        return numeroAffileCaisse;
    }

    public java.lang.String getSuiviCaisseId() {
        return suiviCaisseId;
    }

    /**
     * Rechercher le tiers de l'affiliation en fonction de son ID.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    /**
     * Sélection les suivis de l'affiliation pour le genre d'assurance saisi
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    public AFSuiviCaisseAffiliationManager managerSuiviPourAffiliationEtGenreAssurance(BTransaction transaction)
            throws Exception {
        AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        manager.setForGenreCaisse(getGenreCaisse());
        manager.setExceptIdSuiviCaisse(getSuiviCaisseId());
        manager.setExceptDateFinEgaleDateDebut(true);
        manager.find(transaction);
        return manager;
    }

    public void setAccessoire(Boolean accessoire) {
        this.accessoire = accessoire;
    }

    // ***********************************************
    // Setter
    // ***********************************************

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setAttestationIp(java.lang.Boolean boolean1) {
        attestationIp = boolean1;
    }

    public void setCategorieSalarie(java.lang.String string) {
        categorieSalarie = string;
    }

    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    public void setGenreCaisse(java.lang.String string) {
        genreCaisse = string;
    }

    public void setIdTiersCaisse(java.lang.String string) {
        idTiersCaisse = string;
    }

    public void setMotif(java.lang.String string) {
        motif = string;
    }

    public void setMotifFin(java.lang.String string) {
        motifFin = string;
    }

    public void setNumeroAffileCaisse(java.lang.String string) {
        numeroAffileCaisse = string;
    }

    public void setSuiviCaisseId(java.lang.String string) {
        suiviCaisseId = string;
    }
}
