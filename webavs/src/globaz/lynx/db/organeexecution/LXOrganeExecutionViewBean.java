package globaz.lynx.db.organeexecution;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.service.helios.LXHeliosService;
import globaz.lynx.service.tiers.LXTiersService;

public class LXOrganeExecutionViewBean extends LXOrganeExecution implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGPlanComptableViewBean compteCredit;

    /**
     * Constructeur de LXOrganeExecutionViewBean.
     */
    public LXOrganeExecutionViewBean() {
        super();
    }

    /**
     * Return l'adresse. Si non résolu, vide car utilisé par l'écran.
     * 
     * @return
     */
    public String getAdressePaiementAsString() {
        retrieveSociete();

        if (societe != null) {
            try {
                return LXTiersService.getAdresseOrganeExecutionPaiementAsString(getSession(), null, getCsDomaine(),
                        societe.getIdTiers());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return l'id externe du compte de compta gen.
     * 
     * @return
     */
    public String getIdExterneCompteCredit() {
        retrieveCompteCredit();

        if (compteCredit != null && !compteCredit.isNew()) {
            return compteCredit.getIdExterne();
        } else {
            return "";
        }
    }

    /**
     * Return l'id mandat de la société. Nécessaire pour l'écran de détail, lien vers compta gen recherche compte.
     * 
     * @return
     */
    public String getIdMandat() {
        retrieveSociete();

        if (societe != null) {
            return societe.getIdMandat();
        } else {
            return "";
        }
    }

    /**
     * Return l'id tiers de la société. Nécessaire pour l'écran de détail, lien vers tiers.
     * 
     * @return
     */
    public String getIdTiers() {
        retrieveSociete();

        if (societe != null) {
            return societe.getIdTiers();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleCompteCredit() {
        retrieveCompteCredit();

        if (compteCredit != null && !compteCredit.isNew()) {
            return compteCredit.getLibelle();
        } else {
            return "";
        }
    }

    /**
     * Retrouve le compte de compta gen lié.
     */
    private void retrieveCompteCredit() {
        retrieveSociete();

        if (!JadeStringUtil.isIntegerEmpty(getIdCompteCredit()) && compteCredit == null) {
            try {
                compteCredit = LXHeliosService.getCompte(getSession(), societe.getIdMandat(),
                        JACalendar.todayJJsMMsAAAA(), getIdCompteCredit());
            } catch (Exception e) {
                // nothing
            }
        }
    }

}
