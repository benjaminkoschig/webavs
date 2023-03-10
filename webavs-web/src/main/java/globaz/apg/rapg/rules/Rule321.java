package globaz.apg.rapg.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;

/**
 * <strong>R?gles de validation des plausibilit?s RAPG</br> Description :</strong></br> </br><strong>Champs concern?(s)
 * :</strong></br> Le taux journalier de l'allocation de base figurant dans le champ "basicDailyAmount" ne peut pas
 *  ?tre sup?rieur au montant total maximum de l'allocation journali?re</br>
 * 
 * @author lga
 */
public class Rule321 extends Rule {

    boolean isErrorDailyAmountZero;
    APChampsAnnonce annonce;

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

        annonce = champsAnnonce;

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
            if (numberOfChilren.equals("0")) {
                testDateNotEmptyAndValid(dateDebut, "startOfPeriod");
                try {
                    BigDecimal tauxUnitaireMaxSansEnfant = new BigDecimal(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                            "1", APParameter.TAUX_UNITAIRE_MAX_0_ENFANT.getParameterName(), dateDebut, "", 2));
                    if (Float.valueOf(basicDailyAmount) > tauxUnitaireMaxSansEnfant.floatValue()) {
                        return false;
                    }
                } catch (Exception e) {
                    throw new APRuleExecutionException(e);
                }
            }
        }

        // Condition sur les deuxi?mes serviceType
        if (services2.contains(serviceType)) {
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            validNotEmpty(numberOfChilren, "numberOfChilren");
            if (numberOfChilren.equals("0")) {
                testDateNotEmptyAndValid(dateDebut, "startOfPeriod");
                try {
                    BigDecimal tauxJournalierMaxDroitAcquisSansEnfant = new BigDecimal(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                            "0", APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT.getParameterName(), dateDebut, "", 2));
                    if (Float.valueOf(basicDailyAmount) > tauxJournalierMaxDroitAcquisSansEnfant.floatValue()) {
                        return false;
                    }
                } catch (Exception e) {
                    throw new APRuleExecutionException(e);
                }
            }
        }


        if (serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()) ||
                serviceType.equals(APGenreServiceAPG.Maternite.getCodePourAnnonce()) ||
                serviceType.equals(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce())) {
            if (isAnnonceModification(champsAnnonce)) {
                return true;
            } else {
                validNotEmpty(basicDailyAmount, "basicDailyAmount");
            }

            testDateNotEmptyAndValid(dateDebut, "startOfPeriod");
            try {
                BigDecimal tauxJournalierMaxDroitAcquisSansEnfant = new BigDecimal(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                        "0", APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT.getParameterName(), dateDebut, "", 2));
                if ((Float.valueOf(basicDailyAmount) > tauxJournalierMaxDroitAcquisSansEnfant.floatValue())) {
                    return false;
                }
            } catch (Exception e) {
                throw new APRuleExecutionException(e);
            }

        } else if (!APGenreServiceAPG.isValidGenreServicePandemie(serviceType)) {
            validNotEmpty(basicDailyAmount, "basicDailyAmount");
            validNotEmpty(numberOfChilren, "numberOfChilren");
            if (Integer.valueOf(numberOfChilren) > 0) {
                testDateNotEmptyAndValid(dateDebut, "startOfPeriod");
                try {
                    BigDecimal tauxJournalierMaxDroitAcquisAvecEnfant = new BigDecimal(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                            "0", APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_PLUS_DE_2_ENFANT.getParameterName(), dateDebut, "", 2));
                    if ((Float.valueOf(basicDailyAmount) > tauxJournalierMaxDroitAcquisAvecEnfant.floatValue())) {
                        return false;
                    }
                } catch (Exception e) {
                    throw new APRuleExecutionException(e);
                }
            }
        }
        return true;
    }

    private boolean isAnnonceModification(APChampsAnnonce annonce) {
        return "3".equals(annonce.getAction()) || "4".equals(annonce.getAction());
    }
}
