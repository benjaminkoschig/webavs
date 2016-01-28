package globaz.lynx.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.ventilation.LXVentilation;
import globaz.lynx.db.ventilation.LXVentilationManager;
import globaz.lynx.service.helios.LXHeliosService;
import globaz.lynx.utils.LXSectionUtil;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.util.ArrayList;

/**
 * Comptabilisation d'un journal de comptabilit� fournisseur<br/>
 * <br/>
 * 1. Mise � jour de l'�tat : Traitement<br/>
 * 2. Cr�er le journal de comptabilit� g�n�rale<br/>
 * 4. Parcours les op�rations et les ventilations pour les ajouter une � une au journal de comptabilit� g�n�rale<br/>
 * 5. Comptabilisation du journal de comptabilit� g�n�rale<br/>
 * 6. Mise � jour de l'�tat : Comptabilise<br/>
 * 
 * @author DDA
 */
public class LXJournalComptabiliserProcess extends LXJournalProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int PIECE_COMTPABLE_MAX_LENGTH = 10;

    /**
     * Commentaire relatif au constructeur LXJournalAnnulerProcess.
     */
    public LXJournalComptabiliserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur LXJournalAnnulerProcess.
     * 
     * @param parent
     *            BProcess
     */
    public LXJournalComptabiliserProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur LXJournalAnnulerProcess.
     * 
     * @param session
     *            BSession
     */
    public LXJournalComptabiliserProcess(BSession session) {
        super(session);
    }

    /**
     * @see BProcess#_executeProcess() throws Exception
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            if (!getJournal().isTraitement()) {
                setEtatJournalToTraitement();
            }

            if (isAborted()) {
                return false;
            }

            if (JadeStringUtil.isIntegerEmpty(getJournal().getIdJournalCG())) {
                createJournalCG();
                attachJournalCGToJournalLX();
            }

            if (isAborted()) {
                return false;
            }

            addOperationsToJournalCG();
            if (checkAllOperationsEtatComptabilise()) {
                comptabiliseJournalCG();
                setEtatJournalToComptabilise();
            } else {
                throw new Exception(getSession().getLabel("COMPTABILISER_FACTURES_ENCORE_OUVERTES"));
            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("COMPTABILISER_ERREUR"));
            this._addError(getTransaction(), e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
            return;
        }

        if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getJournal()
                .getDateValeurCG(), getJournal().getSociete().getIdMandat())) {
            return;
        }

        if (!getJournal().isOuvert() && !getJournal().isTraitement()) {
            this._addError(getTransaction(), getSession().getLabel("COMPTABILISER_ETAT_JOURNAL"));
            return;
        }
    }

    /**
     * Comptabilise les op�rations et passe les montants en comptabilit� g�n�rale.
     * 
     * @throws Exception
     */
    private void addOperationsToJournalCG() throws Exception {
        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) getJournal().getSession().newTransaction();
            readTransaction.openTransaction();

            LXOperationManager manager = initOperationManager();

            manager.find(readTransaction, BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.size() + (int) (manager.size() * 0.20));

            for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                LXOperation operation = (LXOperation) manager.get(i);

                addOperationToCG(readTransaction, operation);
                updateEtatOperationComptabilise(operation);

                if (getTransaction().hasErrors() || getSession().hasErrors()) {
                    getTransaction().rollback();
                    throw new Exception(getSession().getLabel("COMPTABILISER_FACTURE_NON_TRANSMISE"));
                } else {
                    getTransaction().commit();
                }

                incProgressCounter();
            }
        } catch (Exception e) {
            getTransaction().rollback();
            throw e;
        } finally {
            if (readTransaction != null) {
                try {
                    readTransaction.rollback();
                } finally {
                    readTransaction.closeTransaction();
                }
            }
        }

    }

    /**
     * Ajoute la facture au journal de comptabilit� g�n�rale.
     * 
     * @param readTransaction
     * @param operation
     * @throws Exception
     */
    private void addOperationToCG(BTransaction readTransaction, LXOperation operation) throws Exception {
        CGGestionEcritureViewBean ecritures = initEcritures(operation);

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();

        LXVentilationManager ventilationManager = initVentilationManager(operation);

        ventilationManager.find(readTransaction, BManager.SIZE_NOLIMIT);

        for (int j = 0; j < ventilationManager.size(); j++) {
            LXVentilation ventilation = (LXVentilation) ventilationManager.get(j);

            CGEcritureViewBean ecriture = initEcritureFromVentilation(ventilation);

            if (JadeStringUtil.isBlank(ecriture.getLibelle())) {
                ecriture.setLibelle(operation.getLibelle());
            }

            ecrituresList.add(ecriture);
        }

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
    }

    /**
     * Sauvegard l'id journal CG dans le journal LX.
     * 
     * @throws Exception
     */
    private void attachJournalCGToJournalLX() throws Exception {
        getJournal().setIdJournalCG(getJournalCG().getIdJournal());
        getJournal().update(getTransaction());

        if (getJournal().hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("COMPTABILISER_JOURNAL_LIEN"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * Contr�le qu'il ne reste plus de factures non comptabilis�es dans le journal.
     * 
     * @return True si aucune op�ration ouverte.
     * @throws Exception
     */
    private boolean checkAllOperationsEtatComptabilise() throws Exception {
        return (initOperationManager().getCount(getTransaction()) == 0);
    }

    /**
     * Comptabilise le journal de comptabilit� g�n�rale.
     * 
     * @throws Exception
     */
    private void comptabiliseJournalCG() throws Exception {
        getJournalCG().comptabiliser(this);
    }

    /**
     * Cr�er et return le journal de comptabilit� g�n�rale.
     * 
     * @return
     * @throws Exception
     */
    private void createJournalCG() throws Exception {
        if (getJournalCG() == null) {
            CGExerciceComptable exercice = LXHeliosService.getExerciceComptable(getSession(), getJournal().getSociete()
                    .getIdMandat(), getJournal().getDateValeurCG());

            CGJournal journalCG = null;

            journalCG = new CGJournal();
            journalCG.setSession(getSession());
            journalCG.setLibelle(getJournal().getLibelle());
            journalCG.setDate(JACalendar.todayJJsMMsAAAA());
            journalCG.setDateValeur(getJournal().getDateValeurCG());
            journalCG.setReferenceExterne(CGJournal.LX_REFERENCE_PREFIX + getJournal().getIdJournal());
            journalCG.setEstPublic(new Boolean(true));

            journalCG.setIdExerciceComptable(exercice.getIdExerciceComptable());
            journalCG.setIdPeriodeComptable(LXHeliosService.getPeriode(getSession(), exercice.getIdExerciceComptable(),
                    getJournal().getDateValeurCG()).getIdPeriodeComptable());

            journalCG.add(getTransaction());

            if (journalCG.hasErrors()) {
                getTransaction().rollback();
                throw new Exception(journalCG.getErrors().toString());
            }

            if (journalCG.isNew()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("COMPTABILISER_JOURNAL_CG_NON_CREE"));
            }

            getTransaction().commit();
            setJournalCG(journalCG);
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("COMPTABILISER_ERREUR");
        } else {
            return getSession().getLabel("COMPTABILISER_OK");
        }
    }

    /**
     * Initialise une ligne d'�criture � partir d'une ventilation.
     * 
     * @param ventilation
     * @return
     * @throws Exception
     */
    private CGEcritureViewBean initEcritureFromVentilation(LXVentilation ventilation) throws Exception {
        CGEcritureViewBean ecriture = new CGEcritureViewBean();

        ecriture.setIdCompte(ventilation.getIdCompte());
        ecriture.setIdCentreCharge(ventilation.getIdCentreCharge());
        ecriture.setLibelle(ventilation.getLibelle());
        ecriture.setCodeDebitCredit(ventilation.getCodeDebitCredit());
        ecriture.setMontant(ventilation.getMontant());
        ecriture.setMontantMonnaie(ventilation.getMontantMonnaie());
        ecriture.setCoursMonnaie(ventilation.getCoursMonnaie());

        ecriture.setIdExterneCompte(LXHeliosService.getIdExterneCompte(getSession(), getJournalCG()
                .getIdExerciceComptable(), ventilation.getIdCompte()));

        return ecriture;
    }

    /**
     * Initialise une �critures afin d'ajouter une op�ration au journal de comptabilit� g�n�rale.
     * 
     * @param operation
     * @return
     * @throws Exception
     */
    private CGGestionEcritureViewBean initEcritures(LXOperation operation) throws Exception {
        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        ecritures.setIdJournal(getJournalCG().getIdJournal());
        ecritures.setDateValeur(operation.getDateOperation());
        ecritures.setIdExerciceComptable(getJournalCG().getIdExerciceComptable());
        ecritures.setRemarque(operation.getMotif());

        LXSection section = LXSectionUtil.getSection(getSession(), getTransaction(), operation.getIdSection());

        LXApplication application = (LXApplication) GlobazServer.getCurrentSystem().getApplication(
                LXApplication.DEFAULT_APPLICATION_LYNX);
        if (application.isCopierNumeroFactureInterne()) {
            String idExterneSection = section.getIdExterne();

            if (!JadeStringUtil.isBlank(idExterneSection)) {
                if (idExterneSection.length() > LXJournalComptabiliserProcess.PIECE_COMTPABLE_MAX_LENGTH) {
                    ecritures.setPiece(idExterneSection.substring(0,
                            LXJournalComptabiliserProcess.PIECE_COMTPABLE_MAX_LENGTH));
                } else {
                    ecritures.setPiece(idExterneSection);
                }
            }
        }

        ecritures.setIdFournisseur(section.getIdFournisseur());
        ecritures.setIdSection(operation.getIdSection());

        return ecritures;
    }

    /**
     * Init un manager de recherche des op�rations (tout type de facture) du journal en �tat ouverte.
     * 
     * @return
     * @throws Exception
     */
    private LXOperationManager initOperationManager() throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());

        manager.setForIdJournal(getJournal().getIdJournal());

        manager.setForIdTypeOperationNot(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);

        ArrayList<String> csEtatIn = new ArrayList<String>();
        csEtatIn.add(LXOperation.CS_ETAT_OUVERT);
        manager.setForCsEtatIn(csEtatIn);

        return manager;
    }

    /**
     * Return un manager pour les ventilations d'une op�ration.
     * 
     * @param operation
     * @return
     * @throws Exception
     */
    private LXVentilationManager initVentilationManager(LXOperation operation) throws Exception {
        LXVentilationManager manager = new LXVentilationManager();
        manager.setSession(getSession());

        manager.setForIdOperation(operation.getIdOperation());

        return manager;
    }

    /**
     * L'�tat du journal est comptabilis� une fois le processus termin�.
     * 
     * @throws Exception
     */
    private void setEtatJournalToComptabilise() throws Exception {
        getJournal().setCsEtat(LXJournal.CS_ETAT_COMPTABILISE);

        getJournal().update(getTransaction());

        if (getJournal().hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("COMPTABILISER_JOURNAL_NON_MISE_A_JOUR"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * L'�tat du journal est traitement d�s le d�but du processus de comptabilisation.
     * 
     * @throws Exception
     */
    private void setEtatJournalToTraitement() throws Exception {
        getJournal().setCsEtat(LXJournal.CS_ETAT_TRAITEMENT);

        getJournal().update(getTransaction());

        if (getJournal().hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("COMPTABILISER_JOURNAL_NON_MISE_A_JOUR"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * Apr�s cr�ation de l'�criture en comptabilit� g�n�rale, l'op�ration passe � comptabilis�
     * 
     * @param operation
     * @throws Exception
     */
    private void updateEtatOperationComptabilise(LXOperation operation) throws Exception {
        operation.setCsEtatOperation(LXOperation.CS_ETAT_COMPTABILISE);
        operation.update(getTransaction());
    }
}
