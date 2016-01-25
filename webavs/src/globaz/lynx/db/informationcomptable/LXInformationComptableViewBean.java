package globaz.lynx.db.informationcomptable;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.service.helios.LXHeliosService;
import globaz.lynx.utils.LXSocieteDebitriceUtil;

public class LXInformationComptableViewBean extends LXInformationComptable implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGPlanComptableViewBean compteCharge;
    private CGPlanComptableViewBean compteCreance;

    private CGPlanComptableViewBean compteEscompte;
    private CGPlanComptableViewBean compteTva;
    private LXSocieteDebitrice societe;
    private FWParametersUserCode ucCodeTVA = null;

    /**
     * Constructeur de LXInformationComptableViewBean.
     */
    public LXInformationComptableViewBean() {
        super();
    }

    public String getDefaultIdCentreChargeCompteCharge() {
        retrieveCompteCharge();

        if (compteCharge != null && !compteCharge.isNew()) {
            return compteCharge.getDefaultIdCentreCharge();
        } else {
            return "0";
        }
    }

    public String getDefaultIdCentreChargeCompteCreance() {
        retrieveCompteCreance();

        if (compteCreance != null && !compteCreance.isNew()) {
            return compteCreance.getDefaultIdCentreCharge();
        } else {
            return "0";
        }
    }

    public String getDefaultIdCentreChargeCompteEscompte() {
        retrieveCompteEscompte();

        if (compteEscompte != null && !compteEscompte.isNew()) {
            return compteEscompte.getDefaultIdCentreCharge();
        } else {
            return "0";
        }
    }

    public String getDefaultIdCentreChargeCompteTVA() {
        retrieveCompteTva();

        if (compteTva != null && !compteTva.isNew()) {
            return compteTva.getDefaultIdCentreCharge();
        } else {
            return "0";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return l'id externe du compte de compta gen.
     * 
     * @return
     */
    public String getIdExterneCompteCharge() {
        retrieveCompteCharge();

        if (compteCharge != null && !compteCharge.isNew()) {
            return compteCharge.getIdExterne();
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
        retrieveCompteCreance();

        if (compteCreance != null && !compteCreance.isNew()) {
            return compteCreance.getIdExterne();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return l'id externe du compte de compta gen.
     * 
     * @return
     */
    public String getIdExterneCompteEscompte() {
        retrieveCompteEscompte();

        if (compteEscompte != null && !compteEscompte.isNew()) {
            return compteEscompte.getIdExterne();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return l'id externe du compte de compta gen.
     * 
     * @return
     */
    public String getIdExterneCompteTva() {
        retrieveCompteTva();

        if (compteTva != null && !compteTva.isNew()) {
            return compteTva.getIdExterne();
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
     * Retourne la nature du compte de charge
     * 
     * @return l'id de la nature
     */
    public String getIdNatureCompteCharge() {
        retrieveCompteCharge();

        if (compteCharge != null && !compteCharge.isNew()) {
            return compteCharge.getIdNature();
        } else {
            return "";
        }
    }

    /**
     * Retourne la nature du compte de créance
     * 
     * @return l'id de la nature
     */
    public String getIdNatureCompteCreance() {
        retrieveCompteCreance();

        if (compteCreance != null && !compteCreance.isNew()) {
            return compteCreance.getIdNature();
        } else {
            return "";
        }
    }

    /**
     * Retourne la nature du compte d'escompte
     * 
     * @return l'id de la nature
     */
    public String getIdNatureCompteEscompte() {
        retrieveCompteEscompte();

        if (compteEscompte != null && !compteEscompte.isNew()) {
            return compteEscompte.getIdNature();
        } else {
            return "";
        }
    }

    /**
     * Retourne la nature du compte TVA
     * 
     * @return l'id de la nature
     */
    public String getIdNatureCompteTVA() {
        retrieveCompteTva();

        if (compteTva != null && !compteTva.isNew()) {
            return compteTva.getIdNature();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleCompteCharge() {
        retrieveCompteCharge();

        if (compteCharge != null && !compteCharge.isNew()) {
            return compteCharge.getLibelle();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleCompteCreance() {
        retrieveCompteCreance();

        if (compteCreance != null && !compteCreance.isNew()) {
            return compteCreance.getLibelle();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleCompteEscompte() {
        retrieveCompteEscompte();

        if (compteEscompte != null && !compteEscompte.isNew()) {
            return compteEscompte.getLibelle();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleCompteTva() {
        retrieveCompteTva();

        if (compteTva != null && !compteTva.isNew()) {
            return compteTva.getLibelle();
        } else {
            return "";
        }
    }

    /**
     * Nécessaire pour l'écran de détail, return le libellé du compte de compta gen.
     * 
     * @return
     */
    public String getLibelleLongCodeTva() {
        return getUcCodeTVA().getCodeUtilisateur() + " - " + getUcCodeTVA().getLibelle();
    }

    /**
     * Return la société débitrice.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() {
        retrieveSociete();

        return societe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcCodeTVA() {

        if (ucCodeTVA == null) {
            // liste pas encore chargee, on la charge
            ucCodeTVA = new FWParametersUserCode();
            ucCodeTVA.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsCodeTva())) {
                ucCodeTVA.setIdCodeSysteme(getCsCodeTva());
                ucCodeTVA.setIdLangue(getSession().getIdLangue());
                try {
                    ucCodeTVA.retrieve();
                    if (ucCodeTVA.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucCodeTVA;
    }

    /**
     * Retrouve le compte de compta gen lié.
     */
    private void retrieveCompteCharge() {
        retrieveSociete();

        if (!JadeStringUtil.isIntegerEmpty(getIdCompteCharge()) && compteCharge == null) {
            try {
                compteCharge = LXHeliosService.getCompte(getSession(), getSociete().getIdMandat(),
                        JACalendar.todayJJsMMsAAAA(), getIdCompteCharge());
            } catch (Exception e) {
                // nothing
            }
        }
    }

    /**
     * Retrouve le compte de compta gen lié.
     */
    private void retrieveCompteCreance() {
        retrieveSociete();

        if (!JadeStringUtil.isIntegerEmpty(getIdCompteCreance()) && compteCreance == null) {
            try {
                compteCreance = LXHeliosService.getCompte(getSession(), getSociete().getIdMandat(),
                        JACalendar.todayJJsMMsAAAA(), getIdCompteCreance());
            } catch (Exception e) {
                // nothing
            }
        }
    }

    /**
     * Retrouve le compte de compta gen lié.
     */
    private void retrieveCompteEscompte() {
        retrieveSociete();

        if (!JadeStringUtil.isIntegerEmpty(getIdCompteEscompte()) && compteEscompte == null) {
            try {
                compteEscompte = LXHeliosService.getCompte(getSession(), getSociete().getIdMandat(),
                        JACalendar.todayJJsMMsAAAA(), getIdCompteEscompte());
            } catch (Exception e) {
                // nothing
            }
        }
    }

    /**
     * Retrouve le compte de compta gen lié.
     */
    private void retrieveCompteTva() {
        retrieveSociete();

        if (!JadeStringUtil.isIntegerEmpty(getIdCompteTva()) && compteTva == null) {
            try {
                compteTva = LXHeliosService.getCompte(getSession(), getSociete().getIdMandat(),
                        JACalendar.todayJJsMMsAAAA(), getIdCompteTva());
            } catch (Exception e) {
                // nothing
            }
        }
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && societe == null) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        } else if (!LXSocieteDebitriceUtil.hasSeveralSociete(getSession()) && societe == null) {
            societe = LXSocieteDebitriceUtil.getOnlyOneSociete(getSession());

            if (societe.hasErrors() || societe.isNew()) {
                societe = null;
                return;
            }
        }
    }
}
