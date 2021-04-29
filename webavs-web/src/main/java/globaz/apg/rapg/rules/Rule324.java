package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
/**
 * Si le champt « serviceType » =  92 et
 * le champ «careLeaveData» = vide ? erreur
 */
public class Rule324 extends Rule {
    public Rule324(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce().equals(serviceType) && JadeStringUtil.isBlankOrZero(champsAnnonce.getCareLeaveEventID())){
            return false;
        }
        return true;

    }
}
