/*
 * Créé le 28 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.beneficiairepc.AFJournalQuittance;
import globaz.naos.db.beneficiairepc.AFQuittance;
import globaz.naos.db.beneficiairepc.AFQuittanceManager;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.util.CIUtil;
import java.text.ParseException;
import java.util.List;

/**
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFQuittancePCGInscriptionCIProcess extends AFQuittancePCGProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class MassesDec {
        public String masseAC;
        public String masseAVS;
    }

    private Boolean comptabilisationCi = Boolean.FALSE;

    /**
     * Facturation des quittances PCG
     */

    private BSession sessionPavo = null;

    public AFQuittancePCGInscriptionCIProcess() {
        super();
    }

    /**
     * @param parent
     */
    public AFQuittancePCGInscriptionCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFQuittancePCGInscriptionCIProcess(BSession session) {
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        int nbCasATraiter = 0;
        try {
            double totalControle = 0.0;

            AFJournalQuittance journalQuittance = new AFJournalQuittance();
            journalQuittance.setSession(getSession());
            journalQuittance.setIdJournalQuittance(getIdJournalQuittances());
            journalQuittance.retrieve();
            setAnneeJournal(Integer.valueOf(journalQuittance.getAnnee()).intValue());

            AFQuittanceManager manager = intiManagerQuittanceAInscrire();
            setProgressScaleValue(manager.getSize());

            // On crée un journal CI par journal de quittance
            CIJournal journal = null;
            if (manager.getSize() > 0) {
                nbCasATraiter = manager.getSize();
                journal = creationJournalCI(journalQuittance, getTransaction());
            }

            for (int i = 0; i < manager.getSize(); i++) {
                incProgressCounter();
                totalControle = 0.0;
                AFQuittance quittance = (AFQuittance) manager.getEntity(i);
                // On va rechercher toutes les quittances pour chaque
                // bénéficiaire et par année
                AFQuittanceManager quittanceManager = rechercheQuittancesBeneficiaire(Boolean.FALSE, quittance,
                        getTransaction(), true);
                if (quittanceSurUnMois(quittanceManager, getTransaction())) {
                    if (checkQuittancesBeneficiaire(getTransaction(), quittanceManager)) {
                        if (!getTransaction().hasErrors()) {
                            // On va grouper les quittances par aide de ménage
                            // et par période continue
                            List<AFQuittance> listQuittance = grouperQuittanceParBeneficiaireSiContinuite(getCi(),
                                    quittanceManager, getTransaction(), true);
                            if ((getTransaction().hasErrors() == false) && (listQuittance != null)) {
                                for (int j = 0; j < listQuittance.size(); j++) {
                                    // Calcul montant effectif et si
                                    // l'affilié est soumis à l'avs et donc
                                    // en premier le nombre de mois
                                    AFQuittance quittanceGroupee = determinerQuittanceAvecMontantEffectif(getCi(),
                                            listQuittance.get(j), getTransaction());
                                    if (Double.valueOf(quittanceGroupee.getMontantEffectif()).doubleValue() > 0.0) {
                                        if (!getTransaction().hasErrors()) {
                                            totalControle += Double.valueOf(quittanceGroupee.getMontantEffectif())
                                                    .doubleValue();
                                            // On rentre les écritures pour
                                            // le journal
                                            if (Double.valueOf(quittanceGroupee.getMontantEffectif()).doubleValue() > 0.0) {
                                                creationEcrituresCI(journal.getIdJournal(), quittanceGroupee,
                                                        getTransaction());
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (getTransaction().hasErrors() || (totalControle <= 0.0)) {
                    String messageErreur = getTransaction().getErrors().toString();
                    if (JadeStringUtil.isEmpty(messageErreur)) {
                        if (totalControle <= 0.0) {
                            messageErreur += "\nLes montants ne dépassent pas la franchise de rentier!";
                        }
                    }
                    if (messageErreur.length() > 254) {
                        messageErreur = messageErreur.substring(0, 254);
                    }
                    getTransaction().clearErrorBuffer();
                    getTransaction().rollback();
                    // Quittancer les erreurs
                    quittancerEtat(getTransaction(), quittance, messageErreur, true, "");
                } else {
                    quittancerEtat(getTransaction(), quittance, "", true, "");
                }
                getTransaction().commit();
                // }
            }
            if (journal != null) {
                if (totalControle > 0) {
                    journal.setTotalControle(String.valueOf(totalControle));
                    getTransaction().disableSpy();
                    journal.update(getTransaction());
                    journal.updateInscription(getTransaction());
                    getTransaction().enableSpy();
                    if (!getTransaction().hasErrors()) {
                        journal.comptabiliser(null, null, getTransaction(), this);
                    }
                } else {
                    journal.delete();
                }
            }
            // Mise à jour du journal
            updateEtatJournalQuittance(getTransaction(), journalQuittance);
        } catch (Exception e) {
            getTransaction().rollback();
        } finally {
            // On imprime les erreurs
            if (nbCasATraiter > 0) {
                imprimerErreurs(AFQuittanceViewBean.ETAT_ERREUR_CI);
            }
        }
        return true;
    }

    private MassesDec creationEcrituresCI(String idJournal, AFQuittance quittance, BTransaction transaction) {
        FWCurrency masse = new FWCurrency();
        try {
            if (isSoumis()) {
                CIEcriture ecriture = new CIEcriture();
                ecriture.setSession(getSessionPavo());
                ecriture.setAvs(quittance.getNumAvsAideMenage());
                ecriture.setForAffilieParitaire(true);
                if (CIUtil.isNNSSlengthOrNegate(quittance.getNumAvsAideMenage())) {
                    ecriture.setNumeroavsNNSS("true");
                    ecriture.setAvsNNSS("true");
                }
                if (quittance.getMontantEffectif().length() > 0) {
                    FWCurrency montant = new FWCurrency(quittance.getMontantEffectif());
                    if (montant.isNegative()) {
                        // if
                        // ("-".equals(quittance.getTotalVerse().trim().substring(0,
                        // 1))) {
                        ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                        montant.negate();
                        quittance.setMontantCI(montant.toString());
                    }
                }
                if (!JadeStringUtil.isEmpty(getAffiliationLue().getBrancheEconomique())) {
                    ecriture.setBrancheEconomique(getAffiliationLue().getBrancheEconomique());
                }
                ecriture.setGenreEcriture(CIEcriture.CS_CIGENRE_1);
                ecriture.setGre("01");
                ecriture.setEmployeur(getAffiliationLue().getAffiliationId());
                ecriture.setMontant(quittance.getMontantEffectif());
                ecriture.setMoisDebut(quittance.getDateDebut());
                ecriture.setMoisFin(quittance.getDateFin());
                ecriture.setAnnee(quittance.getAnnee());
                ecriture.setIdJournal(idJournal);
                ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                // ecriture.setCodeSpecial(codeSpecial);
                ecriture.setNoSumNeeded(true);
                //
                if (quittance.getNumAvsAideMenage().length() == 13) {
                    ecriture.setNumeroavsNNSS("true");
                } else {
                    ecriture.setNumeroavsNNSS("false");
                }
                // ecriture.setEcritureId(toto);
                // TODO ajouter transaction après TESTS
                ecriture.add(transaction);
                masse.add(quittance.getMontantEffectif());
            } else {
                // TODO : signaler que pour cette aide de ménage, ce n'est pas
                // soumis.
            }
            // TODO enlever après TESTS
            // getTransaction().commit();
        } catch (Exception e) {
            this._addError(getTransaction(), "creation écritures CI : " + e.getMessage());
        }
        MassesDec massesDec = new MassesDec();
        massesDec.masseAVS = masse.toString();
        // TODO : calculer la masse AC correctement
        massesDec.masseAC = masse.toString();
        return massesDec;
    }

    private CIJournal creationJournalCI(AFJournalQuittance journalQuittance, BTransaction transaction) throws Exception {
        try {

            CIJournalManager jourManager = new CIJournalManager();
            jourManager.setSession(getSessionPavo());
            jourManager.getSession().connectSession(getSession());
            jourManager.setForReferenceExterneFacturation("PCG - " + getIdJournalQuittances());
            jourManager.setForIdEtat(CIJournal.CS_OUVERT);
            jourManager.find(getTransaction());
            // prendre le premier journal du manager
            if (jourManager.getSize() > 0) {
                return (CIJournal) jourManager.getFirstEntity();
            }
            // Créer le journal CI si il n'y en a pas d'ouver pour ce journal xxxx
            CIJournal jrnCI = new CIJournal();
            jrnCI.setSession(getSessionPavo());
            jrnCI.setAnneeCotisation(journalQuittance.getAnnee());
            jrnCI.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            // On fait un journal de type inscriptions journalières car pour le
            // type déclaration de salaire
            // nous rencontrons un problème d'existance de l'écriture insérée
            jrnCI.setIdTypeInscription(CIJournal.CS_INSCRIPTIONS_JOURNALIERES);
            jrnCI.setRefExterneFacturation("PCG - " + getIdJournalQuittances());
            jrnCI.setLibelle(journalQuittance.getDescriptionJournal());
            jrnCI.add(transaction);
            return jrnCI;
        } catch (Exception e) {
            transaction.addErrors("Création Journal CI : " + journalQuittance.getDescriptionJournal() + " "
                    + e.getMessage());
            return null;
        }
    }

    public Boolean getComptabilisationCi() {
        return comptabilisationCi;
    }

    /**
     * Cette fonction return true, si la periode1 == periode2+1 c'est à dire si la période1, continue la période2
     * exemple : période1 : 1.10.2007 période2 : 30.09.2007 -> comme 30.09.2007 + 1 jours == 1.10.2007 -> return True
     * 
     * @param periode1
     * @param periode2
     * @return boolean
     * @throws ParseException
     */
    /*
     * private static boolean continuiteDate(String periode1, String periode2) throws ParseException { int mois1 =
     * Integer.valueOf(periode1.substring(3, 5)).intValue(); int mois2 = Integer.valueOf(periode2.substring(3,
     * 5)).intValue(); if (mois1 == mois2 || (mois1 + 1) == mois2 || mois1 == (mois2 + 1)) return true; else return
     * false; }
     */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "Inscription CI des quittances PCG";
    }

    private BSession getSessionPavo() {
        if (sessionPavo == null) {
            try {
                sessionPavo = new BSession(CIApplication.DEFAULT_APPLICATION_PAVO);
                getSession().connectSession(sessionPavo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionPavo;
    }

    protected AFQuittanceManager intiManagerQuittanceAInscrire() throws Exception {
        AFQuittanceManager manager = new AFQuittanceManager();
        manager.setSession(getSession());
        manager.setSelectionBeneficiairePC(true);
        manager.setForIdJournalQuittance(getIdJournalQuittances());
        manager.setForCiNonTraite(Boolean.TRUE);
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_CI + ", " + AFQuittanceViewBean.ETAT_FACTURE);
        manager.find(getTransaction());
        return manager;
    }

    /*
     * private coti creationCotisation() { // MassesDec masses = creationEcrituresCI(journal.getIdJournal(), vecteur);
     * DSFacturationHandler.coti cotisation = null; AFCotisation coti; // String tauxAssurance =
     * coti.getAssurance().getTaux("31.12"+"2007").getValeurTotal(); // String fractionAssurance =
     * coti.getAssurance().getTaux("31.12"+"2007").getFraction(); // String idAssurance =
     * coti.getAssurance().getAssuranceId(); // idRubrique d'après idAssurance // String montantDeclaration : /
     * _bCotisation =new BigDecimal( AFCalculAssurance.calculResultatAssurance( dateDebut, dateFin,
     * //getCotisation().getTauxList(dateFin), getCotisation().findTaux(dateFin,getMontantDeclaration()), new
     * Double(JANumberFormatter.deQuote(montantDec)).doubleValue(), getSession()));
     */
    // DSLigneDeclarationViewBean... getMontantFacture
    /*
     * String montantFacture; String masseFacture; return cotisation; }
     */

    /*
     * private void creationDeclarationSalaire(AFQuittance quittance, MassesDec masses) throws Exception {
     * DSDeclarationViewBean ds = new DSDeclarationViewBean(); ds.setSession(getSession());
     * ds.setAffiliationId(quittance.getIdAffBeneficiaire()); ds.setMasseSalTotal(masses.masseAVS);
     * ds.setMasseACTotal(masses.masseAC); // TODO set annee des quittances // ds.setDateRetourDes(JOUR DU TRAITEMENT);
     * // ds.setAnnee(ANNEE QUITTANCE) // ds.setSoumisInteret(EXEMPTE) ds.add(getTransaction()); validerDS(ds); }
     */

    /*
     * private void validerDS(DSDeclarationViewBean ds) throws Exception { DSProcessValidation process = new
     * DSProcessValidation(); process.setEMailAddress(getEMailAddress()); process.setSession(getSession());
     * process.setIdDeclaration(ds.getIdDeclaration()); process.executeProcess(); }
     */

    /**
     * Cette fonction return true, si la periode1 == periode2+1 c'est à dire si la période1, continue la période2
     * exemple : période1 : 1.10.2007 période2 : 30.09.2007 -> comme 30.09.2007 + 1 jours == 1.10.2007 -> return True
     * 
     * @param periode1
     * @param periode2
     * @return boolean
     * @throws ParseException
     */
    /*
     * private static boolean continuiteDate(String periode1, String periode2) throws ParseException { SimpleDateFormat
     * dateFormat = new SimpleDateFormat("dd.MM.yyyy"); Date date1 = dateFormat.parse(periode1); Date date2 =
     * dateFormat.parse(periode2); Calendar calendar1 = Calendar.getInstance(); calendar1.setTime(date1); Calendar
     * calendar2 = Calendar.getInstance(); calendar2.setTime(date2); // On regarde si le jour calendar2+1 est egal à
     * calendar1 calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
     * calendar2.get(Calendar.DAY_OF_MONTH) + 1); if (calendar1.getTime().equals(calendar2.getTime())) return true; else
     * return false; }
     */

    private boolean isSoumis() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setComptabilisationCi(Boolean comptabilisationCi) {
        this.comptabilisationCi = comptabilisationCi;
    }
}
