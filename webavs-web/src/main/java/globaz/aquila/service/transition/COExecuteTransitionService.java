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
     * Exécute l'action de création du contentieux pour le contentieux donné.
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
     * Exécute l'action de création du contentieux pour le contentieux donné.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    public void effectuerActionCreerContentieux(BSession session, BTransaction transaction, COContentieux contentieux,
            boolean checkMotifBlocageCtx) throws Exception {
        // rechercher l'étape de création pour cette séquence
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setForLibEtape(ICOEtape.CS_CONTENTIEUX_CREE);
        etapeManager.setForIdSequence(contentieux.getIdSequence());
        etapeManager.setSession(session);
        etapeManager.find();

        if (etapeManager.size() != 1) {
            throw new COTransitionException("AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE", COActionUtils.getMessage(session,
                    "AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE"));
        }

        // rechercher la transition pour cette étape
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(session, transaction,
                ((COEtape) etapeManager.getFirstEntity()).getIdEtape(), null, null, null);

        if (action == null) {
            throw new COTransitionException("AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE", COActionUtils.getMessage(session,
                    "AQUILA_ETAPE_CREER_CONTENTIEUX_MANQUE"));
        }

        // HACK: dans le cas d'un ajout au contentieux, on utilise la date
        // d'ouverture comme date d'exécution
        action.setDateExecution(contentieux.getDateOuverture());

        /*
         * Effectue la transition, lors de la création d'un contentieux, on a une création un peu simplifiée du fait
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
	 * Execute la transition donnée.
	 * <p>
	 * Par contrat, l'action transmise DOIT avoir été entièrement initialisée (
	 * {@link COTransitionAction#setAnnulerEcritures(boolean)}, {@link COTransitionAction#setDateExecution(String)},
	 * {@link COTransitionAction#setTaxes(List), {@link COTransitionAction#setTransition(COTransition), ...).
	 *
	 * @link COTransitionAction#execute(COContentieux, BTransaction)} puis crée les éléments liés à cette transition
	 *       (historiques, comptabilisation des taxes, ...).
	 *       </p>
	 *       <p>
	 *       Par contrat, l'action DOIT pouvoir être exécutée (
	 *       {@link COTransitionAction#canExecute(COContentieux, BTransaction)}).
	 *       </p>
	 *       <p>
	 *       Si le journal transmis est null, le processus est considéré comme non prévisionnel.
	 *       </p>
	 *
	 * @param session
	 * @param transaction
	 * @param contentieux
	 * @param action
	 * @param journal
	 * @throws Exception
	 *             Si la transition ne peut être exécutée, en particulier si la validation de l'action échoue
	 */
    public void executerTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransitionAction action, COJournalAdapter journal) throws Exception {
        // Doit être placé avant contentieux.save()
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
            // met à jour le contentieux
            contentieux.save(transaction);
            contentieux.refreshLinks(transaction);

            // met à jour le dernier état de la section
            if (!isEtapeSansInfluence(action.getTransition())) {
                if (ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION
                        .equals(contentieux.getEtape().getLibEtape())) {
                    updateSection(transaction, contentieux, ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
                } else {
                    updateSection(transaction, contentieux, action.getTransition().getEtapeSuivante().getLibEtape());
                }
            }

            // crée une ligne d'historique
            COHistorique historique = createHistorique(session, transaction, contentieux, dernierHistorique,
                    action.getTransition(), action, journal);

            // met à jour l'historique si c'est imputer versement
            if (isEtapeSansInfluence(action.getTransition())) {

                // mettre à jour le montant des taxes
                historique.setImpute(Boolean.TRUE);
                historique.update(transaction);
            }

            // sauvegarde les informations par étapes
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

            // met à jour l'historique avec le total des taxes
            if ((totalTaxes != null) && totalTaxes.isPositive()) {
                updateHistoriqueWithTaxes(transaction, historique, totalTaxes);

                // mettre à jour les taxes
                contentieux.setMontantTotalTaxes("");
                COServiceLocator.getTaxeService().initMontantsTaxes(session, contentieux);
            }

            if (journal != null) {
                // crée les écritures dans Osiris
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

        // insére les infos par étapes dans la base
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

        // Frais et Intérets
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

            // totaliser les taxes, on totalise même si c'est pas imputé
            totalTaxes.add(etapeInfo.getValeur());
        }

        return totalTaxes;
    }

    /**
     * Si un IM est existant, il est automatiquement mit au motif "Manuel", la date de facturation est renseignée avec
     * la date d'exécution de l'étape et l'idJournal de facturation est renseigné avec l'id du journal du contentieux.
     * 
     * Si l'IM a été calculé par le contentieux, il est ajouté en DB.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param action
     * @param journal
     * @return montant total des IM calculés
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

            // totaliser les taxes, on totalise même si c'est pas imputé
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
        // mettre à jour le montant des taxes
        historique.setTaxes(totalTaxes.toString());

        // mettre à jour le montant du solde
        totalTaxes.add(historique.getSolde());
        historique.setSolde(totalTaxes.toString());
        historique.update(transaction);
    }
}
