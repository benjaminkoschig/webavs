package globaz.aquila.service.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.COTaxe;
import globaz.aquila.util.COActionUtils;
import globaz.aquila.util.COJournalAdapter;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author vre
 */
public class COExecuteTransitionService extends COAbstractTransitionService {

    /**
     * @param session
     * @param transaction
     * @param contentieux
     * @param dernierHistorique
     * @param transition
     * @param action
     * @param journal
     * @return
     * @throws COTransitionException
     */
    private COHistorique createHistorique(BSession session, BTransaction transaction, COContentieux contentieux,
            COHistorique dernierHistorique, COTransition transition, COTransitionAction action, COJournalAdapter journal)
            throws COTransitionException {
        try {
            String idHistoriquePrecedant = "0";

            if (!contentieux.isNew() && (dernierHistorique != null)) {
                if (isEtapeSansInfluence(transition)) {
                    idHistoriquePrecedant = dernierHistorique.getIdHistoriquePrecedant();
                } else {
                    idHistoriquePrecedant = dernierHistorique.getIdHistorique();
                }
            }

            // historise
            return addHistorique(session, transaction, contentieux, action.getMotif(), transition.getIdEtapeSuivante(),
                    idHistoriquePrecedant, journal);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * Ex�cute l'action de cr�ation du contentieux pour le contentieux donn�.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    public void effectuerActionCreerContentieux(BSession session, BTransaction transaction, COContentieux contentieux)
            throws Exception {
        this.effectuerActionCreerContentieux(session, transaction, contentieux, true);
    }

    /**
     * Ex�cute l'action de cr�ation du contentieux pour le contentieux donn�.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    public void effectuerActionCreerContentieux(BSession session, BTransaction transaction, COContentieux contentieux,
            boolean checkMotifBlocageCtx) throws Exception {
        // rechercher l'�tape de cr�ation pour cette s�quence
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setForLibEtape(ICOEtape.CS_CONTENTIEUX_CREE);
        etapeManager.setForIdSequence(contentieux.getIdSequence());
        etapeManager.setSession(session);
        etapeManager.find();

        if (etapeManager.size() != 1) {
            throw new COTransitionException("AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE", COActionUtils.getMessage(session,
                    "AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE"));
        }

        // rechercher la transition pour cette �tape
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(session, transaction,
                ((COEtape) etapeManager.getFirstEntity()).getIdEtape(), null, null, null);

        if (action == null) {
            throw new COTransitionException("AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE", COActionUtils.getMessage(session,
                    "AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE"));
        }

        // HACK: dans le cas d'un ajout au contentieux, on utilise la date
        // d'ouverture comme date d'ex�cution
        action.setDateExecution(contentieux.getDateOuverture());

        /*
         * Effectue la transition, lors de la cr�ation d'un contentieux, on a une cr�ation un peu simplifi�e du fait
         * qu'il n'y a pas de taxes
         */
        try {
            action.canExecute(contentieux, transaction, checkMotifBlocageCtx);
        } catch (COTransitionException e) {
            if ((e.getMessageId() != null) && !e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")) {
                throw e;
            }
        }
        action.execute(contentieux, transaction);

        updateSection(transaction, contentieux, action.getTransition().getEtapeSuivante().getLibEtape());

        createHistorique(session, transaction, contentieux, null, action.getTransition(), action, null);
    }

    /**
     * @see #executerTransition(BSession, BTransaction, COContentieux, COTransitionAction, List, COJournalAdapter)
     */
    public void executerTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransitionAction action) throws Exception {
        COJournalAdapter journal = createJournal(session, transaction, contentieux);

        this.executerTransition(session, transaction, contentieux, action, journal);
    }

/**
	 * Execute la transition donn�e.
	 * <p>
	 * Par contrat, l'action transmise DOIT avoir �t� enti�rement initialis�e (
	 * {@link COTransitionAction#setAnnulerEcritures(boolean)}, {@link COTransitionAction#setDateExecution(String)},
	 * {@link COTransitionAction#setTaxes(List), {@link COTransitionAction#setTransition(COTransition), ...).
	 *
	 * @link COTransitionAction#execute(COContentieux, BTransaction)} puis cr�e les �l�ments li�s � cette transition
	 *       (historiques, comptabilisation des taxes, ...).
	 *       </p>
	 *       <p>
	 *       Par contrat, l'action DOIT pouvoir �tre ex�cut�e (
	 *       {@link COTransitionAction#canExecute(COContentieux, BTransaction)}).
	 *       </p>
	 *       <p>
	 *       Si le journal transmis est null, le processus est consid�r� comme non pr�visionnel.
	 *       </p>
	 *
	 * @param session
	 * @param transaction
	 * @param contentieux
	 * @param action
	 * @param journal
	 * @throws Exception
	 *             Si la transition ne peut �tre ex�cut�e, en particulier si la validation de l'action �choue
	 */
    public void executerTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransitionAction action, COJournalAdapter journal) throws Exception {
        // Doit �tre plac� avant contentieux.save()
        COHistorique dernierHistorique = contentieux.loadHistorique();

        FWCurrency totalIM = new FWCurrency("0");

        if ((journal != null) && (journal.getJournal() != null)) {
            totalIM = saveIMCalcule(transaction, contentieux.getDateExecution(), action.getInteretCalcule(), journal
                    .getJournal().getIdJournal());
        }

        // Effectue la transition
        contentieux.setPrevisionnel(((journal != null) && journal.isPrevisionnel()) ? Boolean.TRUE : Boolean.FALSE);
        action.execute(contentieux, transaction);

        if ((journal == null) || !journal.isPrevisionnel()) {
            // met � jour le contentieux
            contentieux.save(transaction);
            contentieux.refreshLinks(transaction);

            // met � jour le dernier �tat de la section
            if (!isEtapeSansInfluence(action.getTransition())) {
                if (ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION
                        .equals(contentieux.getEtape().getLibEtape())) {
                    updateSection(transaction, contentieux, ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
                } else {
                    updateSection(transaction, contentieux, action.getTransition().getEtapeSuivante().getLibEtape());
                }
            }

            // cr�e une ligne d'historique
            COHistorique historique = createHistorique(session, transaction, contentieux, dernierHistorique,
                    action.getTransition(), action, journal);

            // met � jour l'historique si c'est imputer versement
            if (isEtapeSansInfluence(action.getTransition())) {

                // mettre � jour le montant des taxes
                historique.setImpute(Boolean.TRUE);
                historique.update(transaction);
            }

            // sauvegarde les informations par �tapes
            FWCurrency totalTaxes = new FWCurrency(0);
            totalTaxes.add(totalIM);

            if ((action.getEtapeInfos() != null) && !action.getEtapeInfos().isEmpty()) {
                saveEtapesInfos(transaction, contentieux, historique.getIdHistorique(), action);
            }

            // sauvegarde les frais et interets
            if ((action.getFrais() != null) && !action.getFrais().isEmpty()) {
                totalTaxes.add(saveFrais(transaction, contentieux, historique.getIdHistorique(), action));
            }

            // sauvegarde les taxes
            if ((action.getTaxes() != null) && !action.getTaxes().isEmpty()) {
                totalTaxes.add(saveTaxes(transaction, contentieux, historique.getIdHistorique(), action));
            }

            // met � jour l'historique avec le total des taxes
            if ((totalTaxes != null) && totalTaxes.isPositive()) {
                updateHistoriqueWithTaxes(transaction, historique, totalTaxes);

                // mettre � jour les taxes
                contentieux.setMontantTotalTaxes("");
                COServiceLocator.getTaxeService().initMontantsTaxes(session, contentieux);
            }

            if (journal != null) {
                // cr�e les �critures dans Osiris
                journal.creerEcritures(transaction, contentieux, action.getTransition());
                journal.imputerTaxes(transaction, contentieux, action.getTaxes());
                journal.imputerInteretManuel(transaction, contentieux, action.getInteretCalcule());
            }
        }
    }

    /**
     * @see #executerTransition(BSession, BTransaction, COContentieux, COTransitionAction, List, COJournalAdapter)
     */
    public void executerTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransitionViewBean transition, COTransitionAction action) throws Exception {
        COJournalAdapter journal = createJournal(session, transaction, contentieux);

        this.executerTransition(session, transaction, contentieux, action, journal);
        journal.imputerFraisVariables(transaction, action, contentieux);
    }

    /**
     * @param transition
     * @return
     */
    private boolean isEtapeSansInfluence(COTransition transition) {
        return ICOEtape.CS_VERSEMENT_IMPUTE.equals(transition.getEtapeSuivante().getLibEtape())
                || ICOEtape.CS_ETAPE_MANUELLE.equals(transition.getEtapeSuivante().getLibEtape());
    }

    /**
     * @param transaction
     * @param contentieux
     * @param idHistorique
     * @param action
     * @return
     * @throws Exception
     */
    private FWCurrency saveEtapesInfos(BTransaction transaction, COContentieux contentieux, String idHistorique,
            COTransitionAction action) throws Exception {
        FWCurrency totalTaxes = new FWCurrency(0);

        // ins�re les infos par �tapes dans la base
        for (Iterator infoIter = action.getEtapeInfos().entrySet().iterator(); infoIter.hasNext();) {
            Map.Entry entry = (Map.Entry) infoIter.next();
            COEtapeInfo etapeInfo = new COEtapeInfo();

            etapeInfo.setIdEtapeInfoConfig((String) entry.getKey());
            etapeInfo.setIdHistorique(idHistorique);
            etapeInfo.setValeur((String) entry.getValue());
            etapeInfo.setSession(transaction.getSession());

            etapeInfo.add(transaction);
        }

        return totalTaxes;
    }

    /**
     * @param transaction
     * @param contentieux
     * @param idHistorique
     * @param action
     * @return
     * @throws Exception
     */
    private FWCurrency saveFrais(BTransaction transaction, COContentieux contentieux, String idHistorique,
            COTransitionAction action) throws Exception {
        FWCurrency totalTaxes = new FWCurrency(0);

        // Frais et Int�rets
        for (Iterator iterator = action.getFrais().iterator(); iterator.hasNext();) {
            HashMap frais = (HashMap) iterator.next();

            COEtapeInfo etapeInfo = new COEtapeInfo();

            etapeInfo.setIdHistorique(idHistorique);
            etapeInfo.setValeur((String) frais.get(COTransitionViewBean.MONTANT));
            etapeInfo.setComplement1((String) frais.get(COTransitionViewBean.RUBRIQUE));
            if (JadeStringUtil.isBlank((String) frais.get(COTransitionViewBean.LIBELLE))) {
                etapeInfo.setComplement3((String) frais.get(COTransitionViewBean.RUB_DESCRIPTION));
            } else {
                etapeInfo.setComplement3((String) frais.get(COTransitionViewBean.LIBELLE));
            }
            etapeInfo.setComplement4("true");
            etapeInfo.setSession(transaction.getSession());
            etapeInfo.add(transaction);

            // totaliser les taxes, on totalise m�me si c'est pas imput�
            totalTaxes.add(etapeInfo.getValeur());
        }

        return totalTaxes;
    }

    /**
     * Si un IM est existant, il est automatiquement mit au motif "Manuel", la date de facturation est renseign�e avec
     * la date d'ex�cution de l'�tape et l'idJournal de facturation est renseign� avec l'id du journal du contentieux.
     * 
     * Si l'IM a �t� calcul� par le contentieux, il est ajout� en DB.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param action
     * @param journal
     * @return montant total des IM calcul�s
     * @throws Exception
     */
    private FWCurrency saveIMCalcule(BTransaction transaction, String dateFacturation,
            List<CAInteretManuelVisualComponent> interetsCalcules, String idJournal) throws Exception {
        FWCurrency totalIM = new FWCurrency("0");
        if (interetsCalcules != null) {
            for (CAInteretManuelVisualComponent immc : interetsCalcules) {
                // Totalise les montants d'IM pour l'historique du contentieux
                totalIM.add(immc.montantInteretTotalCalcule());

                CAInteretMoratoire interetMoratoire = new CAInteretMoratoire();
                if (!immc.getInteretMoratoire().isNew()) {
                    interetMoratoire.setIdInteretMoratoire(immc.getInteretMoratoire().getIdInteretMoratoire());
                    interetMoratoire.retrieve(transaction);

                    if (interetMoratoire.isNew() || transaction.hasErrors()) {
                        throw (new Exception("Error on retrieve interetMoratoire : "
                                + interetMoratoire.getIdInteretMoratoire()));
                    }

                    interetMoratoire.setMotifcalcul(CAInteretMoratoire.CS_MANUEL);
                    interetMoratoire.setDateFacturation(dateFacturation);
                    interetMoratoire.setIdJournalFacturation(idJournal);
                    interetMoratoire.update(transaction);
                } else {
                    interetMoratoire = immc.getInteretMoratoire();
                    interetMoratoire.setIdJournalCalcul(idJournal);
                    interetMoratoire.setDateFacturation(dateFacturation);
                    interetMoratoire.setIdJournalFacturation(idJournal);
                    interetMoratoire.add(transaction);

                    if (interetMoratoire.isNew() || transaction.hasErrors()) {
                        throw (new Exception("Error on add interetMoratoire !"));
                    }

                    for (CADetailInteretMoratoire detailIM : immc.getDetailInteretMoratoire()) {
                        detailIM.setIdInteretMoratoire(interetMoratoire.getIdInteretMoratoire());
                        detailIM.add(transaction);
                    }
                }
            }
        }
        return totalIM;
    }

    /**
     * @param transaction
     * @param contentieux
     * @param idHistorique
     * @param action
     * @return
     * @throws Exception
     */
    private FWCurrency saveTaxes(BTransaction transaction, COContentieux contentieux, String idHistorique,
            COTransitionAction action) throws Exception {
        FWCurrency totalTaxes = new FWCurrency(0);

        // taxes
        for (Iterator<COTaxe> iterator = action.getTaxes().iterator(); iterator.hasNext();) {
            COTaxe taxe = iterator.next();

            COEtapeInfo etapeInfo = new COEtapeInfo();
            etapeInfo.setIdHistorique(idHistorique);
            etapeInfo.setValeur(taxe.getMontantTaxe());
            etapeInfo.setComplement1(COEtapeInfo.COMPLEMENT1_TAXE);
            etapeInfo.setComplement2(taxe.getIdCalculTaxe());
            etapeInfo.setComplement3(taxe.getLibelle(contentieux.getSession()));
            etapeInfo.setComplement4(taxe.isImputerTaxe() ? "true" : "false");
            etapeInfo.setSession(transaction.getSession());
            etapeInfo.add(transaction);

            // totaliser les taxes, on totalise m�me si c'est pas imput�
            totalTaxes.add(taxe.getMontantTaxe());
        }

        return totalTaxes;
    }

    /**
     * @param transaction
     * @param historique
     * @param totalTaxes
     * @throws Exception
     */
    private void updateHistoriqueWithTaxes(BTransaction transaction, COHistorique historique, FWCurrency totalTaxes)
            throws Exception {
        // mettre � jour le montant des taxes
        historique.setTaxes(totalTaxes.toString());

        // mettre � jour le montant du solde
        totalTaxes.add(historique.getSolde());
        historique.setSolde(totalTaxes.toString());
        historique.update(transaction);
    }
}
