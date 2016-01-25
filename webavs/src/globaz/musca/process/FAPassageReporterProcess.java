/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CASectionEnteteFacture;
import globaz.osiris.db.comptes.CASectionEnteteFactureManager;
import globaz.osiris.translation.CACodeSystem;

/**
 * @author mmu Ce module examine pour l'ensemble des factures du passage, s'il y a des sections ouvertes dans le compte
 *         du débiteur dont le signe est considéré comme minime et du même signe qe le montant de la facture. Une ligne
 *         (afact) de report est générée sous la forme d'une compensation
 */
public class FAPassageReporterProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FAApplication app;
    private String idModuleFacturation = "";
    private int nbReporte = 0;

    /**
     * @param context
     */
    public FAPassageReporterProcess(BProcess context) {
        super(context);
    }

    public String _calculerIdExterneRubrique(CASectionEnteteFacture entFacture) {

        String montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;
        String montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;
        FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
        try {
            montantMinimeNeg = ((FAApplication) getSession().getApplication()).getMontantMinimeNeg();

            montantMinimePos = ((FAApplication) getSession().getApplication()).getMontantMinimePos();
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible les montants minimes: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }

        if ((totalFacture.compareTo(new FWCurrency(montantMinimeNeg)) > 0)
                && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) < 0)) {
            return getRubriqueCode(APIReferenceRubrique.MONTANT_MINIME);
        } else {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_SOLDE);
        }
    }

    /**
     * Crée un afact de report à la hauteur du montant de la section au montant minime
     * 
     * @param section
     * @param cpt
     * @param aQuittancer
     * @param remarque
     */
    private boolean _creerAfactReport(CASectionEnteteFacture section, CACompteAnnexe cpt, boolean aQuittancer,
            String remarque) {
        // compenser à hauteur de la section
        FAAfact afactDeReport = new FAAfact();
        afactDeReport.setSession(getSession());
        afactDeReport.setIdPassage(passage.getIdPassage());
        afactDeReport.setIdEnteteFacture(section.getIdEnteteFacture());
        afactDeReport.setIdModuleFacturation(getIdModuleFacturation());
        afactDeReport.setIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
        afactDeReport.setIdExterneRubrique(_calculerIdExterneRubrique(section));
        afactDeReport.setMontantFacture(section.getSolde());

        // Informations provenant de la section Osiris
        afactDeReport.setIdExterneFactureCompensation(section.getIdExterne());
        afactDeReport.setIdTypeFactureCompensation(section.getIdTypeSection());

        afactDeReport.setAQuittancer(new Boolean(aQuittancer));
        afactDeReport.setRemarque(remarque);

        try {
            afactDeReport.add(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        }

        return !getTransaction().hasErrors();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        // prendre le passage en cours;
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());
        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }
        // Vérifier si le passage a les critères de validité pour une impression
        if (!_passageIsValid(passage)) {
            abort();
            return false;
        } // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }
        // Exécuter l'impression
        // ----------------------------------------------------------------
        boolean estTraite = _executeReporterProcess(passage);

        // finaliser le passage (le déverrouiller)
        if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_TRAITEMENT)) {
            abort();
            return false;
        }
        ;
        return estTraite;
    }

    /**
     * @return
     */
    public boolean _executeReporterProcess(IFAPassage passage) {
        // test du passage
        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                passage.retrieve(getTransaction());
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
            ;
        }

        this.setPassage((FAPassage) passage);
        boolean successful = false;

        try {

            // initialiser le module de la comptabilité d'Osiris
            app = (FAApplication) getSession().getApplication();

            CASectionEnteteFactureManager sectionMgr = new CASectionEnteteFactureManager();
            // garantir que le manager rapatrie toute les données dans son
            // container
            sectionMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            sectionMgr.setISession(app.getSessionOsiris(getSession()));
            sectionMgr.setForIdPassage(passage.getIdPassage());

            // Report des montants minimes
            successful = reporterMontantNeg(passage, sectionMgr);
            successful &= reporterMontantPos(passage, sectionMgr);

        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur dans le traitement du report...:" + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            successful = false;
        } finally {
            // Fin du processus
            getMemoryLog().logMessage("Clôture du process de report", globaz.framework.util.FWMessage.INFORMATION,
                    this.getClass().getName());
            String emailSubject = getSession().getLabel("OBJEMAIL_FA_SUBJECT_INFO")
                    + getSession().getLabel("OBJEMAIL_FA_REPORT");
            super.setEMailObject(emailSubject + "::(" + nbReporte + ")::" + passage.getIdPassage() + "::"
                    + passage.getDateFacturation());

            // permet l'affichage des données du processus
            setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_ENVOIEMAIL"));

            return (!isOnError() && successful);
        }
    }

    /**
     * Retourne le compte annexe associé à un entête de facture
     * 
     * @param compensationAnnexeEntity
     * @return
     */
    public CACompteAnnexe getCompteAnnexe(FAEnteteFacture enteteFacture) {

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setISession(app.getSessionOsiris(getSession()));
        compteAnnexe.setIdTiers(enteteFacture.getIdTiers());
        compteAnnexe.setIdRole(enteteFacture.getIdRole());
        compteAnnexe.setIdExterneRole(enteteFacture.getIdExterneRole());

        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);

        try {
            compteAnnexe.retrieve(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(), this.getClass().getName() + ": Erreur dans le compte annexe");
        } finally {
            if (!getTransaction().hasErrors()) {
                return compteAnnexe;
            } else {
                return null;
            }
        }
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getMontantMinimeNeg() throws Exception {
        return ((FAApplication) getSession().getApplication()).getMontantMinimeNeg();
    }

    public String getMontantMinimePos() throws Exception {

        return ((FAApplication) getSession().getApplication()).getMontantMinimePos();

    }

    /**
     * @param idRubrique
     *            Code système de la référence rubrique. Voir APIReferenceRubrique.
     * @return le numéro de la rubrique.
     */
    public String getRubriqueCode(String idRubrique) {
        String rubrique = "";
        CAReferenceRubrique reference = new CAReferenceRubrique();
        reference.setSession(getSession());
        rubrique = reference.getRubriqueByCodeReference(idRubrique).getIdExterne();
        if (rubrique.equals("") || rubrique.equals(null)) {
            this._addError("La reference rubrique n'est pas définie !!! (refRubrique:" + idRubrique + ")");
        }
        return rubrique;
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

    /**
     * @param passage
     * @param sectionMgr
     */
    private boolean reporterMontant(IFAPassage passage, CASectionEnteteFactureManager sectionMgr) throws Exception {
        BStatement statement = sectionMgr.cursorOpen(getTransaction());

        // initialisation de la progress bar
        int nbFactures = sectionMgr.getCount();
        setProgressScaleValue(nbFactures + 1);
        setProgressCounter(nbReporte);
        setState(getSession().getLabel("PROCESSSTATE_PASSAGE_REPORT"));
        CASectionEnteteFacture section = null;

        boolean aQuittancer;
        boolean successful = true;
        String remarque;
        while ((null != (section = (CASectionEnteteFacture) sectionMgr.cursorReadNext(statement)))
                && (!section.isNew()) && !isAborted()) {
            nbReporte++;
            setProgressCounter(nbReporte);

            // Reporter si un compte annex existe avec un montant de même signe

            FWCurrency totalFacture = new FWCurrency(section.getTotalFacture());
            FWCurrency totalSection = new FWCurrency(section.getSolde());

            // Condition minimale pour le report: les montants doivent être de
            // même signe
            if ((totalFacture.isNegative() && totalSection.isNegative())
                    || (totalFacture.isPositive() && totalSection.isPositive())) {

                aQuittancer = false;
                remarque = "";
                CACompteAnnexe cpt = (CACompteAnnexe) section.getCompteAnnexe();

                // si compte bloqué et actif,
                if (cpt.isCompteBloqueEtActif(passage.getDateFacturation())) {
                    aQuittancer = true;
                    remarque = getSession().getLabel("COMPTEANNEXE_CONTENTIEUX_MOTIF")
                            + CACodeSystem.getLibelle(getSession(), cpt.getIdContMotifBloque());
                }
                // ou si section aux contentieux, on met l'afact à quittancer
                // avec une remarque
                else if (totalSection.isPositive() && section.getContentieuxEstSuspendu().booleanValue()) {
                    aQuittancer = true;
                    remarque = getSession().getLabel("SECTION_CONTENTIEUX");
                }

                if (!_creerAfactReport(section, cpt, aQuittancer, remarque)) {
                    successful = false;
                }

            }
        }
        return successful;
    }

    /**
     * Reporte les montants minimaux négatif
     * 
     * @param passage
     * @param sectionMgr
     */
    private boolean reporterMontantNeg(IFAPassage passage, CASectionEnteteFactureManager sectionMgr) throws Exception {
        sectionMgr.setFromSolde(getMontantMinimeNeg());
        sectionMgr.setUntilSolde("0");
        return reporterMontant(passage, sectionMgr);

    }

    /**
     * Reporte les montants minimaux positifs
     * 
     * @param passage
     * @param sectionMgr
     */
    private boolean reporterMontantPos(IFAPassage passage, CASectionEnteteFactureManager sectionMgr) throws Exception {
        sectionMgr.setFromSolde("0");
        sectionMgr.setUntilSolde(getMontantMinimePos());
        return reporterMontant(passage, sectionMgr);

    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }
}
