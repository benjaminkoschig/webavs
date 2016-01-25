/**
 *
 */
package globaz.osiris.db.mapping;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author sel
 * 
 */
public class CAJournalDebitViewBean extends CAJournalDebit implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return
     */
    public String getCompteCourantDestDescription() {
        CGPlanComptableViewBean compte;
        try {
            compte = getCompteCompteCourantDest();
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(),
                    "Error in CAJournalDebitViewBean.getCompteCourantDestDescription : " + e.toString());
            return "";
        }

        return compte.getLibelle();
    }

    /**
     * @return
     */
    public String getCompteCourantSrcDescription() {
        CACompteCourant cc = new CACompteCourant();
        cc.setSession(getSession());
        cc.setAlternateKey(APICompteCourant.AK_IDEXTERNE);
        cc.setIdExterne(getCompteCourantSrc());

        try {
            cc.retrieve();
            if (cc.isNew()) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANT_NON_TROUVE") + " ["
                                + getCompteCourantSrc() + "]");
                return "";
            }
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANT_NON_TROUVE") + " ["
                            + getCompteCourantSrc() + "] - " + e.toString());
            return "";
        }

        return cc.getRubrique().getDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.mapping.CAJournalDebit#getContrePartieDestDescription()
     */
    public String getContrePartieDestDescription() {
        CGPlanComptableViewBean compte = new CGPlanComptableViewBean();

        try {
            compte = getCompteContrePartieDest();
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(),
                    "Error in CAJournalDebitViewBean.getContrePartieDestDescription : " + e.toString());
            return "";
        }

        return compte.getLibelle();
    }

    /**
     * @return
     */
    public String getContrePartieSrcDescription() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdExterne(getContrePartieSrc());
        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
        try {
            rubrique.retrieve();
            if (rubrique.isNew()) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel("JOURNALDEBIT_ERREUR_CONTREPARTIE_NON_TROUVE") + " ["
                                + getContrePartieSrc() + "]");
                return "";
            }
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_CONTREPARTIE_NON_TROUVE") + " [" + getContrePartieSrc()
                            + "] - " + e.toString());
            return "";
        }

        return rubrique.getDescription();
    }

    /**
     * @return
     */
    public String getMandatDescription() {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());
        try {
            mandat.retrieve();
            if (mandat.isNew()) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel("JOURNALDEBIT_ERREUR_MANDAT_NOT_FIND") + " [" + getIdMandat() + "]");
                return "";
            }
        } catch (Exception e) {
            _addError(
                    getSession().getCurrentThreadTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_MANDAT_NOT_FIND") + " [" + getIdMandat() + "] - "
                            + e.toString());
            return "";
        }

        return mandat.getLibelle();
    }
}
