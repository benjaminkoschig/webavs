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
import globaz.jade.client.util.JadeStringUtil;

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

                int nbJours = 0;

                for (APPrestation prestation:
                     APDroitProcheAidantUtils.getPrestationForCareLeaveEventIdEtNssEnfant(champsAnnonce.getCareLeaveEventID(),
                                                                                          champsAnnonce.getChildInsurantVn(),
                                                                                          getSession())) {
                    nbJours += Integer.parseInt(prestation.getNombreJoursSoldes()) * 2;
                }
                return nbJours <= MAX_NUMBER_DAYS;
            } catch (Exception e){
                throwRuleExecutionException(e);
                return false;
            }
        }
        return true;
    }
}
