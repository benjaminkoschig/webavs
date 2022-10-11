package globaz.aquila.process.batch.utils;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.db.access.batch.transition.COAbstractDelaiPaiementAction;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.ICOTaxeProducer;
import globaz.aquila.vb.process.COSelection;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.musca.db.facturation.FAEnteteFacture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class COProcessContentieuxUtils {

    /**
     * @see ICOTaxeProducer#getListeTaxes(BSession, COContentieux, globaz.aquila.db.access.batch.COEtape)
     * @param session
     * @param contentieux
     * @param transition
     * @param action
     * @return la liste des taxes
     * @throws Exception
     */
    public static List getTaxes(BSession session, COContentieux contentieux, COTransition transition,
            COTransitionAction action) throws Exception {
        ICOTaxeProducer producer = COServiceLocator.getTaxeService().getTaxeProducer(transition.getEtapeSuivante());

        return producer.getListeTaxes(session, contentieux, transition.getEtapeSuivante());
    }

    /**
     * Recherche l'action pour la transition donn�e.
     * <p>
     * V�rifie que la transition soit s�lectionn�e par l'utilisateur et si oui, cr�e une instance de l'action pour cette
     * transition, sinon retourne null.
     * </p>
     * 
     * @param selection
     *            la s�lection de l'utilisateur
     * @param transition
     *            la transition pour laquelle rechercher l'action
     * @param dateSurDocument
     * @param dateDelaiPaiement
     * @return l'action ou null si la transition n'est pas s�lectionn�e par l'utilisateur
     * @throws COTransitionException
     *             si une instance de l'action ne peut �tre cr��e.
     */
    public static COTransitionAction getTransitionAction(BProcess parent, COSelection selection,
            COTransition transition, String dateSurDocument, String dateDelaiPaiement, boolean eBillPrintable) throws COTransitionException {
        // v�rifier que la transition a �t� s�lectionn�e
        if (!selection.getDetailEtapes().contains(transition.getIdEtapeSuivante())) {
            return null;
        }

        // instancier l'action de transition a effectuer
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(transition);

        String eBillTransactionID  = FAEnteteFacture.incrementAndGetEBillTransactionID(eBillPrintable, transition.getSession());

        transition.setEBillTransactionID(eBillTransactionID);
        transition.setEBillPrintable(eBillPrintable);

        action.setEBillTransactionID(eBillTransactionID);
        action.setEBillPrintable(eBillPrintable);
        action.setDateExecution(dateSurDocument);
        action.setParent(parent);

        if (action instanceof COAbstractDelaiPaiementAction) {
            ((COAbstractDelaiPaiementAction) action).setDateDelaiPaiement(dateDelaiPaiement);
        }

        return action;
    }

    /**
     * L'utilisateur a-t'il s�lectionn� uniquement l'�tape "Cr�er contentieux" ?
     * 
     * @param session
     * @param selections
     *            Map de COSelection
     * @return true s'il n'y a que l'�tape "Cr�er contentieux" selectionn�e
     * @throws Exception
     */
    public static boolean isOnlyCreerContentieux(BSession session, Map selections) throws Exception {
        Iterator selectionIter = selections.values().iterator();
        while (selectionIter.hasNext()) {
            COSelection selection = (COSelection) selectionIter.next();

            if (!selection.isCreerAmorcesContentieux(session)) {
                return false;
            }
            if (selection.getDetailEtapes().size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recherche la prochaine transition pour ce contentieux
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return la liste des prochaines transitions pour ce contentieux
     * @throws Exception
     */
    public static List loadTransitions(BSession session, BTransaction transaction, COContentieux contentieux)
            throws Exception {
        COTransitionManager transitionManager = new COTransitionManager();
        transitionManager.setForAuto(Boolean.TRUE);

        // On obtient les transitions tri�s par ordre de priorit� car
        // Par defaut: transitionManager.setOrderBy(COTransition.FNAME_PRIORITE
        // + " desc");
        transitionManager.setSession(session);

        if (!contentieux.getEtape().getTypeEtape().equals(ICOEtape.CS_TYPE_SPECIFIQUE)) {
            transitionManager.setForIdEtape(contentieux.getIdEtape());
        } else {
            transitionManager.setForIdEtape(contentieux.getIdEtapePrecedenteNonSpecifique());
            transitionManager.setOrIdEtapePrecedent(contentieux.getIdEtape());
        }

        transitionManager.find(transaction);

        return transitionManager.getContainer();
    }
}
