package globaz.apg.vb.prestation;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.pojo.APErreurValidationPeriode;
import globaz.apg.pojo.APValidationPrestationAPGContainer;
import globaz.framework.bean.FWViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.util.ArrayList;
import java.util.List;

public class APValidationPrestationViewBean extends FWViewBean implements FWViewBeanInterface {

    /**
     * Détails du requérant Champ affiché dans la jsp d'affichage des erreurs de validation des plausibilités RAPG
     */
    private String detailRequerant;
    /**
     * Date de début du droit Champ affiché dans la jsp d'affichage des erreurs de validation des plausibilités RAPG
     */
    private String dateDeDebutDroit;

    /**
     * Contient les breakRules RAPG
     */
    private String breakRules;
    private String breakRulesGlobaz;
    private String idLangue;
    private String idDroit;
    private String genreService;
    private List<APValidationPrestationAPGContainer> prestationValidees = new ArrayList<APValidationPrestationAPGContainer>();
    private List<APErreurValidationPeriode> erreursValidationPeriodes = new ArrayList<APErreurValidationPeriode>();

    public final List<APValidationPrestationAPGContainer> getPrestationValidees() {
        return prestationValidees;
    }

    public final void setPrestationValidees(List<APValidationPrestationAPGContainer> prestationValidees) {
        this.prestationValidees = prestationValidees;
    }

    public final String getIdDroit() {
        return idDroit;
    }

    public final void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public boolean hasErreursValidationPeriodes() {
        return (erreursValidationPeriodes != null) && (erreursValidationPeriodes.size() > 0);
    }

    /**
     * Informe si, lors de la validation des prestation, des erreurs sont survenue
     * 
     * @return si des erreurs de validation sont survenue lors de la validation des prestation
     */
    public boolean hasValidationError() {
        if (prestationValidees != null) {
            for (APValidationPrestationAPGContainer container : prestationValidees) {
                if (container.getHasValidationError()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Informe si, lors de la validation des prestation, des erreurs non 'quittançable' sont survenue
     * 
     * @return si des erreurs de validation non 'quittançable' sont survenue lors de la validation des prestation
     */
    public boolean hasUnbreakableValidationError() {
        if (prestationValidees != null) {
            for (APValidationPrestationAPGContainer container : prestationValidees) {
                if (container.getHasUnbreakableValidationError()) {
                    return true;
                }
            }
        }
        return false;
    }

    public final String getIdLangue() {
        return idLangue;
    }

    public final void setIdLangue(String idLangue) {
        this.idLangue = idLangue;
    }

    public final String getDetailRequerant() {
        return detailRequerant;
    }

    public final void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    public final String getDateDeDebutDroit() {
        return dateDeDebutDroit;
    }

    public final void setDateDeDebutDroit(String dateDeDebutDroit) {
        this.dateDeDebutDroit = dateDeDebutDroit;
    }

    public final void setBreakRules(String breakRules) {
        this.breakRules = breakRules;
    }

    public void setBreakRulesGlobaz(String breakRulesGlobaz) {
        this.breakRulesGlobaz = breakRulesGlobaz;
    }

    public List<APErreurValidationPeriode> getErreursValidationPeriodes() {
        return erreursValidationPeriodes;
    }

    public void setErreursValidationPeriodes(List<APErreurValidationPeriode> erreursValidationPeriodes) {
        this.erreursValidationPeriodes = erreursValidationPeriodes;
    }

    /**
     * Retourne une liste de breakRules reçu lors de la contrôle des erreurs de validation des prestations</br> Le
     * format du champ de classe breakRules est le suivant : idPrestation + . +numéroRègle + separateur (_)</br> Exemple
     * : 71255.509_71256.510_
     * 
     * @return
     */
    public List<APBreakRulesFromView> getBreakRules() throws IllegalArgumentException {
        List<APBreakRulesFromView> breakRules = new ArrayList<APBreakRulesFromView>();
        if (!JadeStringUtil.isEmpty(this.breakRules)) {
            String[] rules = this.breakRules.split("_");
            for (String rule : rules) {
                if (rule.length() > 1) {
                    String[] values = rule.split("\\.");
                    if (values.length != 2) {
                        throw new IllegalArgumentException("Unreadable breakRule received from view [" + rule + "]");
                    } else {
                        String idPrestation = values[0];
                        String code = values[1];
                        if (JadeStringUtil.isIntegerEmpty(idPrestation)) {
                            throw new IllegalArgumentException("Unreadable breakRule received from view [" + rule
                                    + "]. The id of APPrestaionAPG is not valid [" + idPrestation + "]");
                        } else if (JadeStringUtil.isIntegerEmpty(code)) {
                            throw new IllegalArgumentException("Unreadable breakRule received from view [" + rule
                                    + "]. The id of breakRule code is not valid [" + code + "]");
                        } else {
                            try {
                                APAllPlausibiliteRules codeEnum = APAllPlausibiliteRules.valueOfCode(code);
                                breakRules.add(new APBreakRulesFromView(idPrestation, codeEnum.getCode()));
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Bad breakRule code  [" + rule
                                        + "]. The breakRule code is not a valid APBreakRule code [" + code
                                        + "]. Exception : " + e.toString());
                            }
                        }
                    }
                }
            }
        }
        return breakRules;
    }

    // /**
    // * PAs utilisé, ce mécanisme n'à pas été mis en place
    // * @return
    // * @throws APMalformedBreakRule
    // */
    // public List<APBreakRulesFromView> getBreakRulesGlobaz() throws APMalformedBreakRule {
    // List<APBreakRulesFromView> breakRules = new ArrayList<APBreakRulesFromView>();
    // if (!JadeStringUtil.isEmpty(this.breakRulesGlobaz)) {
    // String[] rules = this.breakRulesGlobaz.split("_");
    // for (String rule : rules) {
    // if (rule.length() > 1) {
    // String[] values = rule.split("\\.");
    // if (values.length != 2) {
    // throw new APMalformedBreakRule("Unreadable breakRule received from view [" + rule + "]");
    // }
    // else {
    // String idPeriode = values[0];
    // String code = values[1];
    // if (JadeStringUtil.isIntegerEmpty(idPeriode)) {
    // throw new APMalformedBreakRule("Unreadable breakRule received from view [" + rule
    // + "]. The id of APPeriodeAPG is not valid [" + idPeriode + "]");
    // }
    // else if (JadeStringUtil.isIntegerEmpty(code)) {
    // throw new APMalformedBreakRule("Unreadable breakRule received from view [" + rule
    // + "]. The id of breakRule code is not valid [" + code + "]");
    // }
    // else {
    // try {
    // APBreakRuleGlobaz codeEnum = APBreakRuleGlobaz.valueOfCode(code);
    // breakRules.add(new APBreakRulesFromView(codeEnum.getCode(), true));
    // }
    // catch (Exception e) {
    // throw new APMalformedBreakRule("Bad breakRule code  [" + rule
    // + "]. The breakRule code is not a valid APBreakRule code [" + code + "]. Exception : " + e.toString());
    // }
    // }
    // }
    // }
    // }
    // }
    // return breakRules;
    // }

    public String getActionFinalser() {
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(getGenreService())) {
            return "apg.droits.droitMatP.finaliserCreationDroit";
        } else {
            return "apg.droits.droitAPGP.finaliserCreationDroit";
        }
    }

    public String getActionCorriger() {
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(getGenreService())) {
            return "apg.droits.droitMatP.afficher";
        } else {
            return "apg.droits.droitAPGP.afficher";
        }
    }

    public String getGenreService() {
        return genreService;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public String getAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }
}
