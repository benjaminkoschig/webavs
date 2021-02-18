package globaz.apg.rapg.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.PRCanton;
import globaz.pyxis.constantes.IConstantes;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Le taux journalier de l'allocation de base figurant dans le champ "basicDailyAmount" ne peut pas
 *  être supérieur au montant total maximum de l'allocation journalière</br>
 * 
 * @author lga
 */
public class Rule321 extends Rule {

    private final int montantMiniAllocationJournalier = 62;
    private final int montantMiniAllocationJournalierMaternite = 196;
    private final int montantMiniAllocationJournalierCadre = 245;
    
    /**
     * @param errorCode
     */
    public Rule321(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws IllegalArgumentException, APRuleExecutionException {
        String serviceType = champsAnnonce.getServiceType();
        String basicDailyAmount = champsAnnonce.getBasicDailyAmount();
        String numberOfChilren = champsAnnonce.getNumberOfChildren();
        String dateDebut = champsAnnonce.getStartOfPeriod();
        
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services1 = new ArrayList<String>();
        services1.add("11");
        services1.add("13");
        services1.add("15");
        services1.add("21");
        services1.add("41");
        
        List<String> services2 = new ArrayList<String>();
        services2.add("10");
        services2.add("12");
        services2.add("14");
        services2.add("16");
        services2.add("20");
        services2.add("22");
        services2.add("23");
        
        // 1ere condition sur les premiers serviceType
        if (services1.contains(serviceType)) {
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            validNotEmpty(numberOfChilren, "numberOfChilren");
            if (numberOfChilren.equals("0") && (Float.valueOf(basicDailyAmount) > montantMiniAllocationJournalier)) {
                return false;
            }
            
        }
        
        // Condition sur les deuxièmes serviceType
        if (services2.contains(serviceType)) {
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            validNotEmpty(numberOfChilren, "numberOfChilren");
            if (numberOfChilren.equals("0") && (Float.valueOf(basicDailyAmount) > montantMiniAllocationJournalierMaternite)) {
                return false;
            }            
        }



         if(serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()) || serviceType.equals(APGenreServiceAPG.Maternite.getCodePourAnnonce())){
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            String parameterName = null;
            parameterName = APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT.getParameterName();
            try {
                BigDecimal montantMax = new BigDecimal(
                        FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "0", parameterName, dateDebut, "", 2));
                if ((Float.valueOf(basicDailyAmount) > montantMax.floatValue())) {
                    return false;
                }
            } catch (Exception e) {
                throw new APRuleExecutionException(e);
            }

        }else if(!APGenreServiceAPG.isValidGenreServicePandemie(serviceType)) {
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            validNotEmpty(numberOfChilren, "numberOfChilren");
            if ((Integer.valueOf(numberOfChilren) > 0) && (Float.valueOf(basicDailyAmount) > montantMiniAllocationJournalierCadre)) {
                return false;
            }
        }
        return true;
    }
}
