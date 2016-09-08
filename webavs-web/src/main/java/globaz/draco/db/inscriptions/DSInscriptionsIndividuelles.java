package globaz.draco.db.inscriptions;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import java.math.BigDecimal;
import java.util.HashMap;

public class DSInscriptionsIndividuelles extends BEntity {
    private static final long serialVersionUID = 1562021393146632677L;
    private static final String CS_PARAMETRES_IM = "12000006";

    private String aCI = new String();
    private String aCII = new String();
    private String anneeInsc = "";
    private Boolean casSpecial = new Boolean(true);
    private String categoriePerso = "";
    private String codeCanton = new String();
    private String codeSecure = "0";
    private String compteIndividuelId = "";
    public AFCotisationManager coti = new AFCotisationManager();
    private String CS_REGISTRE_ASSURES = "309001";
    private DSInscriptionsIndividuellesListeViewBean declaration = null;
    private CIEcriture ecritureCi = null;
    private boolean fromPucs = false;
    private String genreEcriture = "";
    private final String GROUPE_CODE_CS = "CICATPER";
    private String idDeclaration = new String();
    private String idEcrtirueCI = new String();
    private String idInscription = new String();
    private String idPlan = new String();
    private Boolean isNNNSS = new Boolean(false);
    private boolean isProvisoire = false;
    private String jourDebut = new String();
    private String jourFin = new String();
    private CIJournal journal = null;
    HashMap mapCodeParamAC = new HashMap();
    HashMap mapCodeParamAF = new HashMap();
    private String moisDebut = "";
    private String moisFin = "";
    private String moisDebutAF = "";
    private String moisFinAF = "";
    private String montant = "";
    private String montantAf = "";
    private String nbJour = "";
    private String nomPrenom = "";
    private boolean noSum = false;
    private boolean notWantCI = false;
    private String numeroAvs = "";
    private String numeroAvsNNSS = "";
    private String periodeDebut = "";

    private String periodeFin = "";
    private String registre = "";
    private String remarqueControle = "";

    private boolean sortie = true;
    private Boolean soumis = new Boolean(true);

    private boolean wantSubstringNomPrenom = true;

    private boolean isMontantCAFToSetWithMontantAVS = true;

    public boolean isMontantCAFToSetWithMontantAVS() {
        return isMontantCAFToSetWithMontantAVS;
    }

    public void setMontantCAFToSetWithMontantAVS(boolean isMontantCAFToSetWithMontantAVS) {
        this.isMontantCAFToSetWithMontantAVS = isMontantCAFToSetWithMontantAVS;
    }

    public DSInscriptionsIndividuelles() {
        super();
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        if (!transaction.hasErrors() && (journal != null)) {
            declaration.setIdJournal(journal.getIdJournal());
        }
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (!JAUtil.isIntegerEmpty(idEcrtirueCI)) {
            ecritureCi = new CIEcriture();
            ecritureCi.setEcritureId(idEcrtirueCI);
            ecritureCi.retrieve(transaction);
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (!transaction.hasErrors() && (journal != null)) {
            declaration.setIdJournal(journal.getIdJournal());
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // afficherMasse();
        if (!notWantCI) {
            // Si ça vient des CI on met pas de sortie
            if (JadeStringUtil.isIntegerEmpty(periodeFin) || fromPucs) {
                sortie = false;
            }
            try {
                parsePeriode();
            } catch (Exception e) {
                _addError(transaction, getSession().getLabel("PERIODE_INVALIDE"));
            }

        }
        if (JadeStringUtil.isBlankOrZero(idInscription)) {
            setIdInscription(this._incCounter(transaction, "0"));
        }
        if (JadeStringUtil.isIntegerEmpty(idEcrtirueCI)
                && !(JadeStringUtil.isBlank(numeroAvs) && JadeStringUtil.isBlank(nomPrenom))
                && !JadeStringUtil.isIntegerEmpty(montant)) {

            ecritureCi = new CIEcriture();
            ecritureCi.setNoSumNeeded(noSum);
            ecritureCi.setSession((BSession) getSessionCI(getSession()));
            ecritureCi.setAvs(numeroAvs);
            ecritureCi.setNomPrenom(nomPrenom);
            ecritureCi.setAvsNNSS(numeroAvsNNSS);
            ecritureCi.getWrapperUtil().rechercheCI(transaction);
            ecritureCi.setIdAffilie(declaration.getAffilieNumero());
            ecritureCi.setEmployeur(declaration.getAffiliationId());
            ecritureCi.setForAffilieParitaire(true);
            // ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
            if (genreEcriture.length() > 1) {
                if ("1".equals(getGenreEcriture().substring(0, 1))) {
                    if (montant.startsWith("-")) {
                        montant = montant.substring(1);
                    }
                } else if (montant.startsWith("-")) {
                    if (JadeStringUtil.isIntegerEmpty(getGenreEcriture())) {
                        genreEcriture = "11";
                    }
                    if (genreEcriture.length() > 1) {
                        genreEcriture = "1" + genreEcriture.substring(1);
                    }
                }
            }
            ecritureCi.setMontant(montant);
            ecritureCi.setAnnee(getAnneeInsc());
            ecritureCi.setMoisDebut(moisDebut);
            ecritureCi.setJourDebut(jourDebut);
            ecritureCi.setJourFin(jourFin);
            ecritureCi.setMoisFin(moisFin);
            ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
            ecritureCi.setCodeSpecial("");
            if (JadeStringUtil.isBlank(categoriePerso)) {
                ecritureCi.setCategoriePersonnel("0");
            } else {
                ecritureCi.setCategoriePersonnel(categoriePerso);
            }
            if (journal == null) {
                _findJournal(transaction);

            }
            ecritureCi.setIdJournal(journal.getIdJournal());
            ecritureCi.wantCallMethodAfter(false);
            ecritureCi.setBrancheEconomique(declaration.getAffiliation().getBrancheEconomique());
            ecritureCi.setGre(getGenreEcriture().trim());
            ecritureCi.setWantForDeclaration(new Boolean(sortie));
            ecritureCi.add(transaction);
            setNomPrenom(ecritureCi.getNomPrenom());
            setRegistre(ecritureCi.getCI(null, false).getRegistre());
            if (!transaction.hasErrors()) {
                setIdEcrtirueCI(ecritureCi.getEcritureId());
                setCompteIndividuelId(ecritureCi.getCompteIndividuelId());
                // declaration.setIdJournal(journal.getIdJournal());
            }
            genreEcriture = ecritureCi.getGreFormat();
            if (JadeStringUtil.isIntegerEmpty(getGenreEcriture())) {
                genreEcriture = "01";
            }
            if (isMontantCAFToSetWithMontantAVS && JadeStringUtil.isIntegerEmpty(montantAf)
                    && !mapCodeParamAF.containsKey(categoriePerso)) {
                if ("1".equals(getGenreEcriture().substring(0, 1))) {
                    montantAf = "-" + montant;
                } else {
                    montantAf = montant;
                }
            }
            // On change l'idJournal pour affecter la provisoire à un nouveau
            // journal
        } else if (isProvisoire()) {
            ecritureCi = new CIEcriture();
            ecritureCi.setSession((BSession) getSessionCI(getSession()));
            ecritureCi.setEcritureId(idEcrtirueCI);
            ecritureCi.retrieve();
            if (!ecritureCi.isNew()) {

                if (journal != null) {
                    ecritureCi.setIdJournal(journal.getIdJournal());
                } else {
                    _findJournal(transaction);
                    ecritureCi.setIdJournal(journal.getIdJournal());
                }
                ecritureCi.simpleUpdate(transaction);
            }

        } else if (!notWantCI) {
            CICompteIndividuel ciTemp = null;
            // retrouver le CI
            // d'abord au RA ensuite
            CICompteIndividuelManager mgr = new CICompteIndividuelManager();
            mgr.setSession(getSession());
            mgr.setForNumeroAvs(numeroAvs);
            mgr.setForNumeroAvsNNSS(numeroAvsNNSS);
            mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            if (!JadeStringUtil.isBlank(numeroAvs)) {
                mgr.find(transaction);
            }
            if (mgr.size() <= 0) {
                mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                if (JadeStringUtil.isBlank(numeroAvs)) {
                    mgr.setForNomPrenom(nomPrenom);
                }
                if (!JadeStringUtil.isBlank(numeroAvs) || !JadeStringUtil.isBlank(nomPrenom)) {
                    mgr.find(transaction);
                }

                ciTemp = (CICompteIndividuel) mgr.getFirstEntity();
                if (mgr.size() <= 0) {
                    // Pas de ci au RA ni au registre provisoire => on en crée
                    // un
                    ciTemp = new CICompteIndividuel();
                    ciTemp.wantCallValidate(false);
                    ciTemp.setSession(getSession());
                    ciTemp.setNumeroAvs(numeroAvs);
                    ciTemp.setNumeroAvsNNSS(numeroAvsNNSS);
                    ciTemp.setNomPrenom(getNomPrenom());
                    ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);

                    ciTemp.setCiOuvert(new Boolean(true));
                    if ("true".equalsIgnoreCase(getNumeroAvsNNSS())) {
                        ciTemp.setNnss(new Boolean(true));
                    }
                    ciTemp.add(transaction);
                } else {
                    ciTemp = (CICompteIndividuel) mgr.getFirstEntity();
                }
            } else {
                ciTemp = (CICompteIndividuel) mgr.getFirstEntity();
            }
            setCompteIndividuelId(ciTemp.getCompteIndividuelId());
            setRegistre(ciTemp.getRegistre());
            setNomPrenom(ciTemp.getNomPrenom());
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdEcrtirueCI())) {
            ecritureCi.retrieve(transaction);
            ecritureCi.setSession((BSession) getSessionCI(getSession()));
            if (!ecritureCi.isNew()) {
                if (CIEcriture.CS_CODE_PROVISOIRE.equals(ecritureCi.getCode())) {
                    ecritureCi.setIdJournal("0");
                    ecritureCi.getJournal(transaction, true);
                    ecritureCi.setForAffilieParitaire(true);
                    ecritureCi.update(transaction);
                } else {
                    ecritureCi.setDeleteFromDS(true);
                    ecritureCi.delete(transaction);
                }
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        try {
            parsePeriode();
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("PERIODE_INVALIDE"));
        }
        if (!JadeStringUtil.isIntegerEmpty(montant)) {
            if (JadeStringUtil.isIntegerEmpty(idEcrtirueCI)) {
                ecritureCi = new CIEcriture();
                ecritureCi.setNoSumNeeded(noSum);
                ecritureCi.setSession((BSession) getSessionCI(getSession()));
                ecritureCi.setAvs(numeroAvs);
                ecritureCi.setNomPrenom(nomPrenom);
                ecritureCi.setAvsNNSS(numeroAvsNNSS);
                ecritureCi.getWrapperUtil().rechercheCI(transaction);
                ecritureCi.setIdAffilie(declaration.getAffilieNumero());
                ecritureCi.setEmployeur(declaration.getAffiliationId());
                // ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                ecritureCi.setMontant(montant);
                ecritureCi.setAnnee(getAnneeInsc());
                ecritureCi.setJourDebut(jourDebut);
                ecritureCi.setJourFin(jourFin);

                ecritureCi.setMoisDebut(moisDebut);
                ecritureCi.setMoisFin(moisFin);
                ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                if (journal == null) {
                    _findJournal(transaction);
                }
                ecritureCi.setIdJournal(journal.getIdJournal());
                ecritureCi.setWantForDeclaration(new Boolean(sortie));
                ecritureCi.wantCallMethodAfter(false);
                ecritureCi.setBrancheEconomique(declaration.getAffiliation().getBrancheEconomique());
                ecritureCi.setGre(getGenreEcriture().trim());
                ecritureCi.setCodeSpecial("");
                if (JadeStringUtil.isBlank(categoriePerso)) {
                    ecritureCi.setCategoriePersonnel("0");
                } else {
                    ecritureCi.setCategoriePersonnel(categoriePerso);
                }
                ecritureCi.setForAffilieParitaire(true);
                ecritureCi.add(transaction);
                setNomPrenom(ecritureCi.getNomPrenom());
                setRegistre(ecritureCi.getCI(null, false).getRegistre());
                if (!transaction.hasErrors()) {
                    setIdEcrtirueCI(ecritureCi.getEcritureId());
                    setCompteIndividuelId(ecritureCi.getCompteIndividuelId());
                    setRegistre(ecritureCi.getCI(null, false).getRegistre());
                    // declaration.setIdJournal(journal.getIdJournal());
                }
                genreEcriture = ecritureCi.getGreFormat();
                if (isMontantCAFToSetWithMontantAVS && !mapCodeParamAF.containsKey(categoriePerso)) {
                    if ("1".equals(getGenreEcriture().substring(0, 1))) {
                        montantAf = "-" + montant;
                    } else {
                        montantAf = montant;
                    }
                }
            } else {
                ecritureCi.setSession((BSession) getSessionCI(getSession()));
                ecritureCi.setNoSumNeeded(noSum);
                ecritureCi.setAvs(numeroAvs);
                ecritureCi.setNomPrenom(nomPrenom);
                ecritureCi.setAvsNNSS(numeroAvsNNSS);
                ecritureCi.getWrapperUtil().rechercheCI(transaction);
                ecritureCi.setIdAffilie(declaration.getAffilieNumero());
                ecritureCi.setEmployeur(declaration.getAffiliationId());
                // ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                ecritureCi.setMontant(montant);
                ecritureCi.setAnnee(getAnneeInsc());
                ecritureCi.setJourDebut(jourDebut);
                ecritureCi.setJourFin(jourFin);

                ecritureCi.setMoisDebut(moisDebut);
                ecritureCi.setMoisFin(moisFin);
                // ecritureCi.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                if (journal == null) {
                    _findJournal(transaction);
                }
                ecritureCi.setIdJournal(journal.getIdJournal());

                // Modif 27.09.06 => évite d'écraser la valeur de kbbatt en mode
                // modification
                // ecritureCi.setWantForDeclaration(new Boolean(sortie));
                ecritureCi.wantCallMethodAfter(false);
                ecritureCi.setBrancheEconomique(declaration.getAffiliation().getBrancheEconomique());
                ecritureCi.setGre(getGenreEcriture().trim());
                ecritureCi.setCodeSpecial("");
                if (JadeStringUtil.isBlank(categoriePerso)) {
                    ecritureCi.setCategoriePersonnel("0");
                } else {
                    ecritureCi.setCategoriePersonnel(categoriePerso);
                }
                ecritureCi.setForAffilieParitaire(true);
                ecritureCi.update(transaction);
                setNomPrenom(ecritureCi.getNomPrenom());
                genreEcriture = ecritureCi.getGreFormat();
                if (!transaction.hasErrors()) {
                    setIdEcrtirueCI(ecritureCi.getEcritureId());
                    setCompteIndividuelId(ecritureCi.getCompteIndividuelId());
                    setRegistre(ecritureCi.getCI(null, false).getRegistre());
                    // declaration.setIdJournal(journal.getIdJournal());
                }
            }
        }
    }

    private void _findJournal(BTransaction transaction) {
        try {
            if (journal == null) {
                journal = new CIJournal();
                journal.setSession((BSession) getSessionCI(getSession()));
                if (!JadeStringUtil.isIntegerEmpty(declaration.getIdJournal())) {
                    journal.setIdJournal(declaration.getIdJournal());
                    journal.retrieve();
                    if (!journal.isNew()) {
                        return;
                    }
                }
                if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
                    journal.setAnneeCotisation(declaration.getAnnee());
                }
                journal.setIdAffiliation(declaration.getNumeroAffilie(), true, false);
                // journal.setLibelle("Automatische IK-Verbuchung");
                journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
                if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
                    journal.setIdTypeInscription(CIJournal.CS_CORRECTIF);
                } else {
                    journal.setIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
                }
                if (!JadeStringUtil.isIntegerEmpty(declaration.getTotalControleDS())) {
                    journal.setTotalControle(declaration.getTotalControleDS());
                } else {
                    journal.setTotalControle("200.00");
                }
                journal.setDateReception(getDeclaration().getDateRetourEff());
                journal.add(transaction);
                // declaration.setIdJournal(journal.getIdJournal());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        joinStr = " left outer join " + _getCollection() + "CIECRIP on " + _getCollection() + _getTableName()
                + ".KBIECR=" + _getCollection() + "CIECRIP.KBIECR" + " left outer join " + _getCollection()
                + "CIINDIP on " + _getCollection() + _getTableName() + ".KAIIND=" + _getCollection() + "CIINDIP.KAIIND"
                + " inner join " + _getCollection() + "DSDECLP on " + _getCollection() + _getTableName() + ".TAIDDE="
                + _getCollection() + "DSDECLP.TAIDDE";
        return _getCollection() + _getTableName() + joinStr;
    }

    @Override
    protected String _getTableName() {
        return "DSINDP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idInscription = statement.dbReadNumeric("TEID");
        idEcrtirueCI = statement.dbReadNumeric("KBIECR");
        idDeclaration = statement.dbReadNumeric("TAIDDE");
        idPlan = statement.dbReadNumeric("TFID");
        jourDebut = statement.dbReadNumeric("TENJOD");
        jourFin = statement.dbReadNumeric("TENJOF");
        moisDebutAF = statement.dbReadNumeric("TENMOD");
        moisFinAF = statement.dbReadNumeric("TENMOF");
        codeCanton = statement.dbReadNumeric("TETCAN");
        aCI = statement.dbReadNumeric("TEMAI", 2);
        aCII = statement.dbReadNumeric("TEMAII", 2);
        montantAf = statement.dbReadNumeric("TEMAF", 2);
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        numeroAvs = statement.dbReadString("KANAVS");
        montant = statement.dbReadNumeric("KBMMON", 2);
        moisDebut = statement.dbReadNumeric("KBNMOD");
        moisFin = statement.dbReadNumeric("KBNMOF");
        soumis = statement.dbReadBoolean("TEBSOU");
        casSpecial = statement.dbReadBoolean("TEBCAS");
        nbJour = statement.dbReadNumeric("TENNBJ");
        nomPrenom = statement.dbReadString("KALNOM");
        genreEcriture = JadeStringUtil.rightJustifyInteger(
                CodeSystem.getCodeUtilisateur(statement.dbReadNumeric("KBTEXT"), (BSession) getSessionCI(getSession()))
                        + CodeSystem.getCodeUtilisateur(statement.dbReadNumeric("KBTGEN"),
                                (BSession) getSessionCI(getSession())), 2);
        isNNNSS = statement.dbReadBoolean("KABNNS");
        registre = statement.dbReadNumeric("KAIREG");
        categoriePerso = statement.dbReadNumeric("TETCPE");
        anneeInsc = statement.dbReadNumeric("TENANN");
        if (isNNNSS.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }
        codeSecure = statement.dbReadNumeric("KATSEC");
        remarqueControle = statement.dbReadString("TALREM");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (getNumeroAvs().trim().length() < 8) {
            if (!JadeStringUtil.isBlank(numeroAvs) && JadeStringUtil.isBlank(nomPrenom.trim())) {
                _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_NUMERO_AVS_HUIT"));
            }
        }
        if ("true".equals(numeroAvsNNSS) || isNNNSS.booleanValue() || (numeroAvs.trim().length() == 13)) {
            if (!NSUtil.nssCheckDigit(numeroAvs)) {
                if (!JadeStringUtil.isBlank(numeroAvs)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_NSS_INVALIDE"));
                }
            }
        }

        setCasSpecial(new Boolean(false));
        if (JadeStringUtil.isIntegerEmpty(periodeFin) && !isNew()) {
            sortie = false;
        }
        try {
            parsePeriode();

            if (Integer.parseInt(jourFin) > Integer.parseInt(determineJourFin())) {

                _addError(statement.getTransaction(), getSession().getLabel("PERIODE_INVALIDE"));
            }

        } catch (Exception e) {
            _addError(statement.getTransaction(), getSession().getLabel("PERIODE_INVALIDE"));
        }

        if (JadeStringUtil.isIntegerEmpty(codeCanton)) {
            codeCanton = declaration.getCodeCantonAF();
        }
        try {
            if (getPeriodeFinForCompare() < getPeriodeDebutForCompare()) {
                _addError(statement.getTransaction(), getSession().getLabel("PERIODE_INVALIDE"));
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), getSession().getLabel("PERIODE_INVALIDE"));
            // Modif 1-5-5, permet d'éviter l'erreur d'affichage
            // 756.756X.XXXX.XX
            return;
        }
        if (!JadeStringUtil.isBlankOrZero(categoriePerso)) {
            afficherMasse();
        }
        if (!mapCodeParamAC.containsKey(categoriePerso)) {
            calculAc1();
        }
        // Si la déclaration est de type LTN, l'assuré doit exister dans les
        // tiers
        DSDeclarationViewBean declaration = new DSDeclarationViewBean();
        declaration.setSession(getSession());
        declaration.setIdDeclaration(getIdDeclaration());
        // Opti PUCS on ne recherche pas le canton par défaut comme il est saisi
        declaration.wantCallMethodAfter(false);
        declaration.retrieve();
        // On rajoute les méthodes after en cas d'update
        declaration.wantCallMethodAfter(true);
        if ((declaration != null) && !declaration.isNew()) {
            if (DSDeclarationViewBean.CS_LTN.equals(declaration.getTypeDeclaration())) {
                if (!JadeStringUtil.isBlankOrZero(getNumeroAvs())) {
                    TIPersonneAvsManager tiers = new TIPersonneAvsManager();
                    tiers.setSession(getSession());
                    tiers.setForNumAvsActuel(getNssFormate());
                    tiers.setForIncludeInactif(new Boolean(false));
                    tiers.find(statement.getTransaction());
                    if (tiers.size() == 0) {
                        _addError(statement.getTransaction(), getSession().getLabel("ASSURE_PAS_DANS_TIERS"));
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("NSS_OBLIGATOIRE"));
                }
            }
        }

        // Vérification pour les inscriptions uniquement AF Seul
        if (JadeStringUtil.isBlankOrZero(getMontant()) && !JadeStringUtil.isBlankOrZero(getMontantAf())) {

            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);

            AFAffiliation aff = application.getAffilieByNo(getSession(), getDeclaration().getAffilieNumero(), true,
                    false, moisDebutAF, moisFinAF, getAnneeInsc(), jourDebut, jourFin);

            if ((aff == null) || (aff.isNew())) {
                _addError(statement.getTransaction(), getSession().getLabel("DT_ERR_DATE_AFFILIATION"));
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".TEID",
                this._dbWriteNumeric(statement.getTransaction(), getIdInscription(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("TEID", this._dbWriteNumeric(statement.getTransaction(), idInscription, "idInscription"));
        statement.writeField("KBIECR", this._dbWriteNumeric(statement.getTransaction(), idEcrtirueCI, "idEcriture"));
        statement
                .writeField("TAIDDE", this._dbWriteNumeric(statement.getTransaction(), idDeclaration, "idDeclaration"));
        statement.writeField("TETCAN", this._dbWriteNumeric(statement.getTransaction(), codeCanton, "codeCanton"));
        statement.writeField("TFID", this._dbWriteNumeric(statement.getTransaction(), idPlan, "idPlan"));
        statement.writeField("TENJOD", this._dbWriteNumeric(statement.getTransaction(), jourDebut, "jourDebut"));
        statement.writeField("TENJOF", this._dbWriteNumeric(statement.getTransaction(), jourFin, "jourFin"));

        statement.writeField("TENMOD", this._dbWriteNumeric(statement.getTransaction(), moisDebutAF, "moisDebutAF"));
        statement.writeField("TENMOF", this._dbWriteNumeric(statement.getTransaction(), moisFinAF, "moisFinAF"));

        statement.writeField("TEMAI", this._dbWriteNumeric(statement.getTransaction(), aCI, "acI"));
        statement.writeField("TEMAII", this._dbWriteNumeric(statement.getTransaction(), aCII, "acII"));
        statement.writeField("TEMAF", this._dbWriteNumeric(statement.getTransaction(), montantAf, "AF"));

        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), compteIndividuelId, "idCompteIndividuel"));
        statement.writeField("TENNBJ", this._dbWriteNumeric(statement.getTransaction(), nbJour, "nombreDeJours"));
        statement.writeField("TEBSOU",
                this._dbWriteBoolean(statement.getTransaction(), soumis, BConstants.DB_TYPE_BOOLEAN_CHAR, "soumis"));
        statement.writeField("TEBCAS", this._dbWriteBoolean(statement.getTransaction(), casSpecial,
                BConstants.DB_TYPE_BOOLEAN_CHAR, "casSpecial"));
        statement.writeField("TETCPE",
                this._dbWriteNumeric(statement.getTransaction(), categoriePerso, "categoriePerso"));
        statement.writeField("TENANN", this._dbWriteNumeric(statement.getTransaction(), anneeInsc, "annee"));
        statement.writeField("TALREM", this._dbWriteString(statement.getTransaction(), remarqueControle, "remarque"));

    }

    public void afficherMasse() {
        AFAssurance assurance = new AFAssurance();
        FWParametersSystemCodeManager codeCat = new FWParametersSystemCodeManager();
        String[] codeExcluAC;
        String[] codeExcluAF;
        int nbrAssuranceAC = 0;
        int nbrAssuranceAF = 0;
        try {
            codeCat.setSession(getSession());
            codeCat.setForIdGroupe(GROUPE_CODE_CS);
            codeCat.find();
            String[][] codeSysExclu = new String[codeCat.size()][3];
            if (codeCat.size() > 0) {
                for (int x = 0; x < codeCat.size(); x++) {
                    codeSysExclu[x][0] = ((FWParametersSystemCode) codeCat.getEntity(x)).getIdCode();
                    codeSysExclu[x][1] = "0";
                    codeSysExclu[x][2] = "0";
                }
            }
            coti.setSession(getSession());
            // coti.setForPlanAffiliationId(idPlan);
            coti.setForAffiliationId(declaration.getAffiliationId());
            coti.find();
            if (coti.size() > 0) {
                for (int i = 0; i < coti.size(); i++) {
                    assurance = ((AFCotisation) coti.getEntity(i)).getAssurance();
                    if (assurance.getTypeAssurance().equals(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AC)
                            || assurance.getTypeAssurance().equals(
                                    globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AC2)) {
                        codeExcluAC = assurance.getListExclusionsCatPers("31.12." + anneeInsc);
                        nbrAssuranceAC++;
                        for (int j = 0; j < codeSysExclu.length; j++) {
                            for (int k = 0; k < codeExcluAC.length; k++) {
                                if (codeSysExclu[j][0].equals(codeExcluAC[k])) {
                                    Integer nbrFois = new Integer(codeSysExclu[j][1]);
                                    nbrFois = new Integer(nbrFois.intValue() + 1);
                                    codeSysExclu[j][1] = nbrFois.toString();
                                }
                            }
                        }
                    }
                }
                for (int j = 0; j < codeSysExclu.length; j++) {
                    if (codeSysExclu[j][1].equals((new Integer(nbrAssuranceAC)).toString()) && (nbrAssuranceAC != 0)) {
                        mapCodeParamAC.put(codeSysExclu[j][0], codeSysExclu[j][1]);
                    }
                }

                for (int i = 0; i < coti.size(); i++) {
                    assurance = ((AFCotisation) coti.getEntity(i)).getAssurance();
                    if (assurance.getTypeAssurance().equals(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AF)) {
                        codeExcluAF = assurance.getListExclusionsCatPers("31.12." + anneeInsc);
                        nbrAssuranceAF++;
                        for (int j = 0; j < codeSysExclu.length; j++) {
                            for (int k = 0; k < codeExcluAF.length; k++) {
                                if (codeSysExclu[j][0].equals(codeExcluAF[k])) {
                                    Integer nbrFois = new Integer(codeSysExclu[j][2]);
                                    nbrFois = new Integer(nbrFois.intValue() + 1);
                                    codeSysExclu[j][2] = nbrFois.toString();
                                }
                            }
                        }
                    }
                }
                for (int j = 0; j < codeSysExclu.length; j++) {
                    if (codeSysExclu[j][2].equals((new Integer(nbrAssuranceAF)).toString()) && (nbrAssuranceAF != 0)) {
                        mapCodeParamAF.put(codeSysExclu[j][0], codeSysExclu[j][2]);
                    }
                }
            }
        } catch (Exception e) {
            _addError(null, getSession().getLabel("INSCIND_AFFMASSE")
                    + " : DSInscriptionsIndividuelles.afficherMasse()");
        }

    }

    private void calculAc1() throws Exception {
        jourDebut = JadeStringUtil.rightJustifyInteger(jourDebut, 2);
        jourFin = JadeStringUtil.rightJustifyInteger(jourFin, 2);
        moisDebut = JadeStringUtil.rightJustifyInteger(moisDebut, 2);
        moisFin = JadeStringUtil.rightJustifyInteger(moisFin, 2);
        boolean calculAcII = false;
        if (JadeStringUtil.isBlankOrZero(anneeInsc)) {
            anneeInsc = declaration.getAnnee();
        }

        // Maintenant il faut savoir si la personne est soumise à l'AC
        if (getSoumis().booleanValue() && !numeroAvs.startsWith("00000")) {
            String dateRetraite = CIUtil.getDateRetraiteAc(numeroAvs, Integer.parseInt(anneeInsc), getSession());
            String dateDebut = jourDebut + "." + moisDebut + "." + anneeInsc;
            String dateFin = jourFin + "." + moisFin + "." + anneeInsc;
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateFin, dateRetraite)) {
                // soumis car la date de fin est plus petite que la date de
                // retraite
                setSoumis(new Boolean(true));
            } else {
                // il faut regarder si l'inscritption est après => non soumis
                // (date début>= date retraite)
                // et si l'inscription chevauche cas spécial à signaler
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebut, dateRetraite)) {
                    setSoumis(new Boolean(false));
                    setACI("0.00");
                } else {
                    setCasSpecial(new Boolean(true));
                }
            }
        }
        if (JadeStringUtil.isIntegerEmpty(aCI) && !JadeStringUtil.isIntegerEmpty(montant) && getSoumis().booleanValue()) {
            // Recherche des plafonds AC1 et AC2
            FWFindParameterManager param = new FWFindParameterManager();
            param.setSession(getSession());
            param.setIdApplParametre(getSession().getApplicationId());
            param.setIdCodeSysteme(DSInscriptionsIndividuelles.CS_PARAMETRES_IM);
            param.setIdCleDiffere("PLAFONDAC1");
            param.setIdActeurParametre("0");
            param.setPlageValDeParametre("0");
            param.setDateDebutValidite("01.01." + anneeInsc);
            param.find();
            BigDecimal plafondAc = new BigDecimal(((FWFindParameter) param.getFirstEntity()).getValeurNumParametre());
            param.setIdCleDiffere("PLAFONDAC2");
            param.find();
            BigDecimal plafondAcII = new BigDecimal(((FWFindParameter) param.getFirstEntity()).getValeurNumParametre());
            BigDecimal montantAvs = new BigDecimal(montant);

            if (plafondAcII.compareTo(new BigDecimal("0")) != 0) {
                calculAcII = true;
            }
            // Fin de recherche des plafonds
            if (JadeStringUtil.isIntegerEmpty(getGenreEcriture())) {
                genreEcriture = "01";
            }
            // Si c'est une extourne,met le montant en négatif
            if ("1".equals(getGenreEcriture().substring(0, 1))) {
                montantAvs = montantAvs.multiply(new BigDecimal("-1"));
            }
            BigDecimal montantSoumis = new BigDecimal("0.00000");
            // /Traiter les mois spéciaux
            montantSoumis = getNombreDeJour();
            /*
             * Si le montant AC est négatif
             */
            // Maintenant qu'on à le nb de jour, il faut rechercher les
            // antécédants
            DSInscriptionsIndividuellesManager mgr = new DSInscriptionsIndividuellesManager();
            mgr.setSession(getSession());
            mgr.setForAnneeChomage(anneeInsc);
            mgr.setForCompteIndividuelId(getCompteIndividuelId());
            mgr.setForIdAffiliation(declaration.getAffiliationId());
            mgr.setForNotId(getIdInscription());
            // mgr.setNotForInCatPerso(notInAC());
            mgr.find();
            BigDecimal montantSoumisAvantInscEnCours = new BigDecimal("0");
            BigDecimal montantCIAvantInscEnCours = new BigDecimal("0");
            BigDecimal montantACIIAvantInscEnCours = new BigDecimal("0");
            if ((mgr.size() > 0) && !numeroAvs.startsWith("000000")) {

                for (int i = 0; i < mgr.size(); i++) {
                    DSInscriptionsIndividuelles inscAChecker = (DSInscriptionsIndividuelles) mgr.get(i);
                    if (checkChevauchement(inscAChecker)) {
                        // Dans ce cas là, on ne peut pas calculer l'AC => on
                        // sort
                        setCasSpecial(new Boolean(true));
                        return;
                    }
                    // Si code extourne = 1 => montant négatif

                    String montantPourExtourne = inscAChecker.getMontant();
                    if ("1".equals(inscAChecker.getGenreEcriture().substring(0, 1))) {
                        montantPourExtourne = "-" + montantPourExtourne;
                    }
                    if (inscAChecker.getPeriodeDebut().equals(getPeriodeDebut())
                            && inscAChecker.getPeriodeDebut().equals(getPeriodeDebut())) {
                        montantCIAvantInscEnCours = montantCIAvantInscEnCours.add(new BigDecimal(montantPourExtourne));
                        montantSoumisAvantInscEnCours = montantSoumisAvantInscEnCours.add(new BigDecimal(inscAChecker
                                .getACI()));
                        montantACIIAvantInscEnCours = montantACIIAvantInscEnCours.add(new BigDecimal(inscAChecker
                                .getACII()));
                    }
                }

                BigDecimal nombreJoursCourant = new BigDecimal("0.00");
                BigDecimal montantEnCours = new BigDecimal(getMontant());
                if ("1".equals(getGenreEcriture().substring(0, 1))) {
                    montantEnCours = montantEnCours.multiply(new BigDecimal("-1"));
                }
                // Récupérer le nb de jour pour la période
                if (JadeStringUtil.isIntegerEmpty(periodeDebut)) {
                    periodeDebut = jourDebut + "." + moisDebut;
                }
                if (JadeStringUtil.isIntegerEmpty(periodeFin)) {
                    periodeFin = jourFin + "." + moisFin;
                }
                nombreJoursCourant = nombreJoursCourant.add(getNombreDeJoursParPeriode(getPeriodeDebut(),
                        getPeriodeFin()));
                // On prépare la multiplicaiton suivante
                montantSoumis = nombreJoursCourant;
            }
            // ajouter le nb de jour de l'isnc en cours si elle n'est pas
            // présente dans la map
            BigDecimal montantSoumisACII = montantSoumis;
            if (calculAcII) {
                montantSoumisACII = montantSoumisACII.multiply(plafondAcII);
                montantSoumisACII = montantSoumisACII.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
                montantSoumisACII = JANumberFormatter.round(montantSoumisACII, 0.05, 2, JANumberFormatter.NEAR);
            }
            montantSoumis = montantSoumis.multiply(plafondAc);
            montantSoumis = montantSoumis.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
            montantSoumis = JANumberFormatter.round(montantSoumis, 0.05, 2, JANumberFormatter.NEAR);
            montantAvs = montantAvs.add(montantCIAvantInscEnCours);
            BigDecimal acIISoumis = new BigDecimal("0");
            //
            // On compare pour savoir si le montant avs est supérieur au plafond
            if (montantAvs.compareTo(montantSoumis) < 0) {
                aCI = montantAvs.subtract(montantSoumisAvantInscEnCours).toString();
                // Restituer si trop payé pour l'ACII
                if (calculAcII) {
                    acIISoumis = new BigDecimal("0");
                    aCII = acIISoumis.subtract(montantACIIAvantInscEnCours).toString();
                }
            } else {
                aCI = montantSoumis.subtract(montantSoumisAvantInscEnCours).toString();
                if (calculAcII) {
                    if (montantAvs.compareTo(montantSoumisACII) < 0) {
                        acIISoumis = montantAvs.subtract(new BigDecimal(aCI).add(montantSoumisAvantInscEnCours));
                    } else {
                        acIISoumis = montantSoumisACII.subtract(new BigDecimal(aCI).add(montantSoumisAvantInscEnCours));
                    }
                    aCII = acIISoumis.subtract(montantACIIAvantInscEnCours).toString();
                }
            }
            // On a le montant pour la période
        }
    }

    private boolean checkChevauchement(DSInscriptionsIndividuelles inscToCheck) {
        long periodeDebutInscCourante = Long.parseLong(JadeStringUtil.rightJustifyInteger(moisDebut, 2)
                + JadeStringUtil.rightJustifyInteger(jourDebut, 2));
        long periodeFinInscCourante = Long.parseLong(JadeStringUtil.rightJustifyInteger(moisFin, 2)
                + JadeStringUtil.rightJustifyInteger(jourFin, 2));
        long periodeDebutEcrToCheck = Long.parseLong(JadeStringUtil.rightJustifyInteger(inscToCheck.getMoisDebut(), 2)
                + JadeStringUtil.rightJustifyInteger(inscToCheck.getJourDebut(), 2));
        long periodeFinEcrToCheck = Long.parseLong(JadeStringUtil.rightJustifyInteger(inscToCheck.getMoisFin(), 2)
                + JadeStringUtil.rightJustifyInteger(inscToCheck.getJourFin(), 2));
        // Les deux ecriture ont une période identique => OK
        if ((periodeDebutInscCourante == periodeDebutEcrToCheck) && (periodeFinInscCourante == periodeFinEcrToCheck)) {
            return false;
        }
        // les cas 1-5/5-7
        // 1-12 1-5
        if (periodeFinInscCourante < periodeDebutEcrToCheck) {
            return false;
            // les cas 5-7/1-5
        } else if (periodeFinEcrToCheck < periodeDebutInscCourante) {
            return false;
        }
        return true;
    }

    /**
     * Méthode
     * 
     * @return
     */
    private String determineJourFin() {
        if (moisFin.trim().equals("01") || moisFin.trim().equals("03") || moisFin.trim().equals("05")
                || moisFin.trim().equals("07") || moisFin.trim().equals("08") || moisFin.trim().equals("10")
                || moisFin.trim().equals("12")) {
            return "31";
        } else if (moisFin.trim().equals("02")) {

            try {
                if (new JACalendarGregorian().isLeapYear(Integer.parseInt(getAnneeInsc()))) {
                    return "29";
                }
            } catch (Exception e) {
                return "28";
            }

            return "28";
        } else {
            return "30";
        }
    }

    public String donneIdJournal(BTransaction transaction) {
        if (journal == null) {
            _findJournal(transaction);
        }
        return journal.getIdJournal();
    }

    /**
     * @return
     */
    public String getACI() {
        return aCI;
    }

    /**
     * @return
     */
    public String getACII() {
        return aCII;
    }

    public String getAnneeInsc() {
        return anneeInsc;
    }

    /**
     * @return
     */
    public Boolean getCasSpecial() {
        return casSpecial;
    }

    public String getCategoriePerso() {
        return categoriePerso;
    }

    /**
     * @return
     */
    public String getCodeCanton() {
        return codeCanton;
    }

    public String getCodeSecure() {
        return codeSecure;
    }

    /**
     * @return
     */
    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    /**
     * @return
     */
    public DSInscriptionsIndividuellesListeViewBean getDeclaration() {
        return declaration;
    }

    public String getEtatFormate() {
        try {
            CICompteIndividuel ci = new CICompteIndividuel();
            ci.setCompteIndividuelId(getCompteIndividuelId());
            ci.setSession((BSession) getSessionCI(getSession()));
            ci.retrieve();
            if (!ci.isNew()) {
                return ci.getEtatFormate();
            }
            return "";
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * @return
     */
    public String getGenreEcriture() {
        return genreEcriture;
    }

    /**
     * @return
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * @return
     */
    public String getIdEcrtirueCI() {
        return idEcrtirueCI;
    }

    /**
     * @return
     */
    public String getIdInscription() {
        return idInscription;
    }

    /**
     * @return
     */
    public String getIdPlan() {
        return idPlan;
    }

    public Boolean getIsNNNSS() {
        return isNNNSS;
    }

    /**
     * @return
     */
    public String getJourDebut() {
        return jourDebut;
    }

    /**
     * @return
     */
    public String getJourFin() {
        return jourFin;
    }

    /**
     * @return
     */
    public String getMoisDebut() {
        return moisDebut;
    }

    /**
     * @return
     */
    public String getMoisFin() {
        return moisFin;
    }

    /**
     * @return
     */
    public String getMoisDebutAF() {
        return moisDebutAF;
    }

    /**
     * @return
     */
    public String getMoisFinAF() {
        return moisFinAF;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public String getMontantAf() {
        return montantAf;
    }

    /**
     * @return
     */
    public String getNbJour() {
        return nbJour;
    }

    public BigDecimal getNombreDeJour() {
        BigDecimal retour = new BigDecimal("0");
        BigDecimal moisDebutAc = new BigDecimal(moisDebut);
        BigDecimal moisFinAc = new BigDecimal(moisFin);
        BigDecimal jourDebutAc = new BigDecimal(jourDebut);
        BigDecimal jourFinAc = new BigDecimal(jourFin);
        // /Traiter les mois spéciaux
        if (jourFin.trim().equals("31") || JadeStringUtil.isIntegerEmpty(jourFin)) {
            jourFinAc = new BigDecimal("30");
        }
        if (jourDebut.trim().equals("31")) {
            jourDebutAc = new BigDecimal("30");
        }
        if (JadeStringUtil.isIntegerEmpty(jourDebut)) {
            jourDebutAc = new BigDecimal("01");
        }
        if (JadeStringUtil.rightJustifyInteger(moisDebut, 2).equals("02")) {
            if (jourDebut.equals("28") || jourDebut.equals("29") || jourDebut.equals("31")) {
                jourDebutAc = new BigDecimal("30");
            }
        }
        if (JadeStringUtil.rightJustifyInteger(moisFin, 2).equals("02")) {
            if (jourFin.equals("28") || jourFin.equals("29") || jourFin.equals("31")) {
                jourFinAc = new BigDecimal("30");
            }
        }
        // moisFin - moisDebut
        retour = (moisFinAc.subtract(moisDebutAc));
        // (moisFin - moisDebut)*30
        retour = retour.multiply(new BigDecimal("30"));
        retour = retour.add(jourFinAc).subtract(jourDebutAc).add(new BigDecimal("1"));
        return retour;
    }

    public BigDecimal getNombreDeJoursParPeriode(String periodeDebutPer, String periodeFinPer) {
        String jourDebutPer = periodeDebutPer.substring(0, 2);
        String moisDebutPer = periodeDebutPer.substring(3);
        String jourFinPer = periodeFinPer.substring(0, 2);
        String moisFinPer = periodeFinPer.substring(3);
        BigDecimal retour = new BigDecimal("0");
        BigDecimal moisDebutAc = new BigDecimal(moisDebutPer);
        BigDecimal moisFinAc = new BigDecimal(moisFinPer);
        BigDecimal jourDebutAc = new BigDecimal(jourDebutPer);
        BigDecimal jourFinAc = new BigDecimal(jourFinPer);
        if (jourFinPer.trim().equals("31") || JadeStringUtil.isIntegerEmpty(jourFinPer)) {
            jourFinAc = new BigDecimal("30");
        }
        if (jourDebutPer.trim().equals("31")) {
            jourDebutAc = new BigDecimal("30");
        }
        if (JadeStringUtil.isIntegerEmpty(jourDebutPer)) {
            jourDebutAc = new BigDecimal("01");
        }
        if (JadeStringUtil.rightJustifyInteger(moisDebutPer, 2).equals("02")) {
            if (jourDebutPer.equals("28") || jourDebutPer.equals("29") || jourDebutPer.equals("31")) {
                jourDebutAc = new BigDecimal("30");
            }
        }
        if (JadeStringUtil.rightJustifyInteger(moisFinPer, 2).equals("02")) {
            if (jourFinPer.equals("28") || jourFinPer.equals("29") || jourFinPer.equals("31")) {
                jourFinAc = new BigDecimal("30");
            }
        }
        // moisFin - moisDebut
        retour = (moisFinAc.subtract(moisDebutAc));
        // (moisFin - moisDebut)*30
        retour = retour.multiply(new BigDecimal("30"));
        retour = retour.add(jourFinAc).subtract(jourDebutAc).add(new BigDecimal("1"));
        return retour;
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        try {
            if (!JadeStringUtil.isBlank(nomPrenom) && wantSubstringNomPrenom) {
                if (nomPrenom.length() >= 20) {
                    nomPrenom = nomPrenom.substring(0, 19);
                }
            }
        } catch (Exception e) {
            return nomPrenom;
        }
        return nomPrenom;
    }

    public String getNssFormate() {
        return NSUtil.formatAVSNew(numeroAvs, isNNNSS.booleanValue());
    }

    public String getNssFormateWithoutPrefix() {
        return NSUtil.formatWithoutPrefixe(numeroAvs, isNNNSS.booleanValue());
    }

    /**
     * @return
     */
    public String getNumeroAvs() {
        return numeroAvs;
    }

    /**
     * @return
     */
    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * @return
     */
    public String getPeriodeDebut() {
        String moisRetour = "";
        if (JadeStringUtil.isIntegerEmpty(moisDebut)) {
            return "";
        }
        moisRetour = JadeStringUtil.rightJustifyInteger(jourDebut, 2) + "."
                + JadeStringUtil.rightJustifyInteger(moisDebut, 2);
        return JadeStringUtil.isIntegerEmpty(moisRetour) ? "" : moisRetour;
    }

    /**
     * @return
     */
    public String getPeriodeDebutAF() {
        String moisRetour = "";
        if (JadeStringUtil.isIntegerEmpty(moisDebutAF)) {
            return "";
        }
        moisRetour = JadeStringUtil.rightJustifyInteger(jourDebut, 2) + "."
                + JadeStringUtil.rightJustifyInteger(moisDebutAF, 2);
        return JadeStringUtil.isIntegerEmpty(moisRetour) ? "" : moisRetour;
    }

    private long getPeriodeDebutForCompare() {
        return Long.parseLong(JadeStringUtil.rightJustifyInteger(moisDebut, 2)
                + JadeStringUtil.rightJustifyInteger(jourDebut, 2));
    }

    /**
     * @return
     */
    public String getPeriodeFin() {
        String moisRetour = "";
        if (JadeStringUtil.isIntegerEmpty(moisFin)) {
            return "";
        }
        moisRetour = JadeStringUtil.rightJustifyInteger(jourFin, 2) + "."
                + JadeStringUtil.rightJustifyInteger(moisFin, 2);
        return JadeStringUtil.isIntegerEmpty(moisRetour) ? "" : moisRetour;
    }

    public String getPeriodeFinAF() {
        String moisRetour = "";
        if (JadeStringUtil.isIntegerEmpty(moisFinAF)) {
            return "";
        }
        moisRetour = JadeStringUtil.rightJustifyInteger(jourFin, 2) + "."
                + JadeStringUtil.rightJustifyInteger(moisFinAF, 2);
        return JadeStringUtil.isIntegerEmpty(moisRetour) ? "" : moisRetour;
    }

    private long getPeriodeFinForCompare() {
        return Long.parseLong(JadeStringUtil.rightJustifyInteger(moisFin, 2)
                + JadeStringUtil.rightJustifyInteger(jourFin, 2));
    }

    public String getRegistre() {
        return registre;
    }

    public String getRemarqueControle() {
        return remarqueControle;
    }

    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * @return
     */
    public Boolean getSoumis() {
        return soumis;
    }

    public boolean hasShowRight() {
        try {
            if (JadeStringUtil.isBlank(codeSecure)) {
                // ci pas chargé correctement -> écriture cachée
                return false;
            } else if (!JadeStringUtil.isBlank(codeSecure) && !CICompteIndividuel.CS_ACCESS_0.equals(codeSecure)) {
                FWSecureUserDetail user = new FWSecureUserDetail();
                user.setSession(getSession());
                user.setUser(getSession().getUserId());
                user.setLabel(CICompteIndividuel.SECURITE_LABEL);
                user.retrieve();
                if (!user.isNew()) {
                    int accesUser = Integer.parseInt(user.getData());
                    int accesCI = Character.getNumericValue(codeSecure.charAt(codeSecure.length() - 1));
                    if (accesUser < accesCI) {
                        // sécurité utilisateur inférieure -> -> ecriture cachée
                        return false;
                    }
                } else {
                    // l'utilisateur n'a pas de code accès -> ecriture cachée
                    return false;
                }
                // sécurité ok -> retourner le montant
                return true;
            } else {
                // pas de code sécurité ou code 0 -> retourner le montant
                return true;
            }
        } catch (Exception ex) {
            // si exception -> ecriture cachée
            return false;
        }
    }

    /**
     * @return
     */
    public boolean isEcritureCI() {
        if ((ecritureCi == null) || ecritureCi.isNew()) {
            return false;
        }
        if (CIEcriture.CS_CI.equals(ecritureCi.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS.equals(ecritureCi.getIdTypeCompte())
                || CIEcriture.CS_GENRE_6.equals(ecritureCi.getIdTypeCompte())) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isFromPucs() {
        return fromPucs;
    }

    /**
     * @return
     */
    public boolean isNoSum() {
        return noSum;
    }

    /**
     * @return
     */
    public boolean isNotWantCI() {
        return notWantCI;
    }

    /**
     * @return
     */
    public boolean isProvisoire() {
        return isProvisoire;
    }

    public String isRAString() {
        if (CS_REGISTRE_ASSURES.equals(registre)) {
            return "true";
        } else {
            return "false";
        }
    }

    public boolean isWantSubstringNomPrenom() {
        return wantSubstringNomPrenom;
    }

    /**
     * @author jmc
     * 
     *         Méthode qui parse la période pour séparer les jours et les mois
     */
    private void parsePeriode() {
        int indexPointFin = periodeFin.indexOf(".");
        if (indexPointFin > 0) {
            jourFin = periodeFin.substring(0, indexPointFin);
            moisFin = periodeFin.substring(indexPointFin + 1);
            moisFin = JadeStringUtil.rightJustifyInteger(moisFin, 2);
        } else {
            if (JadeStringUtil.isIntegerEmpty(periodeFin)) {
                moisFin = "12";
            } else {
                moisFin = periodeFin;
            }
            moisFin = JadeStringUtil.rightJustifyInteger(moisFin, 2);

            jourFin = determineJourFin();
        }
        int indexPointDebut = periodeDebut.indexOf(".");
        if (indexPointDebut > 0) {
            jourDebut = periodeDebut.substring(0, indexPointDebut);
            moisDebut = periodeDebut.substring(indexPointDebut + 1);
            moisDebut = JadeStringUtil.rightJustifyInteger(moisDebut, 2);
        } else {
            if (JadeStringUtil.isIntegerEmpty(periodeDebut)) {
                moisDebut = "01";
            } else {
                moisDebut = periodeDebut;
                moisDebut = JadeStringUtil.rightJustifyInteger(moisDebut, 2);

            }
            jourDebut = "01";
        }
        if (JadeStringUtil.isIntegerEmpty(montant)) {
            moisFinAF = moisFin;
            moisDebutAF = moisDebut;
        }
    }

    /**
     * @param string
     */
    public void setACI(String string) {
        aCI = string;
    }

    /**
     * @param string
     */
    public void setACII(String string) {
        aCII = string;
    }

    public void setAnneeInsc(String anneeInsc) {
        this.anneeInsc = anneeInsc;
    }

    /**
     * @param boolean1
     */
    public void setCasSpecial(Boolean boolean1) {
        casSpecial = boolean1;
    }

    public void setCategoriePerso(String categoriePerso) {
        this.categoriePerso = categoriePerso;
    }

    /**
     * @param string
     */
    public void setCodeCanton(String string) {
        codeCanton = string;
    }

    public void setCodeSecure(String codeSecure) {
        this.codeSecure = codeSecure;
    }

    /**
     * @param string
     */
    public void setCompteIndividuelId(String string) {
        compteIndividuelId = string;
    }

    /**
     * @param bean
     */
    public void setDeclaration(DSInscriptionsIndividuellesListeViewBean bean) {
        declaration = bean;
    }

    public void setFromPucs(boolean fromPucs) {
        this.fromPucs = fromPucs;
    }

    /**
     * @param string
     */
    public void setGenreEcriture(String string) {
        genreEcriture = string;
    }

    /**
     * @param string
     */
    public void setIdDeclaration(String string) {
        idDeclaration = string;
    }

    /**
     * @param string
     */
    public void setIdEcrtirueCI(String string) {
        idEcrtirueCI = string;
    }

    /**
     * @param string
     */
    public void setIdInscription(String string) {
        idInscription = string;
    }

    /**
     * @param string
     */
    public void setIdPlan(String string) {
        idPlan = string;
    }

    public void setIsNNNSS(Boolean isNNNSS) {
        this.isNNNSS = isNNNSS;
    }

    /**
     * @param string
     */
    public void setJourDebut(String string) {
        jourDebut = string;
    }

    /**
     * @param string
     */
    public void setJourFin(String string) {
        jourFin = string;
    }

    /**
     * @param string
     */
    public void setMoisDebut(String string) {
        moisDebut = string;
    }

    /**
     * @param string
     */
    public void setMoisFin(String string) {
        moisFin = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * @param string
     */
    public void setMontantAf(String string) {
        montantAf = string;
    }

    /**
     * @param string
     */
    public void setNbJour(String string) {
        nbJour = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * @param b
     */
    public void setNoSum(boolean b) {
        noSum = b;
    }

    /**
     * @param b
     */
    public void setNotWantCI(boolean b) {
        notWantCI = b;
    }

    /**
     * @param string
     */
    public void setNumeroAvs(String string) {
        numeroAvs = string;
    }

    /**
     * @param string
     */
    public void setNumeroAvsNNSS(String string) {
        numeroAvsNNSS = string;
    }

    /**
     * @param string
     */
    public void setPeriodeDebut(String string) {
        periodeDebut = string;
    }

    /**
     * @param string
     */
    public void setPeriodeFin(String string) {
        periodeFin = string;
    }

    /**
     * @param b
     */
    public void setProvisoire(boolean b) {
        isProvisoire = b;
    }

    public void setRegistre(String registre) {
        this.registre = registre;
    }

    public void setRemarqueControle(String remarqueControle) {
        this.remarqueControle = remarqueControle;
    }

    /**
     * @param boolean1
     */
    public void setSoumis(Boolean boolean1) {
        soumis = boolean1;
    }

    public void setWantSubstringNomPrenom(boolean wantSubstringNomPrenom) {
        this.wantSubstringNomPrenom = wantSubstringNomPrenom;
    }

}
