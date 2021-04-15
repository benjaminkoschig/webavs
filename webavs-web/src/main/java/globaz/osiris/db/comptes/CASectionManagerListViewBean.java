package globaz.osiris.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;

public class CASectionManagerListViewBean extends CASectionManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String geteBillEtatTraitementImage(CATraitementEtatEBillEnum statutEBill) {
        if (statutEBill != null) {
            switch (statutEBill.getNumeroEtat()) {
                case CATraitementEtatEBillEnum.NUMERO_ETAT_EN_ERREUR:
                    return "eBill_rouge.png";
                case CATraitementEtatEBillEnum.NUMERO_ETAT_REJECTED_OR_PENDING:
                    return "eBill_orange.png";
                case CATraitementEtatEBillEnum.NUMERO_ETAT_TRAITE:
                    return "eBill_vert.png";
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
}
