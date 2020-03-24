package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointTiersManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.ws.APRapgConsultationUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.lang.String;

import java.util.ArrayList;
import java.util.List;

public class Rule1509 extends Rule {
    private String numCaisse ="";
    private String debutPeriode="";
    private String finPeriode="";
    private String genreService="";


    /**
     * @param errorCode
     */
    public Rule1509(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();
        String deliveryOfficeBranch =  champsAnnonce.getDeliveryOfficeBranch();

        validNotEmpty(nss, "NSS");
        testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
        testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
        nss = nss.replaceAll("\\.", "");
        try {
            APRapgConsultationUtil.findAnnonces(getSession(),nss,deliveryOfficeBranch,"000");
        } catch (PropertiesException e) {
            return true;
        }


        // Voir les prestations en même temps
        numCaisse = champsAnnonce.getDeliveryOfficeBranch();
        debutPeriode = champsAnnonce.getStartOfPeriod();
        finPeriode = champsAnnonce.getEndOfPeriod();
        genreService = champsAnnonce.getServiceType();

        //Message Error à préparer
        return false;
    }
    public java.lang.String getErrorCode() {
        String errorCode = getSession().getLabel("APG_RULE_1509");
        errorCode = errorCode.replace("{0}", numCaisse);
        errorCode = errorCode.replace("{1}", debutPeriode);
        errorCode = errorCode.replace("{2}", finPeriode);
        errorCode = errorCode.replace("{3}", genreService);
        return errorCode;
    }


}
