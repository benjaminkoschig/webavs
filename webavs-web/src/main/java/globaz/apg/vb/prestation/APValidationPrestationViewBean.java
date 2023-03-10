package globaz.apg.vb.prestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.pojo.APErreurValidationPeriode;
import globaz.apg.pojo.APValidationPrestationAPGContainer;
import globaz.apg.pojo.ViolatedRule;
import globaz.apg.servlet.IAPActions;
import globaz.apg.util.APGSeodorErreurListEntities;
import globaz.framework.bean.FWViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;

public class APValidationPrestationViewBean extends FWViewBean implements FWViewBeanInterface {

    /**
     * D?tails du requ?rant Champ affich? dans la jsp d'affichage des erreurs de validation des plausibilit?s RAPG
     */
    private String detailRequerant;
    /**
     * Date de d?but du droit Champ affich? dans la jsp d'affichage des erreurs de validation des plausibilit?s RAPG
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
    private List<String> erreursValidationsJoursIsoles = new ArrayList<String>();
    private boolean calculMATCIAB2 = false;

    private boolean isFromAcorWeb = false;
    private List<String> messagesError = new ArrayList<>();

    private boolean hasMessagePropError = false;

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

    public boolean getHasErreursValidationsJoursIsoles() {
        return (erreursValidationsJoursIsoles != null) && (erreursValidationsJoursIsoles.size() > 0);
    }

    public boolean isCalculMATCIAB2() {
        return calculMATCIAB2;
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
     * Informe si, lors de la validation des prestation, des erreurs non 'quittan?able' sont survenue
     *
     * @return si des erreurs de validation non 'quittan?able' sont survenue lors de la validation des prestation
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
     * Retourne une liste de breakRules re?u lors de la contr?le des erreurs de validation des prestations</br>
     * Le
     * format du champ de classe breakRules est le suivant : idPrestation + . +num?roR?gle + separateur (_)</br>
     * Exemple
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
                                /**
                                 * Enregistrer le libelle avec la plausi pour la rule 1509
                                 */
                                if(code.equals("1509")){
                                    String libelle = getLibelleFromPrestation(idPrestation,"1509");
                                    breakRules.add(new APBreakRulesFromView(idPrestation, codeEnum.getCode(),libelle));
                                }else{
                                    breakRules.add(new APBreakRulesFromView(idPrestation, codeEnum.getCode()));
                                }
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

    private String getLibelleFromPrestation(String idPrestation, String ruleCode) {
        for(APValidationPrestationAPGContainer validationPrestationAPGContainer : prestationValidees){
            if(validationPrestationAPGContainer.getPrestation().getId().equals(idPrestation)){
                for(ViolatedRule ruleFromView : validationPrestationAPGContainer.getValidationErrors()){
                    if(ruleFromView.getErrorCode().equals(ruleCode)){
                        return ruleFromView.getErrorMessage();
                    }
                }
            }
        }
        return "";
    }

    // /**
    // * PAs utilis?, ce m?canisme n'? pas ?t? mis en place
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
    // throw new APMalformedBreakRule("Bad breakRule code [" + rule
    // + "]. The breakRule code is not a valid APBreakRule code [" + code + "]. Exception : " + e.toString());
    // }
    // }
    // }
    // }
    // }
    // }
    // return breakRules;
    // }

    public String getActionFinaliser() {
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(getGenreService())) {
            return "apg.droits.droitMatP.finaliserCreationDroit";
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(getGenreService())) {
            return "apg.droits.droitPatP.finaliserCreationDroit";
        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(getGenreService())) {
            return IAPActions.ACTION_SAISIE_CARTE_PAI +".finaliserCreationDroit";
        } else  if (APGenreServiceAPG.isValidGenreServicePandemie(APGenreServiceAPG.resoudreGenreParCodeSystem(getGenreService()).getCodePourAnnonce())) {
            return "apg.droits.droitPan.finaliserCreationDroit";
        } else {
            return "apg.droits.droitAPGP.finaliserCreationDroit";
        }
    }

    public String getActionCorriger() {
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(getGenreService())) {
            return "apg.droits.droitMatP.afficher";
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(getGenreService())) {
            return "apg.droits.droitPatP.afficher";
        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(getGenreService())) {
            return IAPActions.ACTION_SAISIE_CARTE_PAI +".afficher";
        } else if (APGenreServiceAPG.isValidGenreServicePandemie(APGenreServiceAPG.resoudreGenreParCodeSystem(getGenreService()).getCodePourAnnonce())) {
            return "apg.droits.droitPan.passerDroitErreur";
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

    public List<String> getErreursValidationsJoursIsoles() {
        return erreursValidationsJoursIsoles;
    }

    public void setErreursValidationsJoursIsoles(List<String> erreursValidationsJoursIsoles) {
        this.erreursValidationsJoursIsoles = erreursValidationsJoursIsoles;
    }

    public void setCalculMATCIAB2(boolean calculMATCIAB2) {
        this.calculMATCIAB2 = calculMATCIAB2;
    }

    public String getAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }

    public void setMessagePropError(boolean b) {
        hasMessagePropError = b;
    }
    public boolean hasMessagePropError() {
        return hasMessagePropError;
    }

    public String getMessagesErrorPopUp() {
        String msgHTML = "";
        for(String msg: messagesError){
            msgHTML = msgHTML+ "<p>"+msg+"<p><br>";
        }
        return msgHTML;
    }

    public List<String> getMessagesError() {
        return messagesError;
    }

    public void setMessagesError(List<String> messagesError) {
        this.messagesError = messagesError;
    }

    public void setIsFromAcorWeb(boolean isFromAcorWeb){
        this.isFromAcorWeb = isFromAcorWeb;
    }

    public boolean getIsFromAcorWeb(){
        return isFromAcorWeb;
    }
}
