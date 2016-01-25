package globaz.helios.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSoldesDesComptesListViewBean;
import java.util.ArrayList;

/**
 * Class permettant de gérer le manager du solde pour imprimer à les écrans les informations correctement formatées.
 * 
 * @author dda
 * 
 */
public class CGSoldesDesComptesParser {

    /**
     * Ajout du ligne contenant les informations du compte.
     * 
     * @param entity
     * @param avoir
     * @param doit
     * @param solde
     * @return
     */
    private static CGSoldesDesComptesLine addLine(CGPlanComptableViewBean entity, FWCurrency avoir, FWCurrency doit,
            FWCurrency solde) {
        CGSoldesDesComptesLine line = new CGSoldesDesComptesLine();
        line.setIdCompte(entity.getIdCompte());
        line.setIdExterne(entity.getIdExterne());
        line.setLibelle(entity.getLibelle());
        line.setGenreLibelle(entity.getGenreLibelle());

        line.setIdNature(entity.getIdNature());
        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
            line.setCodeISOMonnaie(entity.getCodeISOMonnaie());
        }

        line.setAvoir(JANumberFormatter.fmt(avoir.toString(), true, true, true, 2));
        line.setDoit(JANumberFormatter.fmt(doit.toString(), true, true, true, 2));
        line.setSolde(JANumberFormatter.fmt(solde.toString(), true, true, false, 2));

        return line;
    }

    /**
     * Ajout du ligne contenant les informations du compte groupé par idCompteOfas.
     * 
     * @param entity
     * @param avoirTotal
     * @param doitTotal
     * @param soldeTotal
     * @return
     */
    private static CGSoldesDesComptesLine addLineOfas(CGPlanComptableViewBean entity, FWCurrency avoirTotal,
            FWCurrency doitTotal, FWCurrency soldeTotal) {
        CGCompteOfas compteOfas = new CGCompteOfas();
        compteOfas.setSession(entity.getSession());

        compteOfas.setIdCompteOfas(entity.getIdCompteOfas());

        try {
            compteOfas.retrieve();
        } catch (Exception e) {
            // do nothing
        }

        CGSoldesDesComptesLine line = new CGSoldesDesComptesLine();
        line.setIdExterne(compteOfas.getIdExterne());
        line.setLibelle(compteOfas.getLibelle());
        line.setGenreLibelle(entity.getGenreLibelle());

        line.setAvoir(JANumberFormatter.fmt(avoirTotal.toString(), true, true, true, 2));
        line.setDoit(JANumberFormatter.fmt(doitTotal.toString(), true, true, true, 2));
        line.setSolde(JANumberFormatter.fmt(soldeTotal.toString(), true, true, false, 2));

        return line;
    }

    /**
     * Return l'avoir ou l'avoir provisoire.
     * 
     * @param listViewBean
     * @param pos
     * @return
     */
    private static FWCurrency getAvoir(CGSoldesDesComptesListViewBean listViewBean, int pos) {
        FWCurrency avoir = new FWCurrency();

        if (CGEcritureViewBean.CS_MONTANT_CHF.equals(listViewBean.getReqMontant())) {
            avoir = new FWCurrency(listViewBean.getAvoir(pos));
        } else {
            avoir = new FWCurrency(listViewBean.getAvoirMonnaie(pos));
        }

        avoir.negate();
        return avoir;
    }

    /**
     * Return le doit ou le doit provisoire.
     * 
     * @param listViewBean
     * @param pos
     * @return
     */
    private static FWCurrency getDoit(CGSoldesDesComptesListViewBean listViewBean, int pos) {
        if (CGEcritureViewBean.CS_MONTANT_CHF.equals(listViewBean.getReqMontant())) {
            return new FWCurrency(listViewBean.getDoit(pos));
        } else {
            return new FWCurrency(listViewBean.getDoitMonnaie(pos));
        }
    }

    /**
     * Return une ArrayList contenant les soldes des comptes à imprimer à l'écran.
     * 
     * @param listViewBean
     * @param exerciceComptable
     * @return
     */
    public static ArrayList getLinesToPrint(CGSoldesDesComptesListViewBean listViewBean) {
        if (listViewBean.isGroupIdCompteOfas()) {
            return getLinesToPrintGrouped(listViewBean);
        } else {
            return getLinesToPrintNotGrouped(listViewBean);
        }
    }

    /**
     * Retourne les lignes à imprimer MODE GROUPE par idCompteOfas.
     * 
     * @param listViewBean
     * @return
     */
    private static ArrayList getLinesToPrintGrouped(CGSoldesDesComptesListViewBean listViewBean) {
        ArrayList result = new ArrayList();

        FWCurrency avoirTotal = new FWCurrency();
        FWCurrency doitTotal = new FWCurrency();
        FWCurrency soldeTotal = new FWCurrency();

        for (int i = 0; i < listViewBean.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) listViewBean.getEntity(i);

            CGPlanComptableViewBean nextEntity = null;
            if (listViewBean.getEntity(i + 1) != null) {
                nextEntity = (CGPlanComptableViewBean) listViewBean.getEntity(i + 1);
            }

            avoirTotal.add(getAvoir(listViewBean, i));
            doitTotal.add(getDoit(listViewBean, i));
            soldeTotal.add(getSolde(listViewBean, i));

            if ((nextEntity == null) || (!entity.getIdCompteOfas().equals(nextEntity.getIdCompteOfas()))) {
                result.add(addLineOfas(entity, avoirTotal, doitTotal, soldeTotal));

                avoirTotal = new FWCurrency();
                doitTotal = new FWCurrency();
                soldeTotal = new FWCurrency();
            }
        }

        return result;
    }

    /**
     * Retourne les lignes à imprimer à l'écran en mode SANS group by.
     * 
     * @param listViewBean
     * @return
     */
    private static ArrayList getLinesToPrintNotGrouped(CGSoldesDesComptesListViewBean listViewBean) {
        ArrayList result = new ArrayList();

        for (int i = 0; i < listViewBean.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) listViewBean.getEntity(i);
            result.add(addLine(entity, getAvoir(listViewBean, i), getDoit(listViewBean, i), getSolde(listViewBean, i)));
        }

        return result;
    }

    /**
     * Return le solde ou le solde provisoire.
     * 
     * @param listViewBean
     * @param pos
     * @return
     */
    private static FWCurrency getSolde(CGSoldesDesComptesListViewBean listViewBean, int pos) {
        if (CGEcritureViewBean.CS_MONTANT_CHF.equals(listViewBean.getReqMontant())) {
            return new FWCurrency(listViewBean.getSolde(pos));
        } else {
            return new FWCurrency(listViewBean.getSoldeMonnaie(pos));
        }
    }
}
