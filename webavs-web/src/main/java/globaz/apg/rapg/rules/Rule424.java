package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Si toutes les annonces pour un même « childinsurantVn » avec « serviceType » = 92 et « careLeaveEventID »
 * identique, la somme du champ « totalAPG » > 98 * 196 -> erreur
 *
 * Ajout : S211025_004
 *
 * @author dga
 */
public class Rule424 extends Rule{

    private static final int MAX_DAYS = 98;
    private static final BigDecimal MAX_INDEMN_JOURNALIERE = new BigDecimal(196);

    public Rule424(String errorCode) {
        super(errorCode, APAllPlausibiliteRules.R_424.isBreakable());
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        if(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce().equals(champsAnnonce.getServiceType())){
            try{
                BigDecimal totalApg = new BigDecimal(champsAnnonce.getTotalAPG());
                for (APPrestation prestation:
                        APDroitProcheAidantUtils.getPrestationForCareLeaveEventIdEtNssEnfant(champsAnnonce.getCareLeaveEventID(),
                                champsAnnonce.getChildInsurantVn(),
                                getSession())) {
                    totalApg = totalApg.add(new BigDecimal(prestation.getMontantBrut()));
                }
                return totalApg.compareTo(MAX_INDEMN_JOURNALIERE.multiply(new BigDecimal(MAX_DAYS))) < 1;

            } catch (Exception e){
                throwRuleExecutionException(e);
                return false;
            }

        } else{
            return true;
        }
    }
}
