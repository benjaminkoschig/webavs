package globaz.lynx.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.util.ArrayList;

public class LXOrdreGroupeAnnulerProcess extends LXOrdreGroupeProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.process.LXOrdreGroupeProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            if (isAborted()) {
                return false;
            }

            // Passage en traitement
            updateEtatOrdreGroupe(LXOrdreGroupe.CS_ETAT_TRAITEMENT);

            if (isAborted()) {
                return false;
            }

            // On annule le journal
            annulationOrdreGroupe();

            if (isAborted()) {
                return false;
            }

            annuleJournalDestination();

            if (isAborted()) {
                return false;
            }

            comptabiliseOperationOrpheline();

            if (isAborted()) {
                return false;
            }

            // Annulation
            updateEtatOrdreGroupe(LXOrdreGroupe.CS_ETAT_ANNULE);

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("ANNU_ORDREGROUPE_ERROR"));
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

        // Vérification présence d'un id ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_ORDRE_GROUPE"));
            return;
        }

        // L'id de société doit être renseigné
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        // L'ordre groupé doit etre dans un état différent de "annulé"
        if (LXOrdreGroupe.CS_ETAT_ANNULE.equals(getOrdreGroupe().getCsEtat())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNU_ORDREGROUPE_ETAT_ANNULE"));
            return;
        }

        // Doit etre dans une période comptable ouverte
        if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getOrdreGroupe()
                .getDateEcheance(), getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * Permet l'annulation de l'ordre groupé
     * 
     * @throws Exception
     */
    protected void annulationOrdreGroupe() throws Exception {

        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            // Mise a jour des opérations sources a l'etat "comptabilisé"
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdOrdreGroupe(getIdOrdreGroupe());

            ArrayList<String> list = new ArrayList<String>();
            list.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
            list.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
            list.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
            list.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
            list.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
            list.add(LXOperation.CS_TYPE_ESCOMPTE);
            manager.setForIdTypeOperationIn(list);

            manager.find(readTransaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                LXOperation ope = (LXOperation) manager.get(i);

                updateOperationSrc(readTransaction, ope);

                // Annulation de l'opération si l'ordre groupé n'a pas passé
                // l'exécution
                // Car le processus d'annulation du journal annulerait ces
                // dernières
                if (JadeStringUtil.isIntegerEmpty(getOrdreGroupe().getIdJournalLie())) {
                    updateEtatOperation(ope, LXOperation.CS_ETAT_ANNULE);
                }
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
     * Annulation du journal destinataire de l'ordre groupé si l'ordregroupé a passé l'exécution.
     * 
     * @throws Exception
     */
    private void annuleJournalDestination() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getOrdreGroupe().getIdJournalLie())) {
            LXJournalAnnulerProcess process = new LXJournalAnnulerProcess();
            process.setSession(getSession());

            process.setIdJournal(getOrdreGroupe().getIdJournalLie());
            process.setIdSociete(getOrdreGroupe().getIdSociete());

            process.setDesactivationTestPourOrdreGroupe(true);

            process.setEMailAddress(getEMailAddress());

            process.executeProcess();
        }
    }

    /**
     * Permet de passer les opérations qui sont préparées mais qui n'ont pas d'escomptes/paiements associés.
     * 
     * @throws Exception
     */
    private void comptabiliseOperationOrpheline() throws Exception {

        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());
        manager.setForIdOrdreGroupe(getIdOrdreGroupe());

        ArrayList<String> list = new ArrayList<String>();
        list.add(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE);
        list.add(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE);
        list.add(LXOperation.CS_TYPE_FACTURE_LSV);
        list.add(LXOperation.CS_TYPE_FACTURE_CAISSE);
        list.add(LXOperation.CS_TYPE_FACTURE_VIREMENT);
        list.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        manager.setForIdTypeOperationIn(list);

        ArrayList<String> listeEtat = new ArrayList<String>();
        listeEtat.add(LXOperation.CS_ETAT_PREPARE);
        listeEtat.add(LXOperation.CS_ETAT_SOLDE);
        manager.setForCsEtatIn(listeEtat);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            LXOperation ope = (LXOperation) manager.get(i);

            updateEtatOperation(ope, LXOperation.CS_ETAT_COMPTABILISE);
        }

    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("ANNU_ORDREGROUPE_ERROR");
        } else {
            return getSession().getLabel("ANNU_ORDREGROUPE_OK");
        }
    }

    /**
     * Permet la mise a jour de l'etat des notes de crédit liées aux factures sources de l'ordre groupé
     * 
     * @param readTransaction
     * @param ope
     * @throws Exception
     */
    protected void updateEtatNdcLiee(BTransaction readTransaction, LXOperation ope) throws Exception {

        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());
        manager.setForIdOperationLiee(ope.getIdOperation());
        manager.setForIdOrdreGroupe(getOrdreGroupe().getIdOrdreGroupe());
        manager.find(readTransaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            LXOperation opeLiee = (LXOperation) manager.get(i);

            updateEtatOperation(opeLiee, LXOperation.CS_ETAT_COMPTABILISE);
        }

    }

    /**
     * Permet de passer toutes les opérations sources des paiements et/ou escompte en comptabilisé
     * 
     * @throws Exception
     */
    protected void updateOperationSrc(BTransaction readTransaction, LXOperation ope) throws Exception {

        // REcehrche de lla facture associé

        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());
        manager.setForIdOperation(ope.getIdOperationSrc());

        manager.find(readTransaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            LXOperation opeSrc = (LXOperation) manager.get(i);

            updateEtatOperation(opeSrc, LXOperation.CS_ETAT_COMPTABILISE);

            // recherche d'une note de crédit liée a cette facture et passage en
            // comptabilisé
            updateEtatNdcLiee(readTransaction, opeSrc);
        }
    }
}
