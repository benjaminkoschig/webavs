package globaz.pavo.db.compte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import globaz.commons.nss.NSUtil;
import globaz.globall.api.BISession;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.splitting.CIEcrituresSplittingContainer;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIAdministration;

/**
 * Object repr�sentant la p�riode de splitting d'un compte individuel. Date de cr�ation : (12.11.2002 13:42:47)
 *
 * @author: David Girardin
 */
public class CIPeriodeSplitting extends BEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_SAISIE_MANUELLE = "322002";
    // Type d'enregistrement
    public final static String CS_SPLITTING = "322000";
    public final static String CS_SPLITTING_MANUEL = "322003";
    public final static String CS_SPLITTING_OUVERTURE = "322001";

    /** (KLNADE) */
    private String anneeDebut = new String();
    /** (KLNAFI) */
    private String anneeFin = new String();
    private String caisseAgence = new String();
    private String caisseAgenceNom = new String();
    /** (KLICCO) */
    private String caisseCommettante = new String();
    private String caisseTenantCI = new String();
    private CICompteIndividuel CIPartenaire = null;
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (KLDREV) */
    private String dateRevocation = new String();
    /** (KLIDJN) */
    private String idJournal = new String();
    private CIJournal journal = null;
    /** (KLNPAR) */
    private String partenaireNumeroAvs = new String();
    /** (KLTPAR) */
    private String particulier = new String();
    /** (KLIPSP) */
    private String periodeSplittingId = new String();
    private String realCaisse = new String();

    /** (KLTENR) */
    private String typeEnregistrement = new String();

    /**
     * Initialisation du bean.
     */
    public CIPeriodeSplitting() {
        super();
        setTypeEnregistrement(CIPeriodeSplitting.CS_SAISIE_MANUELLE);
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // no caisse
        if (!JAUtil.isIntegerEmpty(getCaisseCommettante())) {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
            if (isLoadedFromManager()) {
                // uniquement le num�ro de la caisse si rc_liste
                caisseAgence = application.getAdministration(getSession(), getCaisseCommettante(),
                        new String[] { "getCodeAdministration" }).getCodeAdministration();
            } else {
                // num�ro et nom complet de la caisse
                ITIAdministration admin = application.getAdministration(getSession(), getCaisseCommettante(),
                        new String[] { "getCodeAdministration", "getNom" });
                caisseAgence = admin.getCodeAdministration();
                caisseAgenceNom = admin.getNom();
            }
        }
    }

    /**
     * Reset de l'id si entity en erreur
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {
        if (transaction.hasErrors()) {
            setPeriodeSplittingId("");
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        if (JAUtil.isIntegerEmpty(periodeSplittingId)) {
            setPeriodeSplittingId(this._incCounter(transaction, "0"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISPLIP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     *
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        periodeSplittingId = statement.dbReadNumeric("KLIPSP");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        partenaireNumeroAvs = statement.dbReadString("KLNPAR");
        anneeDebut = statement.dbReadNumeric("KLNADE");
        anneeFin = statement.dbReadNumeric("KLNAFI");
        particulier = statement.dbReadNumeric("KLTPAR");
        caisseCommettante = statement.dbReadNumeric("KLICCO");
        typeEnregistrement = statement.dbReadNumeric("KLTENR");
        dateRevocation = statement.dbReadDateAMJ("KLDREV");
        idJournal = statement.dbReadNumeric("KLIDJN");
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
        if (!app.isAnnoncesWA()) {
            caisseTenantCI = statement.dbReadNumeric("KLTCTE");
            realCaisse = statement.dbReadNumeric("KLCTCI");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     *
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
        if (JAUtil.isIntegerEmpty(caisseCommettante)) {
            if (JAUtil.isStringEmpty(caisseAgence)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
            } else {
                if (caisseAgence.trim().length() <= 7) {
                    // recherche de l'id tiers
                    int sep = caisseAgence.indexOf('.');
                    String caisse = null;
                    String agence = null;
                    if (sep > 0) {
                        caisse = caisseAgence.substring(0, sep);
                        agence = caisseAgence.substring(sep + 1);
                    } else {
                        caisse = caisseAgence;
                    }
                    try {
                        CIApplication application = (CIApplication) getSession().getApplication();
                        caisseCommettante = application.getAdministration(getSession(), caisse, agence,
                                new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
                    } catch (Exception ex) {
                        // impossible d'appliquer le changement
                    }
                    if ((caisseCommettante == null) || (caisseCommettante.length() == 0)) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
                    }
                }
            }
        }
        // dates
        _checkDate(statement.getTransaction(), "01.01." + anneeDebut, getSession().getLabel("MSG_PESPLIT_VAL_DEBUT"));
        _checkDate(statement.getTransaction(), "01.01." + anneeFin, getSession().getLabel("MSG_PESPLIT_VAL_FIN"));
        if (!JAUtil.isStringEmpty(dateRevocation)) {
            _checkDate(statement.getTransaction(), dateRevocation, getSession().getLabel("MSG_PESPLIT_VAL_REVOC"));
        }
        if (!statement.getTransaction().hasErrors()) {
            // test p�riodes
            int dateDebut = Integer.parseInt(anneeDebut);
            if ((1948 > dateDebut) || (dateDebut >= JACalendar.today().getYear())) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_PESPLIT_VAL_DEBUTP"));
            }
            int dateFin = Integer.parseInt(anneeFin);
            if ((dateFin >= JACalendar.today().getYear()) || (dateFin < dateDebut)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_PESPLIT_VAL_FINP"));
            }
            if (!JAUtil.isStringEmpty(dateRevocation)) {
                try {
                    if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01.1997", dateRevocation)) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_PESPLIT_VAL_REVOCG"));
                    }
                    if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateRevocation,
                            JACalendar.todayJJsMMsAAAA())) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_PESPLIT_VAL_REVOCG"));
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), e.getMessage());
                }
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KLIPSP", this._dbWriteNumeric(statement.getTransaction(), getPeriodeSplittingId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KLIPSP",
                this._dbWriteNumeric(statement.getTransaction(), getPeriodeSplittingId(), "periodeSplittingId"));
        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KLNPAR",
                this._dbWriteString(statement.getTransaction(), getPartenaireNumeroAvs(), "partenaireNumeroAvs"));
        statement.writeField("KLNADE", this._dbWriteNumeric(statement.getTransaction(), getAnneeDebut(), "anneeDebut"));
        statement.writeField("KLNAFI", this._dbWriteNumeric(statement.getTransaction(), getAnneeFin(), "anneeFin"));
        statement.writeField("KLTPAR",
                this._dbWriteNumeric(statement.getTransaction(), getParticulier(), "particulier"));
        statement.writeField("KLICCO",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseCommettante(), "caisseCommettante"));
        statement.writeField("KLTENR",
                this._dbWriteNumeric(statement.getTransaction(), getTypeEnregistrement(), "typeEnregistrement"));
        statement.writeField("KLDREV",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRevocation(), "dateRevocation"));
        statement.writeField("KLIDJN", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
        if (!app.isAnnoncesWA()) {
            statement.writeField("KLTCTE",
                    this._dbWriteNumeric(statement.getTransaction(), getCaisseTenantCI(), "caisseTenantCI"));
            statement.writeField("KLCTCI",
                    this._dbWriteNumeric(statement.getTransaction(), getRealCaisse(), "realCaisseTenantCI"));

        }
    }

    /**
     * @param nocheck
     *            ins�re les inscriptions sans test du journal
     * @return les inscriptions g�n�r�es ou null si le montant est � z�ro.
     */
    private ArrayList addEcritureSplitting(BTransaction transaction, int anneeCourante, long montant,
            CICompteIndividuel ciAssure, CICompteIndividuel ciConjoint, boolean checkJournal) throws Exception {
        if (montant != 0) {
            ArrayList result = new ArrayList();
            CIEcriture ecritureAssure = new CIEcriture();
            CIEcriture ecritureConjoint = new CIEcriture();
            if (montant > 0) {
                // 18
                ecritureAssure.setExtourne(CIEcriture.CS_EXTOURNE_1);
                ecritureAssure.setGenreEcriture(CIEcriture.CS_CIGENRE_8);
                ecritureAssure.setParticulier(getParticulier());
                // 08
                ecritureConjoint.setGenreEcriture(CIEcriture.CS_CIGENRE_8);
                ecritureConjoint.setParticulier(getParticulier());
            } else {
                montant = Math.abs(montant);
                // 08
                ecritureAssure.setGenreEcriture(CIEcriture.CS_CIGENRE_8);
                ecritureAssure.setParticulier(getParticulier());
                // 18
                ecritureConjoint.setExtourne(CIEcriture.CS_EXTOURNE_1);
                ecritureConjoint.setGenreEcriture(CIEcriture.CS_CIGENRE_8);
                ecritureConjoint.setParticulier(getParticulier());
            }
            long montantAssure = montant / 2;
            long montantConjoint = montant / 2;
            if (montant % 2 != 0) {
                if (montant > 0) {
                    montantConjoint += 1;
                } else {
                    montantAssure += 1;
                }
            }
            // id du CI et celui du conjoint
            ecritureAssure.setCompteIndividuelId(ciAssure.getCompteIndividuelId());
            ecritureAssure.setCI(ciAssure);
            ecritureAssure.setPartenaireId(ciConjoint.getCompteIndividuelId());
            // idem pour conjoint (crois�)
            ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
            ecritureConjoint.setCI(ciConjoint);
            ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());

            // montant
            ecritureAssure.setMontant(String.valueOf(montantAssure));
            ecritureConjoint.setMontant(String.valueOf(montantConjoint));
            // ann�e
            ecritureAssure.setAnnee(String.valueOf(anneeCourante));
            ecritureConjoint.setAnnee(String.valueOf(anneeCourante));
            // id journal
            if (!checkJournal) {
                ecritureAssure.setIdJournal(getIdJournal());
                ecritureConjoint.setIdJournal(getIdJournal());
            } else {
                ecritureAssure.setIdJournal(getJournalId(transaction, ciAssure.getNumeroAvs()));
                ecritureConjoint.setIdJournal(getJournalId(transaction, ciAssure.getNumeroAvs()));
            }
            // id type compte
            ecritureAssure.setIdTypeCompte(CIEcriture.CS_CI);
            ecritureConjoint.setIdTypeCompte(CIEcriture.CS_CI);
            // sauve
            if (montantAssure != 0) {
                ecritureAssure.simpleAdd(transaction);
                result.add(ecritureAssure);

            }
            if (montantConjoint != 0) {
                ecritureConjoint.simpleAdd(transaction);
                result.add(ecritureConjoint);
            }

            journal.updateInscription(transaction);
            return result;
        }
        return null;
    }

    /**
     * Teste si l'�criture donn�e doit �tre associ�e � cette p�riode de splitting. Si le genre de l'�criture est
     * diff�rent de 7 ou 8, l'�criture est automatiquement split�e.
     *
     * @param transaction
     *            la transaction � utiliser
     * @param ecriture
     *            l'�criture � tester
     * @return Les �critures de splitting. Si l'�criture donn�e est d�j� de genre 8 ou de genre 7, celle-ci est
     *         retourn�e (dans une liste). Null si l'�criture donn�e ne correspond pas � la p�riode.
     * @throws Exception
     *             si un probl�me survient
     */
    public ArrayList checkPeriode(BTransaction transaction, CIEcriture ecriture, boolean check) throws Exception {
        // test la r�vocation
        if (JAUtil.isStringEmpty(getDateRevocation()) && !JAUtil.isIntegerEmpty(getAnneeDebut())
                && !JAUtil.isIntegerEmpty(getAnneeFin())) {
            // test des p�riodes
            int anneeDebutInt = Integer.parseInt(getAnneeDebut());
            int anneeFinInt = Integer.parseInt(getAnneeFin());
            int anneeEcritureInt = Integer.parseInt(ecriture.getAnnee());
            if ((anneeDebutInt <= anneeEcritureInt) && (anneeFinInt >= anneeEcritureInt)) {
                // �criture comprise dans la p�riode de splitting
                // test le genre
                if (!CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                    CICompteIndividuel ciAssure = ecriture.getCI(transaction, false);
                    CICompteIndividuel ciConjoint = null;
                    if (!JAUtil.isStringEmpty(getPartenaireNumeroAvs())) {
                        ciConjoint = CICompteIndividuel.loadCI(getPartenaireNumeroAvs(), transaction);
                    }
                    // Pas trouv� dans le RA, on regarde au registre provisoire
                    if (ciConjoint == null) {
                        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                        ciMgr.setSession(getSession());
                        ciMgr.setForNumeroAvs(getPartenaireNumeroAvs());
                        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                        ciMgr.find();
                        if (ciMgr.size() > 0) {
                            ciConjoint = (CICompteIndividuel) ciMgr.getFirstEntity();
                        }

                    }
                    if (ciConjoint == null) {
                        // On n'ajoute pas de CI si le num�ro AVS est = � 0
                        if (!JAUtil.isIntegerEmpty(getPartenaireNumeroAvs())) {
                            CICompteIndividuel ciTemp = new CICompteIndividuel();
                            ciTemp.setSession(getSession());
                            ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                            ciTemp.setNumeroAvs(getPartenaireNumeroAvs());
                            // Modif NNSS
                            if (CIUtil.isNNSSlengthOrNegate(getPartenaireNumeroAvs())) {
                                ciTemp.setNnss(new Boolean(true));
                            } else {
                                ciTemp.setNnss(new Boolean(false));
                            }
                            ciTemp.setCiOuvert(new Boolean(true));
                            ciTemp.add(transaction);
                            if (!transaction.hasErrors()) {
                                ciConjoint = ciTemp;
                            } else {
                                _addError(transaction, getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW"));
                                return null;
                            }
                            // si pas d'erreur dans la transaction, envoie d'un
                            // 65 --> 63 depuis le 01.01.19
                            if (!transaction.hasErrors()) {
                                if (ciConjoint != null) {
                                    CIApplication application = (CIApplication) GlobazServer.getCurrentSystem()
                                            .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
                                    String genreAnnonce = "63";
                                    // annonce
                                    HashMap attributs = new HashMap();
                                    attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                                    attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, genreAnnonce);
                                    // assur�
                                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, ciConjoint.getNumeroAvs());
                                    // envoi
                                    application.annonceARC(transaction, attributs, false);
                                }
                            }
                        }
                    }
                    if (ciConjoint == null) {
                        // ci conjoint non pr�sent... ne devrait pas arriver
                        _addError(transaction, getSession().getLabel("MSG_ECRITURE_PARTSPL"));
                        return null;
                    }
                    long montant = (long) Math
                            .floor(Double.parseDouble(JANumberFormatter.deQuote(ecriture.getMontantSigne())));
                    return addEcritureSplitting(transaction, anneeEcritureInt, montant, ciAssure, ciConjoint, check);
                }
                ArrayList result = new ArrayList();
                result.add(ecriture);
                return result;
            }
        }
        return null;
    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public String getCaisseAgenceCommettante() {
        return caisseAgence;
    }

    public String getCaisseCommettante() {
        return caisseCommettante;
    }

    public String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    /**
     * Renvoie le num�ro d'agence qui tient le ci
     */
    public String getCaisseTenantCIWA() {
        if ((typeEnregistrement != null) && (typeEnregistrement.length() == 6)) {
            String agence = typeEnregistrement.substring(3, 4);
            if (!JAUtil.isIntegerEmpty(agence)) {
                try {
                    CIApplication app = (CIApplication) GlobazServer.getCurrentSystem()
                            .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
                    if (app.isAnnoncesWA()) {
                        return app.getProperty(CIApplication.CODE_CAISSE) + "." + typeEnregistrement.substring(3, 4);
                    } else {
                        String caisseRetour = getCaisseTenantCI();
                        if (caisseRetour == null) {
                            caisseRetour = "0";
                        }
                        return app.getProperty(CIApplication.CODE_CAISSE) + "." + caisseRetour;
                    }

                } catch (Exception e) {
                    return "";
                }
            }
        }
        return "";
    }

    private CICompteIndividuel getCiPartenaire() throws Exception {

        if ((CIPartenaire != null) && !CIPartenaire.isNew()) {
            return CIPartenaire;

        }
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        if (!JadeStringUtil.isIntegerEmpty(getPartenaireNumeroAvs())) {
            ciMgr.setForNumeroAvs(getPartenaireNumeroAvs());
        } else {
            return null;
        }
        ciMgr.find();
        if (ciMgr.size() > 0) {
            CIPartenaire = (CICompteIndividuel) ciMgr.getFirstEntity();
            return CIPartenaire;
        } else {
            return null;
        }
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getDateDeNaissancePar() {
        try {
            return getCiPartenaire().getDateNaissance();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDateRevocation() {
        return dateRevocation;
    }

    public java.lang.String getIdJournal() {
        return idJournal;
    }

    /**
     * Renvoie l'id du journal � utiliser pour cette p�riode de splitting. Date de cr�ation : (14.05.2003 12:05:40)
     *
     * @param transaction
     *            la transaction � utiliser
     * @param noAvs
     *            utilis� comme libell� du journal si celui-ci n'est pas encore cr��. Peut �tre null.
     * @return l'id du journal
     */
    public String getJournalId(BTransaction transaction, String noAvs) throws Exception {
        if (journal == null) {
            // ouvrir journal splitting
            journal = new CIJournal();
            boolean idGiven = false;
            if (!JAUtil.isIntegerEmpty(idJournal)) {
                idGiven = true;
                journal.setIdJournal(getIdJournal());
                journal.retrieve(transaction);
            }
            if (journal.isNew()) {
                journal.setIdTypeInscription(CIJournal.CS_SPLITTING);
                journal.setIdTypeCompte(CIJournal.CS_CI);
                journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                if (JAUtil.isStringEmpty(noAvs)) {
                    journal.setLibelle("Splitting");
                } else {
                    journal.setLibelle("Splitting " + NSUtil.formatAVSUnknown(noAvs));
                }
                journal.add(transaction);
                if (!idGiven) {
                    setIdJournal(journal.getIdJournal());
                    if (!isNew()) {
                        wantCallMethodBefore(false);

                        this.update(transaction);

                    }
                }
            }
        }
        return idJournal;
    }

    public String getNomCaisseCommettante() {
        return caisseAgenceNom;
    }

    public String getNomPrenomPar() {
        try {
            return getCiPartenaire().getNomPrenom();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNSSPartenaireNNSS() {
        if (getPartenaireNumeroAvs().length() == 13) {
            return "true";

        } else {
            return "false";
        }
    }

    public String getNSSPartenaireWithoutPrefixe() {
        if (getPartenaireNumeroAvs().length() == 13) {
            return NSUtil.formatWithoutPrefixe(getPartenaireNumeroAvs(), true);
        } else {
            return NSUtil.formatWithoutPrefixe(getPartenaireNumeroAvs(), false);
        }
    }

    public String getPartenaireNumeroAvs() {
        return partenaireNumeroAvs;
    }

    public String getParticulier() {
        return particulier;
    }

    public String getPaysCodePar() {
        try {
            return getCiPartenaire().getPaysForNNSS();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysFormatePar() {
        try {
            return getCiPartenaire().getPaysFormate();
        } catch (Exception e) {
            return "";

        }
    }

    public String getPeriodeSplittingId() {
        return periodeSplittingId;
    }

    /**
     * @return the realCaisse
     */
    public String getRealCaisse() {
        return realCaisse;
    }

    public String getSexeCodePAr() {
        try {
            return getCiPartenaire().getSexeForNNSS();
        } catch (Exception e) {
            return "";

        }
    }

    public String getSexeLibellePar() {
        try {
            return getCiPartenaire().getSexeLibelle();
        } catch (Exception e) {
            return "";

        }
    }

    public String getTypeEnregistrement() {
        return typeEnregistrement;
    }

    /**
     * Renvoie le type d'enregistrement en mode WA de la caisse teant le CI
     */
    public String getTypeEnregistrementWA() {

        try {
            CIApplication app = (CIApplication) GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
            if ((typeEnregistrement != null) && (typeEnregistrement.length() == 6)) {
                if (app.isAnnoncesWA()) {
                    return typeEnregistrement.substring(0, 3) + "0" + typeEnregistrement.substring(4);
                }
            }
        } catch (Exception e) {
            return typeEnregistrement;
        }
        return typeEnregistrement;
    }

    public String giveFormatCaisseFusion() {
        String caisseRetour = "";
        try {
            if (!CIRassemblementOuverture.CS_OUVERTURE.equals(getTypeEnregistrement())
                    && !CIRassemblementOuverture.CS_SAISIE_MANUELLE.equals(getTypeEnregistrement())) {
                String caisseReel = getRealCaisse();
                String agenceTenant = getCaisseTenantCI();
                return caisseReel + "." + agenceTenant;
            }
        } catch (Exception e) {
            // Si la valeur n'est pas � jour en db(reprise) => on �vite de faire planter l'�cran
        }
        return caisseRetour;
    }

    public boolean isCaisseDiffferente() {
        boolean retour = false;
        try {
            retour = CIUtil.isCaisseDifferente(getSession());
        } catch (Exception e) {
            // potentiellement propertie inexistante => false
            retour = false;
        }
        return retour;
    }

    /**
     * R�voque la p�riode de splitting. Date de cr�ation : (16.05.2003 08:47:53)
     *
     * @param transaction
     *            la transaction � utiliser.
     * @exception Exception
     *                si une erreur survient.
     */
    public void revoquerEcrituresSplitting(BTransaction transaction) throws Exception {
        // efface le journal associ�
        CIJournal journal = new CIJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());
        journal.retrieve(transaction);
        if (!journal.isNew()) {
            journal.revoquerJournalSplitting(transaction);
        }
        setIdJournal("");
    }

    public void setAnneeDebut(String newAnneeDebut) {
        anneeDebut = newAnneeDebut;
    }

    public void setAnneeFin(String newAnneeFin) {
        anneeFin = newAnneeFin;
    }

    /**
     * Sp�cifie le no de la caisse et de l'agence au format "CCC.AAA". Date de cr�ation : (15.01.2003 15:44:00)
     *
     * @param caisse
     *            la caisse et l'agence
     */
    public void setCaisseAgenceCommettante(String caisse) {
        caisseAgence = caisse;
    }

    public void setCaisseCommettante(String newCaisseCommettante) {
        caisseCommettante = newCaisseCommettante;
    }

    /**
     * @param string
     */
    public void setCaisseTenantCI(String string) {
        caisseTenantCI = string;
    }

    public void setCompteIndividuelId(String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    public void setDateRevocation(String newDateRevocation) {
        dateRevocation = newDateRevocation;
    }

    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setPartenaireNumeroAvs(String newPartenaireNumeroAvs) {
        partenaireNumeroAvs = CIUtil.unFormatAVS(newPartenaireNumeroAvs);
    }

    public void setParticulier(String newParticulier) {
        particulier = newParticulier;
    }

    public void setPeriodeSplittingId(String newPeriodeSplittingId) {
        periodeSplittingId = newPeriodeSplittingId;
    }

    /**
     * @param realCaisse
     *            the realCaisse to set
     */
    public void setRealCaisse(String realCaisse) {
        this.realCaisse = realCaisse;
    }

    public void setTypeEnregistrement(String newTypeEnregistrement) {
        typeEnregistrement = newTypeEnregistrement;
    }

    /**
     * Effectue un splitting des �critures du CI sp�cifi� pour cette p�riode de splitting.
     *
     * @param transaction
     *            la transaction � utiliser.
     * @param ciAssure
     *            le CI de la personne concern�e Date de cr�ation : (06.01.2003 08:54:51)
     * @return le container des �critures de splitting.
     * @exception si
     *                une erreur survient
     */
    public CIEcrituresSplittingContainer splitter(BTransaction transaction, CICompteIndividuel ciAssure,
            CIApplication application, CIEcrituresSplittingContainer container) throws Exception {

        // CI ouvert pour le conjoint?
        CICompteIndividuel ciConjoint = null;
        if (!JAUtil.isStringEmpty(getPartenaireNumeroAvs())) {
            ciConjoint = CICompteIndividuel.loadCI(getPartenaireNumeroAvs(), transaction);
        }
        boolean need65 = false;

        // ci trouv� et ouvert
        // recherche des inscriptions de la p�riode de splitting
        // note: uniquement les �critures actives
        CIEcritureManager ecrituresMgr = new CIEcritureManager();
        ecrituresMgr.setSession(getSession());
        ecrituresMgr.setForCompteIndividuelId(getCompteIndividuelId());
        ecrituresMgr.setForIdTypeCompte(CIEcriture.CS_CI);
        ecrituresMgr.setForRassemblementOuvertureId("0");
        ecrituresMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        ecrituresMgr.orderByAnnee();
        ecrituresMgr.find(transaction);
        int anneeCourante = 0;
        long montant = 0;
        for (int i = 0; i < ecrituresMgr.size(); i++) {
            // �critures trouv�es
            CIEcriture ecriture = (CIEcriture) ecrituresMgr.getEntity(i);
            try {
                int anneeDebutInt = Integer.parseInt(getAnneeDebut());
                int anneeFinInt = Integer.parseInt(getAnneeFin());
                int anneeEcritureInt = Integer.parseInt(ecriture.getAnnee());
                if ((anneeDebutInt <= anneeEcritureInt) && (anneeFinInt >= anneeEcritureInt)) {
                    // �criture comprise dans la p�riode de splitting
                    if (ciConjoint == null) {
                        // ci prov
                        CICompteIndividuelManager ciMng = new CICompteIndividuelManager();
                        ciMng.setSession(getSession());
                        ciMng.orderByAvs(false);
                        ciMng.setForNumeroAvs(getPartenaireNumeroAvs());
                        ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                        ciMng.find(transaction);
                        if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
                            // Si il existe un CI au registre provisoire
                            // correspondant au num�ro d'AVS on s'accroche �
                            // celui-l�
                            ciConjoint = (CICompteIndividuel) ciMng.getEntity(0);
                        } else {
                            // cr�ation d'un ci provisoire
                            ciConjoint = new CICompteIndividuel();
                            ciConjoint.setSession(getSession());
                            ciConjoint.setNumeroAvs(getPartenaireNumeroAvs());
                            // Modif NNSS
                            if (CIUtil.isNNSSlengthOrNegate(getPartenaireNumeroAvs())) {
                                ciConjoint.setNnss(new Boolean(true));
                            } else {
                                ciConjoint.setNnss(new Boolean(false));
                            }
                            ciConjoint.setRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                            ciConjoint.setCiOuvert(new Boolean(true));
                            ciConjoint.add(transaction);
                        }
                        need65 = true;
                    }

                    // ajout du CI du conjoint
                    container.addCI(ciConjoint);
                    if (!CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                        if (anneeCourante != anneeEcritureInt) {
                            if (anneeCourante != 0) {
                                ArrayList result = addEcritureSplitting(transaction, anneeCourante, montant, ciAssure,
                                        ciConjoint, true);
                                if (result != null) {
                                    Iterator it = result.iterator();
                                    while (it.hasNext()) {
                                        container.addEcriture((CIEcriture) it.next());
                                    }
                                }

                            }
                            anneeCourante = anneeEcritureInt;
                            montant = 0;
                        }
                        if (!JAUtil.isIntegerEmpty(ecriture.getExtourne())
                                && !CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                                && !CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                                && !CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
                            montant -= Math.floor(Double.parseDouble(ecriture.getMontant()));
                        } else {
                            montant += Math.floor(Double.parseDouble(ecriture.getMontant()));
                        }
                        // count++;
                    }
                }
            } catch (NumberFormatException ex) {
                // conversion impossible, passer � l'�criture suivante
            }
        } // for
          // ajout derni�re �criture
        if (anneeCourante != 0) {
            ArrayList result = addEcritureSplitting(transaction, anneeCourante, montant, ciAssure, ciConjoint, true);
            if (result != null) {
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    container.addEcriture((CIEcriture) it.next());
                }
            }
        }
        // test si le conjoint a des �critures en suspens (apr�s cl�ture)
        if (!need65 && (ciConjoint != null)) {
            ArrayList ecrConjoint = container.getEcrituresSplitting(ciConjoint.getCompteIndividuelId());
            if (ecrConjoint != null) {
                Iterator it = ecrConjoint.iterator();
                while (it.hasNext()) {
                    CIEcriture ecrCo = (CIEcriture) it.next();
                    if (CIEcriture.CS_CI_SUSPENS.equals(ecrCo.getIdTypeCompte())) {
                        // si oui, un 65 est n�cessaire
                        need65 = true;
                    }
                }
            }
        }
        if (JAUtil.isStringEmpty(container.getIdAnnonce65()) && need65) {
            // effectuer un 65 --> 63 depuis le 01.01.19
            String idAnnonce = null;
            // test si pas d�j� pr�sent
            BISession sessionHE = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES).newSession(getSession());
            HEOutputAnnonceListViewBean annonceMgr = new HEOutputAnnonceListViewBean();
            annonceMgr.setISession(sessionHE);
            annonceMgr.setForNumeroAVS(getPartenaireNumeroAvs());
            annonceMgr.setForMotif("63");
            annonceMgr.find(transaction);
            if (!annonceMgr.isEmpty()) {
                // existe d�j�
                idAnnonce = ((HEOutputAnnonceViewBean) annonceMgr.getFirstEntity()).getIdAnnonce();
            } else {
                // cr�er le 65 --> 63 depuis le 01.01.19
                HashMap attributs = new HashMap();
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "63");
                // assur�
                attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getPartenaireNumeroAvs());
                idAnnonce = application.annonceARC(transaction, attributs, true);
            }
            container.setIdAnnonce65(idAnnonce);
        }
        return container;
    }

}
