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
 * Object représentant la période de splitting d'un compte individuel. Date de création : (12.11.2002 13:42:47)
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
                // uniquement le numéro de la caisse si rc_liste
                caisseAgence = application.getAdministration(getSession(), getCaisseCommettante(),
                        new String[] { "getCodeAdministration" }).getCodeAdministration();
            } else {
                // numéro et nom complet de la caisse
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
        // incrémente de +1 le numéro
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
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     *
     * @exception Exception
     *                si la lecture des propriétés échoue
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
     *            L'objet d'accès à la base
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
            // test périodes
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
     *            insère les inscriptions sans test du journal
     * @return les inscriptions générées ou null si le montant est à zéro.
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
            // idem pour conjoint (croisé)
            ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
            ecritureConjoint.setCI(ciConjoint);
            ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());

            // montant
            ecritureAssure.setMontant(String.valueOf(montantAssure));
            ecritureConjoint.setMontant(String.valueOf(montantConjoint));
            // année
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
     * Teste si l'écriture donnée doit être associée à cette période de splitting. Si le genre de l'écriture est
     * différent de 7 ou 8, l'écriture est automatiquement splitée.
     *
     * @param transaction
     *            la transaction à utiliser
     * @param ecriture
     *            l'écriture à tester
     * @return Les écritures de splitting. Si l'écriture donnée est déjà de genre 8 ou de genre 7, celle-ci est
     *         retournée (dans une liste). Null si l'écriture donnée ne correspond pas à la période.
     * @throws Exception
     *             si un problème survient
     */
    public ArrayList checkPeriode(BTransaction transaction, CIEcriture ecriture, boolean check) throws Exception {
        // test la révocation
        if (JAUtil.isStringEmpty(getDateRevocation()) && !JAUtil.isIntegerEmpty(getAnneeDebut())
                && !JAUtil.isIntegerEmpty(getAnneeFin())) {
            // test des périodes
            int anneeDebutInt = Integer.parseInt(getAnneeDebut());
            int anneeFinInt = Integer.parseInt(getAnneeFin());
            int anneeEcritureInt = Integer.parseInt(ecriture.getAnnee());
            if ((anneeDebutInt <= anneeEcritureInt) && (anneeFinInt >= anneeEcritureInt)) {
                // écriture comprise dans la période de splitting
                // test le genre
                if (!CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                    CICompteIndividuel ciAssure = ecriture.getCI(transaction, false);
                    CICompteIndividuel ciConjoint = null;
                    if (!JAUtil.isStringEmpty(getPartenaireNumeroAvs())) {
                        ciConjoint = CICompteIndividuel.loadCI(getPartenaireNumeroAvs(), transaction);
                    }
                    // Pas trouvé dans le RA, on regarde au registre provisoire
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
                        // On n'ajoute pas de CI si le numéro AVS est = à 0
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
                                    // assuré
                                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, ciConjoint.getNumeroAvs());
                                    // envoi
                                    application.annonceARC(transaction, attributs, false);
                                }
                            }
                        }
                    }
                    if (ciConjoint == null) {
                        // ci conjoint non présent... ne devrait pas arriver
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
     * Renvoie le numéro d'agence qui tient le ci
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
     * Renvoie l'id du journal à utiliser pour cette période de splitting. Date de création : (14.05.2003 12:05:40)
     *
     * @param transaction
     *            la transaction à utiliser
     * @param noAvs
     *            utilisé comme libellé du journal si celui-ci n'est pas encore créé. Peut être null.
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
            // Si la valeur n'est pas à jour en db(reprise) => on évite de faire planter l'écran
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
     * Révoque la période de splitting. Date de création : (16.05.2003 08:47:53)
     *
     * @param transaction
     *            la transaction à utiliser.
     * @exception Exception
     *                si une erreur survient.
     */
    public void revoquerEcrituresSplitting(BTransaction transaction) throws Exception {
        // efface le journal associé
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
     * Spécifie le no de la caisse et de l'agence au format "CCC.AAA". Date de création : (15.01.2003 15:44:00)
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
     * Effectue un splitting des écritures du CI spécifié pour cette période de splitting.
     *
     * @param transaction
     *            la transaction à utiliser.
     * @param ciAssure
     *            le CI de la personne concernée Date de création : (06.01.2003 08:54:51)
     * @return le container des écritures de splitting.
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

        // ci trouvé et ouvert
        // recherche des inscriptions de la période de splitting
        // note: uniquement les écritures actives
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
            // écritures trouvées
            CIEcriture ecriture = (CIEcriture) ecrituresMgr.getEntity(i);
            try {
                int anneeDebutInt = Integer.parseInt(getAnneeDebut());
                int anneeFinInt = Integer.parseInt(getAnneeFin());
                int anneeEcritureInt = Integer.parseInt(ecriture.getAnnee());
                if ((anneeDebutInt <= anneeEcritureInt) && (anneeFinInt >= anneeEcritureInt)) {
                    // écriture comprise dans la période de splitting
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
                            // correspondant au numéro d'AVS on s'accroche à
                            // celui-là
                            ciConjoint = (CICompteIndividuel) ciMng.getEntity(0);
                        } else {
                            // création d'un ci provisoire
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
                // conversion impossible, passer à l'écriture suivante
            }
        } // for
          // ajout dernière écriture
        if (anneeCourante != 0) {
            ArrayList result = addEcritureSplitting(transaction, anneeCourante, montant, ciAssure, ciConjoint, true);
            if (result != null) {
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    container.addEcriture((CIEcriture) it.next());
                }
            }
        }
        // test si le conjoint a des écritures en suspens (après clôture)
        if (!need65 && (ciConjoint != null)) {
            ArrayList ecrConjoint = container.getEcrituresSplitting(ciConjoint.getCompteIndividuelId());
            if (ecrConjoint != null) {
                Iterator it = ecrConjoint.iterator();
                while (it.hasNext()) {
                    CIEcriture ecrCo = (CIEcriture) it.next();
                    if (CIEcriture.CS_CI_SUSPENS.equals(ecrCo.getIdTypeCompte())) {
                        // si oui, un 65 est nécessaire
                        need65 = true;
                    }
                }
            }
        }
        if (JAUtil.isStringEmpty(container.getIdAnnonce65()) && need65) {
            // effectuer un 65 --> 63 depuis le 01.01.19
            String idAnnonce = null;
            // test si pas déjà présent
            BISession sessionHE = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES).newSession(getSession());
            HEOutputAnnonceListViewBean annonceMgr = new HEOutputAnnonceListViewBean();
            annonceMgr.setISession(sessionHE);
            annonceMgr.setForNumeroAVS(getPartenaireNumeroAvs());
            annonceMgr.setForMotif("63");
            annonceMgr.find(transaction);
            if (!annonceMgr.isEmpty()) {
                // existe déjà
                idAnnonce = ((HEOutputAnnonceViewBean) annonceMgr.getFirstEntity()).getIdAnnonce();
            } else {
                // créer le 65 --> 63 depuis le 01.01.19
                HashMap attributs = new HashMap();
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "63");
                // assuré
                attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getPartenaireNumeroAvs());
                idAnnonce = application.annonceARC(transaction, attributs, true);
            }
            container.setIdAnnonce65(idAnnonce);
        }
        return container;
    }

}
