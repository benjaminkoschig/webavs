package globaz.naos.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.beneficiairepc.AFJournalQuittance;
import globaz.naos.db.beneficiairepc.AFQuittance;
import globaz.naos.db.beneficiairepc.AFQuittanceManager;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.naos.itext.beneficiairesPC.AFBeneficiaireFacturationErreurs;
import globaz.naos.util.AFUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiersManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AFQuittancePCGProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    public class MassesDec {
        public String masseAC;
        public String masseAVS;
    }

    protected final static double FRANCHISE_ANNUELLE = 16800;
    protected final static double FRANCHISE_MENSUELLE = 1400;

    private AFAffiliation affiliationLue = null;

    private int anneeJournal = 0;
    private CICompteIndividuel ci = null;
    BProcess context;
    private String idJournalQuittances = "";
    IFAPassage passage = new FAPassage();

    public AFQuittancePCGProcess() {
        super();
    }

    /**
     * @param parent
     */
    public AFQuittancePCGProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFQuittancePCGProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return true;
    }

    protected void checkPeriodeValide(BTransaction transaction, AFQuittance quittance) throws NumberFormatException {
        int moisDebut = Integer.valueOf(quittance.getPeriodeDebut().substring(3, 5)).intValue();
        int moisFin = Integer.valueOf(quittance.getPeriodeFin().substring(3, 5)).intValue();
        if ((moisFin - moisDebut) > 1) {
            transaction.addErrors("Les quittances ne peuvent pas être sur plusieurs mois. ");
        }
    }

    protected boolean checkQuittancesBeneficiaire(BTransaction transaction, AFQuittanceManager quittanceManager) {
        try {

            for (int i = 0; i < quittanceManager.getSize(); i++) {
                AFQuittance quittance = (AFQuittance) quittanceManager.get(i);
                // Contrôle si l'id affiliation est toujours valable (ex: en cas de radiation depuis la saisie)
                // Comme c'est le même idaffiliation. ne charger faire que la première fois
                AFAffiliation aff = getAffiliationLue();
                if (aff == null || !aff.getAffiliationId().equals(quittance.getIdAffBeneficiaire())) {
                    aff = loadAffiliation(quittance.getIdAffBeneficiaire(), transaction);
                    setAffiliationLue(aff);

                }
                if (getTransaction().hasErrors() == true) {
                    return false;
                }
                returnAffiliationValide(transaction, quittance, aff);

                if (getTransaction().hasErrors() == true) {
                    return false;
                }
                // Tests si date valide
                checkPeriodeValide(transaction, quittance);
                // Test si affiliation toujours valide depuis la saisie
                if (getTransaction().hasErrors() == true) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean controleNombreQuittancesParRapportAffiliation(AFQuittanceManager quittanceManager,
            BTransaction transaction) {
        try {
            AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            if (app.isControleNombreQuittances()) {
                return true;
            }

            int nbMoisAffilie = Integer.parseInt(CPToolBox.nbMoisDansPeriode(getAffiliationLue().getDateDebut(),
                    getAffiliationLue().getDateFin(), quittanceManager.getForAnnee()));

            if (quittanceManager.size() >= nbMoisAffilie) {
                return true;
            } else {
                System.out.println(getAffiliationLue().getAffilieNumero());
                transaction.addErrors("Pas assez de quittances pour l'affilié. ");
                return false;
            }
        } catch (Exception e) {
            transaction.addErrors("Erreur pendant la recherche de l'affiliation : "
                    + quittanceManager.getForIdTiersBeneficiaire() + " : " + e.getMessage());
            return false;
        }

    }

    protected void cumulMontant(List<AFQuittance> listQuittance, int i) throws NumberFormatException {
        // Si c'est le même mois, on cumule le montant
        AFQuittance quittanceAAjouter = listQuittance.get(i);
        quittanceAAjouter.setSession(getSession());
        quittanceAAjouter.setMontantCI(String.valueOf(Float.parseFloat((listQuittance.get(i)).getMontantCI())
                + Float.parseFloat((listQuittance.get(i + 1)).getMontantCI())));
        quittanceAAjouter.setTotalVerse(String.valueOf(Float.parseFloat((listQuittance.get(i)).getTotalVerse())
                + Float.parseFloat((listQuittance.get(i + 1)).getTotalVerse())));
        quittanceAAjouter.setIdAffBeneficiaireBrut(quittanceAAjouter.getIdAffBeneficiaire());
        listQuittance.set(i + 1, quittanceAAjouter);
        listQuittance.set(i, null);
    }

    protected void determinerMontantCI(AFQuittance quittance, BTransaction transaction, String taux) {
        // Mise à jour du montant CI qui sera inscrit lors du processus d'inscription CI
        // Si le revenu saisi est un revenu net (taux saisi <>0 lors du lancement du process de facturation) alors on
        // recalcule le montant brut
        // sinon on prend le montant saisi sans la quittance
        try {
            if (!JadeStringUtil.isBlankOrZero(taux)) {
                BigDecimal facteurConversion = new BigDecimal("100");
                facteurConversion = facteurConversion.subtract(new BigDecimal(taux));
                facteurConversion = facteurConversion.divide(new BigDecimal(100));
                BigDecimal montantBrut = new BigDecimal(quittance.getTotalVerse()).divide(facteurConversion, 2,
                        RoundingMode.DOWN);
                quittance.setMontantCI(JANumberFormatter.round(montantBrut.toString(), 0.05, 2, JANumberFormatter.INF));
            } else {
                quittance.setMontantCI(quittance.getTotalVerse());
            }
        } catch (Exception e) {
            this._addError(transaction, quittance.getNumAffilieBeneficiaire() + " " + e.getMessage().toString());
        }
    }

    protected AFQuittance determinerQuittanceAvecMontantEffectif(CICompteIndividuel ci, AFQuittance quittance,
            BTransaction transaction) {
        // Calcul du nombre de mois
        // System.out.println(quittanceVector.getNumAffilie());
        // System.out.println(quittanceVector.getPeriodeDebut() + " - " + quittanceVector.getPeriodeFin());
        boolean isAgeAvs = false;
        int moisNaissance = 0;
        int moisDebut = Integer.parseInt(quittance.getPeriodeDebut().substring(3, 5));
        int moisFin = Integer.parseInt(quittance.getPeriodeFin().substring(3, 5));
        int nbreMois = (moisFin - moisDebut) + 1;
        quittance.setNbreMois(nbreMois);
        TITiersManager tiersManager = new TITiersManager();
        tiersManager.setSession(getSession());
        try {

            setCi(findEnteteCI(quittance.getNumAvsAideMenage()));
            quittance.setRentier(AFUtil.isRentierForAnnee(getCi().getSexe(), getCi().getDateNaissance(),
                    getAnneeJournal()));
            moisNaissance = Integer.valueOf(getCi().getDateNaissance().substring(3, 5));
            isAgeAvs = AFUtil.isAgeAVS(getCi().getSexe(), getCi().getDateNaissance(), getAnneeJournal());
        } catch (Exception e) {
            // e.printStackTrace();
            String err = "CI non ouvert ou date de naissance ou sexe non renseigné!";
            if (transaction.getErrors().toString().equalsIgnoreCase(err) == false) {
                transaction.addErrors(err);
            }
        }
        // Si il est rentier on recalcule les masses, si nouvelle masse
        // supérieur à 0 est soumis
        if (!quittance.isRentier()
                || (isAgeAvs && (moisNaissance != 12) && (Integer.valueOf(quittance.getPeriodeFin().substring(3, 5)) <= moisNaissance))) {
            quittance.setMontantEffectif(quittance.getMontantCI());
        } else {
            try {
                double montant = 0;
                if (nbreMois == 12) {
                    // Annuel
                    montant = Double.valueOf(quittance.getMontantCI()).doubleValue()
                            - AFQuittancePCGProcess.FRANCHISE_ANNUELLE;
                } else {
                    // Mensuel
                    montant = Double.valueOf(quittance.getMontantCI()).doubleValue()
                            - (AFQuittancePCGProcess.FRANCHISE_MENSUELLE * nbreMois);

                }
                quittance.setMontantEffectif(String.valueOf(montant));

            } catch (Exception e) {
                quittance.setMontantEffectif("0");
            }
        }
        return quittance;
    }

    protected CICompteIndividuel findEnteteCI(String numAvs) throws Exception {
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(getSession());
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.setForNumeroAvs(NSUtil.unFormatAVS(numAvs));
        ciManager.find();
        CICompteIndividuel ci = (CICompteIndividuel) ciManager.getFirstEntity();
        return ci;
    }

    public AFAffiliation getAffiliationLue() {
        return affiliationLue;
    }

    public int getAnneeJournal() {
        return anneeJournal;
    }

    public CICompteIndividuel getCi() {
        return ci;
    }

    public BProcess getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "facturation des quittances PCG";
    }

    public String getIdJournalQuittances() {
        return idJournalQuittances;
    }

    /**
     * @return
     */
    public IFAPassage getPassage() {
        return passage;
    }

    protected List<AFQuittance> grouperQuittanceParBeneficiaireSiContinuite(CICompteIndividuel ci,
            AFQuittanceManager quittanceManager, BTransaction transaction, boolean processCI) throws Exception {

        if (quittanceManager.size() == 0) {
            return null;
        }

        try {
            if (controleNombreQuittancesParRapportAffiliation(quittanceManager, transaction) == false) {
                return null;
            }

            // On va itérer les quittances de chaque journal
            // vecteur contenant toutes les quittances groupés par Aide Ménage
            // et par période continue
            int moisAnniversaire = 0;
            List<AFQuittance> vecteurFinal = new ArrayList<AFQuittance>();
            List<AFQuittance> vecteurTemp = orderByDate(quittanceManager);

            // Les journaux ne contienne qu'une seule quittance
            if (quittanceManager.size() == 1) {
                AFQuittance quittance = (AFQuittance) quittanceManager.getFirstEntity();
                vecteurFinal.add(quittance);
                return vecteurFinal;
            }

            if (quittanceManager.size() >= 2) {
                for (int i = 0; i < (vecteurTemp.size() - 1); i++) {
                    String numAvs1 = ((vecteurTemp.get(i)).getNumAvsAideMenage());
                    String numAvs2 = ((vecteurTemp.get(i + 1)).getNumAvsAideMenage());
                    int moisFin1 = Integer.valueOf((vecteurTemp.get(i)).getPeriodeFin().substring(3, 5)).intValue();
                    int moisDebut2 = Integer.valueOf((vecteurTemp.get(i + 1)).getPeriodeDebut().substring(3, 5))
                            .intValue();
                    if (numAvs1.equals(numAvs2)) {
                        try {
                            // Recherche de l'entête CI
                            setCi(findEnteteCI(numAvs1));
                            moisAnniversaire = Integer.valueOf(getCi().getDateNaissance().substring(3, 5));
                        } catch (Exception e) {
                            // e.printStackTrace();
                            transaction.addErrors("CI non ouvert ou date de naissance ou sexe non renseigné! ");
                        }
                        if (moisFin1 == moisDebut2) {
                            cumulMontant(vecteurTemp, i);
                        }
                        if (((moisFin1 + 1) == moisDebut2)) {
                            // Si c'est le mois de passage à la retraite on ne regroupe pas, car le mois suivant doit
                            // être traité selon la franchose
                            if (AFUtil.isAgeAVS(getCi().getSexe(), getCi().getDateNaissance(), getAnneeJournal())) {
                                if (moisFin1 != moisAnniversaire) {
                                    regrouperPeriode(vecteurTemp, i, processCI);
                                }
                            } else {
                                // Il y a continuité
                                // on regroupe
                                regrouperPeriode(vecteurTemp, i, processCI);
                            }
                        }
                    }
                }
            }

            // Il n'y a pas de quittances dans les journaux, le vecteur
            // Final est donc vide

            for (int i = 0; i < vecteurTemp.size(); i++) {
                if (vecteurTemp.get(i) != null) {
                    vecteurFinal.add(vecteurTemp.get(i));
                }
            }
            return vecteurFinal;
        } catch (Exception e) {
            transaction.addErrors("Erreur Grouper Bénéficiaires Continuité" + e.getMessage());
            return null;
        }
    }

    protected void imprimerErreurs(String typeErreur) {
        AFQuittanceManager manager = new AFQuittanceManager();
        manager.setSession(getSession());
        manager.setForIdJournalQuittance(getIdJournalQuittances());
        manager.setForEtatQuittances(typeErreur);
        manager.setOrder("MAQTIB");
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        try {
            manager.find(getTransaction());
            // création du doc xls
            AFBeneficiaireFacturationErreurs excelDoc = new AFBeneficiaireFacturationErreurs(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0178CAF");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Liste des erreurs");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    protected AFAffiliation loadAffiliation(String idAffiliation, BTransaction transaction) throws Exception {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(idAffiliation);
        aff.retrieve();
        if (aff.isNew()) {
            this._addError(transaction, getSession().getLabel("2050"));
            return null;
        } else {
            return aff;
        }
    }

    private List<AFQuittance> orderByDate(AFQuittanceManager quittanceManager) {
        List<AFQuittance> listQuitance = new ArrayList<AFQuittance>();
        for (int i = 0; i < quittanceManager.getSize(); i++) {
            AFQuittance quittance = (AFQuittance) quittanceManager.get(i);
            listQuitance.add(quittance);
        }
        return triBulle(listQuitance);
    }

    protected void quittancerEtat(BTransaction transaction, AFQuittance quittance, String messageErreur,
            boolean processCI, String taux) throws Exception {
        /* Si messageErreur est vide => les quittances du cas sont ok et l'état peut passer à Généré */
        AFQuittanceManager quittanceErreurManager = new AFQuittanceManager();
        quittanceErreurManager.setSession(getSession());
        quittanceErreurManager.setForAnnee(quittance.getAnnee());
        quittanceErreurManager.setForIdTiersBeneficiaire(quittance.getIdAffBeneficiaire());
        quittanceErreurManager.setForIdJournalQuittance(getIdJournalQuittances());
        quittanceErreurManager.find(transaction);
        for (int k = 0; k < quittanceErreurManager.size(); k++) {
            AFQuittance quittanceAtraite = (AFQuittance) quittanceErreurManager.get(k);
            if (JadeStringUtil.isEmpty(messageErreur) == false) {
                if (processCI) {
                    quittanceAtraite.setEtatQuittance(AFQuittanceViewBean.ETAT_ERREUR_CI);
                } else {
                    quittanceAtraite.setEtatQuittance(AFQuittanceViewBean.ETAT_ERREUR_FACTU);
                }
                // On met un message d'erreur seulement sur la 1ère quittance
                if (k == 0) {
                    quittanceAtraite.setMessageErreur(messageErreur);
                } else {
                    quittanceAtraite.setMessageErreur("");
                }
            } else {
                if (processCI) {
                    quittanceAtraite.setEtatQuittance(AFQuittanceViewBean.ETAT_CI);
                } else {
                    quittanceAtraite.setEtatQuittance(AFQuittanceViewBean.ETAT_FACTURE);
                    determinerMontantCI(quittanceAtraite, transaction, taux);
                }
                quittanceAtraite.setMessageErreur("");
            }
            quittanceAtraite.wantValidate(new Boolean(false));
            quittanceAtraite.setUpdateFromProcess(true);
            quittanceAtraite.update(transaction);
        }
    }

    protected boolean quittanceSurUnMois(AFQuittanceManager quittanceManager, BTransaction transaction) {
        for (int i = 0; i < quittanceManager.size(); i++) {
            AFQuittance quittance = (AFQuittance) quittanceManager.get(i);
            checkPeriodeValide(transaction, quittance);
        }
        if (transaction.hasErrors()) {
            return false;
        } else {
            return true;
        }
    }

    protected AFQuittanceManager rechercheQuittancesBeneficiaire(Boolean wantSuppressionEtatGenere,
            AFQuittance quittance, BTransaction transaction, boolean processCI) throws Exception {
        try {
            AFQuittanceManager quittanceManager = new AFQuittanceManager();
            quittanceManager.setSession(getSession());
            // quittanceManager.setForEtat(CodeSystem.BENEFICIAIRE_ETAT_VALIDE);
            quittanceManager.setOrder("MAQTIB ASC, MAQTAM ASC, MAQPDE ASC");
            if (wantSuppressionEtatGenere.equals(Boolean.TRUE)) {
                // On est forcément en facturation
                quittanceManager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_FACTU + ", "
                        + AFQuittanceViewBean.ETAT_OUVERT + ", " + AFQuittanceViewBean.ETAT_FACTURE);
            } else {
                if (processCI == false) {
                    quittanceManager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_FACTU + ", "
                            + AFQuittanceViewBean.ETAT_OUVERT);
                } else {
                    quittanceManager.setForCiNonTraite(Boolean.TRUE);
                }
            }
            quittanceManager.setForIdTiersBeneficiaire(quittance.getIdAffBeneficiaire());
            quittanceManager.setForAnnee(quittance.getAnnee());
            quittanceManager.setForIdJournalQuittance(getIdJournalQuittances());
            quittanceManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            quittanceManager.find(transaction);
            return quittanceManager;
        } catch (Exception e) {
            transaction.addErrors("Erreur Recherche Quittances Beneficiaire : " + e.getMessage());
            return null;
        }

    }

    protected void regrouperPeriode(List<AFQuittance> listQuittance, int i, boolean processCI)
            throws NumberFormatException {
        AFQuittance quittanceAAjouter = listQuittance.get(i);
        quittanceAAjouter.setSession(getSession());
        if (processCI) {
            quittanceAAjouter.setMontantCI(String.valueOf(Float.parseFloat((listQuittance.get(i)).getMontantCI())
                    + Float.parseFloat((listQuittance.get(i + 1)).getMontantCI())));
        } else {
            quittanceAAjouter.setTotalVerse(String.valueOf(Float.parseFloat((listQuittance.get(i)).getTotalVerse())
                    + Float.parseFloat((listQuittance.get(i + 1)).getTotalVerse())));
        }
        quittanceAAjouter.setIdAffBeneficiaireBrut(quittanceAAjouter.getIdAffBeneficiaire());
        quittanceAAjouter.setPeriodeFin((listQuittance.get(i + 1)).getPeriodeFin());
        listQuittance.set(i + 1, quittanceAAjouter);
        listQuittance.set(i, null);
    }

    protected void returnAffiliationValide(BTransaction transaction, AFQuittance quittance, AFAffiliation aff)
            throws Exception {
        /* Re actualisation de l'id affiliation en cas de radiation */
        if (BSessionUtil.compareDateFirstLower(getSession(), quittance.getPeriodeDebut(), getAffiliationLue()
                .getDateDebut())
                || ((!JadeStringUtil.isEmpty(getAffiliationLue().getDateFin()) && BSessionUtil.compareDateFirstGreater(
                        getSession(), quittance.getPeriodeFin(), getAffiliationLue().getDateFin())))) {
            aff = AFAffiliation.returnAffiliation(getSession(), getAffiliationLue().getAffilieNumero(), true, false,
                    quittance.getPeriodeDebut(), quittance.getPeriodeFin());
            if (aff != null) {
                setAffiliationLue(aff);
            }
        }
        if (BSessionUtil.compareDateFirstLower(getSession(), quittance.getPeriodeDebut(), getAffiliationLue()
                .getDateDebut())) {
            transaction.addErrors("Date de début de la quittance inférieure à l'affiliation. ");
        }
        if (!JadeStringUtil.isEmpty(getAffiliationLue().getDateFin())) {
            if (BSessionUtil.compareDateFirstGreater(getSession(), quittance.getPeriodeFin(), getAffiliationLue()
                    .getDateFin())) {
                transaction.addErrors("Date de fin de la quittance supérieure à l'affiliation. ");
            }
        }
    }

    public void setAffiliationLue(AFAffiliation affiliationLue) {
        this.affiliationLue = affiliationLue;
    }

    public void setAnneeJournal(int anneeJournal) {
        this.anneeJournal = anneeJournal;
    }

    public void setCi(CICompteIndividuel ci) {
        this.ci = ci;
    }

    public void setContext(BProcess context) {
        this.context = context;
    }

    public void setIdJournalQuittances(String idJournalQuittances) {
        this.idJournalQuittances = idJournalQuittances;
    }

    /*
     * @param passage
     */
    public void setPassage(IFAPassage passage) {
        this.passage = passage;
    }

    public List<AFQuittance> triBulle(List<AFQuittance> tableau) {
        int longueur = tableau.size();
        boolean permut;

        do {
            // hypothése : le tableau est trié
            permut = false;
            for (int i = 0; i < (longueur - 1); i++) {
                // Teste si 2 éléments successifs sont dans le bon ordre ou non
                if (Integer.valueOf((tableau.get(i)).getPeriodeDebut().substring(3, 5)).intValue() > Integer.valueOf(
                        (tableau.get(i + 1)).getPeriodeDebut().substring(3, 5)).intValue()) {
                    if ((tableau.get(i)).getNumAvsAideMenage().equals((tableau.get(i + 1)).getNumAvsAideMenage())) {
                        // s'ils ne le sont pas on échange leurs positions
                        AFQuittance quittanceTemp = tableau.get(i);
                        AFQuittance quittanceTemp2 = tableau.get(i + 1);
                        tableau.set(i + 1, quittanceTemp);
                        tableau.set(i, quittanceTemp2);
                        permut = true;
                    }
                }
            }
        } while (permut);
        return tableau;
    }

    protected void updateEtatJournalQuittance(BTransaction transaction, AFJournalQuittance jrn) throws Exception {
        int nbGenere = 0;
        int nbErreur = 0;
        int nbComptabilise = 0;
        int nbTotal = 0;
        if ((jrn != null) && (jrn.isNew() == false)) {
            AFQuittanceManager jrnMng = new AFQuittanceManager();
            jrnMng.setSession(getSession());
            jrnMng.setForIdJournalQuittance(getIdJournalQuittances());
            jrnMng.setForEtatQuittances(AFQuittanceViewBean.ETAT_FACTURE);
            nbGenere = jrnMng.getCount();
            jrnMng.setForEtatQuittances(AFQuittanceViewBean.ETAT_ERREUR_FACTU);
            nbErreur = jrnMng.getCount();
            jrnMng.setForEtatQuittances(AFQuittanceViewBean.ETAT_CI);
            nbComptabilise = jrnMng.getCount();
            jrnMng.setForEtatQuittances("");
            nbTotal = jrnMng.getCount();

            jrn.setEtat(AFQuittanceViewBean.ETAT_OUVERT);
            if ((nbGenere > 0) || (nbErreur > 0)) {
                if (nbGenere == nbTotal) {
                    jrn.setEtat(AFQuittanceViewBean.ETAT_FACTURE);
                } else {
                    jrn.setEtat(AFQuittanceViewBean.ETAT_FACTURE_PARTIEL);
                }
            }

            if (nbComptabilise > 0) {
                if (nbComptabilise == nbTotal) {
                    jrn.setEtat(AFQuittanceViewBean.ETAT_CI);
                } else {
                    jrn.setEtat(AFQuittanceViewBean.ETAT_CI_PARTIEL);
                }
            }
            jrn.update();
            if (transaction.hasErrors() == false) {
                transaction.commit();
            }
        }
    }
}
