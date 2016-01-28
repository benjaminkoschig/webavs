package globaz.campus.db.annonces;

import globaz.campus.db.etudiants.GEEtudiants;
import globaz.framework.controller.FWController;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;

public class GEAnnonces extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // CodeDoctorant
    public final static String CS_DOCTORANT = "934001";
    // EtatAnnonce
    public final static String CS_ETAT_A_TRAITER = "932001";
    public final static String CS_ETAT_COMPTABILISE = "932005";
    public final static String CS_ETAT_ERREUR = "932002";
    public final static String CS_ETAT_ERREUR_ARCHIVEE = "932003";
    public final static String CS_ETAT_EXEMPTE = "932006";
    public final static String CS_ETAT_IMPAYEE = "932007";
    public final static String CS_ETAT_VALIDE = "932004";
    public final static String CS_POSTGRADE = "934002";
    public static final String FIELDNAME_ADRESSE_ETUDE = "YCADRE";
    public static final String FIELDNAME_ADRESSE_LEGALE = "YCADRL";
    public static final String FIELDNAME_CODE_DOCTORANT = "YCTDOC";
    public static final String FIELDNAME_COTISATION = "YCMCOT";
    public static final String FIELDNAME_DATE_NAISSANCE = "YCDNAI";
    public static final String FIELDNAME_ETAT_ANNONCE = "YCTETA ";
    public static final String FIELDNAME_ETAT_CIVIL = "YCTETC";
    public static final String FIELDNAME_ID_ANNONCE = "YCIANN";
    public static final String FIELDNAME_ID_ANNONCE_PARENT = "YCIANP";
    public static final String FIELDNAME_ID_DECISION = "YCIDEC";
    public static final String FIELDNAME_ID_ETUDIANT = "YBIETU";
    public static final String FIELDNAME_ID_LOG = "BCID";
    public static final String FIELDNAME_ID_LOT = "YAILOT ";
    public static final String FIELDNAME_IS_IMPUTATION = "YCBIMP";
    public static final String FIELDNAME_IS_TIERS_FORCE = "YCISTF";
    public static final String FIELDNAME_LOCALITE_ETUDE = "YCLOCE";
    public static final String FIELDNAME_LOCALITE_LEGAL = "YCLOCL";
    public static final String FIELDNAME_MONTANT_CI = "YCMOCI";
    public static final String FIELDNAME_NOM = "YCNOME";
    public static final String FIELDNAME_NPA_ETUDE = "YCNPOE";
    public static final String FIELDNAME_NPA_LEGAL = "YCNPOL";
    public static final String FIELDNAME_NUM_AVS = "YCNAVS";
    public static final String FIELDNAME_NUM_IMMATRICULATION_TRANSMIS = "YCNIMT";
    public static final String FIELDNAME_NUM_SEQUENCE = "YCNSEQ";
    public static final String FIELDNAME_PRENOM = "YCPREE";
    public static final String FIELDNAME_RESERVE_1 = "YCRES1";

    // Codes system

    public static final String FIELDNAME_RESERVE_2 = "YCRES2";
    public static final String FIELDNAME_RESERVE_3 = "YCRES3";
    public static final String FIELDNAME_RESERVE_4 = "YCRES4";
    public static final String FIELDNAME_RUE_ETUDE = "YCRUEE";
    public static final String FIELDNAME_RUE_LEGAL = "YCRUEL";
    public static final String FIELDNAME_SEXE = "YCTSEX";
    public static final String FIELDNAME_SUFFIXE_POSTAL_ETUDE = "YCSPOE";

    public static final String FIELDNAME_SUFFIXE_POSTAL_LEGAL = "YCSPOL";
    public static final String TABLE_NAME_ANNONCE = "GEANNOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String adresseEtude = "";
    private String adresseLegale = "";
    private GEAnnonces annonceParent = null;
    private String cotisation = "";
    private String csCodeDoctorant = "";
    private String csEtatAnnonce = "";
    private String csEtatCivil = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String idAnnonce = "";
    private String idAnnonceParent = "";
    private String idDecision = "";
    private String idEtudiant = "";
    private String idLog = "";
    private String idLot = null;
    private Boolean isImputation = null;
    private Boolean isTiersForce = new Boolean(false);
    private String localiteEtude = "";
    private String localiteLegal = "";
    private String messagesErreurs = "";
    private String montantCI = "";
    private String nom = "";
    private String npaEtude = "";
    private String npaLegal = "";
    private String numAvs = "";
    private String numImmatriculationTransmis = "";
    private String numSequence = "";
    private String prenom = "";
    private String reserve1 = "";
    private String reserve2 = "";
    private String reserve3 = "";
    private String reserve4 = "";
    private String rueEtude = "";
    private String rueLegal = "";
    private String suffixePostalEtude = "";

    private String suffixePostalLegal = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (!getIsImputation().booleanValue()) {
            // Mettre à jour l'imputation si l'annonce en a une
            GEAnnoncesManager imputationMng = new GEAnnoncesManager();
            imputationMng.setSession(getSession());
            imputationMng.setForIdAnnoncesParent(getIdAnnonce());
            imputationMng.find(transaction);
            for (int i = 0; i < imputationMng.size(); i++) {
                GEAnnonces imputation = (GEAnnonces) imputationMng.getEntity(i);
                imputation.update(transaction);
            }
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAnnonce(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // Impossible de supprimer une annonce validée ou comptabilisée
        if (CS_ETAT_VALIDE.equals(getCsEtatAnnonce()) || CS_ETAT_COMPTABILISE.equals(getCsEtatAnnonce())) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_ANNONCE_IMPOSSIBLE"));
        }
        // Contrôler qu'il n'y ait pas d'imputation avant de supprimer l'annonce
        // parent
        if (!getIsImputation().booleanValue()) {
            GEAnnoncesManager imputationMng = new GEAnnoncesManager();
            imputationMng.setSession(getSession());
            imputationMng.setForIdAnnoncesParent(getIdAnnonce());
            if (imputationMng.getCount(transaction) > 0) {
                _addError(transaction, getSession().getLabel("SUPPRESSION_ANNONCE_IMPUTATION_IMPOSSIBLE"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Impossible de modifier une annonce validée ou comptabilisée
        if (CS_ETAT_VALIDE.equals(getCsEtatAnnonce()) || CS_ETAT_COMPTABILISE.equals(getCsEtatAnnonce())) {
            _addError(transaction, getSession().getLabel("MODIFICATION_ANNONCE_IMPOSSIBLE"));
        }
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_ANNONCE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric(FIELDNAME_ID_ANNONCE);
        idEtudiant = statement.dbReadNumeric(FIELDNAME_ID_ETUDIANT);
        idLot = statement.dbReadNumeric(FIELDNAME_ID_LOT);
        csEtatAnnonce = statement.dbReadNumeric(FIELDNAME_ETAT_ANNONCE);
        idLog = statement.dbReadNumeric(FIELDNAME_ID_LOG);
        numSequence = statement.dbReadNumeric(FIELDNAME_NUM_SEQUENCE);
        numAvs = statement.dbReadString(FIELDNAME_NUM_AVS);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(FIELDNAME_SEXE);
        csEtatCivil = statement.dbReadNumeric(FIELDNAME_ETAT_CIVIL);
        adresseEtude = statement.dbReadString(FIELDNAME_ADRESSE_ETUDE);
        rueEtude = statement.dbReadString(FIELDNAME_RUE_ETUDE);
        npaEtude = statement.dbReadString(FIELDNAME_NPA_ETUDE);
        localiteEtude = statement.dbReadString(FIELDNAME_LOCALITE_ETUDE);
        suffixePostalEtude = statement.dbReadString(FIELDNAME_SUFFIXE_POSTAL_ETUDE);
        adresseLegale = statement.dbReadString(FIELDNAME_ADRESSE_LEGALE);
        rueLegal = statement.dbReadString(FIELDNAME_RUE_LEGAL);
        npaLegal = statement.dbReadString(FIELDNAME_NPA_LEGAL);
        localiteLegal = statement.dbReadString(FIELDNAME_LOCALITE_LEGAL);
        suffixePostalLegal = statement.dbReadString(FIELDNAME_SUFFIXE_POSTAL_LEGAL);
        numImmatriculationTransmis = statement.dbReadString(FIELDNAME_NUM_IMMATRICULATION_TRANSMIS);
        reserve1 = statement.dbReadString(FIELDNAME_RESERVE_1);
        csCodeDoctorant = statement.dbReadNumeric(FIELDNAME_CODE_DOCTORANT);
        reserve2 = statement.dbReadString(FIELDNAME_RESERVE_2);
        reserve3 = statement.dbReadString(FIELDNAME_RESERVE_3);
        reserve4 = statement.dbReadString(FIELDNAME_RESERVE_4);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        cotisation = statement.dbReadNumeric(FIELDNAME_COTISATION);
        montantCI = statement.dbReadNumeric(FIELDNAME_MONTANT_CI);
        idAnnonceParent = statement.dbReadNumeric(FIELDNAME_ID_ANNONCE_PARENT);
        isImputation = statement.dbReadBoolean(FIELDNAME_IS_IMPUTATION);
        isTiersForce = statement.dbReadBoolean(FIELDNAME_IS_TIERS_FORCE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        setMessagesErreurs("");
        if (getIsImputation().booleanValue()) {
            // L'idAnnonceParent est obligatoire
            if (JadeStringUtil.isBlankOrZero(getIdAnnonceParent())) {
                messagesErreurs = messagesErreurs + getSession().getLabel("ANNONCE_PARENT_OBLIGATOIRE") + "\n";
            }
            // Copie des données de l'annonce parent
            copieAnnonceParent(statement.getTransaction());
            if (getAnnonceParent(statement.getTransaction()) != null
                    && CS_ETAT_ERREUR.equals(getAnnonceParent(statement.getTransaction()).getCsEtatAnnonce())) {
                // Si l'annonce parent est en erreur mettre l'imputation en
                // erreur
                messagesErreurs = messagesErreurs + getSession().getLabel("ANNONCE_PARENT_EN_ERREUR") + "\n";
            } else if (GEAnnonces.CS_ETAT_ERREUR_ARCHIVEE.equals(annonceParent.getCsEtatAnnonce())) {
                // Si l'annonce parent est en erreur archivée, mettre
                // l'imputation en erreur archivée
                setCsEtatAnnonce(CS_ETAT_ERREUR_ARCHIVEE);
            }
            if (CS_DOCTORANT.equals(getCsCodeDoctorant()) || CS_POSTGRADE.equals(getCsCodeDoctorant())) {
                // Impossible de créer une imputation pour un doctorant ou un
                // postgrade
                messagesErreurs = messagesErreurs + getSession().getLabel("ANNONCE_PARENT_DOCTORANT") + "\n";
            }
            // Soit le montant CI, soit la cotisation est obligatoire
            if (JadeStringUtil.isBlankOrZero(getMontantCI()) && JadeStringUtil.isBlankOrZero(getCotisation())) {
                messagesErreurs = messagesErreurs + getSession().getLabel("MONTANTS_OBLIGATOIRES") + "\n";
            }
        }
        // idLot obligatoire
        if (JadeStringUtil.isBlankOrZero(getIdLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("ID_LOT_OBLIGATOIRE"));
        }
        // etatAnnonce obligatoire
        if (JadeStringUtil.isBlankOrZero(getCsEtatAnnonce())) {
            _addError(statement.getTransaction(), getSession().getLabel("ETAT_ANNONCE_OBLIGATOIRE"));
        }
        // contrôle du format du numAVS
        /*
         * if (!JadeStringUtil.isBlank(getNumAvs())){ try { JAUtil.checkAvs(getNumAvs()); NSUtil.nssCheckDigit(nss) }
         * catch (Exception e) { setNumAvs(""); } }
         */
        // nom obligatoire
        if (JadeStringUtil.isBlank(getNom())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("NOM_OBLIGATOIRE") + "\n";
        }
        // prenom obligatoire
        if (JadeStringUtil.isBlank(getPrenom())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("PRENOM_OBLIGATOIRE") + "\n";
        }
        // contrôle de la date de naissance
        if (JadeStringUtil.isBlankOrZero(getDateNaissance())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("DATE_NAISSANCE_OBLIGATOIRE") + "\n";
        } else {
            // Contrôle du format de date de naissance
            try {
                JADate date = new JADate(getDateNaissance());
                BSessionUtil.checkDateGregorian(getSession(), date);
            } catch (Exception ex) {
                messagesErreurs = messagesErreurs + getSession().getLabel("FORMAT_DATE_NAISSANCE_INCORRECT") + "\n";
            }
        }
        // sexe obligatoire
        if (JadeStringUtil.isBlankOrZero(getCsSexe())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("SEXE_OBLIGATOIRE") + "\n";
        }
        // Adresse Légale obligatoire
        if (JadeStringUtil.isBlank(getRueLegal()) && JadeStringUtil.isBlank(getNpaLegal())
                && JadeStringUtil.isBlank(getLocaliteLegal())) {
            // Si tous les champs sont vide, on copie l'adresse de courrier
            setRueLegal(getRueEtude());
            setNpaLegal(getNpaEtude());
            setLocaliteLegal(getLocaliteEtude());
        }
        // Check du Npa pour savoir si c'est un étranger
        // ->Erreur archivée
        if (!JadeStringUtil.isEmpty(getNpaLegal())) {
            if (getNpaLegal().length() >= 4) {
                if (JadeStringUtil.isLetter(getNpaLegal().substring(0, 1))
                        && JadeStringUtil.isLetter(getNpaLegal().substring(1, 2))
                        && getNpaLegal().substring(2, 3).equals("-")
                        && JadeStringUtil.isDigit(getNpaLegal().substring(3, 4))) {
                    messagesErreurs = messagesErreurs + getSession().getLabel("NPA_ETRANGER") + "\n";
                    setCsEtatAnnonce(CS_ETAT_ERREUR_ARCHIVEE);
                }
            }
        }

        // ->Erreur archivée
        if (!JadeStringUtil.isEmpty(getLocaliteLegal())) {
            if (getLocaliteLegal().length() >= 4) {
                if (JadeStringUtil.isLetter(getLocaliteLegal().substring(0, 1))
                        && JadeStringUtil.isLetter(getLocaliteLegal().substring(1, 2))
                        && getLocaliteLegal().substring(2, 3).equals("-")
                        && JadeStringUtil.isDigit(getLocaliteLegal().substring(3, 4))) {
                    messagesErreurs = messagesErreurs + getSession().getLabel("NPA_ETRANGER") + "\n";
                    setCsEtatAnnonce(CS_ETAT_ERREUR_ARCHIVEE);
                }
            }
        }

        // La rue n'est plus obligatoire
        // if(JadeStringUtil.isBlank(getRueLegal())){
        // messagesErreurs = messagesErreurs +
        // getSession().getLabel("RUE_LEGAL_OBLIGATOIRE") + "\n";
        // }
        if (JadeStringUtil.isBlankOrZero(getNpaLegal())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("NPA_LEGAL_OBLIGATOIRE") + "\n";
        }
        if (JadeStringUtil.isBlank(getLocaliteLegal())) {
            messagesErreurs = messagesErreurs + getSession().getLabel("LOCALITE_LEGAL_OBLIGATOIRE") + "\n";
        }
        if (!CS_ETAT_IMPAYEE.equals(getCsEtatAnnonce()) && !CS_ETAT_VALIDE.equals(getCsEtatAnnonce())
                && !CS_ETAT_COMPTABILISE.equals(getCsEtatAnnonce())) {
            creationLogMessages(getMessagesErreurs(), statement.getTransaction());
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ANNONCE, _dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_ANNONCE, _dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
        statement.writeField(FIELDNAME_ID_ETUDIANT,
                _dbWriteNumeric(statement.getTransaction(), idEtudiant, "idEtudiant"));
        statement.writeField(FIELDNAME_ID_LOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(FIELDNAME_ETAT_ANNONCE,
                _dbWriteNumeric(statement.getTransaction(), csEtatAnnonce, "csEtatAnnonce"));
        statement.writeField(FIELDNAME_ID_LOG, _dbWriteNumeric(statement.getTransaction(), idLog, "idLog"));
        statement.writeField(FIELDNAME_NUM_SEQUENCE,
                _dbWriteNumeric(statement.getTransaction(), numSequence, "numSequence"));
        statement.writeField(FIELDNAME_NUM_AVS, _dbWriteString(statement.getTransaction(), numAvs, "numAvs"));
        statement.writeField(FIELDNAME_NOM, _dbWriteString(statement.getTransaction(), nom, "nom"));
        statement.writeField(FIELDNAME_PRENOM, _dbWriteString(statement.getTransaction(), prenom, "prenom"));
        statement.writeField(FIELDNAME_DATE_NAISSANCE,
                _dbWriteDateAMJ(statement.getTransaction(), dateNaissance, "dateNaissance"));
        statement.writeField(FIELDNAME_SEXE, _dbWriteNumeric(statement.getTransaction(), csSexe, "csSexe"));
        statement.writeField(FIELDNAME_ETAT_CIVIL,
                _dbWriteNumeric(statement.getTransaction(), csEtatCivil, "csEtatCivil"));
        statement.writeField(FIELDNAME_ADRESSE_ETUDE,
                _dbWriteString(statement.getTransaction(), adresseEtude, "adresseEtude"));
        statement.writeField(FIELDNAME_RUE_ETUDE, _dbWriteString(statement.getTransaction(), rueEtude, "rueEtude"));
        statement.writeField(FIELDNAME_NPA_ETUDE, _dbWriteString(statement.getTransaction(), npaEtude, "npaEtude"));
        statement.writeField(FIELDNAME_LOCALITE_ETUDE,
                _dbWriteString(statement.getTransaction(), localiteEtude, "localiteEtude"));
        statement.writeField(FIELDNAME_SUFFIXE_POSTAL_ETUDE,
                _dbWriteString(statement.getTransaction(), suffixePostalEtude, "suffixePostalEtude"));
        statement.writeField(FIELDNAME_ADRESSE_LEGALE,
                _dbWriteString(statement.getTransaction(), adresseLegale, "adresseLegale"));
        statement.writeField(FIELDNAME_RUE_LEGAL, _dbWriteString(statement.getTransaction(), rueLegal, "rueLegal"));
        statement.writeField(FIELDNAME_NPA_LEGAL, _dbWriteString(statement.getTransaction(), npaLegal, "npaLegal"));
        statement.writeField(FIELDNAME_LOCALITE_LEGAL,
                _dbWriteString(statement.getTransaction(), localiteLegal, "localiteLegal"));
        statement.writeField(FIELDNAME_SUFFIXE_POSTAL_LEGAL,
                _dbWriteString(statement.getTransaction(), suffixePostalLegal, "suffixePostalLegal"));
        statement.writeField(FIELDNAME_NUM_IMMATRICULATION_TRANSMIS,
                _dbWriteString(statement.getTransaction(), numImmatriculationTransmis, "numImmatriculationTransmis"));
        statement.writeField(FIELDNAME_RESERVE_1, _dbWriteString(statement.getTransaction(), reserve1, "reserve1"));
        statement.writeField(FIELDNAME_CODE_DOCTORANT,
                _dbWriteNumeric(statement.getTransaction(), csCodeDoctorant, "csCodeDoctorant"));
        statement.writeField(FIELDNAME_RESERVE_2, _dbWriteString(statement.getTransaction(), reserve2, "reserve2"));
        statement.writeField(FIELDNAME_RESERVE_3, _dbWriteString(statement.getTransaction(), reserve3, "reserve3"));
        statement.writeField(FIELDNAME_RESERVE_4, _dbWriteString(statement.getTransaction(), reserve4, "reserve4"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_COTISATION,
                _dbWriteNumeric(statement.getTransaction(), cotisation, "cotisation"));
        statement.writeField(FIELDNAME_MONTANT_CI, _dbWriteNumeric(statement.getTransaction(), montantCI, "montantCI"));
        statement.writeField(FIELDNAME_ID_ANNONCE_PARENT,
                _dbWriteNumeric(statement.getTransaction(), idAnnonceParent, "idAnnonceParent"));
        statement.writeField(
                FIELDNAME_IS_IMPUTATION,
                _dbWriteBoolean(statement.getTransaction(), isImputation, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isImputation"));
        statement.writeField(FIELDNAME_IS_TIERS_FORCE,
                _dbWriteBoolean(statement.getTransaction(), isTiersForce, "isTiersForce"));
    }

    private void copieAnnonceParent(BTransaction transaction) throws Exception {
        // Copie des données de l'annonce parent
        if (getAnnonceParent(transaction) != null) {
            setIdEtudiant(annonceParent.getIdEtudiant());
            setIdLot(annonceParent.getIdLot());
            setNumAvs(annonceParent.getNumAvs());
            setNom(annonceParent.getNom());
            setPrenom(annonceParent.getPrenom());
            setDateNaissance(annonceParent.getDateNaissance());
            setCsSexe(annonceParent.getCsSexe());
            setCsEtatCivil(annonceParent.getCsEtatCivil());
            setAdresseEtude(annonceParent.getAdresseEtude());
            setRueEtude(annonceParent.getRueEtude());
            setNpaEtude(annonceParent.getNpaEtude());
            setLocaliteEtude(annonceParent.getLocaliteEtude());
            setSuffixePostalEtude(annonceParent.getSuffixePostalEtude());
            setAdresseLegale(annonceParent.getAdresseLegale());
            setRueLegal(annonceParent.getRueLegal());
            setNpaLegal(annonceParent.getNpaLegal());
            setLocaliteLegal(annonceParent.getLocaliteLegal());
            setSuffixePostalLegal(annonceParent.getSuffixePostalLegal());
            setNumImmatriculationTransmis(annonceParent.getNumImmatriculationTransmis());
            setReserve1(annonceParent.getReserve1());
            setCsCodeDoctorant(annonceParent.getCsCodeDoctorant());
            setReserve2(annonceParent.getReserve2());
            setReserve3(annonceParent.getReserve3());
            setReserve4(annonceParent.getReserve4());
            setIsTiersForce(annonceParent.isTiersForce);
        }

    }

    public void creationLogMessages(String messagesErreurs, BTransaction transaction) throws Exception {
        FWLog log = new FWLog();
        log.setISession(GlobazSystem.getApplication("FRAMEWORK").newSession(getSession()));
        if (!JadeStringUtil.isBlankOrZero(getIdLog())) {
            if (CS_ETAT_ERREUR_ARCHIVEE.equals(getCsEtatAnnonce())) {
                messagesErreurs = "";
            }
            // Suppression des messages du log
            FWMessageManager messagesManager = new FWMessageManager();
            messagesManager.setSession(getSession());
            messagesManager.setForIdLog(getIdLog());
            messagesManager.find(transaction);
            for (int i = 0; i < messagesManager.size(); i++) {
                FWMessage message = (FWMessage) messagesManager.getEntity(i);
                if (CS_ETAT_ERREUR_ARCHIVEE.equals(getCsEtatAnnonce())) {
                    messagesErreurs += message.getComplement() + "\n";
                }
                message.delete(transaction);
            }
            log.setIdLog(getIdLog());
            log.retrieve(transaction);
        } else {
            log.add(transaction);
            setIdLog(log.getIdLog());
        }
        if (!JadeStringUtil.isBlank(messagesErreurs)) {
            StringTokenizer token = new StringTokenizer(messagesErreurs, "\n");
            while (token.hasMoreElements()) {
                log.logMessage(token.nextToken(), FWMessage.FATAL, this.getClass().getName());
            }
            log.update(transaction);
            // Si l'annonce est en erreur archivée, il ne faut pas changer
            // l'état
            if (!CS_ETAT_ERREUR_ARCHIVEE.equals(getCsEtatAnnonce())) {
                setCsEtatAnnonce(CS_ETAT_ERREUR);
            }
        } else {
            suppressionLogMessages(getIdLog(), transaction);
        }
    }

    public String getAdresseEtude() {
        return adresseEtude;
    }

    public String getAdresseLegale() {
        return adresseLegale;
    }

    public GEAnnonces getAnnonceParent(BTransaction transaction) {
        if (isImputation.booleanValue()) {
            if (annonceParent == null) {
                try {
                    GEAnnonces annonce = new GEAnnonces();
                    annonce.setSession(getSession());
                    annonce.setIdAnnonce(getIdAnnonceParent());
                    annonce.retrieve(transaction);
                    if (annonce != null && !annonce.isNew()) {
                        setAnnonceParent(annonce);
                    }
                } catch (Exception e) {
                    setAnnonceParent(null);
                }
            }
        } else {
            setAnnonceParent(null);
        }
        return annonceParent;
    }

    public String getCotisation() {
        return cotisation;
    }

    public String getCsCodeDoctorant() {
        return csCodeDoctorant;
    }

    public String getCsEtatAnnonce() {
        return csEtatAnnonce;
    }

    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdAffiliation(BSession session) {
        String idAffiliation = null;
        try {
            if (!JadeStringUtil.isBlankOrZero(getIdDecision())) {
                CPDecision decision = new CPDecision();
                decision.setSession(getSession());
                decision.setIdDecision(getIdDecision());
                decision.retrieve();
                if (decision != null && !decision.isNew()) {
                    idAffiliation = decision.getIdAffiliation();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return idAffiliation;
    }

    public String getIdAffiliation(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        return getIdAffiliation(bSession);
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdAnnonceParent() {
        return idAnnonceParent;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdEtudiant() {
        return idEtudiant;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdTiers(BSession session) {
        String idTiersEtudiant = null;
        try {
            if (!JadeStringUtil.isBlankOrZero(getIdEtudiant())) {
                GEEtudiants etudiant = new GEEtudiants();
                etudiant.setSession(session);
                etudiant.setIdEtudiant(getIdEtudiant());
                etudiant.retrieve();
                if (etudiant != null && !etudiant.isNew()) {
                    idTiersEtudiant = etudiant.getIdTiersEtudiant();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return idTiersEtudiant;
    }

    public String getIdTiers(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        return getIdTiers(bSession);
    }

    public Boolean getIsImputation() {
        return isImputation;
    }

    public Boolean getIsTiersForce() {
        return isTiersForce;
    }

    public String getLocaliteEtude() {
        return localiteEtude;
    }

    public String getLocaliteLegal() {
        return localiteLegal;
    }

    public String getMessageLog() {
        String msg = "";
        if (!JadeStringUtil.isIntegerEmpty(getIdLog())) {
            FWLog log = new FWLog();
            log.setISession(getSession());
            log.setIdLog(getIdLog());
            try {
                log.retrieve();
            } catch (Exception e) {
                _addError(null, "Problème du chargement du log pour : " + getIdLog());
            }
            Enumeration enumer = log.getMessagesToEnumeration();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; enumer.hasMoreElements(); i++) {
                sb.append(((FWMessage) enumer.nextElement()).getMessageText() + "\n");
            }
            msg = sb.toString();
        }
        return msg;
    }

    public String getMessagesErreurs() {
        return messagesErreurs;
    }

    public String getMontantCI() {
        return montantCI;
    }

    public String getNom() {
        return nom;
    }

    public String getNpaEtude() {
        return npaEtude;
    }

    public String getNpaLegal() {
        return npaLegal;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumImmatriculationTransmis() {
        return numImmatriculationTransmis;
    }

    public String getNumSequence() {
        return numSequence;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getReserve1() {
        return reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public String getRueEtude() {
        return rueEtude;
    }

    public String getRueLegal() {
        return rueLegal;
    }

    public String getSuffixePostalEtude() {
        return suffixePostalEtude;
    }

    public String getSuffixePostalLegal() {
        return suffixePostalLegal;
    }

    public void setAdresseEtude(String adresseEtude) {
        this.adresseEtude = adresseEtude;
    }

    public void setAdresseLegale(String adresseLegale) {
        this.adresseLegale = adresseLegale;
    }

    public void setAnnonceParent(GEAnnonces annonceParent) {
        this.annonceParent = annonceParent;
    }

    public void setCotisation(String cotisation) {
        this.cotisation = cotisation;
    }

    public void setCsCodeDoctorant(String csCodeDoctorant) {
        this.csCodeDoctorant = csCodeDoctorant;
    }

    public void setCsEtatAnnonce(String csEtatAnnonce) {
        this.csEtatAnnonce = csEtatAnnonce;
    }

    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setIdAnnonceParent(String idAnnonceParent) {
        this.idAnnonceParent = idAnnonceParent;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdEtudiant(String idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsImputation(Boolean isImputation) {
        this.isImputation = isImputation;
    }

    public void setIsTiersForce(Boolean isTiersForce) {
        this.isTiersForce = isTiersForce;
    }

    public void setLocaliteEtude(String localiteEtude) {
        this.localiteEtude = localiteEtude;
    }

    public void setLocaliteLegal(String localiteLegal) {
        this.localiteLegal = localiteLegal;
    }

    public void setMessagesErreurs(String messagesErreurs) {
        this.messagesErreurs = getMessagesErreurs() + messagesErreurs + "\n";
    }

    public void setMontantCI(String montantCI) {
        this.montantCI = montantCI;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNpaEtude(String npaEtude) {
        this.npaEtude = npaEtude;
    }

    public void setNpaLegal(String npaLegal) {
        this.npaLegal = npaLegal;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setNumImmatriculationTransmis(String numImmatriculationTransmis) {
        this.numImmatriculationTransmis = numImmatriculationTransmis;
    }

    public void setNumSequence(String numSequence) {
        this.numSequence = numSequence;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public void setRueEtude(String rueEtude) {
        this.rueEtude = rueEtude;
    }

    public void setRueLegal(String rueLegal) {
        this.rueLegal = rueLegal;
    }

    public void setSuffixePostalEtude(String suffixePostalEtude) {
        this.suffixePostalEtude = suffixePostalEtude;
    }

    public void setSuffixePostalLegal(String suffixePostalLegal) {
        this.suffixePostalLegal = suffixePostalLegal;
    }

    public void suppressionLogMessages(String idLog, BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getIdLog())) {
            // Suppression des messages du log
            FWMessageManager messagesManager = new FWMessageManager();
            messagesManager.setSession(getSession());
            messagesManager.setForIdLog(getIdLog());
            messagesManager.find(transaction);
            for (int i = 0; i < messagesManager.size(); i++) {
                FWMessage message = (FWMessage) messagesManager.getEntity(i);
                message.delete(transaction);
            }
            // Suppression du log
            FWLog log = new FWLog();
            log.setSession(getSession());
            log.setIdLog(idLog);
            log.retrieve(transaction);
            if (log != null && !log.isNew()) {
                log.delete(transaction);
            }
            setCsEtatAnnonce(CS_ETAT_A_TRAITER);
            setIdLog("");
        }
    }

}
