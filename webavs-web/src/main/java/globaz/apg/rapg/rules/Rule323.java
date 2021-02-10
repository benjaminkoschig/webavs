package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
/**
 * Si le champ « serviceType » =  91 et
 * le champ «paternityLeaveData» = vide ? erreur
 */
public class Rule323 extends Rule {
    public Rule323(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if( serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()) &&
                (JadeStringUtil.isBlankOrZero(champsAnnonce.getNewbornDateOfBirth())
                  || JadeStringUtil.isBlankOrZero(champsAnnonce.getNumberOfWorkdays())
                || JadeStringUtil.isBlankOrZero(champsAnnonce.getParternityLeaveType())
                || JadeStringUtil.isBlankOrZero(champsAnnonce.getChildDomicile())
                )
        ){
            return false;
        }
        return true;
    }
}
