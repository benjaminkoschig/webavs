package globaz.lynx.db.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import java.util.ArrayList;

public class LXJournalViewBean extends LXJournal implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWParametersUserCode ucEtat = null;

    /**
     * Constructeur de LXJournalViewBean.
     */
    public LXJournalViewBean() {
        super();
    }

    private LXOperationManager getPaiementManager() throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());

        manager.setForIdJournal(getIdJournal());

        ArrayList<String> forCsTypeOperationIn = new ArrayList<String>();

        forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
        forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
        forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);

        manager.setForIdTypeOperationIn(forCsTypeOperationIn);

        manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

        return manager;
    }

    /**
     * Return le total des escomptes. Information pour l'écran de détail de l'ordre groupé.
     * 
     * @return
     */
    public String getTotalEscompte() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_ESCOMPTE);
            manager.setForIdTypeOperationIn(idTypeOperationIn);

            manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le total des extournes. Information pour l'écran de détail de l'ordre groupé.
     * 
     * @return
     */
    public String getTotalExtourne() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_EXTOURNE);
            manager.setForIdTypeOperationIn(idTypeOperationIn);

            manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le total des factures. Information pour l'écran de détail du journal.
     * 
     * @return
     */
    public String getTotalFactures() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE);
            idTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE);
            idTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_VIREMENT);
            idTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_LSV);
            idTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_CAISSE);
            manager.setForIdTypeOperationIn(idTypeOperationIn);

            manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le total des notes de crédit. Information pour l'écran de détail du journal.
     * 
     * @return
     */
    public String getTotalNoteDeCredit() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE);
            idTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
            manager.setForIdTypeOperationIn(idTypeOperationIn);

            manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le total des paiements. Information pour l'écran de détail de l'ordre groupé.
     * 
     * @return
     */
    public String getTotalPaiement() {

        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            LXOperationManager manager = getPaiementManager();

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcEtat() {
        return this.getUcEtat(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @param code
     *            Un code systeme
     * @return
     */
    public FWParametersUserCode getUcEtat(String code) {

        if ((ucEtat == null) || ((ucEtat != null) && !JadeStringUtil.isIntegerEmpty(code))) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsEtat()) || !JadeStringUtil.isIntegerEmpty(code)) {
                ucEtat.setIdCodeSysteme(JadeStringUtil.isIntegerEmpty(code) ? getCsEtat() : code);
                ucEtat.setIdLangue(getSession().getIdLangue());
                try {
                    ucEtat.retrieve();
                    if (ucEtat.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucEtat;
    }

}
