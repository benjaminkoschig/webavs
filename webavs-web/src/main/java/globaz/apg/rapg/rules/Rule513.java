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

    private final String methodPaiement = "2";

    public Rule513(String errorCode, boolean breakable) {
        super(errorCode, breakable);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {

        if(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce().equals(champsAnnonce.getServiceType()) && methodPaiement.equals(champsAnnonce.getPaymentMethod())){
            APDroitProcheAidantManager droitPaiManager = new APDroitProcheAidantManager();
            droitPaiManager.setSession(getSession());
            droitPaiManager.setForCareLeaveEventId(champsAnnonce.getCareLeaveEventID());
            try{
                droitPaiManager.find(BManager.SIZE_NOLIMIT);
                int totalJoursCareLeaveEventId = 0;
                for (APDroitProcheAidant droitPai:
                     droitPaiManager.<APDroitProcheAidant>getContainerAsList()) {
                    totalJoursCareLeaveEventId += Integer.parseInt(droitPai.getNbrJourSoldes());
                }
                return totalJoursCareLeaveEventId > 196;
            } catch (Exception e){
                throwRuleExecutionException(e);
                return false;
            }
        }
        return true;
    }
}
