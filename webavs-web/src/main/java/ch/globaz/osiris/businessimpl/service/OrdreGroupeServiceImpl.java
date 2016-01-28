package ch.globaz.osiris.businessimpl.service;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import java.rmi.RemoteException;
import ch.globaz.osiris.business.service.OrdreGroupeService;
import ch.globaz.osiris.exception.OsirisException;

public class OrdreGroupeServiceImpl implements OrdreGroupeService {

    @Override
    public void createOrdreGroupeeAndPrepare(String idJournal, String idOrganeExecution, String numeroOG,
            String dateEcheance, String typeOrdre, String natureOrdre) throws OsirisException {

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            checkOrganeExecution(idOrganeExecution);

            if (JAUtil.isDateEmpty(dateEcheance)) {
                throw new OsirisException("DateEcheance is empty");
            }

            // - Validité de l'organe d'execution
            if (JadeStringUtil.isIntegerEmpty(idOrganeExecution)) {
                throw new OsirisException("idOrganeExecution is empty");
            }

            // Vérifie sa validité
            try {
                BSessionUtil.checkDateGregorian(BSessionUtil.getSessionFromThreadContext(), dateEcheance);
            } catch (Exception jae) {
                throw new OsirisException("Error check date gregorian", jae);
            }

            // - Présence d'un journal
            if (idJournal == null) {
                throw new OsirisException("The id journal is null");
            }

            CAJournal journal = readJournal(idJournal);

            // - Etat du journal
            if (!CAJournal.COMPTABILISE.equals(journal.getEtat())) {
                throw new OsirisException("The journal is not comptabilise");
            }

            CAOrdreGroupe ordreGroupe = new CAOrdreGroupe();
            ordreGroupe.setSession(BSessionUtil.getSessionFromThreadContext());
            ordreGroupe.setIdOrganeExecution(idOrganeExecution);
            ordreGroupe.setNumeroOG(numeroOG);
            ordreGroupe.setDateEcheance(dateEcheance);
            ordreGroupe.setTypeOrdreGroupe(typeOrdre);
            ordreGroupe.setNatureOrdresLivres(natureOrdre);
            ordreGroupe.setMotif(journal.getIdJournal() + " " + journal.getLibelle());

            try {
                ordreGroupe.add();
            } catch (Exception e) {
                throw new OsirisException("Error on create ordreGroupe", e);
            }
            preparOrdreGroupee(ordreGroupe, journal);
        }
    }

    private void checkOrganeExecution(String idOrganeExecution) throws JadeNoBusinessLogSessionError, OsirisException {
        CAOrganeExecution organeExecution = new CAOrganeExecution();
        organeExecution.setSession(BSessionUtil.getSessionFromThreadContext());
        organeExecution.setIdOrganeExecution(idOrganeExecution);

        try {
            organeExecution.retrieve();
            if (organeExecution.isNew() || organeExecution.hasErrors()) {
                throw new OsirisException("OrganeExecution not exits (id:" + idOrganeExecution + ")");
            }
        } catch (Exception e) {
            throw new OsirisException("Error On retrive organeExecution", e);
        }
    }

    private CAJournal readJournal(String idJournal) throws OsirisException {
        CAJournal journal = new CAJournal();
        journal.setId(idJournal);
        try {
            journal.retrieve();
        } catch (Exception e1) {
            throw new OsirisException("Unalbled to read the journal with this id:" + idJournal, e1);
        }
        return journal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.02.2002 10:46:32)
     * 
     * @param context
     *            CAProcessAttacherOrdre
     */
    private void executeAttacherOrdreVersement(CAOrdreGroupe ordreGroupe, String idJournalSource) {

        // Initialiser
        FWCurrency cTotal = new FWCurrency();
        long lNTransactions = 0;
        String lastIdOperation = "0";

        // Sous contrôle d'exception
        try {
            // Instancier un manager
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            // mgr.setSession(BSessionUtil.getSessionFromThreadContext());
            mgr.setOrderBy(CAOperationOrdreManager.ORDRE_IDORDREGROUPE_IDOPERATION);
            // On prend que les ordres qui ne sont pas bloqués
            mgr.wantForEstBloque(false);

            // On restreint les ordres sur le journal spécifié en paramètre
            if (!JadeStringUtil.isIntegerEmpty(idJournalSource)) {
                mgr.setForIdJournal(idJournalSource);
            }

            // On récupère les ordres non rattachés qui correspondent
            mgr.setForIdOrdreGroupe("0");
            // - à la nature des ordres désirés
            if (!JadeStringUtil.isIntegerEmpty(ordreGroupe.getNatureOrdresLivres())
                    && (!ordreGroupe.getNatureOrdresLivres().equals(CAOrdreGroupe.ORDRESTOUS))) {
                mgr.setForIdNatureOrdre(ordreGroupe.getNatureOrdresLivres());
            }

            // - date d'échéance de l'ordre inférieure ou égale à la date de
            // l'ordre groupé
            mgr.setUntilDate(ordreGroupe.getDateEcheance());
            // - dont l'état est comptabilisé
            mgr.setForEtat(APIOperation.ETAT_COMPTABILISE);

            while (true) {

                // Récupérer les prochaines transactions
                mgr.clear();
                mgr.find(BManager.SIZE_NOLIMIT);
                if (mgr.hasErrors()) {
                    throw new RuntimeException(getLibelleOsiris("5403"));
                }

                // Sortir si vide
                if (mgr.size() == 0) {
                    break;
                }

                // Parcourir les ordres
                for (int i = 0; i < mgr.size(); i++) {

                    // Récupérer l'ordre
                    CAOperationOrdreVersement oper = (CAOperationOrdreVersement) mgr.getEntity(i);
                    oper.setSession(BSessionUtil.getSessionFromThreadContext());

                    // Sauver le dernier ID
                    lastIdOperation = oper.getIdOperation();

                    // Vérifier l'organe d'exécution
                    if (JadeStringUtil.isIntegerEmpty(oper.getIdOrganeExecution())
                            || oper.getIdOrganeExecution().equals(ordreGroupe.getIdOrganeExecution())) {
                        FWCurrency tmp = new FWCurrency(oper.getMontant());
                        tmp.add(oper.getSection().getSolde());
                        if (!oper.getIdTypeOperation().equals(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE)
                                && CAApplication.getApplicationOsiris().getCAParametres().isCheckMontantARembourser()
                                && tmp.isPositive()) {
                            oper.setEstBloque(new Boolean(true));
                            oper.setIdOrdreGroupe("0");

                            throw new RuntimeException(getLibelleOsiris("SOLDE_SECTION_INFERIEUR_MONTANT_REMBOURSE"));

                        } else {
                            // Le rattacher
                            oper.setIdOrdreGroupe(ordreGroupe.getIdOrdreGroupe());

                            // Totaux
                            lNTransactions++;
                            cTotal.add(oper.getMontant());
                        }
                        oper.update();
                        // oper.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());
                        if (oper.hasErrors()) {
                            throw new RuntimeException(getLibelleOsiris("5025"));
                        }
                    }
                }

                // Prochain numéro
                mgr.setAfterIdOperation(lastIdOperation);
            }

            // Stocker le nombre total de transactions et le montant versé
            ordreGroupe.setTotal(cTotal.toString());
            ordreGroupe.setNombreTransactions(String.valueOf(lNTransactions));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void preparOrdreGroupee(CAOrdreGroupe ordreGroupe, CAJournal journal) throws OsirisException {

        if (ordreGroupe == null) {

        }
        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(ordreGroupe.getIdOrdreGroupe())) {
            throw new OsirisException("IdOrdreGroupe is empty");
        }

        try {
            if (ordreGroupe.getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                executeAttacherOrdreVersement(ordreGroupe, journal.getId());
            } else {
                throw new RuntimeException("Not supported -> todo");
            }

            // Si aucune transaction, on donne un message d'erreur
            if (JadeStringUtil.isIntegerEmpty(ordreGroupe.getNombreTransactions())) {
                throw new RuntimeException(getLibelleOsiris("5404"));
            }

            // Si OK, l'ordre est ouvert
            if (!BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().hasErrors()) {
                ordreGroupe.setEtat(CAOrdreGroupe.OUVERT);
                ordreGroupe.setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            }
            ordreGroupe.update();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lor da la préparation de l'ordre groupé", e);
        }

    }

    private String getLibelleOsiris(String idMessage) throws RemoteException, Exception {
        return ((BSession) CAApplication.getApplicationOsiris().newSession(BSessionUtil.getSessionFromThreadContext()))
                .getLabel(idMessage);
    }

}
