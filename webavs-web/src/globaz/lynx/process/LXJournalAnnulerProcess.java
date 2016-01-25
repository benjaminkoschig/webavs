package globaz.lynx.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationAnnulerManager;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.util.ArrayList;

public class LXJournalAnnulerProcess extends LXJournalProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean desactivationTestPourOrdreGroupe = false;

    /**
     * @see globaz.lynx.process.LXJournalProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            if (isAborted()) {
                return false;
            }

            // On regarde si toutes les conditions sont r�unies pour faire une
            // annulation
            checkPossibiliteAnnulation();

            if (isAborted()) {
                return false;
            }

            // Passage du journal en traitement
            updateEtatJournal(LXJournal.CS_ETAT_TRAITEMENT);

            if (isAborted()) {
                return false;
            }

            annuleJournalCG();

            if (isAborted()) {
                return false;
            }

            // On annule le journal
            annulationJournal();

            if (isAborted()) {
                return false;
            }

            updateEtatJournal(LXJournal.CS_ETAT_ANNULE);

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("ANNU_JOURNAL_ERROR"));
            this._addError(getTransaction(), e.getMessage());

            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        // V�rification pr�sence d'un id de journal
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
            return;
        }

        // L'id de soci�t� doit �tre renseign�
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        // Le journal doit etre dans un �tat diff�rent de "annul�"
        if (LXJournal.CS_ETAT_ANNULE.equals(getJournal().getCsEtat())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNU_JOURNAL_ETAT_ANNULE"));
            return;
        }

        // V�rification p�riode comptable ouverte
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())
                && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getJournal()
                        .getDateValeurCG(), getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * Permet l'annulation du journal<br>
     * - Annulation du journal dans la comptabilit� g�n�rale<br>
     * - Annulation des op�rations et du journal dans la comptabilit� foutnisseur
     * 
     * @throws Exception
     */
    protected void annulationJournal() throws Exception {
        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            // Annulation dans la comptabilit� fournisseur
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());
            manager.setForIdJournal(getIdJournal());
            manager.find(readTransaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                LXOperation ope = (LXOperation) manager.get(i);

                updateEtatOperation(ope, LXOperation.CS_ETAT_ANNULE);
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
     * Annulation dans la comptabilit� g�n�rale.
     * 
     * @throws Exception
     */
    private void annuleJournalCG() throws Exception {
        if (getJournalCG() != null) {
            getJournalCG().annuler(getTransaction(), getEMailAddress());
        }
    }

    /**
     * Check si on a pas une ndc encaiss� qui est li� a une NDC du journal
     * 
     * @throws Exception
     */
    protected void checkNdcEncaisseSurNdcDuJournal() throws Exception {

        LXOperationAnnulerManager manager = new LXOperationAnnulerManager();
        manager.setSession(getSession());
        manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);
        manager.setForIdOperationSrcInIdJournal(getIdJournal());

        ArrayList<String> list = new ArrayList<String>();
        list.add(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
        manager.setForCsTypeOperationIn(list);

        manager.find(getTransaction());

        if (manager.size() > 0) {
            throw new Exception(getSession().getLabel("ANNU_NDC_ENCAISSE_LIEE_AUX_NDC"));
        }
    }

    /**
     * Controle si on a une note de cr�dit li�e dans le journal avec comme etat diff�rent de comptabilis�
     * 
     * @return
     */
    protected void checkNdcLieeComptabilisee() throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getIdJournal());
        manager.setForCsEtatNot(LXOperation.CS_ETAT_COMPTABILISE);

        ArrayList<String> list = new ArrayList<String>();
        list.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        manager.setForIdTypeOperationIn(list);

        if (manager.size() > 0) {
            throw new Exception(getSession().getLabel("ANNU_NDC_LIEE"));
        }
    }

    /**
     * Check si on a une NDC li� � une facture
     * 
     * @throws Exception
     */
    protected void checkNdcLieeSurFactureDuJournal() throws Exception {

        LXOperationAnnulerManager manager = new LXOperationAnnulerManager();
        manager.setSession(getSession());
        manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);
        manager.setForIdOperationLieeInIdJournal(getIdJournal());

        ArrayList<String> list = new ArrayList<String>();
        list.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        manager.setForCsTypeOperationIn(list);

        manager.find(getTransaction());

        if (manager.size() > 0) {
            throw new Exception(getSession().getLabel("ANNU_NDC_LIEE_AUX_FACTURES"));
        }
    }

    protected void checkOrdreGroupeLie() throws Exception {

        if (!isDesactivationTestPourOrdreGroupe() && !JadeStringUtil.isIntegerEmpty(getJournal().getIdOrdreGroupeSrc())) {
            this._addError(getTransaction(), getSession().getLabel("ANNU_JOURNAL_IMPOSSIBLE_LIEN_ORDREGROUPE"));
        }

    }

    /**
     * Check si paiement sur facture du journal dont etat != annul�
     * 
     * @throws Exception
     */
    protected void checkPaiementSurFactureDuJournal() throws Exception {

        LXOperationAnnulerManager manager = new LXOperationAnnulerManager();
        manager.setSession(getSession());
        manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);
        manager.setForIdOperationSrcInIdJournal(getIdJournal());

        ArrayList<String> list = new ArrayList<String>();
        list.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
        list.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
        list.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        list.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        list.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
        manager.setForCsTypeOperationIn(list);

        manager.find(getTransaction());

        if (manager.size() > 0) {
            throw new Exception(getSession().getLabel("ANNU_PAIEMENT_PRESENT_NON_ANNULE"));
        }
    }

    /**
     * Check de la possibilit� d'annulation du journal
     * 
     * @throws Exception
     */
    protected void checkPossibiliteAnnulation() throws Exception {

        // Check si le journal n'est pas rattach� un un ordre group� (d�sactiv�
        // si annulation depuis un ordre group�)
        checkOrdreGroupeLie();

        // Check si toutes les NDC li�es sont toutes a l'etat Comptabilis�
        checkNdcLieeComptabilisee();

        // Check si paiement sur facture du journal dont etat != annul�
        checkPaiementSurFactureDuJournal();

        // Check si on a une NDC li� � une facture
        checkNdcLieeSurFactureDuJournal();

        // Check si on a pas une ndc encaiss� qui est li� a une NDC du journal
        checkNdcEncaisseSurNdcDuJournal();
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("ANNU_JOURNAL_ERROR");
        } else {
            return getSession().getLabel("ANNU_JOURNAL_OK");
        }
    }

    public boolean isDesactivationTestPourOrdreGroupe() {
        return desactivationTestPourOrdreGroupe;
    }

    public void setDesactivationTestPourOrdreGroupe(boolean deactivationTestPourOrdreGroupe) {
        desactivationTestPourOrdreGroupe = deactivationTestPourOrdreGroupe;
    }
}
