package globaz.naos.db.beneficiairepc;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;

public class AFImpressionQuittanceViewBean extends AFImpressionQuittance implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String isProcessRunning = "";

    private boolean checkDate(String fieldValue) {
        try {
            BSessionUtil.checkDateGregorian(getSession(), fieldValue);
            if (JadeStringUtil.isEmpty(fieldValue)) {
                my_addError(getSession().getLabel("BENEFICIAIRE_DATE_EVALUATION"));
                return false;
            }
            return true;
        } catch (Exception ex) {
            my_addError(getSession().getLabel("BENEFICIAIRE_DATE_EVALUATION"));
            return false;
        }
    }

    private boolean checkHeures(String intValue) {
        try {
            Integer nbre = Integer.valueOf(intValue);
            if ((nbre.intValue() <= 0) && (nbre.intValue() > 100)) {
                my_addError(getSession().getLabel("BENEFICIAIRE_HEURES"));
                return false;
            }
            return true;
        } catch (Exception e) {
            my_addError(getSession().getLabel("BENEFICIAIRE_HEURES"));
            return false;
        }
    }

    private boolean checkNbreQuittances(String doubleValue) {
        try {
            Double nbre = Double.valueOf(doubleValue);
            if (nbre.doubleValue() <= 0.0) {
                my_addError(getSession().getLabel("BENEFICIAIRE_NBRE_QUITTANCES"));
                return false;
            }
            return true;
        } catch (Exception e) {
            my_addError(getSession().getLabel("BENEFICIAIRE_NBRE_QUITTANCES"));
            return false;
        }
    }

    public String getIsProcessRunning() {
        return isProcessRunning;
    }

    private void my_addError(String error) {
        setMsgType(FWViewBeanInterface.ERROR);
        setMessage(getMessage() + error + "\n");
    }

    public void setIsProcessRunning(String isProcessRunning) {
        this.isProcessRunning = isProcessRunning;
    }

    public boolean validate() {
        try {
            AFAffiliationManager affManager = new AFAffiliationManager();
            affManager.setSession(getSession());
            affManager.setForAffilieNumero(getNumAffilie());
            affManager.setForTypesAffParitaires();
            affManager.find();
            // On regarde si l'affilié est affilié avec comme mention :
            // déclaration de salaire : "bénéficiaire PC"
            AFAffiliation aff = (AFAffiliation) affManager.getFirstEntity();
            if (!(aff.getDeclarationSalaire().equals(CodeSystem.BENEFICIAIRE_PC))) {
                my_addError(getSession().getLabel("BENEFICIAIRE_PC_ERREUR"));
            }
        } catch (Exception ex) {
            my_addError(getSession().getLabel("BENEFICIAIRE_AFFILIE") + " : " + getNumAffilie());
        }

        if (JadeStringUtil.isEmpty(getNumAffilie())) {
            my_addError(getSession().getLabel("BENEFICIAIRE_NUM_AFFILIE") + " : " + getNumAffilie());
        }
        if ((getCasExistant().booleanValue() == false)
                && (JadeStringUtil.isEmpty(getNbreQuittances()) && (JadeStringUtil.isEmpty(getHeures())))) {
            my_addError(getSession().getLabel("BENEFICIAIRE_IMPRESSION"));
        } else if ((getCasExistant().booleanValue() == false)
                && (JadeStringUtil.isEmpty(getNbreQuittances()) && (JadeStringUtil.isEmpty(getDateEvaluation())))) {
            my_addError(getSession().getLabel("BENEFICIAIRE_IMPRESSION"));
        }
        if (getCasExistant().booleanValue() == false) {
            if (JadeStringUtil.isEmpty(getNbreQuittances())) {
                checkDate(getDateEvaluation());
                checkHeures(getHeures());
            } else {
                checkNbreQuittances(getNbreQuittances());
            }
        }

        return getMsgType() != FWViewBeanInterface.ERROR;
    }

}
