package globaz.osiris.db.ebill;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.ebill.enums.CAFichierTraitementStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;

public class CATraitementEBillViewBean extends CATraitementEBill implements FWViewBeanInterface {

    public void updateStatutFichier() throws Exception {
        BTransaction transaction = (BTransaction) getSession().newTransaction();
        try {
            transaction.openTransaction();
            String idFichier = getIdFichier();
            CATraitementEBillManager traitementManager = new CATraitementEBillManager();
            traitementManager.setSession(getSession());
            traitementManager.setForIdFichier(idFichier);
            traitementManager.find(transaction, BManager.SIZE_NOLIMIT);

            if (traitementManager.size() > 0) {
                boolean foundAtraiter = false;
                for (int i = 0; i < traitementManager.size(); i++) {
                    CATraitementEBill traitementEBill = (CATraitementEBill) traitementManager.getEntity(i);
                    if (traitementEBill.getStatut().equals(CAStatutEBillEnum.A_TRAITER)) {
                        foundAtraiter = true;
                        break;
                    }
                }
                CAFichierTraitementEBillManager fichierManager = new CAFichierTraitementEBillManager();
                fichierManager.setSession(getSession());
                fichierManager.setForIdFichier(idFichier);
                fichierManager.find(transaction, BManager.SIZE_NOLIMIT);

                if (fichierManager.size() == 1) {
                    CAFichierTraitementEBill fichierTraitementEBill = (CAFichierTraitementEBill) fichierManager.getFirstEntity();
                    fichierTraitementEBill.retrieve();
                    if (!foundAtraiter) {
                        fichierTraitementEBill.setStatutFichier(String.valueOf(CAFichierTraitementStatutEBillEnum.TRAITE.getIndex()));
                    } else {
                        fichierTraitementEBill.setStatutFichier(String.valueOf(CAFichierTraitementStatutEBillEnum.A_TRAITE.getIndex()));
                    }
                    fichierTraitementEBill.update();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            transaction.rollback();
        } finally {
            transaction.closeTransaction();
        }
    }
}
