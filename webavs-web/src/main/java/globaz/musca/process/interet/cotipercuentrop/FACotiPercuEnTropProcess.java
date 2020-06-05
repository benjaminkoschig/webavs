package globaz.musca.process.interet.cotipercuentrop;

import ch.globaz.common.properties.CommonProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.interet.cotipercuentrop.cotpers.FARubriqueCotPers;
import globaz.musca.db.interet.cotipercuentrop.cotpers.FARubriqueCotPersManager;
import globaz.musca.db.interet.cotipercuentrop.montantsoumis.FACotiPercuEnTropParRubrique;
import globaz.musca.db.interet.cotipercuentrop.montantsoumis.FACotiPercuEnTropParRubriqueManager;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlan;
import globaz.musca.process.interet.util.FAInteretGenericUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseParSection;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseParSectionManager;
import globaz.osiris.db.interet.util.sectionfacturee.CAMontantFacture;
import globaz.osiris.db.interet.util.sectionfacturee.CAMontantFactureManager;
import globaz.osiris.db.interet.util.sectionfacturee.CASectionFacturee;
import globaz.osiris.db.interet.util.sectionfacturee.CASectionFactureeManager;
import globaz.osiris.db.interet.util.tauxParametres.CATauxParametre;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAPlanCalculInteret;
import globaz.osiris.db.interets.CAPlanCalculInteretManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FACotiPercuEnTropProcess extends BProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String ONZE_JANVIER = "0111";
    private static final String PREMIER_JANVIER = "0101";

    private JADate dateJournalFacturation = null;
    private FAPassage passage = null;

    /**
     * Commentaire relatif au constructeur FACotiPercuEnTropProcess.
     */
    public FACotiPercuEnTropProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur FACotiPercuEnTropProcess.
     *
     * @param parent BProcess
     */
    public FACotiPercuEnTropProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette méthode exécute le processus de facturation des intérêts rémunératoires pour cotisations perçuent en trop.
     *
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        BTransaction osirisTransaction = null;

        try {
            BSession osirisSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession(getSession());
            osirisTransaction = (BTransaction) osirisSession.newTransaction();
            osirisTransaction.openTransaction();

            FWCurrency limiteExempte = getLimiteExempte(osirisSession);

            CAPlanCalculInteretManager planManager = new CAPlanCalculInteretManager();
            planManager.setSession(osirisSession);

            planManager.find(osirisTransaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < planManager.size(); i++) {
                CAPlanCalculInteret plan = (CAPlanCalculInteret) planManager.get(i);

                executeProcessParPlan(osirisSession, osirisTransaction, plan, limiteExempte);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());

            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            }

            if (osirisTransaction != null) {
                try {
                    osirisTransaction.rollback();
                } catch (Exception eTransactionRollback) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }
        } finally {
            if (osirisTransaction != null) {
                try {
                    if (osirisTransaction.hasErrors()) {
                        osirisTransaction.rollback();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                } finally {
                    try {
                        osirisTransaction.closeTransaction();
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                    }
                }
            }
        }

        return ((getMemoryLog() != null) && !getMemoryLog().hasErrors());
    }

    /**
     * Somme les écritures (paiements) par année en les plafonands au montant max de la section.
     *
     * @param lastIdSection
     * @param lastYear
     * @param montantCumuleFactureParSection
     * @param montantAffecteFactureSomme
     * @param result
     */
    private void addEcritureToYear(String lastIdSection, String lastYear,
                                   TreeMap<String, String> montantCumuleFactureParSection, BigDecimal montantAffecteFactureSomme,
                                   TreeMap<String, FWCurrency> result) {
        // Limite le montant payé sur la section au montant facturé.
        if (montantCumuleFactureParSection.containsKey(lastIdSection)) {
            FWCurrency tmp = new FWCurrency("" + montantCumuleFactureParSection.get(lastIdSection));
            tmp.negate();
            if (montantAffecteFactureSomme.compareTo(tmp.getBigDecimalValue()) == -1) {
                montantAffecteFactureSomme = tmp.getBigDecimalValue();
            }
        }

        FWCurrency montant = result.get(lastYear);

        if (montant == null) {
            montant = new FWCurrency();
        }

        montant.add(montantAffecteFactureSomme.toString());
        result.put(lastYear, montant);
    }

    /**
     * Ajoute une ligne de détail à l'intérêt rémunératoire.
     *
     * @param osirisSession
     * @param interet
     * @param keyYear
     * @param tmp
     * @param taux
     * @param montantInteret
     * @param keys
     * @param actuelIndex
     * @throws Exception
     */
    private void addLigneDetailInteret(BSession osirisSession, CAInteretMoratoire interet, String keyYear,
                                       FWCurrency tmp, double taux, FWCurrency montantInteret, Object[] keys, int actuelIndex) throws Exception {
        CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
        ligne.setSession(osirisSession);

        ligne.setIdJournalFacturation(getPassage().getIdPassage());

        ligne.setMontantInteret(montantInteret.toString());

        ligne.setMontantSoumis(tmp.toString());

        ligne.setDateDebut(getDateDebutCalcul(keyYear).toString());

        ligne.setTaux(String.valueOf(taux));

        ligne.setDateFin(getDateFinCalcul(keys, actuelIndex).toString());

        ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());

        ligne.setAnneeCotisation(keyYear);

        ligne.add(getTransaction());
    }
    private void addLigneDetailInteretNew(BSession osirisSession, CAInteretMoratoire interet, String keyYear,
                                          FWCurrency tmp, double taux, FWCurrency montantInteret, JADate dateDebut,JADate dateFin, int actuelIndex) throws Exception {
        CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
        ligne.setSession(osirisSession);

        ligne.setIdJournalFacturation(getPassage().getIdPassage());

        ligne.setMontantInteret(montantInteret.toString());

        ligne.setMontantSoumis(tmp.toString());

        ligne.setDateDebut(dateDebut.toString());

        ligne.setTaux(String.valueOf(taux));

        ligne.setDateFin(dateFin.toString());

        ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());

        ligne.setAnneeCotisation(keyYear);

        ligne.add(getTransaction());
    }

    /**
     * Complète et retourne la liste des rubriques. Pour un décompte de type cot pers il faudra ajouter la rubrique dont
     * l'assurance (affiliation) = cot pers. <br/>
     * Exemple : CCJU idRubrique = 28
     *
     * @param osirisSession
     * @param plan
     * @param montantSoumisPlanEntete
     * @param montantEffectifCumule
     * @param montantDejaFactureCumule
     * @param listIdRubrique
     * @return
     * @throws Exception
     */
    private ArrayList<String> completeListIdRubrique(BSession osirisSession, CAPlanCalculInteret plan,
                                                     FACotiPercuEnTropParRubrique montantSoumisPlanEntete, FWCurrency montantEffectifCumule,
                                                     FWCurrency montantDejaFactureCumule, ArrayList<String> listIdRubrique) throws Exception {
        ArrayList<String> result = new ArrayList<String>();

        if ((!montantSoumisPlanEntete.isDecompteTypeCotPers() && !montantSoumisPlanEntete
                .isDecompteTypeCotPersEtudiants())
                || !montantEffectifCumule.isZero()
                || !montantDejaFactureCumule.isZero()) {
            return result;
        }

        FARubriqueCotPersManager manager = new FARubriqueCotPersManager();
        manager.setSession(osirisSession);

        manager.setForIdPlan(plan.getIdPlanCalculInteret());

        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        for (int i = 0; i < manager.size(); i++) {
            FARubriqueCotPers rubrique = (FARubriqueCotPers) manager.get(i);

            if (!listIdRubrique.contains(rubrique.getIdRubrique())) {
                result.add(rubrique.getIdRubrique());
            }
        }

        return result;
    }

    /**
     * Créer ou mise à jour du motif de l'intérêt moratoire.
     *
     * @param osirisSession
     * @param limiteExempte
     * @param plan
     * @param interet
     * @param montantSoumisPlanEntete
     * @param montantInteretCumule
     * @return
     * @throws Exception
     */
    private CAInteretMoratoire createOrUpdateInteret(BSession osirisSession, FWCurrency limiteExempte,
                                                     CAPlanCalculInteret plan, CAInteretMoratoire interet, FACotiPercuEnTropParRubrique montantSoumisPlanEntete,
                                                     FWCurrency montantInteretCumule) throws Exception {
        if (interet.isNew()) {
            interet.setSession(osirisSession);

            interet.setIdSection(montantSoumisPlanEntete.getIdEnteteFacture());
            interet.setIdSectionFacture(montantSoumisPlanEntete.getIdEnteteFacture());
            interet.setIdPlan(plan.getIdPlanCalculInteret());

            interet.setDateCalcul(JACalendar.today().toStr("."));
            interet.setDateFacturation(getPassage().getDateFacturation());

            interet.setIdGenreInteret(CAGenreInteret.CS_TYPE_REMUNERATOIRES);

            interet.setIdJournalFacturation(getPassage().getIdPassage());
            interet.setIdJournalCalcul(getPassage().getIdPassage());

            interet.setIdRubrique(CAInteretUtil.getIdContrePartie(osirisSession, getTransaction(),
                    plan.getIdPlanCalculInteret(), CAGenreInteret.CS_TYPE_REMUNERATOIRES));

            if (!interet.isAControler()) {
                interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
                interet = setMotifCalculInteret(interet, limiteExempte, montantInteretCumule, montantSoumisPlanEntete);
            }

            interet.add(getTransaction());
        } else {
            if (!interet.isAControler()) {
                interet = setMotifCalculInteret(interet, limiteExempte, montantInteretCumule, montantSoumisPlanEntete);
            }

            interet.update(getTransaction());
        }
        return interet;
    }

    /**
     * Execute le process d'intérêt rémunératoire pour cotisations perçu en trop pour un plan.
     *
     * @param osirisSession
     * @param osirisTransaction
     * @param plan
     * @param limiteExempte
     * @throws Exception
     */
    private void executeProcessParPlan(BSession osirisSession, BTransaction osirisTransaction,
                                       CAPlanCalculInteret plan, FWCurrency limiteExempte) throws Exception {
        FACotiPercuEnTropParRubriqueManager manager = getCotiPercuEnTropParPlanManager(plan);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        FWCurrency montantEffectifCumule = new FWCurrency();
        FWCurrency montantFactureCumule = new FWCurrency();
        FWCurrency montantDejaFactureCumule = new FWCurrency();
        ArrayList<String> listIdRubrique = new ArrayList<String>();

        CAInteretMoratoire interet = new CAInteretMoratoire();

        for (int j = 0; j < manager.size(); j++) {
            FACotiPercuEnTropParRubrique montantSoumisPlanEntete = (FACotiPercuEnTropParRubrique) manager.get(j);

            montantEffectifCumule.add(montantSoumisPlanEntete.getMontantInitial());
            montantFactureCumule.add(montantSoumisPlanEntete.getMontant());
            montantDejaFactureCumule.add(montantSoumisPlanEntete.getMontantDejaFacture());

            listIdRubrique.add(montantSoumisPlanEntete.getIdRubrique());

            FASumMontantSoumisParPlan next = null;
            if (j < manager.size() - 1) {
                next = (FASumMontantSoumisParPlan) manager.get(j + 1);
            }

            if ((next == null)
                    || !next.getIdEnteteFacture().equals(montantSoumisPlanEntete.getIdEnteteFacture())
                    || (next.getIdEnteteFacture().equals(montantSoumisPlanEntete.getIdEnteteFacture()) && !next
                    .getAnneeCotisation().equals(montantSoumisPlanEntete.getAnneeCotisation()))) {
                listIdRubrique.addAll(completeListIdRubrique(osirisSession, plan, montantSoumisPlanEntete,
                        montantEffectifCumule, montantDejaFactureCumule, listIdRubrique));

                CASectionFactureeManager sectionManager = getListSectionFacturee(osirisSession, osirisTransaction,
                        montantSoumisPlanEntete, listIdRubrique);

                if (!sectionManager.isEmpty()) {
                    ArrayList<String> listIdSection = getListIdSection(sectionManager);
                    ArrayList<String> listIdCompteCourant = getListIdCompteCourant(sectionManager);

                    if ((!montantSoumisPlanEntete.isExempte()) && (hasSectionMontantNegative(sectionManager))) {
                        interet.setMotifcalcul(CAInteretMoratoire.CS_A_CONTROLER);
                    }

                    FWCurrency montantDejaFactureDansOsiris = getMontantDejaFacture(osirisSession, osirisTransaction,
                            montantSoumisPlanEntete, listIdSection, listIdCompteCourant, listIdRubrique);
                    if (montantDejaFactureCumule.isZero()) {
                        montantEffectifCumule = new FWCurrency(montantDejaFactureDansOsiris.toString());
                        montantEffectifCumule.add(montantFactureCumule);
                    }
                    montantDejaFactureCumule = montantDejaFactureDansOsiris;

                    TreeMap<String, FWCurrency> ecritureNonSoumiseParAnnee = getEcritureNonSoumiseParAnnee(
                            osirisSession, osirisTransaction, montantSoumisPlanEntete, plan, listIdSection,
                            listIdCompteCourant, listIdRubrique, montantDejaFactureCumule,
                            getListMontantFactureParSection(sectionManager));

                    if (!ecritureNonSoumiseParAnnee.isEmpty()) {
                        FWCurrency montantEcritureCumule = new FWCurrency();
                        FWCurrency montantInteretCumule = new FWCurrency();

                        Object[] keys = ecritureNonSoumiseParAnnee.keySet().toArray();

                        for (int k = 0; k < keys.length; k++) {
                            String keyYear = (String) keys[k];
                            FWCurrency montantAffecteFacture = ecritureNonSoumiseParAnnee.get(keyYear);

                            if ((!montantSoumisPlanEntete.isExempte()) && (montantAffecteFacture.isPositive())) {
                                interet.setMotifcalcul(CAInteretMoratoire.CS_A_CONTROLER);
                            }

                            montantAffecteFacture.abs();
                            montantEcritureCumule.add(montantAffecteFacture);

                            if (montantEcritureCumule.compareTo(montantEffectifCumule) == 1) {
                                FWCurrency tmp = new FWCurrency(montantEcritureCumule.toString());
                                tmp.sub(montantEffectifCumule);
                                tmp.negate();

                                // On maximise le montant de l'intérêt à la
                                // différence facturée.
                                if (tmp.compareTo(montantFactureCumule) == -1) {
                                    tmp = new FWCurrency(montantFactureCumule.toString());
                                }

                                tmp.round(FWCurrency.ROUND_5CT);

                                if (CommonProperties.TAUX_INTERET_PANDEMIE.getBooleanValue()) {
                                    boolean isFirst =true;
                                    JADate dateCalculDebut = getDateDebutCalcul(keyYear);
                                    JADate dateCalculFin = getDateFinCalcul(keys, k);
                                    List<CATauxParametre> listTaux = CAInteretUtil.getTaux(getTransaction(),dateCalculDebut.toStr("."), dateCalculFin.toStr(".")  ,CAInteretUtil.CS_PARAM_TAUX_REMU,2);
                                    for(CATauxParametre CATauxParametre : listTaux) {
                                        double taux = CATauxParametre.getTaux();
                                        JADate dateDebut;
                                        JADate dateFin;
                                        //Aide pour découpage des périodes après la première ligne
                                        if (isFirst) {
                                            dateDebut = dateCalculDebut;
                                            isFirst = false;
                                        } else {
                                            dateDebut = new JADate(CATauxParametre.getDateDebut().getSwissValue());
                                        }
                                        if (CATauxParametre.getDateFin() == null) {
                                            dateFin = dateCalculFin;
                                        } else {
                                            dateFin = new JADate(CATauxParametre.getDateFin().getSwissValue());
                                        }
                                        FWCurrency montantInteret = CAInteretUtil.getMontantInteret(osirisSession, tmp,
                                                dateFin, dateDebut, taux);
                                        if ((montantInteret != null)) {
                                            montantInteretCumule.add(montantInteret);

                                            interet = createOrUpdateInteret(osirisSession, limiteExempte, plan, interet,
                                                    montantSoumisPlanEntete, montantInteretCumule);
                                            addLigneDetailInteretNew(osirisSession, interet, keyYear, tmp, taux, montantInteret,
                                                    dateDebut, dateFin, k);
                                        }
                                    }
                                } else {
                                    double taux = CAInteretUtil.getTaux(getTransaction(), getDateDebutCalcul(keyYear)
                                            .toString());
                                    FWCurrency montantInteret = CAInteretUtil.getMontantInteret(osirisSession, tmp,
                                            getDateFinCalcul(keys, k), getDateDebutCalcul(keyYear), taux);

                                    if ((montantInteret != null) && !montantInteret.isZero()) {
                                        montantInteretCumule.add(montantInteret);

                                        interet = createOrUpdateInteret(osirisSession, limiteExempte, plan, interet,
                                                montantSoumisPlanEntete, montantInteretCumule);
                                        addLigneDetailInteret(osirisSession, interet, keyYear, tmp, taux, montantInteret,
                                                keys, k);
                                    }
                                }
                            }
                        }

                        if (interet.isSoumis()) {
                            FAInteretGenericUtil.facturer(getSession(), getTransaction(), osirisSession, getPassage(),
                                    montantSoumisPlanEntete.getIdEnteteFacture(), interet, null);
                        }

                        if (!getTransaction().hasErrors()) {
                            getTransaction().commit();
                        } else {
                            throw new Exception(getTransaction().getErrors().toString()
                                    + montantSoumisPlanEntete.getIdExterneFacture() + "/"
                                    + montantSoumisPlanEntete.getIdEnteteFacture());
                        }
                    }

                }

                montantEffectifCumule = new FWCurrency();
                montantDejaFactureCumule = new FWCurrency();
                montantFactureCumule = new FWCurrency();
                listIdRubrique = new ArrayList<String>();
                interet = new CAInteretMoratoire();

            }
        }
    }

    /**
     * Return le manager qui groupe les entete et afacts et somme montant en fonction d'un plan, groupé par idrubrique
     * et compte annexe.
     *
     * @param plan
     * @return
     */
    private FACotiPercuEnTropParRubriqueManager getCotiPercuEnTropParPlanManager(CAPlanCalculInteret plan) {
        FACotiPercuEnTropParRubriqueManager manager = new FACotiPercuEnTropParRubriqueManager();
        manager.setSession(getSession());

        manager.setForIdPassage(getPassage().getIdPassage());
        manager.setForIdPlan(plan.getIdPlanCalculInteret());

        manager.setForMontantPositif(false);

        ArrayList<String> idSousTypeIn = new ArrayList<String>();
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
        manager.setForIdSousTypeIn(idSousTypeIn);

        return manager;
    }

    /**
     * Retourne la date de début du calcul de la ligne de l'intérêt rémunératoire.
     *
     * @param year
     * @return
     * @throws Exception
     */
    private JADate getDateDebutCalcul(String year) throws Exception {
        return new JADate(new JACalendarGregorian().addYears(FACotiPercuEnTropProcess.PREMIER_JANVIER + year, 1));
    }

    /**
     * Retourne la date de fin du calcul de la ligne de l'intérêt rémunératoire.
     *
     * @param keys
     * @param actuelIndex
     * @return
     * @throws Exception
     */
    private JADate getDateFinCalcul(Object[] keys, int actuelIndex) throws Exception {
        if (actuelIndex == keys.length - 1) {
            // Si dernière ligne (année).

            return getDateJournalFacturation();
        } else {
            // Jusqu'au 31.12 de l'année de la prochaine ligne.

            String keyNextYear = (String) keys[actuelIndex + 1];

            JADate tmp = new JADate(FACotiPercuEnTropProcess.PREMIER_JANVIER + keyNextYear);
            tmp.setDay(31);
            tmp.setMonth(12);

            return tmp;
        }
    }

    /**
     * @return
     * @throws JAException
     */
    private JADate getDateJournalFacturation() throws JAException {
        if (dateJournalFacturation == null) {
            if (!JadeStringUtil.isBlank(getPassage().getDateEcheance())) {
                dateJournalFacturation = new JADate(getPassage().getDateEcheance());
            } else {
                dateJournalFacturation = new JADate(getPassage().getDateFacturation());
            }
        }

        return dateJournalFacturation;
    }

    /**
     * Retourne la liste des écritures non soumises (paimements, compensations etc.) pour une liste de section.
     *
     * @param osirisSession
     * @param osirisTransaction
     * @param plan
     * @param listIdSection
     * @param listIdCompteCourant
     * @return
     * @throws Exception
     */
    private CAEcritureNonSoumiseParSectionManager getEcritureNonSoumise(BSession osirisSession,
                                                                        BTransaction osirisTransaction, CAPlanCalculInteret plan, ArrayList<String> listIdSection,
                                                                        ArrayList<String> listIdCompteCourant) throws Exception {
        CAEcritureNonSoumiseParSectionManager manager = new CAEcritureNonSoumiseParSectionManager();
        manager.setSession(osirisSession);

        manager.setForIdCompteCourantIn(listIdCompteCourant);
        manager.setForIdSectionIn(listIdSection);
        manager.setForIdPlan(plan.getIdPlanCalculInteret());

        manager.setForDateBefore(JACalendar.today().getYear() + FACotiPercuEnTropProcess.ONZE_JANVIER);

        manager.setGroupBySection(true);

        manager.find(osirisTransaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        return manager;
    }

    /**
     * Return une HashMap contenant les paiements, compensations et écritures non soumises regroupés (montant sommé) par
     * années. <br/>
     * Pour calculer ce montant, on somme ces derniers par sections en concidérant que l'on couvre les intérêts en
     * premier et en les plafonands au montant de la facture.
     *
     * @param osirisSession
     * @param osirisTransaction
     * @param montantSoumisPlanEntete
     * @param plan
     * @param listIdSection
     * @param listIdCompteCourant
     * @param listIdRubrique
     * @param montantEffectifCumule
     * @param montantCumuleFactureParSection
     * @return
     * @throws Exception
     */
    private TreeMap<String, FWCurrency> getEcritureNonSoumiseParAnnee(BSession osirisSession,
                                                                      BTransaction osirisTransaction, FACotiPercuEnTropParRubrique montantSoumisPlanEntete,
                                                                      CAPlanCalculInteret plan, ArrayList<String> listIdSection, ArrayList<String> listIdCompteCourant,
                                                                      ArrayList<String> listIdRubrique, FWCurrency montantEffectifCumule,
                                                                      TreeMap<String, String> montantCumuleFactureParSection) throws Exception {
        TreeMap<String, FWCurrency> result = new TreeMap<String, FWCurrency>();

        CAEcritureNonSoumiseParSectionManager ecritureManager = getEcritureNonSoumise(osirisSession, osirisTransaction,
                plan, listIdSection, listIdCompteCourant);

        String lastIdSection = null;
        String lastYear = null;
        BigDecimal montantAffecteFactureSomme = new FWCurrency().getBigDecimalValue();

        for (int l = 0; l < ecritureManager.size(); l++) {
            CAEcritureNonSoumiseParSection ecriture = (CAEcritureNonSoumiseParSection) ecritureManager.get(l);

            String yearKey = ecriture.getYearKey(montantSoumisPlanEntete.getAnneeCotisation());
            if ((yearKey != null) && (Integer.parseInt(yearKey) < getDateJournalFacturation().getYear())) {
                if (((lastIdSection != null) && (!ecriture.getIdSection().equals(lastIdSection) || !ecriture
                        .getYearKey(montantSoumisPlanEntete.getAnneeCotisation()).equals(lastYear)))) {
                    addEcritureToYear(lastIdSection, lastYear, montantCumuleFactureParSection,
                            montantAffecteFactureSomme, result);
                    montantAffecteFactureSomme = new FWCurrency().getBigDecimalValue();
                }

                montantAffecteFactureSomme = montantAffecteFactureSomme.add(ecriture.getMontantToCurrency()
                        .getBigDecimalValue());
                lastIdSection = ecriture.getIdSection();
                lastYear = ecriture.getYearKey(montantSoumisPlanEntete.getAnneeCotisation());

                if (l == ecritureManager.size() - 1) {
                    addEcritureToYear(lastIdSection, lastYear, montantCumuleFactureParSection,
                            montantAffecteFactureSomme, result);
                }
            }
        }

        return result;
    }

    /**
     * @see BProcess.getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("5031") + " " + getPassage().getIdPassage();
        } else {
            return getSession().getLabel("5030") + " " + getPassage().getIdPassage();
        }
    }

    /**
     * Return la limite exempté d'un intérêt rénumératoire.
     *
     * @param osirisSession
     * @return
     * @throws Exception
     */
    private FWCurrency getLimiteExempte(BSession osirisSession) throws Exception {
        return new FWCurrency(CAInteretUtil.getLimiteExempteInteretRenumeratoire(osirisSession, getTransaction()));
    }

    /**
     * Retourne la liste des compte courants facturés (touchés)
     *
     * @param sectionManager
     * @return
     * @throws Exception
     */
    private ArrayList<String> getListIdCompteCourant(CASectionFactureeManager sectionManager) throws Exception {
        ArrayList<String> listIdCompteCourant = new ArrayList<String>();

        for (int k = 0; k < sectionManager.size(); k++) {
            CASectionFacturee section = (CASectionFacturee) sectionManager.get(k);

            if (!listIdCompteCourant.contains(section.getIdCompteCourant())) {
                listIdCompteCourant.add(section.getIdCompteCourant());
            }
        }

        return listIdCompteCourant;
    }

    /**
     * Retourne la liste des sections facturées (touchées)
     *
     * @param sectionManager
     * @return
     * @throws Exception
     */
    private ArrayList<String> getListIdSection(CASectionFactureeManager sectionManager) throws Exception {
        ArrayList<String> listIdSection = new ArrayList<String>();

        for (int k = 0; k < sectionManager.size(); k++) {
            CASectionFacturee section = (CASectionFacturee) sectionManager.get(k);

            if (!listIdSection.contains(section.getIdSection())) {
                listIdSection.add(section.getIdSection());
            }
        }

        return listIdSection;
    }

    /**
     * Retourne la liste des sections facturées avec cumul des montant facturé
     *
     * @param sectionManager
     * @return
     * @throws Exception
     */
    private TreeMap<String, String> getListMontantFactureParSection(CASectionFactureeManager sectionManager)
            throws Exception {
        TreeMap<String, String> result = new TreeMap<String, String>();

        for (int k = 0; k < sectionManager.size(); k++) {
            CASectionFacturee section = (CASectionFacturee) sectionManager.get(k);

            FWCurrency tmp;
            if (result.containsKey(section.getIdSection())) {
                tmp = new FWCurrency("" + result.get(section.getIdSection()));
                tmp.add(section.getMontant());
            } else {
                tmp = section.getMontantAsCurrency();
            }

            result.put(section.getIdSection(), tmp.toString());
        }

        return result;
    }

    /**
     * Retourne la liste des sections qui sont touchés. Càd. les sections qui ont déjà été facturées.
     *
     * @param osirisSession
     * @param osirisTransaction
     * @param montantSoumisPlanEntete
     * @param listIdRubrique
     * @return
     * @throws Exception
     */
    private CASectionFactureeManager getListSectionFacturee(BSession osirisSession, BTransaction osirisTransaction,
                                                            FACotiPercuEnTropParRubrique montantSoumisPlanEntete, ArrayList<String> listIdRubrique) throws Exception {
        CASectionFactureeManager manager = new CASectionFactureeManager();
        manager.setSession(osirisSession);

        manager.setForIdExterneRole(montantSoumisPlanEntete.getIdExterneRole());
        manager.setForIdRole(montantSoumisPlanEntete.getIdRole());
        manager.setForAnneeCotisation(montantSoumisPlanEntete.getAnneeCotisation());
        manager.setForIdRubriqueIn(listIdRubrique);

        manager.find(osirisTransaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        return manager;
    }

    /**
     * Retourne le montant déjà facturé. Ce montant est résolue en comptabilité auxiliaire, remplace le montant déjà
     * facturé que l'on pourrait sommer dans les afacts-
     *
     * @param osirisSession
     * @param osirisTransaction
     * @param montantSoumisPlanEntete
     * @param listIdSection
     * @param listIdCompteCourant
     * @param listIdRubrique
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantDejaFacture(BSession osirisSession, BTransaction osirisTransaction,
                                             FACotiPercuEnTropParRubrique montantSoumisPlanEntete, ArrayList<String> listIdSection,
                                             ArrayList<String> listIdCompteCourant, ArrayList<String> listIdRubrique) throws Exception {
        CAMontantFactureManager manager = new CAMontantFactureManager();
        manager.setSession(osirisSession);

        manager.setForIdExterneRole(montantSoumisPlanEntete.getIdExterneRole());
        manager.setForIdRole(montantSoumisPlanEntete.getIdRole());
        manager.setForAnneeCotisation(montantSoumisPlanEntete.getAnneeCotisation());
        manager.setForIdRubriqueIn(listIdRubrique);
        manager.setForIdSectionIn(listIdSection);
        manager.setForIdCompteCourantIn(listIdCompteCourant);

        manager.find(osirisTransaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel("MONTANT_DEJA_FACTURE_ERROR"));
        }

        return ((CAMontantFacture) manager.getFirstEntity()).getMontantAsCurrency();
    }

    public FAPassage getPassage() {
        return passage;
    }

    /**
     * Y-a-t'il une section avec un montant facturé négatif ? Si oui l'intérêt devra être contrôlé.
     *
     * @param sectionManager
     * @return
     * @throws Exception
     */
    private boolean hasSectionMontantNegative(CASectionFactureeManager sectionManager) throws Exception {
        for (int k = 0; k < sectionManager.size(); k++) {
            CASectionFacturee section = (CASectionFacturee) sectionManager.get(k);

            if (section.getMontantAsCurrency().isNegative()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @see FAInteretDecompteFinalProcess.isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule)
     */
    private boolean isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule) {
        return (montantInteretCumule.compareTo(limiteExempte) == -1);
    }

    /**
     * @see BProcess.jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Mise à jour du motif de calcul de l'intérêt.
     *
     * @param interet
     * @param limiteExempte
     * @param montantInteretCumule
     * @param planEntete
     * @return
     */
    private CAInteretMoratoire setMotifCalculInteret(CAInteretMoratoire interet, FWCurrency limiteExempte,
                                                     FWCurrency montantInteretCumule, FASumMontantSoumisParPlan planEntete) {
        if ((interet.isExempte() && !planEntete.isExempte() && isMontantSoumis(limiteExempte, montantInteretCumule))
                || (planEntete.isSoumis())) {
            interet.setMotifcalcul(CAInteretMoratoire.CS_SOUMIS);
        }

        return interet;
    }

    /**
     * Method setPassage. Utilise le passage passé en paramètre depuis la facturation
     *
     * @param passage passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

}
