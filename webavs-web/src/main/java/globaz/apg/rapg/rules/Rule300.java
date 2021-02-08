package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.rapg.messages.Message1509;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType »
 * (genre de service) = 90, le NAVS13 figurant dans le champ « insu-rant/personIdentificationType/vn » doit appartenir à
 * une femme.</br><strong>Champs concerné(s) :</strong></br> serviceType</br>insurant/personIdentificationType/vn
 * OU
 * Si le champ « serviceType » (genre de service) = 91, le NAVS13 figurant dans le champ « insurant/personIdentificationType/vn » doit appartenir à un homme.
 * @author lga
 */
public class Rule300 extends Rule {
    private String genreService = "";

    /**
     * @param errorCode
     */
    public Rule300(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if (!JadeStringUtil.isEmpty(serviceType) && "90".equals(serviceType)) {
            if (!"516002".equals(champsAnnonce.getInsurantSexe())) {
                genreService = "90";
                return false;
            }
        }
        if (!JadeStringUtil.isEmpty(serviceType) && "91".equals(serviceType)) {
            if (!"516001".equals(champsAnnonce.getInsurantSexe())) {
                genreService = "91";
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDetailMessageErreur() {
        String errorMessage = "";
        if("90".equals(genreService)){
            errorMessage = getSession().getLabel("APG_RULE_300_F");
        }else{
            errorMessage = getSession().getLabel("APG_RULE_300_H");
        }
        return errorMessage;

    }
}
