package globaz.aquila.ts.opge.utils;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.print.CO01RequisitionPoursuite;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.interets.CARubriqueSoumiseInteretManager;

public class COTSOPGEUtils {

    /**
     * Retourne la description (libellé étendu) d'une ligne de l'extrait de compte.
     * 
     * @param session
     * @param extraitCompte
     * @param idTransition
     * @return
     * @throws Exception
     */
    public static String getDescriptionExtraitCompte(BSession session, CAExtraitCompte extraitCompte,
            String idTransition) throws Exception {
        CO01RequisitionPoursuite document = new CO01RequisitionPoursuite();
        document.setSession(session);
        document.setIdTransition(idTransition);

        return document.getDescriptionExtraitCompte(extraitCompte);
    }

    /**
     * Return l'extrait de compte manager pour la section. Mode contentieux. <br/>
     * Permettra de lister les paiements (imputations) ou les taxes.
     * 
     * @param session
     * @param contentieux
     * @param forNotIdJournal
     * @param fromDate
     * @return l'extrait de compte manager pour la section.
     * @throws Exception
     */
    public static COExtraitCompteManager getExtraitCompteManager(BSession session, COContentieux contentieux,
            String forNotIdJournal, String fromDate) throws Exception {
        COExtraitCompteManager extraitCompteManager = new COExtraitCompteManager();

        extraitCompteManager.setSession(session);

        extraitCompteManager.setForSectionForPmtComp(contentieux.getIdSection());
        extraitCompteManager.setFromDate(fromDate);
        extraitCompteManager.setForNotIdJournal(forNotIdJournal);

        return extraitCompteManager;
    }

    /**
     * Somme le montant taxé pour la requisition.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param forNotIdJournal
     * @param ecrituresFromDate
     * @return le montant taxé pour la requisition
     * @throws Exception
     */
    public static String getMontantCumuleTaxe(BSession session, BTransaction transaction, COContentieux contentieux,
            String forNotIdJournal, String ecrituresFromDate) throws Exception {
        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(session, contentieux,
                forNotIdJournal, ecrituresFromDate);

        extraitCompteManager.find(transaction, BManager.SIZE_NOLIMIT);

        FWCurrency totalTaxe = new FWCurrency();
        for (int i = 0; i < extraitCompteManager.size(); ++i) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

            FWCurrency montant = new FWCurrency(extraitCompte.getMontant());

            if (montant.isPositive()
                    && !montant.isZero()
                    && !COTSOPGEUtils.isRubriqueSoumise(session, transaction, extraitCompte.getIdRubrique())
                    && !CORequisitionPoursuiteUtil.isLineBlocked(transaction,
                            ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE, extraitCompte.getIdRubrique())) {
                totalTaxe.add(extraitCompte.getMontant());
            }
        }

        return totalTaxe.toString();
    }

    /**
     * La rubrique est-elle soumis à intérêt ? Utilisé pour retrouvé les taxes de la réquisitions de poursuites.
     * 
     * @param session
     * @param transaction
     * @param idRubrique
     * @return
     * @throws Exception
     */
    public static boolean isRubriqueSoumise(BSession session, BTransaction transaction, String idRubrique)
            throws Exception {
        CARubriqueSoumiseInteretManager manager = new CARubriqueSoumiseInteretManager();
        manager.setSession(session);

        manager.setForIdRubrique(idRubrique);

        manager.find(transaction);

        return !manager.isEmpty();
    }
}
