package globaz.apg.rapg.rules;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.droits.APDroitMaterniteJointTiers;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadePeriodWrapper;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : 
 * Si, durant le cong� maternit� code 90 et codes 10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 30, 40, 41, 50 </br>
 * 
 * @author mmo
 */
public class Rule400 extends Rule {

    public Rule400(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        
        List<String> services = new ArrayList<String>();
        services.add("10");
        services.add("11");
        services.add("12");
        services.add("13");
        services.add("14");
        services.add("15");
        services.add("16");
        services.add("20");
        services.add("21");
        services.add("22");
        services.add("23");
        services.add("30");
        services.add("40");
        services.add("41");
        services.add("50");
        services.add(APGenreServiceAPG.Maternite.getCodePourAnnonce());

        if (services.contains(serviceType)) {

            String nss = champsAnnonce.getInsurant();
            String startOfPeriod = champsAnnonce.getStartOfPeriod();
            String endOfPeriod = champsAnnonce.getEndOfPeriod();
            
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
            
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
            validNotEmpty(nss, "nss");

            // Ne pas traiter les droits en �tat refus� ou transf�r�
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            JadePeriodWrapper periodeControle = new JadePeriodWrapper(startOfPeriod, endOfPeriod);

            try {

                // On recherche tous les droits li�s � ce NSS pour la p�riode donn�e
                APDroitMaterniteJointTiersManager manager = new APDroitMaterniteJointTiersManager();
                manager.setSession(getSession());
                manager.setForEtatDroitNotIn(etatIndesirable);
                manager.setLikeNumeroAvs(nss);
                List<APDroitAvecParent> droitsSansParents = null;
                try {
                    manager.find();
                    List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                    droitsSansParents = skipDroitParent(tousLesDroits);
                    
                    for (Object d : tousLesDroits) {
                        APDroitMaterniteJointTiers droit = (APDroitMaterniteJointTiers) d;
//                        LocalDate debutPeriodDate = LocalDate.parse(droit.getUneDateDebutPeriode(), formatter);
//                        LocalDate finPeriodDate = LocalDate.parse(droit.getUneDateFinPeriode(), formatter);
//                        
//                        String debutPeriodeString = String.valueOf(debutPeriodDate.getYear()) + String.valueOf(debutPeriodDate.getMonthOfYear()) 
//                                                    + String.valueOf(debutPeriodDate.getDayOfMonth()); 
//                        
//                        String finPeriodeString = String.valueOf(finPeriodDate.getYear()) + String.valueOf(finPeriodDate.getMonthOfYear()) 
//                                                    + String.valueOf(finPeriodDate.getDayOfMonth());

                        boolean isPrestationDansPeriodeControle = periodeControle.isDateDansLaPeriode(droit.getUneDateDebutPeriode()) 
                                && periodeControle.isDateDansLaPeriode(droit.getUneDateFinPeriode());
                        if (isPrestationDansPeriodeControle) {
                            return false;
                        }                    
                    }                                       
                } catch (Exception e) {
                    throwRuleExecutionException(e);
                }              
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }
}
