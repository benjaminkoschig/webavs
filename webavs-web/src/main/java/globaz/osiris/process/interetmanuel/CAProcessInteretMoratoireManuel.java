package globaz.osiris.process.interetmanuel;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.*;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.impl.CASectionAVS;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CASectionAuxPoursuites;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumise;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseManager;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSection;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSectionManager;
import globaz.osiris.db.interet.util.tauxParametres.CATauxParametre;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.osiris.utils.CATiersUtil;
import ch.globaz.common.properties.CommonProperties;

import java.util.*;

/**
 * Processus permettant la création d'intérêt tardif manuellement (sur demande pour une date donnée et un numéro de
 * facture donné).<br/>
 * Ce processus peut-être exécuté en mode prévisionnel pour présenter les informations avant ajout à l'utilisateur.<br/>
 * Wizard.
 *
 * @author DDA
 */
public class CAProcessInteretMoratoireManuel extends BProcess {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String dateFin = new String();
    private String idJournal = "";
    private String idSection = new String();
    private String numeroFactureGroupe = new String();
    private Boolean simulationMode = new Boolean(true);
    private Boolean isRDPProcess = false;
    private boolean forceExempte = false;

    private CAInteretManuelVisualComponent visualComponent = null;
    private ArrayList<CAInteretManuelVisualComponent> visualComponents = new ArrayList<CAInteretManuelVisualComponent>();
    Map<Periode, Boolean> mapMotifs;
    JADate dateCalculDebutInteret;
    JADate dateDebut1erPassage = dateCalculDebutInteret;


    private FWCurrency montantSoumisSurcisCalcul;
    private FWCurrency montantCumuleSurcisCalcul;

    /**
     *
     */
    public CAProcessInteretMoratoireManuel() {
        super();
    }

    /**
     * @param parent BProcess
     */
    public CAProcessInteretMoratoireManuel(BProcess parent) {
        super(parent);
    }

    /**
     *
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Execute la création ou la simulation de l'intérêt moratoire manuel.
     *
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
                throw new Exception(getSession().getLabel("5125"));
            }

            JADate tmp = new JADate(getDateFin());
            BSessionUtil.checkDateGregorian(getSession(), tmp);

            CAInteretTardif interetTardif = CAInteretTardifFactory.getInteretTardif(getSection().getCategorieSection());

            if (interetTardif == null) {
                // throw new Exception(this.getSession().getLabel("IM_MANUEL_CALCUL_IMPOSSIBLE"));
                return false;
            }

            interetTardif.setIdSection(getIdSection());

            if (!interetTardif.isTardif(getSession(), getTransaction(), getDateFin())) {
                throw new Exception(getSession().getLabel("IM_MANUEL_DATE_FIN_ERROR"));
            }

            checkForInteretMoratoireEnSuspens();

            if (!JadeStringUtil.isBlank(getNumeroFactureGroupe())) {
                if ((getNumeroFactureGroupe().length() != 7) && (getNumeroFactureGroupe().length() != 9)) {
                    throw new Exception(getSession().getLabel("IM_MANUEL_NUM_SECTION_TROP_LONG"));
                }

                checkIdExterneSection();
            }

            visualComponents = new ArrayList<CAInteretManuelVisualComponent>();
            visualComponent = null;

            calculerManuellement(interetTardif);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return true;
    }

    /**
     * Validation des champs principaux.
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();

        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

        if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
            this._addError(getTransaction(), getSession().getLabel("5125"));
            return;
        }

        CAInteretTardif interetTardif = CAInteretTardifFactory.getInteretTardif(getSection().getCategorieSection());
        if (interetTardif == null) {
            if (simulationMode && isRDPProcess == false) {
                this._addError(getTransaction(), getSession().getLabel("IM_MANUEL_CALCUL_IMPOSSIBLE"));
                // } else {
                // this.getMemoryLog().logMessage(
                // this.getSession().getLabel("IM_MANUEL_CALCUL_IMPOSSIBLE") + " "
                // + this.getSection().getCompteAnnexe().getIdExterneRole() + " / "
                // + this.getSection().getIdExterne() + " (idSection : " + this.getIdSection() + ")",
                // FWMessage.AVERTISSEMENT, this.getClass().getName());
            }
            return;
        }

        interetTardif.setIdSection(getIdSection());

        try {
            JADate tmp = new JADate(getDateFin());
            BSessionUtil.checkDateGregorian(getSession(), tmp);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return;
        }

        if (!interetTardif.isTardif(getSession(), getTransaction(), getDateFin())) {
            this._addError(getTransaction(), getSession().getLabel("IM_MANUEL_DATE_FIN_ERROR"));
            return;
        }

        try {
            checkForInteretMoratoireEnSuspens();
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return;
        }

        if (!JadeStringUtil.isBlank(getNumeroFactureGroupe())) {
            if ((getNumeroFactureGroupe().length() != 7) && (getNumeroFactureGroupe().length() != 9)) {
                this._addError(getTransaction(), getSession().getLabel("IM_MANUEL_NUM_SECTION_TROP_LONG"));
                return;
            }

            checkIdExterneSection();
        }

    }

    /**
     * Ajout d'une ligne de détail à l'intérêt.
     *
     * @param interet
     * @param montantSoumis
     * @param taux
     * @param montantInteret
     * @param dateCaculDebutInteret
     * @param tmpDateFin
     * @return
     * @throws Exception
     */
    private CAInteretMoratoire addDetailInteretMoratoire(CAInteretMoratoire interet, FWCurrency montantSoumis,
                                                         double taux, FWCurrency montantInteret, JADate dateCaculDebutInteret, String tmpDateFin) throws Exception {
        CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
        ligne.setSession(getSession());

        ligne.setMontantInteret(montantInteret.toString());

        ligne.setMontantSoumis(montantSoumis.toString());

        ligne.setDateDebut(dateCaculDebutInteret.toStr("."));

        ligne.setTaux(String.valueOf(taux));

        ligne.setDateFin(tmpDateFin);

        ligne.setAnneeCotisation(Integer.toString(dateCaculDebutInteret.getYear()));

        if (interet.isNew()) {
            if (!isSimulationMode()) {
                if (JadeStringUtil.isBlankOrZero(getIdJournal())) {
                    interet.setIdJournalCalcul(CAJournal.fetchJournalJournalier(getSession(), getTransaction())
                            .getIdJournal());
                } else {
                    interet.setIdJournalCalcul(getIdJournal());
                }

                interet.add(getTransaction());
            }
            if (visualComponent == null) {
                visualComponent = new CAInteretManuelVisualComponent(interet);
            }
        }

        ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());

        if (!isSimulationMode()) {
            ligne.add(getTransaction());
        }
        visualComponent.addDetailInteretMoratoire(ligne);

        return interet;
    }

    /**
     * Calculer "manuellement" l'intérêt moratoire.
     *
     * @param interetTardif
     * @throws Exception
     */
    public void calculerManuellement(CAInteretTardif interetTardif) throws Exception {
        // Recherche des plans affectants la section
        CAPlanParSectionManager manager = CAInteretUtil.getSectionPlans(getSession(), getTransaction(), null,
                getIdSection(), false);

        for (int i = 0; i < manager.size(); i++) {
            CAPlanParSection plan = (CAPlanParSection) manager.get(i);

            // CAInteretMoratoire interet = interetTardif.getInteretMoratoire(this.getSession(), this.getTransaction(),
            // plan.getIdPlan());
            CAInteretMoratoire interet = CAInteretUtil.getInteretMoratoire(getTransaction(), plan.getIdPlan(),
                    interetTardif.getIdSection(), interetTardif.getIdJournal());

            if (interet.isNew()) {
                if (isForceExempte()) {
                    interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
                } else {
                    // Mise à jour de l'intérêt tardif "standard" créé précédement
                    // pour l'indentifé comme "manuel"
                    interet.setMotifcalcul(CAInteretMoratoire.CS_MANUEL);
                }
                interet.setNumeroFactureGroupe(getNumeroFactureGroupe());

                FWCurrency montantSoumis = CAInteretUtil.getMontantSoumisParPlans(getTransaction(), getIdSection(),
                        plan.getIdPlan(), null);

                if ((montantSoumis != null) && montantSoumis.isPositive()) {
                    creerInteret(interetTardif, plan, interet, montantSoumis);
                }

                if (/* this.isSimulationMode() && */(visualComponent != null)) {
                    visualComponent.setPlan(plan);
                    visualComponents.add(visualComponent);
                    visualComponent = null;
                }
            }
        }

        if (isSimulationMode() && visualComponents.isEmpty()) {
            throw new Exception(getSession().getLabel("IM_MANUEL_AUCUN_CALCUL"));
        }

    }

    private boolean isNotForceExempte() {
        return !forceExempte;
    }

    private boolean isForceExempte() {
        return forceExempte;
    }

    public void setForceExempte(boolean forceExempte) {
        this.forceExempte = forceExempte;
    }

    /**
     * Il y a t'il déjà un intéret moratoire soumis ou manuel pour la section (intérêt non facturé actuellement).
     *
     * @throws Exception
     */
    private void checkForInteretMoratoireEnSuspens() throws Exception {
        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setSession(getSession());
        manager.setForIdSection(getIdSection());
        manager.setForIdJournalFacturation("0");
        manager.setForFacturable(true);
        manager.find(getTransaction());

        if (manager.hasErrors() || !manager.isEmpty()) {
            throw new Exception(getSession().getLabel("IM_MANUEL_SUSPENS"));
        }
    }

    /**
     * Contrôle le format de l'id externe de la section de facturation des intérêts moratoires.
     *
     * @throws Exception
     */
    private void checkIdExterneSection() throws Exception {
        CASection tmpSection = getSection();
        tmpSection.setIdExterne(getNumeroFactureGroupe());
        CASectionAVS sectionAVS = new CASectionAVS();
        sectionAVS.setISession(getSession());
        sectionAVS.setSection(tmpSection);
    }

    /**
     * Créer l'intéret moratoire manuel ainsi que sont détails (toutes les lignes liées).
     *
     * @param interetTardif
     * @param plan
     * @param interet
     * @param montantSoumis
     * @throws Exception
     */
    private void creerInteret(CAInteretTardif interetTardif, CAPlanParSection plan, CAInteretMoratoire interet,
                              FWCurrency montantSoumis) throws Exception {
        JACalendar cal = new JACalendarGregorian();
        JADate dateMaxPourPaiement = cal.addDays(getDateFinAsJADate(), 1);

        // Liste les écritures du plan et non pas du compte courant
        CAEcritureNonSoumiseManager manager = CAInteretUtil.getEcrituresNonSoumises(getSession(), getTransaction(),
                plan.getIdPlan(), getIdSection(), null, dateMaxPourPaiement.toStrAMJ());

        FWCurrency montantCumule = new FWCurrency();
        montantCumuleSurcisCalcul = new FWCurrency();

        // POAVS-223
        boolean aControlerManuellement = false;
        CASectionAuxPoursuites sectionAuxPoursuite = CAInteretTardif.getSectionAuxPoursuites(getSession(),
                getIdSection());
        String dateExecution = "";

        // POAVS-223 ajout de && !isRDPProcess
        boolean isNouveauCDP = CAInteretTardif.isNouveauCDP(sectionAuxPoursuite) && !isRDPProcess;

        // si le nouveau régime est activé
        if (isNouveauCDP) {
            // récupération du canton de l'office des poursuites relatif au tiers
            CASection section = interetTardif.getSection(getSession(), getTransaction());
            String cantonOfficePoursuite = CATiersUtil.getCantonOfficePoursuite(getSession(), section.getCompteAnnexe()
                    .getTiers(), section.getIdExterne(), section.getCompteAnnexe().getIdExterneRole());
            if (JadeStringUtil.isBlank(cantonOfficePoursuite)) {
                JadeLogger.warn(FWMessage.ERREUR, getSession().getLabel("IM_ERR_OP_INTROUVABLE") + " : "
                        + section.getCompteAnnexe().getIdExterneRole() + " / " + section.getIdExterne());
            }

            // si le canton n'est pas exclu du nouveau régime on applique le nouveau régime
            if (!CAInteretTardif.isOfficeExcluDuNouveauRegime(cantonOfficePoursuite, getSession())) {
                dateExecution = JadeDateUtil.addDays(sectionAuxPoursuite.getHistorique().getDateExecution(), 1);
                dateCalculDebutInteret = new JADate(dateExecution);
                interet.setNouveauRegime(true);
            } else {
                dateCalculDebutInteret = interetTardif.getDateCalculDebutInteret(getSession(), getTransaction());
            }
        } else {
            dateCalculDebutInteret = interetTardif.getDateCalculDebutInteret(getSession(), getTransaction());
        }

        for (int i = 0; i < manager.size(); i++) {
            CAEcritureNonSoumise ecriture = (CAEcritureNonSoumise) manager.get(i);

            if (ecriture.getMontantToCurrency().isNegative()) {

                boolean isDateEcritureApresDatePoursuite = (JadeDateUtil
                        .isDateBefore(dateExecution, ecriture.getDate()) || JadeDateUtil.areDatesEquals(dateExecution,
                        ecriture.getDate()));

                if (montantSoumis.isPositive()
                        && interetTardif.isTardif(getSession(), getTransaction(), ecriture.getDate())
                        && (isDateEcritureApresDatePoursuite || !isNouveauCDP)) {
                    if (CommonProperties.TAUX_INTERET_PANDEMIE.getBooleanValue()) {
                        List<Periode> listPeriodeMotifsSurcis = CAInteretUtil.isSectionSurcisProgaPaiementInPandemie(getTransaction(), getSession(), idSection);
                        if (!listPeriodeMotifsSurcis.isEmpty()) {
                            /**
                             * Cas spécial : Quand il y'a des motifs de surcis ou progation
                             */
                            creerInteretForSurcisProro(dateCalculDebutInteret, ecriture.getJADate(), listPeriodeMotifsSurcis, interet,montantSoumis);
                            montantCumule = montantCumuleSurcisCalcul;
                        } else {
                            boolean isFirst = true;
                            JADate dateCalculDebut = dateCalculDebutInteret;
                            JADate dateCalculFin = ecriture.getJADate();
                            List<CATauxParametre> listTaux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX, 2);
                            for (CATauxParametre CATauxParametre : listTaux) {
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
                                FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                                        dateFin, dateDebut, taux);
                                if ((montantInteret != null)) {
                                    interet = addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret,
                                            dateDebut, dateFin.toStr("."));
                                }
                                montantCumule.add(montantInteret);
                                dateDebut1erPassage = getSession().getApplication().getCalendar()
                                        .addDays(dateFin, 1);
                                // Si il y'a plusieurs écritures.
                                dateCalculDebutInteret = getSession().getApplication().getCalendar()
                                        .addDays(dateFin, 1);

                            }
                        }
                    } else {
                        double taux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebutInteret.toStr("."));
                        FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                                ecriture.getJADate(), dateCalculDebutInteret, taux);

                        interet = addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret,
                                dateCalculDebutInteret, ecriture.getJADate().toStr("."));

                        montantCumule.add(montantInteret);
                        dateCalculDebutInteret = getSession().getApplication().getCalendar()
                                .addDays(ecriture.getJADate(), 1);
                    }
                }

                montantSoumis.add(ecriture.getMontantToCurrency());
            } else if (ecriture.getMontantToCurrency().isPositive()) {
                // POAVS-223
                aControlerManuellement = true;
            }
        }

        // POAVS-223
        if (aControlerManuellement) {
            interet.setMotifcalcul(CAInteretMoratoire.CS_A_CONTROLER);
        } else if (isNotForceExempte()) {
            // On force donc on va pas regarder la limite exempté.
            FWCurrency limiteExempte = new FWCurrency(CAInteretUtil.getLimiteExempteInteretMoratoire(getSession(),
                    getTransaction()));

            if (montantCumule.compareTo(limiteExempte) == 1) {
                interet.setMotifcalcul(CAInteretMoratoire.CS_SOUMIS);
            }
        }
        if (!interet.isNew()) {
            interet.update(getTransaction());
        }

        if (montantSoumis.isPositive()
                && (getSession().getApplication().getCalendar().compare(dateCalculDebutInteret, getDateFinAsJADate()) == JACalendar.COMPARE_FIRSTLOWER)) {

            if (CommonProperties.TAUX_INTERET_PANDEMIE.getBooleanValue()) {
                List<Periode> listPeriodeMotifsSurcis = CAInteretUtil.isSectionSurcisProgaPaiementInPandemie(getTransaction(), getSession(), idSection);
                if (!listPeriodeMotifsSurcis.isEmpty()) {
                    creerInteretForSurcisProro(dateDebut1erPassage, getDateFinAsJADate(), listPeriodeMotifsSurcis, interet,montantSoumis);
                } else {
                    JADate dateCalculDebut = dateDebut1erPassage;
                    JADate dateCalculFin = getDateFinAsJADate();
                    List<CATauxParametre> listTaux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX, 2);
                    boolean isFirst = true;
                    for (CATauxParametre tauxParametre : listTaux) {
                        double taux = tauxParametre.getTaux();
                        JADate dateDebut;
                        JADate dateFin;
                        if (isFirst) {
                            dateDebut = dateCalculDebut;
                            isFirst = false;
                        } else {
                            dateDebut = new JADate(tauxParametre.getDateDebut().getSwissValue());
                        }
                        if (tauxParametre.getDateFin() == null) {
                            dateFin = dateCalculFin;
                        } else {
                            dateFin = new JADate(tauxParametre.getDateFin().getSwissValue());
                        }
                        FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                                dateFin, dateDebut, taux);
                        if ((montantInteret != null)) {
                            addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret, dateDebut,
                                    dateFin.toStr("."));
                        }
                    }
                }
            } else {
                double taux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebutInteret.toStr("."));
                FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                        getDateFinAsJADate(), dateCalculDebutInteret, taux);

                addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret, dateCalculDebutInteret,
                        getDateFinAsJADate().toStr("."));
            }
        }
    }

    public void creerInteretForSurcisProro(JADate dateCalculDebut, JADate dateCalculFin, List<Periode> listPeriodeMotifsSurcis, CAInteretMoratoire interet,FWCurrency montantSoumis) throws Exception {

        Map<String, CATauxParametre> mapTaux = CAInteretUtil.getTauxInMap(getTransaction(), dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX, 2);
        Map<String, CATauxParametre> mapTauxSurcisProro = CAInteretUtil.getTauxInMap(getTransaction(), dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX_SURCIS_PROGATION_PANDEMIE_2020, 2);
        Map<Periode, Boolean> mapIntermediaire = new LinkedHashMap<>();
        Map<Periode, CATauxParametre> mapLigneAInscrire = new LinkedHashMap<>();
        JADate dateDebut = dateCalculDebut;
        JADate dateFin = null;

        //Cas 1 : Multiple motif en surcis/prorogation
        Periode periodeCalcul = new Periode(dateCalculDebut.toStr("."),dateCalculFin.toStr("."));
        for(Periode periodeMotifsRaw : listPeriodeMotifsSurcis){
            if(periodeCalcul.comparerChevauchement(periodeMotifsRaw) == Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES){
                listPeriodeMotifsSurcis.remove(periodeMotifsRaw);
            }
        }
        Collections.sort(listPeriodeMotifsSurcis);
        /**
         * Préparation des différents périodes avec switch sur les 2 types de taux
         */
        mapIntermediaire = CAInteretUtil.preparesPeriodeInteretsCovids(getSession(),dateCalculDebut,dateCalculFin,listPeriodeMotifsSurcis);

        /**
         * Préparation des lignes des intérêts avec les bons taux
         */
        CAInteretUtil.createLignesAInscrire(getSession(), mapIntermediaire, mapLigneAInscrire, mapTaux, mapTauxSurcisProro);


        FWCurrency montantInteret;
        CATauxParametre taux;
        for (Periode keyAVS : mapLigneAInscrire.keySet()) {
            taux = mapLigneAInscrire.get(keyAVS);
            dateDebut = new JADate(keyAVS.getDateDebut());
            dateFin = new JADate(keyAVS.getDateFin());
            montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                    dateFin, dateDebut, taux.getTaux());
            if ((montantInteret != null)) {
                interet = addDetailInteretMoratoire(interet, montantSoumis, taux.getTaux(), montantInteret,
                        dateDebut, dateFin.toStr("."));
            }
            montantCumuleSurcisCalcul.add(montantInteret);
        }
        dateDebut1erPassage = getSession().getApplication().getCalendar()
                .addDays(dateFin, 1);
        // Si il y'a plusieurs écritures.
        dateCalculDebutInteret = getSession().getApplication().getCalendar()
                .addDays(dateFin, 1);

    }

    private void createLignesAInscrire(Map<Periode, Boolean> mapIntermediaire, Map<Periode, CATauxParametre> mapLigneAInscrire) {
    }

    public String getDateFin() {
        return dateFin;
    }

    public JADate getDateFinAsJADate() throws JAException {
        return new JADate(dateFin);
    }

    /**
     * Set le titre de l'email.
     *
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel("IM_MANUEL_ERROR");
        } else {
            return getSession().getLabel("IM_MANUEL_OK");
        }
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getNumeroFactureGroupe() {
        return numeroFactureGroupe;
    }

    /**
     * Return la section en cours sur laquelle les intérêts moratoires doivent être calculées.
     *
     * @return
     * @throws Exception
     */
    private CASection getSection() throws Exception {
        CASection section = new CASection();
        section.setSession(getSession());

        section.setIdSection(getIdSection());

        section.retrieve(getTransaction());

        if (section.hasErrors()) {
            throw new Exception(section.getErrors().toString());
        }
        return section;
    }

    public Boolean getSimulationMode() {
        return simulationMode;
    }

    public ArrayList<CAInteretManuelVisualComponent> getVisualComponents() {
        return visualComponents;
    }

    public boolean isSimulationMode() {
        return getSimulationMode().booleanValue();
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     *
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param idJournal the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setNumeroFactureGroupe(String numeroFactureGroupe) {
        this.numeroFactureGroupe = numeroFactureGroupe;
    }

    public void setSimulationMode(Boolean simulationMode) {
        this.simulationMode = simulationMode;
    }

    public Boolean getIsRDPProcess() {
        return isRDPProcess;
    }

    public void setIsRDPProcess(Boolean isRDPProcess) {
        this.isRDPProcess = isRDPProcess;
    }

    public FWCurrency getMontantSoumisSurcisCalcul() {
        return montantSoumisSurcisCalcul;
    }

    public void setMontantSoumisSurcisCalcul(FWCurrency montantSoumisSurcisCalcul) {
        this.montantSoumisSurcisCalcul = montantSoumisSurcisCalcul;
    }

    public FWCurrency getMontantCumuleSurcisCalcul() {
        return montantCumuleSurcisCalcul;
    }

    public void setMontantCumuleSurcisCalcul(FWCurrency montantCumuleSurcisCalcul) {
        this.montantCumuleSurcisCalcul = montantCumuleSurcisCalcul;
    }
    public CAInteretManuelVisualComponent getVisualComponent() {
        return visualComponent;
    }
}
