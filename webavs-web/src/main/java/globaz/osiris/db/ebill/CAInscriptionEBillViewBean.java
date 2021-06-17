package globaz.osiris.db.ebill;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.ebill.enums.CAFichierInscriptionStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;

public class CAInscriptionEBillViewBean  extends CAInscriptionEBill implements FWViewBeanInterface {

    public void updateStatutFichier() throws Exception {
        BTransaction transaction = (BTransaction) getSession().newTransaction();
        try {
            transaction.openTransaction();
            String idFichier = getIdFichier();
            CAInscriptionEBillManager inscriptionManager = new CAInscriptionEBillManager();
            inscriptionManager.setSession(getSession());
            inscriptionManager.setForIdFichier(idFichier);
            inscriptionManager.find(transaction, BManager.SIZE_NOLIMIT);

            if (inscriptionManager.size() > 0) {
                int foundAtraiter = 0;
                int found = 0;
                for (int i = 0; i < inscriptionManager.size(); i++) {
                    CAInscriptionEBill inscriptionEBill = (CAInscriptionEBill) inscriptionManager.getEntity(i);
                    if (inscriptionEBill.getStatut().equals(CAStatutEBillEnum.A_TRAITER)) {
                        foundAtraiter ++;
                    }
                    found ++;
                }
                CAFichierInscriptionEBillManager fichierManager = new CAFichierInscriptionEBillManager();
                fichierManager.setSession(getSession());
                fichierManager.setForIdFichier(idFichier);
                fichierManager.find(transaction, BManager.SIZE_NOLIMIT);

                if (fichierManager.size() == 1) {
                    CAFichierInscriptionEBill fichierInscriptionEBill = (CAFichierInscriptionEBill) fichierManager.getFirstEntity();
                    fichierInscriptionEBill.retrieve();
                    if (foundAtraiter == 0 && found > 0) {
                        fichierInscriptionEBill.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.TRAITE.getIndex()));
                    } else if (foundAtraiter > 0) {
                        fichierInscriptionEBill.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.TRAITE_ERREUR.getIndex()));
                    } else {
                        fichierInscriptionEBill.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.NON_TRAITE.getIndex()));
                    }
                    fichierInscriptionEBill.update();
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
