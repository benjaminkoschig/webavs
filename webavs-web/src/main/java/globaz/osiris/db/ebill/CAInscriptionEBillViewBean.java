package globaz.osiris.db.ebill;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ebill.enums.CAFichierInscriptionStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.process.ebill.EBillMail;

import java.util.List;
import java.util.Optional;

public class CAInscriptionEBillViewBean extends CAInscriptionEBill implements FWViewBeanInterface {

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

    public boolean envoieMailConfirmation() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForEBillAccountID(geteBillAccountID());
        try {
            manager.find(BManager.SIZE_USEDEFAULT);
            if(manager.isEmpty()) {
                throw new Exception("Pas de lien eBill trouvé dans les comptes annexes");
            }
        } catch (Exception e) {
            return addError("EBILL_COMPTE_ANNEXE_RETRIEVE_NUMERO_ADHERENT_FAILED", geteBillAccountID());
        }
        if(!controleCompteAnnexe(manager)) {
            return addError("EBILL_COMPTE_ANNEXE_UNIQUE_NUMERO_ADHERENT_FAILED", geteBillAccountID());
        }

        CACompteAnnexe ca = (CACompteAnnexe) manager.getFirstEntity();
        List<CACompteAnnexe> list = manager.getContainer();
        Optional<CACompteAnnexe> sansAdresseMail = list.stream().filter(c -> c.geteBillMail().isEmpty()).findFirst();
        if(sansAdresseMail.isPresent()) {
            return addError("EBILL_COMPTE_ANNEXE_EMAIL_COMPTE_ANNEXE_MANQUANTE", sansAdresseMail.get().getIdExterneRole());
        }
        String email = ca.geteBillMail();
        String codeIso = ca.getTiers().getLangueISO();
        if(codeIso.isEmpty()) {
            return addError("EBILL_COMPTE_ANNEXE_LANGUE_FAILED", ca.getIdTiers());
        }
        try {
            EBillMail.sendMailConfirmation(email, codeIso);
        } catch (Exception e) {
            _addError(this.getSession().getCurrentThreadTransaction(), e.getMessage());
            return false;
        }

        return true;
    }

    private boolean controleCompteAnnexe(CACompteAnnexeManager manager){
        if(manager.size() == 1) {
            return true;
        } else if (manager.size() == 2) {
            // 2 comptes paritaire + personnel
            CACompteAnnexe ca1 = (CACompteAnnexe) manager.get(0);
            CACompteAnnexe ca2 = (CACompteAnnexe) manager.get(1);
            return ca1.getIdExterneRole().equals(ca2.getIdExterneRole());
        }
        return false;
    }

    private boolean addError(String label, String param) {
        _addError(this.getSession().getCurrentThreadTransaction(), FWMessageFormat.format(getSession().getLabel(label), param));
        return false;
    }
    
}
