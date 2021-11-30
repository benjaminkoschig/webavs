package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Rule513 extends Rule {

    private final String METHOD_PAIEMENT = "4";
    private final int MAX_NUMBER_DAYS = 196;

    public Rule513(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        if(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce().equals(champsAnnonce.getServiceType()) && METHOD_PAIEMENT.equals(champsAnnonce.getPaymentMethod())){
            try{
                APDroitProcheAidantManager droitManager = new APDroitProcheAidantManager();
                droitManager.setForIdDroit(champsAnnonce.getIdDroit());
                droitManager.setSession(getSession());
                droitManager.find(BManager.SIZE_NOLIMIT);
                APDroitProcheAidant droit = droitManager.<APDroitProcheAidant>getContainerAsList().get(0);
                if(droit != null){
                    int totalJoursIndemnise = droit.calculerNbjourTotalDuDroit();
                    totalJoursIndemnise += droit.calculerNbjourTotalIndemnise();
                    return totalJoursIndemnise < MAX_NUMBER_DAYS;
                }
                return true;
            } catch (Exception e){
                throwRuleExecutionException(e);
                return false;
            }
        }
        return true;
    }
}
