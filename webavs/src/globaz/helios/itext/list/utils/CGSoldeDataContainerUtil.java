package globaz.helios.itext.list.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.db.comptes.helper.CGSoldeDataContainer;
import java.math.BigDecimal;

public class CGSoldeDataContainerUtil {

    public static CGSoldeDataContainer getSoldeDataForPeriode(BSession session, BITransaction transaction,
            String idMandat, String idExerciceComptable, String idPeriode, String idCompte, String idCentreCharge)
            throws Exception {

        CGSoldeDataContainer result = new CGSoldeDataContainer();

        CGSoldeManager soldeMgr = new CGSoldeManager();
        soldeMgr.setSession(session);

        soldeMgr.setForEstPeriode(new Boolean(true));
        soldeMgr.setForIdCompte(idCompte);
        soldeMgr.setForIdExerComptable(idExerciceComptable);
        soldeMgr.setForIdMandat(idMandat);
        soldeMgr.setForIdPeriodeComptable(idPeriode);
        soldeMgr.setForIdCentreCharge(idCentreCharge);
        soldeMgr.find(transaction);

        if (soldeMgr.size() > 1) {
            throw new Exception(CGSoldeDataContainerUtil.class.getName()
                    + "prepareValue() : plusieurs solde trouvé pour le même compte, compte id = " + idCompte
                    + "exercice id = " + idExerciceComptable);
        } else {
            CGSolde solde = (CGSolde) soldeMgr.getEntity(0);
            if (solde == null) {
                result.setAvoir(new FWCurrency(0));
                result.setAvoirMonnaie(new FWCurrency(0));
                result.setAvoirProvisoire(new FWCurrency(0));
                result.setAvoirProvisoireMonnaie(new FWCurrency(0));

                result.setDoit(new FWCurrency(0));
                result.setDoitMonnaie(new FWCurrency(0));
                result.setDoitProvisoire(new FWCurrency(0));
                result.setDoitProvisoireMonnaie(new FWCurrency(0));

                result.setSolde(new FWCurrency(0));
                result.setSoldeMonnaie(new FWCurrency(0));
                result.setSoldeProvisoire(new FWCurrency(0));
                result.setSoldeProvisoireMonnaie(new FWCurrency(0));
                result.setBudget(new FWCurrency(0));
            } else {
                result.setAvoir(new FWCurrency(solde.getAvoir()));
                result.setAvoirMonnaie(new FWCurrency(solde.getAvoirMonnaie()));
                result.setAvoirProvisoire(new FWCurrency(solde.getAvoirProvisoire()));
                result.setAvoirProvisoireMonnaie(new FWCurrency(solde.getAvoirProvisoireMonnaie()));

                result.setDoit(new FWCurrency(solde.getDoit()));
                result.setDoitMonnaie(new FWCurrency(solde.getDoitMonnaie()));
                result.setDoitProvisoire(new FWCurrency(solde.getDoitProvisoire()));
                result.setDoitProvisoireMonnaie(new FWCurrency(solde.getDoitProvisoireMonnaie()));

                result.setSolde(new FWCurrency(solde.getSolde()));
                result.setSoldeMonnaie(new FWCurrency(solde.getSoldeMonnaie()));
                result.setSoldeProvisoire(new FWCurrency(solde.getSoldeProvisoire()));
                result.setSoldeProvisoireMonnaie(new FWCurrency(solde.getSoldeProvisoireMonnaie()));
                result.setBudget(new FWCurrency(solde.getBudget()));
            }
            // Le budget pour la période est = 0 -> on récupère le budget annuel
            if (result.getBudget().isZero()) {
                soldeMgr.setForEstPeriode(new Boolean(false));
                soldeMgr.setForIdPeriodeComptable("0");
                soldeMgr.find(transaction);
                if (soldeMgr.size() > 1) {
                    throw new Exception(CGSoldeDataContainerUtil.class.getName()
                            + "prepareValue() : plusieurs solde trouvé pour le même compte, compte id = " + idCompte
                            + "exercice id = " + idExerciceComptable);
                } else {
                    solde = (CGSolde) soldeMgr.getEntity(0);
                    BigDecimal sld = null;
                    if (solde == null) {
                        sld = new BigDecimal(0);
                    } else {
                        sld = new BigDecimal(solde.getBudget());
                    }
                    // solde == 0
                    if (sld.signum() == 0) {
                        result.setBudget(new FWCurrency(0));
                    } else {
                        CGPeriodeComptable periode = new CGPeriodeComptable();
                        periode.setSession(session);
                        periode.setIdPeriodeComptable(idPeriode);
                        periode.retrieve(transaction);
                        if (periode == null || periode.isNew()) {
                            throw new Exception(CGSoldeDataContainerUtil.class.getName()
                                    + "prepareValue() : periode non trouvée, id = " + idPeriode);
                        }

                        int divisor = 12;
                        if (CGPeriodeComptable.CS_MENSUEL.equals(periode.getIdTypePeriode())) {
                            divisor = 12;
                        } else if (CGPeriodeComptable.CS_ANNUEL.equals(periode.getIdTypePeriode())) {
                            divisor = 1;
                        } else if (CGPeriodeComptable.CS_SEMESTRIEL.equals(periode.getIdTypePeriode())) {
                            divisor = 2;
                        } else if (CGPeriodeComptable.CS_TRIMESTRIEL.equals(periode.getIdTypePeriode())) {
                            divisor = 4;
                        }

                        sld = sld.divide(new BigDecimal(divisor), BigDecimal.ROUND_HALF_DOWN);
                        result.setBudget(new FWCurrency(sld.toString()));
                    }
                }
            }
        }
        return result;
    }
}
