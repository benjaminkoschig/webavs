package globaz.osiris.process.interetmanuel;

import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.impl.CASectionAVS;
import globaz.osiris.db.contentieux.CASectionAuxPoursuites;
import globaz.osiris.db.contentieux.CASectionAuxPoursuitesManager;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumise;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseManager;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSection;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSectionManager;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.osiris.utils.CATiersUtil;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Processus permettant la cr�ation d'int�r�t tardif manuellement (sur demande pour une date donn�e et un num�ro de
 * facture donn�).<br/>
 * Ce processus peut-�tre ex�cut� en mode pr�visionnel pour pr�senter les informations avant ajout � l'utilisateur.<br/>
 * Wizard.
 * 
 * @author DDA
 * 
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

    private CAInteretManuelVisualComponent visualComponent = null;
    private ArrayList<CAInteretManuelVisualComponent> visualComponents = new ArrayList<CAInteretManuelVisualComponent>();

    /**
	 *
	 */
    public CAProcessInteretMoratoireManuel() {
        super();
    }

    /**
     * @param parent
     *            BProcess
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
     * Execute la cr�ation ou la simulation de l'int�r�t moratoire manuel.
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
     * Ajout d'une ligne de d�tail � l'int�r�t.
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
     * Calculer "manuellement" l'int�r�t moratoire.
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
                // Mise � jour de l'int�r�t tardif "standard" cr�� pr�c�dement
                // pour l'indentif� comme "manuel"
                interet.setMotifcalcul(CAInteretMoratoire.CS_MANUEL);
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

    /**
     * Il y a t'il d�j� un int�ret moratoire soumis ou manuel pour la section (int�r�t non factur� actuellement).
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
     * Contr�le le format de l'id externe de la section de facturation des int�r�ts moratoires.
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
     * Cr�er l'int�ret moratoire manuel ainsi que sont d�tails (toutes les lignes li�es).
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

        // Liste les �critures du plan et non pas du compte courant
        CAEcritureNonSoumiseManager manager = CAInteretUtil.getEcrituresNonSoumises(getSession(), getTransaction(),
                plan.getIdPlan(), getIdSection(), null, dateMaxPourPaiement.toStrAMJ());

        FWCurrency montantCumule = new FWCurrency();
        JADate dateCalculDebutInteret;
        CASectionAuxPoursuites sectionAuxPoursuite = getSectionAuxPoursuites();
        String dateExecution = "";

        boolean isNouveauCDP = isNouveauCDP(sectionAuxPoursuite) && !isRDPProcess;

        // si le nouveau r�gime est activ�
        if (isNouveauCDP) {
            // r�cup�ration du canton de l'office des poursuites relatif au tiers
            CASection section = interetTardif.getSection(getSession(), getTransaction());
            String cantonOfficePoursuite = CATiersUtil.getCantonOfficePoursuite(getSession(), section.getCompteAnnexe()
                    .getTiers(), section.getIdExterne(), section.getCompteAnnexe().getIdExterneRole());
            if (JadeStringUtil.isBlank(cantonOfficePoursuite)) {
                JadeLogger.warn(FWMessage.ERREUR, getSession().getLabel("IM_ERR_OP_INTROUVABLE"));
            }

            // si le canton n'est pas exclu du nouveau r�gime on applique le nouveau r�gime
            if (!isOfficeExcluDuNouveauRegime(cantonOfficePoursuite)) {
                dateExecution = JadeDateUtil.addDays(sectionAuxPoursuite.getHistorique().getDateExecution(), 1);
                dateCalculDebutInteret = new JADate(dateExecution);
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
                    double taux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebutInteret.toStr("."));
                    FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                            ecriture.getJADate(), dateCalculDebutInteret, taux);

                    if ((montantInteret != null) && !montantInteret.isZero()) {
                        interet = addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret,
                                dateCalculDebutInteret, ecriture.getJADate().toStr("."));
                    }

                    montantCumule.add(montantInteret);
                    dateCalculDebutInteret = getSession().getApplication().getCalendar()
                            .addDays(ecriture.getJADate(), 1);
                }

                montantSoumis.add(ecriture.getMontantToCurrency());
            }
        }

        if (montantSoumis.isPositive()
                && (getSession().getApplication().getCalendar().compare(dateCalculDebutInteret, getDateFinAsJADate()) == JACalendar.COMPARE_FIRSTLOWER)) {
            double taux = CAInteretUtil.getTaux(getTransaction(), dateCalculDebutInteret.toStr("."));
            FWCurrency montantInteret = CAInteretUtil.getMontantInteret(getSession(), montantSoumis,
                    getDateFinAsJADate(), dateCalculDebutInteret, taux);

            if ((montantInteret != null) && !montantInteret.isZero()) {
                addDetailInteretMoratoire(interet, montantSoumis, taux, montantInteret, dateCalculDebutInteret,
                        getDateFinAsJADate().toStr("."));
            }

        }
    }

    public Boolean isOfficeExcluDuNouveauRegime(String cantonOfficePoursuite) throws RemoteException, Exception {
        return CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSessionAquila(getSession()),
                cantonOfficePoursuite);
    }

    private static BSession getSessionAquila(BSession session) throws RemoteException, Exception {
        return (BSession) GlobazSystem.getApplication("AQUILA").newSession(session);
    }

    /**
     * Retourne true si la date d'ex�cution de la section aux poursuites est apr�s la date de mise en production du
     * nouveau CDP (propri�t� en DB - dateProductionNouveauCDP)
     * Attention retourne false si la section aux poursuites est null
     * 
     * @param sectionAuxPoursuite
     * @return
     * @throws RemoteException
     * @throws Exception
     */
    private boolean isNouveauCDP(CASectionAuxPoursuites sectionAuxPoursuite) throws RemoteException, Exception {
        boolean isNouveauCDP = false;
        if (sectionAuxPoursuite != null) {

            String dateProductionNouveauCDP = GlobazSystem.getApplication("AQUILA").getProperty(
                    "dateProductionNouveauCDP");

            isNouveauCDP = !JadeStringUtil.isBlank(dateProductionNouveauCDP)
                    && !JadeDateUtil.isDateBefore(sectionAuxPoursuite.getHistorique().getDateExecution(),
                            dateProductionNouveauCDP);
        }
        return isNouveauCDP;
    }

    private CASectionAuxPoursuites getSectionAuxPoursuites() throws Exception {
        CASectionAuxPoursuitesManager managerReqPoursuite = new CASectionAuxPoursuitesManager();
        managerReqPoursuite.setSession(getSession());
        managerReqPoursuite.setForIdSection(getIdSection());
        // POAVS-294
//        managerReqPoursuite.setSoldeDifferentZero(true);
        managerReqPoursuite.find();
        CASectionAuxPoursuites sectionAuxPoursuite = (CASectionAuxPoursuites) managerReqPoursuite.getFirstEntity();

        return sectionAuxPoursuite;
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
     * Return la section en cours sur laquelle les int�r�ts moratoires doivent �tre calcul�es.
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
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
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
     * @param idJournal
     *            the idJournal to set
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

}
